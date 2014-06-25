package de.mrapp.android.sidebar;

import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import de.mrapp.android.sidebar.util.DragHelper;

public class Sidebar extends ViewGroup {

	protected static final SidebarLocation DEFAULT_LOCATION = SidebarLocation.RIGHT;

	protected static final long DEFAULT_ANIMATION_DURATION = 250;

	protected static final int DEFAULT_WIDTH = 80;

	protected static final int DEFAULT_OFFSET = 10;

	protected static final float DEFAULT_SCROLL_RATIO = 0.5f;

	protected static final DragMode DEFAULT_DRAG_MODE = DragMode.BOTH;

	protected static final float DEFAULT_DRAG_THRESHOLD = 0.33f;

	protected static final int DEFAULT_DRAG_SENSITIVITY = 1;

	protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

	protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

	protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICKED = true;

	private Set<SidebarListener> listeners;

	private SidebarLocation location = SidebarLocation.RIGHT;

	private long animationDuration = 250;

	private int sidebarMaxWidth = 100;

	private int sidebarOffset = 10;

	private float scrollRatio = 0.5f;

	private float dragThreshold = 0.33f;

	private int dragSensitivity = 1;

	private DragMode dragMode = DragMode.BOTH;

	private boolean hideOnBackButton = true;

	private boolean hideWhenContentIsClicked = true;

	private boolean showWhenSidebarIsClicked = true;

	private View sidebarView;

	private View contentView;

	private boolean shown;

	private int sidebarWidth;

	private int width;

	private int offset;

