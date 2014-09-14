package de.mrapp.android.sidebar;

import static de.mrapp.android.sidebar.util.Condition.ensureAtLeast;
import static de.mrapp.android.sidebar.util.Condition.ensureAtMaximum;
import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;

import java.util.LinkedHashSet;
import java.util.Set;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import de.mrapp.android.sidebar.animation.ContentViewAnimation;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.sidebar.util.DisplayUtil;
import de.mrapp.android.sidebar.util.DragHelper;
import de.mrapp.android.sidebar.view.ContentView;
import de.mrapp.android.sidebar.view.SidebarView;

public class Sidebar extends ViewGroup {

	protected static final SidebarLocation DEFAULT_LOCATION = SidebarLocation.RIGHT;

	protected static final int DEFAULT_BACKGROUND = -1;

	protected static final int DEFAULT_ANIMATION_DURATION = 250;

	protected static final float DEFAULT_SIDEBAR_WIDTH = 0.8f;

	protected static final int DEFAULT_MAX_SIDEBAR_WIDTH = -1;

	protected static final float DEFAULT_SIDEBAR_OFFSET = 0.1f;

	protected static final int DEFAULT_MAX_SIDEBAR_OFFSET = -1;

	protected static final float DEFAULT_SCROLL_RATIO = 0.5f;

	protected static final DragMode DEFAULT_DRAG_MODE = DragMode.SIDEBAR_ONLY;

	protected static final float DEFAULT_DRAG_THRESHOLD = 0.33f;

	protected static final float DEFAULT_DRAG_SENSITIVITY = 0.25f;

	protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

	protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

	protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICKED = true;

	protected static final int DEFAULT_CONTENT_OVERLAY_COLOR = Color.BLACK;

	protected static final float DEFAULT_CONTENT_OVERLAY_TRANSPARENCY = 0.5f;

	protected static final int DEFAULT_SHADOW_WIDTH = 16;

	protected static final int DEFAULT_SHADOW_COLOR = 0x22000000;

	private static final int MAX_DRAG_SENSITIVITY = 1000;

	private SidebarLocation location;

	private int animationDuration;

	private float sidebarWidth;

	private int maxSidebarWidth;

	private float sidebarOffset;

	private int maxSidebarOffset;

	private float scrollRatio;

	private float dragThreshold;

	private float dragSensitivity;

	private DragMode dragMode;

	private boolean hideOnBackButton;

	private boolean hideOnContentClick;

	private boolean showOnSidebarClick;

	private int sidebarBackground;

	private int contentOverlayColor;

	private float contentOverlayTransparency;

	private int shadowWidth;

	private int shadowColor;

	private Set<SidebarListener> mListeners;

	private SidebarView mSidebarView;

	private ContentView mContentView;

	private boolean mShown;

	private int mSidebarWidth;

	private int mContentWidth;

	private int mOffset;

