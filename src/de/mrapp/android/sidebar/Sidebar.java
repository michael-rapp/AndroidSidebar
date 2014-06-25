package de.mrapp.android.sidebar;

import static de.mrapp.android.sidebar.util.Condition.ensureAtLeast;
import static de.mrapp.android.sidebar.util.Condition.ensureAtMaximum;
import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.sidebar.util.DragHelper;

public class Sidebar extends ViewGroup {

	protected static final SidebarLocation DEFAULT_LOCATION = SidebarLocation.RIGHT;

	protected static final int DEFAULT_ANIMATION_DURATION = 250;

	protected static final int DEFAULT_SIDEBAR_WIDTH = 80;

	protected static final int DEFAULT_OFFSET = 10;

	protected static final float DEFAULT_SCROLL_RATIO = 0.5f;

	protected static final DragMode DEFAULT_DRAG_MODE = DragMode.BOTH;

	protected static final float DEFAULT_DRAG_THRESHOLD = 0.33f;

	protected static final int DEFAULT_DRAG_SENSITIVITY = 1;

	protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

	protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

	protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICKED = true;

	private SidebarLocation location = SidebarLocation.RIGHT;

	private int animationDuration;

	private int sidebarWidth;

	private int offset;

	private float scrollRatio;

	private float dragThreshold;

	private int dragSensitivity;

	private DragMode dragMode;

	private boolean hideOnBackButton;

	private boolean hideOnContentClick;

	private boolean showOnSidebarClick;

	private Set<SidebarListener> mListeners;

	private View mSidebarView;

	private View mContentView;

	private boolean mShown;

	private int mSidebarWidth;

	private int mWidth;

	private int mOffset;