	private DragHelper dragHelper;

	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		this.listeners = new LinkedHashSet<>();
		this.shown = false;
		this.dragHelper = new DragHelper(dragSensitivity);
		this.setFocusableInTouchMode(true);
		obtainStyledAttributes(context, attributeSet);
	}

	/**
	 * Obtains all attributes from a specific attribute set.
	 * 
	 * @param context
	 *            The context, which should be used to obtain the attributes, as
	 *            an instance of the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void obtainStyledAttributes(final Context context,
			final AttributeSet attributeSet) {
		TypedArray typedArray = context.obtainStyledAttributes(attributeSet,
				R.styleable.Sidebar);
		try {
			obtainContentView(typedArray);
			obtainSidebarView(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	private void obtainContentView(TypedArray typedArray) {
		if (typedArray != null) {
			int contentViewId = typedArray.getResourceId(
					R.styleable.Sidebar_contentView, -1);

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			contentView = inflater.inflate(contentViewId, null);
			addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
		}
	}

	private void obtainSidebarView(TypedArray typedArray) {
		if (typedArray != null) {
			int sidebarViewId = typedArray.getResourceId(
					R.styleable.Sidebar_sidebarView, -1);

			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			sidebarView = inflater.inflate(sidebarViewId, null);
			addView(sidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			sidebarView.setBackgroundResource(android.R.color.background_dark);
			sidebarView.bringToFront();
		}
	}

	private void animateShowSidebar(final float toXDelta) {
		animateSidebar(toXDelta, createAnimationListener(true));
	}

	private void animateHideSidebar(final float toXDelta) {
		animateSidebar(toXDelta, createAnimationListener(false));
	}

	private void animateSidebar(final float toXDelta,
			final AnimationListener animationListener) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			long duration = calculateAnimationDuration(toXDelta);

			Animation contentViewAnimation = new TranslateAnimation(0, toXDelta
					* scrollRatio, 0, 0);
			contentViewAnimation.setDuration(duration);
			contentViewAnimation.setAnimationListener(animationListener);

			Animation sidebarViewAnimation = new TranslateAnimation(0,
					toXDelta, 0, 0);
			sidebarViewAnimation.setDuration(duration);

			contentView.startAnimation(contentViewAnimation);
			sidebarView.startAnimation(sidebarViewAnimation);
		}
	}

	private AnimationListener createAnimationListener(final boolean show) {
		return new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				return;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				return;
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				contentView.clearAnimation();
				sidebarView.clearAnimation();
				requestLayout();
				shown = show;

				if (shown) {
					notifyOnSidebarShown();
				} else {
					notifyOnSidebarHidden();
				}
			}

		};
	}

	private long calculateAnimationDuration(final float distance) {
		int total = width - offset - (width - sidebarWidth);
		float ratio = Math.abs(distance) / total;
		return Math.round(animationDuration * ratio);
	}

	private void notifyOnSidebarHidden() {
		for (SidebarListener listener : listeners) {
			listener.onSidebarHidden(this);
		}
	}

	private void notifyOnSidebarShown() {
		for (SidebarListener listener : listeners) {
			listener.onSidebarShown(this);
		}
	}

	private void handleMove(float x, float y) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			dragHelper.update(x);

			if (dragHelper.isDragging() && checkDragMode(x, y)) {
				int sidebarX = 0;
				int contentX = 0;

				if (getLocation() == SidebarLocation.LEFT) {
					if (isShown()) {
						sidebarX = dragHelper.getDistance();
					} else {
						sidebarX = -sidebarWidth + offset
								+ dragHelper.getDistance();
					}

					sidebarX = Math.max(-sidebarWidth + offset, sidebarX);
					sidebarX = Math.min(0, sidebarX);
					contentX = Math.round((sidebarX + sidebarWidth + offset)
							* scrollRatio);
				} else {
					if (isShown()) {
						sidebarX = (width + offset - sidebarWidth)
								+ dragHelper.getDistance();
					} else {
						sidebarX = width + dragHelper.getDistance();
					}

					sidebarX = Math
							.max(width + offset - sidebarWidth, sidebarX);
					sidebarX = Math.min(width, sidebarX);
					contentX = Math.round((sidebarX - width) * scrollRatio);
				}

				sidebarView.layout(sidebarX, sidebarView.getTop(), sidebarX
						+ sidebarWidth, sidebarView.getBottom());
				contentView.layout(contentX, contentView.getTop(), contentX
						+ width, contentView.getBottom());
			}
		}
	}

	private void handleRelease() {
		dragHelper.stopDragging();

		float threshold = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			threshold = sidebarWidth * dragThreshold;

			if (sidebarView.getRight() > threshold) {
				float toXDelta = sidebarWidth - sidebarView.getRight();
				animateShowSidebar(toXDelta);
			} else {
				float toXDelta = offset - sidebarView.getRight();
				animateHideSidebar(toXDelta);
			}
		} else {
			if (isShown()) {
				threshold = width + offset - sidebarWidth + sidebarWidth
						* dragThreshold;

			} else {
				threshold = width + offset - sidebarWidth * dragThreshold;
			}

			if (sidebarView.getLeft() < threshold) {
				float toXDelta = (width + offset - sidebarWidth)
						- sidebarView.getLeft();
				animateShowSidebar(toXDelta);
			} else {
				float toXDelta = width - sidebarView.getLeft();
				animateHideSidebar(toXDelta);
			}
		}
	}

	private void handleClick(float x, float y) {
		if (isSidebarClicked(x, y)) {
			if (showWhenSidebarIsClicked) {
				show();
			}
		} else if (isContentClicked(x, y)) {
			if (hideWhenContentIsClicked) {
				hide();
			}
		}
	}

	private boolean checkDragMode(float x, float y) {
		if (dragMode == DragMode.DISABLED) {
			return false;
		} else if (dragMode == DragMode.SIDEBAR_ONLY) {
			return isSidebarClicked(x, y);
		} else if (dragMode == DragMode.CONTENT_ONLY) {
			return isContentClicked(x, y);
		}

		return true;
	}

	private boolean isSidebarClicked(float x, float y) {
		return sidebarView.getLeft() < x && sidebarView.getRight() > x
				&& sidebarView.getTop() < y && sidebarView.getBottom() > y;
	}

	private boolean isContentClicked(float x, float y) {
		return contentView.getLeft() < x && contentView.getRight() > x
				&& contentView.getTop() < y && contentView.getBottom() > y
				&& !isSidebarClicked(x, y);
	}

	public Sidebar(Context context) {
		this(context, null);
	}

	public Sidebar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}

	public Sidebar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
	}

	public final boolean isShown() {
		return shown;
	}

	public final void show() {
		if (!isShown()) {
			if (location == SidebarLocation.LEFT) {
				animateShowSidebar(sidebarWidth - offset);
			} else {
				animateShowSidebar(-sidebarWidth + offset);
			}

		}
	}

	public final void hide() {
		if (isShown()) {
			if (location == SidebarLocation.LEFT) {
				animateHideSidebar(-sidebarWidth + offset);
			} else {
				animateHideSidebar(sidebarWidth - offset);
			}
		}
	}

	public final void toggle() {
		if (isShown()) {
			hide();
		} else {
			show();
		}
	}

	public final SidebarLocation getLocation() {
		return location;
	}

	public final void setLocation(SidebarLocation location) {
		ensureNotNull(location, "The location may not be null");
		this.location = location;
	}

	public final void addSidebarListener(SidebarListener listener) {
		ensureNotNull(listener, "The listener may not be null");
		listeners.add(listener);
	}

	public final void removeSidebarListener(SidebarListener listener) {
		listeners.remove(listener);
	}

	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP && isShown()
				&& hideOnBackButton) {
			hide();
			return true;
		}

		return false;
	}

	@Override
	public final boolean onTouchEvent(final MotionEvent event) {
		super.onTouchEvent(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_MOVE:
			handleMove(event.getX(), event.getY());
			return true;
		case MotionEvent.ACTION_UP:
			dragHelper.reset();

			if (dragHelper.isDragging()) {
				handleRelease();
			} else {
				handleClick(event.getX(), event.getY());
			}

			return true;
		}

		return false;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (getLocation() == SidebarLocation.LEFT) {
			if (isShown()) {
				int contentViewX = (int) ((sidebarWidth + offset) * scrollRatio);
				contentView.layout(contentViewX, t, contentViewX + width, b);
				sidebarView.layout(0, t, sidebarWidth, b);
			} else {
				contentView.layout(offset, t, width + offset, b);
				sidebarView.layout(-sidebarWidth + offset, t, offset, b);
			}
		} else {
			if (isShown()) {
				int contentViewX = (int) ((-sidebarWidth + offset) * scrollRatio);
				contentView.layout(contentViewX, t, contentViewX + width, b);
				sidebarView.layout(width + offset - sidebarWidth, t, width
						+ offset, b);
			} else {
				contentView.layout(0, t, width, b);
				sidebarView.layout(width, t, width + sidebarWidth, b);
			}
		}
	}

	@Override
	protected final void onMeasure(final int w, final int h) {
		super.onMeasure(w, h);
		super.measureChildren(w, h);
	}

	@Override
	protected final void measureChild(final View child, final int parentWSpec,
			final int parentHSpec) {
		super.measureChild(child, parentWSpec, parentHSpec);
		int mode = MeasureSpec.getMode(parentWSpec);

		if (child == sidebarView) {
			sidebarWidth = Math.round(getMeasuredWidth()
					* (sidebarMaxWidth / 100.0f));
			sidebarWidth = Math.min(child.getMeasuredWidth(), sidebarWidth);
			super.measureChild(child,
					MeasureSpec.makeMeasureSpec(sidebarWidth, mode),
					parentHSpec);
		} else if (child == contentView) {
			offset = (int) (getMeasuredWidth() * (sidebarOffset / 100.0f));
			width = getMeasuredWidth() - offset;
			super.measureChild(child, MeasureSpec.makeMeasureSpec(width, mode),
					parentHSpec);
		}
	}

}