	private DragHelper mDragHelper;

	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		this.mListeners = new LinkedHashSet<>();
		this.mShown = false;
		this.mDragHelper = new DragHelper(getDragSensitivityInPixels());
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
			obtainContentOverlayColor(typedArray);
			obtainContentOverlayTransparency(typedArray);
			obtainShadowColor(typedArray);
			obtainShadowWidth(typedArray);
			obtainBackground(typedArray);
			obtainLocation(typedArray);
			obtainSidebarView(typedArray);
			obtainContentView(typedArray);
			obtainAnimationDuration(typedArray);
			obtainSidebarWidth(typedArray);
			obtainMaxSidebarWidth(typedArray);
			obtainSidebarOffset(typedArray);
			obtainMaxSidebarOffset(typedArray);
			obtainScrollRatio(typedArray);
			obtainDragMode(typedArray);
			obtainDragThreshold(typedArray);
			obtainDragSensitivity(typedArray);
			obtainHideOnBackButton(typedArray);
			obtainHideOnContentClick(typedArray);
			obtainShowOnSidebarClick(typedArray);
			obtainShowSidebar(typedArray);
		} finally {
			typedArray.recycle();
		}
	}

	private void obtainBackground(TypedArray typedArray) {
		if (typedArray != null) {
			sidebarBackground = typedArray.getResourceId(
					R.styleable.Sidebar_android_background, DEFAULT_BACKGROUND);
		} else {
			sidebarBackground = DEFAULT_BACKGROUND;
		}
	}

	private void obtainContentOverlayColor(TypedArray typedArray) {
		if (typedArray != null) {
			setContentOverlayColor(typedArray.getColor(
					R.styleable.Sidebar_contentOverlayColor,
					DEFAULT_CONTENT_OVERLAY_COLOR));
		} else {
			setContentOverlayColor(DEFAULT_CONTENT_OVERLAY_COLOR);
		}
	}

	private void obtainContentOverlayTransparency(TypedArray typedArray) {
		if (typedArray != null) {
			setContentOverlayTransparency(typedArray.getFraction(
					R.styleable.Sidebar_contentOverlayTransparency, 1, 1,
					DEFAULT_CONTENT_OVERLAY_TRANSPARENCY));
		} else {
			setContentOverlayTransparency(DEFAULT_CONTENT_OVERLAY_TRANSPARENCY);
		}
	}

	private void obtainShadowColor(TypedArray typedArray) {
		if (typedArray != null) {
			setShadowColor(typedArray.getColor(R.styleable.Sidebar_shadowColor,
					DEFAULT_SHADOW_COLOR));
		} else {
			setShadowColor(DEFAULT_SHADOW_COLOR);
		}
	}

	private void obtainShadowWidth(TypedArray typedArray) {
		if (typedArray != null) {
			setShadowWidthInPixels(typedArray.getDimensionPixelSize(
					R.styleable.Sidebar_shadowWidth, DisplayUtil
							.convertDpToPixels(getContext(),
									DEFAULT_SHADOW_WIDTH)));
		} else {
			setShadowWidth(DEFAULT_SHADOW_WIDTH);
		}
	}

	private void obtainSidebarView(TypedArray typedArray) {
		if (typedArray != null) {
			try {
				setSidebarView(typedArray.getResourceId(
						R.styleable.Sidebar_sidebarView, -1));
			} catch (NotFoundException e) {
				return;
			}
		}
	}

	private void obtainContentView(TypedArray typedArray) {
		if (typedArray != null) {
			try {
				setContentView(typedArray.getResourceId(
						R.styleable.Sidebar_contentView, -1));
			} catch (NotFoundException e) {
				return;
			}
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
			setSidebarWidth(typedArray.getFraction(
					R.styleable.Sidebar_sidebarWidth, 1, 1,
					DEFAULT_SIDEBAR_WIDTH));
		} else {
			setSidebarWidth(DEFAULT_SIDEBAR_WIDTH);
		}
	}

	private void obtainMaxSidebarWidth(TypedArray typedArray) {
		if (typedArray != null) {
			setMaxSidebarWidthInPixels(typedArray.getDimensionPixelSize(
					R.styleable.Sidebar_maxSidebarWidth,
					DEFAULT_MAX_SIDEBAR_WIDTH));
		} else {
			setMaxSidebarWidth(DEFAULT_MAX_SIDEBAR_WIDTH);
		}
	}

	private void obtainSidebarOffset(TypedArray typedArray) {
		if (typedArray != null) {
			setSidebarOffset(typedArray.getFraction(
					R.styleable.Sidebar_sidebarOffset, 1, 1,
					DEFAULT_SIDEBAR_OFFSET));
		} else {
			setSidebarOffset(DEFAULT_SIDEBAR_OFFSET);
		}
	}

	private void obtainMaxSidebarOffset(TypedArray typedArray) {
		if (typedArray != null) {
			setMaxSidebarOffsetInPixels(typedArray.getDimensionPixelSize(
					R.styleable.Sidebar_maxSidebarOffset,
					DEFAULT_MAX_SIDEBAR_OFFSET));
		} else {
			setMaxSidebarOffset(DEFAULT_MAX_SIDEBAR_OFFSET);
		}
	}

	private void obtainScrollRatio(TypedArray typedArray) {
		if (typedArray != null) {
			setScrollRatio(typedArray
					.getFraction(R.styleable.Sidebar_scrollRatio, 1, 1,
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
			setDragThreshold(typedArray.getFraction(
					R.styleable.Sidebar_dragThreshold, 1, 1,
					DEFAULT_DRAG_THRESHOLD));
		} else {
			setDragThreshold(DEFAULT_DRAG_THRESHOLD);
		}
	}

	private void obtainDragSensitivity(final TypedArray typedArray) {
		if (typedArray != null) {
			setDragSensitivity(typedArray.getFraction(
					R.styleable.Sidebar_dragSensitivity, 1, 1,
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

	private void obtainShowSidebar(TypedArray typedArray) {
		if (typedArray != null) {
			if (typedArray.getBoolean(R.styleable.Sidebar_showSidebar, false)) {
				showSidebar();
			}
		}
	}

	private void inflateSidebarView(Inflater inflater) {
		mSidebarView = new SidebarView(getContext(), inflater, getLocation(),
				sidebarBackground, getShadowWidth(), getShadowColor());
		addView(mSidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	private void inflateContentView(Inflater inflater) {
		mContentView = new ContentView(getContext(), inflater,
				getContentOverlayColor());
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
		animateSidebar(true, toXDelta, createAnimationListener(true));
	}

	private void animateHideSidebar(final float toXDelta) {
		animateSidebar(false, toXDelta, createAnimationListener(false));
	}

	private void animateSidebar(final boolean show, final float toXDelta,
			final AnimationListener animationListener) {
		if (mContentView.getAnimation() == null
				&& mSidebarView.getAnimation() == null) {
			long duration = calculateAnimationDuration(toXDelta);

			Animation contentViewAnimation = new ContentViewAnimation(
					mContentView, duration, toXDelta, scrollRatio,
					getContentOverlayTransparency(), show);

			Animation sidebarViewAnimation = new TranslateAnimation(0,
					toXDelta, 0, 0);
			sidebarViewAnimation.setAnimationListener(animationListener);
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
		int total = mSidebarWidth - mOffset;
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

	private boolean handleMove(float x) {
		if (mContentView.getAnimation() == null
				&& mSidebarView.getAnimation() == null) {
			mDragHelper.update(x);

			if (mDragHelper.isDragging()
					&& checkDragMode(mDragHelper.getStartPosition())) {
				Pair<Integer, Integer> sidebarPos = calculateSidebarDragPosition();
				Pair<Integer, Integer> contentPos = calculateContentDragPosition(sidebarPos);

				mSidebarView.layout(sidebarPos.first, mSidebarView.getTop(),
						sidebarPos.second, mSidebarView.getBottom());
				mContentView.layout(contentPos.first, mContentView.getTop(),
						contentPos.second, mContentView.getBottom());
				mContentView
						.setOverlayTransparency(calculateContentOverlayTransparency());

				return true;
			}
		}

		return false;
	}

	private Pair<Integer, Integer> calculateSidebarDragPosition() {
		Pair<Integer, Integer> shownSidebarPos = calculateSidebarPosition(true);
		Pair<Integer, Integer> hiddenSidebarPos = calculateSidebarPosition(false);

		int sidebarX = calculateSidebarPosition().first
				+ mDragHelper.getDistance();

		if (getLocation() == SidebarLocation.LEFT) {
			sidebarX = Math.max(hiddenSidebarPos.first, sidebarX);
			sidebarX = Math.min(shownSidebarPos.first, sidebarX);
		} else {
			sidebarX = Math.max(shownSidebarPos.first, sidebarX);
			sidebarX = Math.min(hiddenSidebarPos.first, sidebarX);
		}

		return new Pair<Integer, Integer>(sidebarX, sidebarX + mSidebarWidth
				+ shadowWidth);
	}

	private Pair<Integer, Integer> calculateContentDragPosition(
			Pair<Integer, Integer> sidebarPosition) {
		int contentX = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			contentX = mOffset
					+ Math.round((sidebarPosition.second - shadowWidth - mOffset)
							* scrollRatio);
		} else {
			contentX = Math
					.round((sidebarPosition.first + shadowWidth - mContentWidth)
							* scrollRatio);
		}

		return new Pair<Integer, Integer>(contentX, contentX + mContentWidth);
	}

	private void handleRelease() {
		mDragHelper.stopDragging();

		float threshold = calculateDragThreshold();

		if (getLocation() == SidebarLocation.LEFT) {
			if (mSidebarView.getRight() - shadowWidth > threshold) {
				animateShowSidebar(calculateSnapDistance(true));
			} else {
				animateHideSidebar(calculateSnapDistance(false));
			}
		} else {
			if (mSidebarView.getLeft() + shadowWidth < threshold) {
				animateShowSidebar(calculateSnapDistance(true));
			} else {
				animateHideSidebar(calculateSnapDistance(false));
			}
		}
	}

	private float calculateDragThreshold() {
		float threshold = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			if (isSidebarShown()) {
				threshold = mContentWidth - (mSidebarWidth * dragThreshold);
			} else {
				threshold = mOffset + (mContentWidth * dragThreshold);
			}
		} else {
			if (isSidebarShown()) {
				threshold = (getWidth() - mSidebarWidth)
						+ (mSidebarWidth * dragThreshold);

			} else {
				threshold = mContentWidth - (mContentWidth * dragThreshold);
			}
		}

		return threshold;
	}

	private float calculateSnapDistance(boolean shouldBeShown) {
		float distance = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			if (shouldBeShown) {
				distance = mSidebarWidth + shadowWidth
						- mSidebarView.getRight();
			} else {
				distance = mOffset + shadowWidth - mSidebarView.getRight();
			}
		} else {
			if (shouldBeShown) {
				distance = getWidth() - mSidebarWidth - shadowWidth
						- mSidebarView.getLeft();
			} else {
				distance = mContentWidth - shadowWidth - mSidebarView.getLeft();
			}
		}

		return distance;
	}

	private float calculateAnimationDistance() {
		float distance = 0.0f;

		distance = mSidebarWidth - mOffset;

		if (!isSidebarShown()) {
			distance = distance * -1;
		}

		if (getLocation() == SidebarLocation.LEFT) {
			distance = distance * -1;
		}

		return distance;
	}

	private void handleClick(float x) {
		if (isSidebarClicked(x)) {
			if (showOnSidebarClick) {
				showSidebar();
			}
		} else if (isContentClicked(x)) {
			if (hideOnContentClick) {
				hideSidebar();
			}
		}
	}

	private boolean checkDragMode(float x) {
		if (dragMode == DragMode.DISABLED) {
			return false;
		} else if (dragMode == DragMode.SIDEBAR_ONLY) {
			return isSidebarClicked(x);
		} else if (dragMode == DragMode.CONTENT_ONLY) {
			return isContentClicked(x);
		}

		return true;
	}

	private boolean isSidebarClicked(float x) {
		if (getLocation() == SidebarLocation.LEFT) {
			if (isSidebarShown()) {
				return x < mSidebarWidth;
			} else {
				return x < mOffset;
			}
		} else {
			if (isSidebarShown()) {
				return x > getWidth() - mSidebarWidth;
			} else {
				return x > mContentWidth;
			}
		}
	}

	private boolean isContentClicked(float x) {
		return !isSidebarClicked(x);
	}

	private float calculateContentOverlayTransparency() {
		float totalDistance = mSidebarWidth - mOffset;
		float distance = Math.abs(calculateSnapDistance(false));
		return getContentOverlayTransparency() * (distance / totalDistance);
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

	public final boolean isSidebarShown() {
		return mShown;
	}

	public final void showSidebar() {
		if (!isSidebarShown()) {
			if (location == SidebarLocation.LEFT) {
				animateShowSidebar(calculateAnimationDistance());
			} else {
				animateShowSidebar(calculateAnimationDistance());
			}
		}
	}

	public final void hideSidebar() {
		if (isSidebarShown()) {
			if (location == SidebarLocation.LEFT) {
				animateHideSidebar(calculateAnimationDistance());
			} else {
				animateHideSidebar(calculateAnimationDistance());
			}
		}
	}

	public final void toggleSidebar() {
		if (isSidebarShown()) {
			hideSidebar();
		} else {
			showSidebar();
		}
	}

	public final View getSidebarView() {
		return mSidebarView.getSidebarView();
	}

	public final void setSidebarView(final int sidebarViewId) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarViewId));
	}

	public final void setSidebarView(final View sidebarView) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarView));
	}

	public final View getContentView() {
		return mContentView.getContentView();
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

	public final float getSidebarWidth() {
		return sidebarWidth;
	}

	public final void setSidebarWidth(final float sidebarWidth) {
		ensureAtLeast(sidebarWidth, 0, "The sidebar width must be at least 0");
		ensureAtMaximum(sidebarWidth, 100,
				"The sidebar width must be at maximum 100");
		this.sidebarWidth = sidebarWidth;
	}

	public final int getMaxSidebarWidth() {
		return maxSidebarWidth;
	}

	public final void setMaxSidebarWidth(final int maxSidebarWidth) {
		int maxSidebarWidthInPixels = -1;

		if (maxSidebarWidth != -1) {
			DisplayUtil.convertDpToPixels(getContext(), maxSidebarWidth);
		}

		setMaxSidebarWidthInPixels(maxSidebarWidthInPixels);
	}

	private final void setMaxSidebarWidthInPixels(final int maxSidebarWidth) {
		ensureAtLeast(maxSidebarWidth, -1,
				"The max sidebar width must be at least -1");
		this.maxSidebarWidth = maxSidebarWidth;
	}

	public final float getSidebarOffset() {
		return sidebarOffset;
	}

	public final void setSidebarOffset(final float sidebarOffset) {
		ensureAtLeast(sidebarOffset, 0, "The sidebar offset must be at least 0");
		ensureAtMaximum(sidebarOffset, 100,
				"The sidebar offset must be at maximum 100");
		this.sidebarOffset = sidebarOffset;
	}

	public final int getMaxSidebarOffset() {
		return maxSidebarOffset;
	}

	public final void setMaxSidebarOffset(final int maxSidebarOffset) {
		int maxSidebarOffsetInPixels = -1;

		if (maxSidebarOffset != -1) {
			maxSidebarOffsetInPixels = DisplayUtil.convertDpToPixels(
					getContext(), maxSidebarOffset);
		}

		setMaxSidebarWidthInPixels(maxSidebarOffsetInPixels);
	}

	private void setMaxSidebarOffsetInPixels(final int maxSidebarOffset) {
		ensureAtLeast(maxSidebarOffset, -1,
				"The max sidebar offset must be at least -1");
		this.maxSidebarOffset = maxSidebarOffset;
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

	public final float getDragSensitivity() {
		return dragSensitivity;
	}

	public final void setDragSensitivity(final float dragSensitivity) {
		ensureAtLeast(dragSensitivity, 0,
				"The drag sensitivity must be at least 0");
		ensureAtMaximum(dragSensitivity, 1,
				"The drag sensitivity must be at maximum 1");
		this.dragSensitivity = dragSensitivity;
		this.mDragHelper = new DragHelper(getDragSensitivityInPixels());
	}

	private int getDragSensitivityInPixels() {
		return Math.round(getDragSensitivity() * MAX_DRAG_SENSITIVITY);
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

	public final int getContentOverlayColor() {
		return contentOverlayColor;
	}

	public final void setContentOverlayColor(final int contentOverlayColor) {
		this.contentOverlayColor = contentOverlayColor;
	}

	public final float getContentOverlayTransparency() {
		return contentOverlayTransparency;
	}

	public final void setContentOverlayTransparency(
			final float contentOverlayTransparency) {
		ensureAtLeast(contentOverlayTransparency, 0,
				"The transparency must be at least 0");
		ensureAtMaximum(contentOverlayTransparency, 1,
				"The transparency must be at maximum 1");
		this.contentOverlayTransparency = contentOverlayTransparency;
	}

	public final int getShadowColor() {
		return shadowColor;
	}

	public final void setShadowColor(final int shadowColor) {
		this.shadowColor = shadowColor;
	}

	public final int getShadowWidth() {
		return shadowWidth;
	}

	public final void setShadowWidth(final int shadowWidth) {
		setShadowWidthInPixels(DisplayUtil.convertDpToPixels(getContext(),
				shadowWidth));
	}

	private void setShadowWidthInPixels(final int shadowWidth) {
		ensureAtLeast(shadowWidth, 0, "The shadow width must be at least 0");
		this.shadowWidth = shadowWidth;
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
				&& event.getAction() == KeyEvent.ACTION_UP && isSidebarShown()
				&& hideOnBackButton) {
			hideSidebar();
			return true;
		}

		return false;
	}

	@Override
	public final boolean dispatchTouchEvent(final MotionEvent event) {
		boolean handled = false;

		if (isSidebarClicked(event.getX()) && !isSidebarShown()) {
			handled = true;
		} else if (isContentClicked(event.getX()) && isSidebarShown()) {
			handled = true;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			handled = handleMove(event.getX());
			break;
		case MotionEvent.ACTION_UP:
			mDragHelper.reset();

			if (mDragHelper.isDragging()) {
				handleRelease();
			} else {
				handleClick(event.getX());
			}

			break;
		}

		if (handled) {
			return true;
		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public final boolean onTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_MOVE:
			handleMove(event.getX());
			return true;
		case MotionEvent.ACTION_UP:
			mDragHelper.reset();

			if (mDragHelper.isDragging()) {
				handleRelease();
			} else {
				handleClick(event.getX());
			}

			return true;
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Pair<Integer, Integer> sidebarPos = calculateSidebarPosition();
		mSidebarView.layout(sidebarPos.first, t, sidebarPos.second, b);

		Pair<Integer, Integer> contentPos = calculateContentPosition();
		mContentView.layout(contentPos.first, t, contentPos.second, b);
		mContentView
				.setOverlayTransparency(calculateContentOverlayTransparency());
	}

	private Pair<Integer, Integer> calculateSidebarPosition() {
		return calculateSidebarPosition(isSidebarShown());
	}

	private Pair<Integer, Integer> calculateSidebarPosition(final boolean shown) {
		int leftPos = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			if (shown) {
				leftPos = 0;
			} else {
				leftPos = mOffset - mSidebarWidth;
			}
		} else {
			if (shown) {
				leftPos = getWidth() - mSidebarWidth - shadowWidth;
			} else {
				leftPos = getWidth() - mOffset - shadowWidth;
			}
		}

		return new Pair<Integer, Integer>(leftPos, leftPos + mSidebarWidth
				+ shadowWidth);
	}

	private Pair<Integer, Integer> calculateContentPosition() {
		int leftPos = 0;

		if (getLocation() == SidebarLocation.LEFT) {
			if (isSidebarShown()) {
				leftPos = mOffset
						+ Math.round((mSidebarWidth - mOffset) * scrollRatio);
			} else {
				leftPos = mOffset;
			}
		} else {
			if (isSidebarShown()) {
				leftPos = Math.round((-mSidebarWidth + mOffset) * scrollRatio);
			} else {
				leftPos = 0;
			}
		}

		return new Pair<>(leftPos, leftPos + mContentWidth);
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
			mSidebarWidth = Math.round(getMeasuredWidth() * sidebarWidth);

			if (getMaxSidebarWidth() != -1) {
				mSidebarWidth = Math.min(getMaxSidebarWidth(), mSidebarWidth);
			}

			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child, MeasureSpec.makeMeasureSpec(mSidebarWidth
					+ shadowWidth, mode), parentHSpec);
		} else if (child == mContentView) {
			mOffset = Math.round(getMeasuredWidth() * sidebarOffset);

			if (getMaxSidebarOffset() != -1) {
				mOffset = Math.min(getMaxSidebarOffset(), mOffset);
			}

			mContentWidth = getMeasuredWidth() - mOffset;
			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child,
					MeasureSpec.makeMeasureSpec(mContentWidth, mode),
					parentHSpec);
		} else {
			super.measureChild(child, parentWSpec, parentHSpec);
		}
	}

}