	private DragHelper mDragHelper;

	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		this.mListeners = new LinkedHashSet<>();
		this.mShown = false;
		this.mDragHelper = new DragHelper(dragSensitivity);
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
			obtainLocation(typedArray);
			obtainAnimationDuration(typedArray);
			obtainSidebarWidth(typedArray);
			obtainOffset(typedArray);
			obtainScrollRatio(typedArray);
			obtainDragMode(typedArray);
			obtainDragThreshold(typedArray);
			obtainDragSensitivity(typedArray);
			obtainHideOnBackButton(typedArray);
			obtainHideOnContentClick(typedArray);
			obtainShowOnSidebarClick(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	private void obtainSidebarView(TypedArray typedArray) {
		if (typedArray != null) {
			setSidebarView(typedArray.getResourceId(
					R.styleable.Sidebar_sidebarView, -1));
		}
	}

	private void obtainContentView(TypedArray typedArray) {
		if (typedArray != null) {
			setContentView(typedArray.getResourceId(
					R.styleable.Sidebar_contentView, -1));
		}
	}

	private void obtainLocation(TypedArray typedArray) {
		if (typedArray != null) {
			setLocation(SidebarLocation.fromValue(typedArray.getInt(
					R.styleable.Sidebar_location, DEFAULT_LOCATION.getValue())));
		} else {
			setLocation(DEFAULT_LOCATION);
		}
	}

	private void obtainAnimationDuration(TypedArray typedArray) {
		if (typedArray != null) {
			setAnimationDuration(typedArray.getInt(
					R.styleable.Sidebar_animationDuration,
					DEFAULT_ANIMATION_DURATION));
		} else {
			setAnimationDuration(DEFAULT_ANIMATION_DURATION);
		}
	}

	private void obtainSidebarWidth(TypedArray typedArray) {
		if (typedArray != null) {
			setSidebarWidth(typedArray.getInt(R.styleable.Sidebar_sidebarWidth,
					DEFAULT_SIDEBAR_WIDTH));
		} else {
			setSidebarWidth(DEFAULT_SIDEBAR_WIDTH);
		}
	}

	private void obtainOffset(TypedArray typedArray) {
		if (typedArray != null) {
			setOffset(typedArray.getInt(R.styleable.Sidebar_offset,
					DEFAULT_OFFSET));
		} else {
			setOffset(DEFAULT_OFFSET);
		}
	}

	private void obtainScrollRatio(TypedArray typedArray) {
		if (typedArray != null) {
			setScrollRatio(typedArray.getFloat(R.styleable.Sidebar_scrollRatio,
					DEFAULT_SCROLL_RATIO));
		} else {
			setScrollRatio(DEFAULT_SCROLL_RATIO);
		}
	}

	private void obtainDragMode(TypedArray typedArray) {
		if (typedArray != null) {
			setDragMode(DragMode
					.fromValue(typedArray.getInt(R.styleable.Sidebar_dragMode,
							DEFAULT_DRAG_MODE.getValue())));
		} else {
			setDragMode(DEFAULT_DRAG_MODE);
		}
	}

	private void obtainDragThreshold(TypedArray typedArray) {
		if (typedArray != null) {
			setDragThreshold(typedArray.getFloat(
					R.styleable.Sidebar_dragThreshold, DEFAULT_DRAG_THRESHOLD));
		} else {
			setDragThreshold(DEFAULT_DRAG_THRESHOLD);
		}
	}

	private void obtainDragSensitivity(final TypedArray typedArray) {
		if (typedArray != null) {
			setDragSensitivity(typedArray.getInt(
					R.styleable.Sidebar_dragSensitivity,
					DEFAULT_DRAG_SENSITIVITY));
		} else {
			setDragSensitivity(DEFAULT_DRAG_SENSITIVITY);
		}
	}

	private void obtainHideOnBackButton(final TypedArray typedArray) {
		if (typedArray != null) {
			hideOnBackButton(typedArray.getBoolean(
					R.styleable.Sidebar_hideOnBackButton,
					DEFAULT_HIDE_ON_BACK_BUTTON));
		} else {
			hideOnBackButton(DEFAULT_HIDE_ON_BACK_BUTTON);
		}
	}

	private void obtainHideOnContentClick(final TypedArray typedArray) {
		if (typedArray != null) {
			hideOnContentClick(typedArray.getBoolean(
					R.styleable.Sidebar_hideOnContentClick,
					DEFAULT_HIDE_ON_CONTENT_CLICK));
		} else {
			hideOnContentClick(DEFAULT_HIDE_ON_CONTENT_CLICK);
		}
	}

	private void obtainShowOnSidebarClick(TypedArray typedArray) {
		if (typedArray != null) {
			showOnSidebarClick(typedArray.getBoolean(
					R.styleable.Sidebar_showOnSidebarClick,
					DEFAULT_SHOW_ON_SIDEBAR_CLICKED));
		} else {
			showOnSidebarClick(DEFAULT_SHOW_ON_SIDEBAR_CLICKED);
		}
	}

	private void inflateSidebarView(Inflater inflater) {
		mSidebarView = inflater.inflate(getContext(), null);
		addView(mSidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();

		if (mSidebarView.getBackground() == null) {
			if (getLocation() == SidebarLocation.LEFT) {
				mSidebarView
						.setBackgroundResource(R.drawable.sidebar_left_light);
			} else {
				mSidebarView
						.setBackgroundResource(R.drawable.sidebar_right_light);
			}
		}
	}

	private void inflateContentView(Inflater inflater) {
		mContentView = inflater.inflate(getContext(), null);
		addView(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	private void bringSidebarToFront() {
		if (mSidebarView != null) {
			mSidebarView.bringToFront();
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
		if (mContentView.getAnimation() == null
				&& mSidebarView.getAnimation() == null) {
			long duration = calculateAnimationDuration(toXDelta);

			Animation contentViewAnimation = new TranslateAnimation(0, toXDelta
					* scrollRatio, 0, 0);
			contentViewAnimation.setDuration(duration);
			contentViewAnimation.setAnimationListener(animationListener);

			Animation sidebarViewAnimation = new TranslateAnimation(0,
					toXDelta, 0, 0);
			sidebarViewAnimation.setDuration(duration);

			mContentView.startAnimation(contentViewAnimation);
			mSidebarView.startAnimation(sidebarViewAnimation);
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
				mContentView.clearAnimation();
				mSidebarView.clearAnimation();
				requestLayout();
				mShown = show;

				if (mShown) {
					notifyOnSidebarShown();
				} else {
					notifyOnSidebarHidden();
				}
			}

		};
	}

	private int calculateAnimationDuration(final float distance) {
		int total = mWidth - mOffset - (mWidth - mSidebarWidth);
		float ratio = Math.abs(distance) / total;
		return Math.round(animationDuration * ratio);
	}

	private void notifyOnSidebarHidden() {
		for (SidebarListener listener : mListeners) {
			listener.onSidebarHidden(this);
		}
	}

	private void notifyOnSidebarShown() {
		for (SidebarListener listener : mListeners) {
			listener.onSidebarShown(this);
		}
	}

	private void handleMove(float x, float y) {
		if (mContentView.getAnimation() == null
				&& mSidebarView.getAnimation() == null) {
			mDragHelper.update(x);

			if (mDragHelper.isDragging() && checkDragMode(x, y)) {
				int sidebarX = 0;
				int contentX = 0;

				if (getLocation() == SidebarLocation.LEFT) {
					if (isShown()) {
						sidebarX = mDragHelper.getDistance();
					} else {
						sidebarX = -mSidebarWidth + mOffset
								+ mDragHelper.getDistance();
					}

					sidebarX = Math.max(-mSidebarWidth + mOffset, sidebarX);
					sidebarX = Math.min(0, sidebarX);
					contentX = Math.round((sidebarX + mSidebarWidth + mOffset)
							* scrollRatio);
				} else {
					if (isShown()) {
						sidebarX = (mWidth + mOffset - mSidebarWidth)
								+ mDragHelper.getDistance();
					} else {
						sidebarX = mWidth + mDragHelper.getDistance();
					}

					sidebarX = Math.max(mWidth + mOffset - mSidebarWidth,
							sidebarX);
					sidebarX = Math.min(mWidth, sidebarX);
					contentX = Math.round((sidebarX - mWidth) * scrollRatio);
				}

				mSidebarView.layout(sidebarX, mSidebarView.getTop(), sidebarX
						+ mSidebarWidth, mSidebarView.getBottom());
				mContentView.layout(contentX, mContentView.getTop(), contentX
						+ mWidth, mContentView.getBottom());
			}
		}
	}

	private void handleRelease() {
		mDragHelper.stopDragging();

		float threshold = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			threshold = mSidebarWidth * dragThreshold;

			if (mSidebarView.getRight() > threshold) {
				float toXDelta = mSidebarWidth - mSidebarView.getRight();
				animateShowSidebar(toXDelta);
			} else {
				float toXDelta = mOffset - mSidebarView.getRight();
				animateHideSidebar(toXDelta);
			}
		} else {
			if (isShown()) {
				threshold = mWidth + mOffset - mSidebarWidth + mSidebarWidth
						* dragThreshold;

			} else {
				threshold = mWidth + mOffset - mSidebarWidth * dragThreshold;
			}

			if (mSidebarView.getLeft() < threshold) {
				float toXDelta = (mWidth + mOffset - mSidebarWidth)
						- mSidebarView.getLeft();
				animateShowSidebar(toXDelta);
			} else {
				float toXDelta = mWidth - mSidebarView.getLeft();
				animateHideSidebar(toXDelta);
			}
		}
	}

	private void handleClick(float x, float y) {
		if (isSidebarClicked(x, y)) {
			if (showOnSidebarClick) {
				show();
			}
		} else if (isContentClicked(x, y)) {
			if (hideOnContentClick) {
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
		return mSidebarView.getLeft() < x && mSidebarView.getRight() > x
				&& mSidebarView.getTop() < y && mSidebarView.getBottom() > y;
	}

	private boolean isContentClicked(float x, float y) {
		return mContentView.getLeft() < x && mContentView.getRight() > x
				&& mContentView.getTop() < y && mContentView.getBottom() > y
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
		return mShown;
	}

	public final void show() {
		if (!isShown()) {
			if (location == SidebarLocation.LEFT) {
				animateShowSidebar(mSidebarWidth - mOffset);
			} else {
				animateShowSidebar(-mSidebarWidth + mOffset);
			}

		}
	}

	public final void hide() {
		if (isShown()) {
			if (location == SidebarLocation.LEFT) {
				animateHideSidebar(-mSidebarWidth + mOffset);
			} else {
				animateHideSidebar(mSidebarWidth - mOffset);
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

	public final View getSidebarView() {
		return mSidebarView;
	}

	public final void setSidebarView(final int sidebarViewId) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarViewId));
	}

	public final void setSidebarView(final View sidebarView) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarView));
	}

	public final View getContentView() {
		return mContentView;
	}

	public final void setContentView(final int contentViewId) {
		inflateContentView(InflaterFactory.createInflater(contentViewId));
	}

	public void setContentView(final View contentView) {
		inflateContentView(InflaterFactory.createInflater(contentView));
	}

	public final SidebarLocation getLocation() {
		return location;
	}

	public final void setLocation(SidebarLocation location) {
		ensureNotNull(location, "The location may not be null");
		this.location = location;
	}

	public final int getAnimationDuration() {
		return animationDuration;
	}

	public final void setAnimationDuration(final int animationDuration) {
		ensureAtLeast(animationDuration, 0,
				"The animation duration must be at least 0");
		this.animationDuration = animationDuration;
	}

	public final int getSidebarWidth() {
		return sidebarWidth;
	}

	public final void setSidebarWidth(final int width) {
		ensureAtLeast(width, 0, "The width must be at least 0");
		ensureAtMaximum(width, 100, "The width must be at maximum 100");
		this.sidebarWidth = width;
	}

	public final int getOffset() {
		return offset;
	}

	public final void setOffset(final int offset) {
		ensureAtLeast(offset, 0, "The offset must be at least 0");
		ensureAtMaximum(offset, 100, "The offset must be at maximum 100");
		this.offset = offset;
	}

	public final float getScrollRatio() {
		return scrollRatio;
	}

	public final void setScrollRatio(final float scrollRatio) {
		ensureAtLeast(scrollRatio, 0, "The scroll ratio must be at least 0");
		ensureAtMaximum(scrollRatio, 1, "The scroll ratio must be at maximum 1");
		this.scrollRatio = scrollRatio;
	}

	public final DragMode getDragMode() {
		return dragMode;
	}

	public final void setDragMode(final DragMode dragMode) {
		ensureNotNull(dragMode, "The drag mode may not be null");
		this.dragMode = dragMode;
	}

	public final float getDragThreshold() {
		return dragThreshold;
	}

	public final void setDragThreshold(final float dragThreshold) {
		ensureAtLeast(dragThreshold, 0, "The threshold must be at least 0");
		ensureAtMaximum(dragThreshold, 1, "The threshold must be at maximum 1");
		this.dragThreshold = dragThreshold;
	}

	public final int getDragSensitivity() {
		return dragSensitivity;
	}

	public final void setDragSensitivity(final int dragSensitivity) {
		ensureAtLeast(dragSensitivity, 1, "The sensitivity must be at least 1");
		this.dragSensitivity = dragSensitivity;
	}
	
	public final boolean isHiddenOnBackButton() {
		return hideOnBackButton;
	}

	public final void hideOnBackButton(final boolean hideOnBackButton) {
		this.hideOnBackButton = hideOnBackButton;
	}

	public final boolean isHiddenOnContentClick() {
		return hideOnContentClick;
	}

	public final void hideOnContentClick(final boolean hideOnContentClick) {
		this.hideOnContentClick = hideOnContentClick;
	}

	public final boolean isShownOnSidebarClick() {
		return showOnSidebarClick;
	}

	public final void showOnSidebarClick(final boolean showOnSidebarClick) {
		this.showOnSidebarClick = showOnSidebarClick;
	}

	public final void addSidebarListener(SidebarListener listener) {
		ensureNotNull(listener, "The listener may not be null");
		mListeners.add(listener);
	}

	public final void removeSidebarListener(SidebarListener listener) {
		mListeners.remove(listener);
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
			mDragHelper.reset();

			if (mDragHelper.isDragging()) {
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
				int contentViewX = (int) ((mSidebarWidth + mOffset) * scrollRatio);
				mContentView.layout(contentViewX, t, contentViewX + mWidth, b);
				mSidebarView.layout(0, t, mSidebarWidth, b);
			} else {
				mContentView.layout(mOffset, t, mWidth + mOffset, b);
				mSidebarView.layout(-mSidebarWidth + mOffset, t, mOffset, b);
			}
		} else {
			if (isShown()) {
				int contentViewX = (int) ((-mSidebarWidth + mOffset) * scrollRatio);
				mContentView.layout(contentViewX, t, contentViewX + mWidth, b);
				mSidebarView.layout(mWidth + mOffset - mSidebarWidth, t, mWidth
						+ mOffset, b);
			} else {
				mContentView.layout(0, t, mWidth, b);
				mSidebarView.layout(mWidth, t, mWidth + mSidebarWidth, b);
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
		if (child == mSidebarView) {
			mSidebarWidth = Math.round(getMeasuredWidth()
					* (sidebarWidth / 100.0f));
			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child,
					MeasureSpec.makeMeasureSpec(mSidebarWidth, mode),
					parentHSpec);
		} else if (child == mContentView) {
			mOffset = Math.round(getMeasuredWidth() * (offset / 100.0f));
			mWidth = getMeasuredWidth() - mOffset;
			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child,
					MeasureSpec.makeMeasureSpec(mWidth, mode), parentHSpec);
		} else {
			super.measureChild(child, parentWSpec, parentHSpec);
		}
	}

}