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
import de.mrapp.android.sidebar.animation.ContentViewAnimation;
import de.mrapp.android.sidebar.animation.SidebarViewAnimation;
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

	protected static final DragMode DEFAULT_DRAG_MODE_WHEN_HIDDEN = DragMode.SIDEBAR_ONLY;

	protected static final DragMode DEFAULT_DRAG_MODE_WHEN_SHOWN = DragMode.BOTH;

	protected static final float DEFAULT_DRAG_THRESHOLD = 0.25f;

	protected static final float DEFAULT_DRAG_SENSITIVITY = 0.25f;

	protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

	protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

	protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICKED = true;

	protected static final int DEFAULT_CONTENT_OVERLAY_COLOR = Color.BLACK;

	protected static final float DEFAULT_CONTENT_OVERLAY_TRANSPARENCY = 0.5f;

	protected static final int DEFAULT_SHADOW_WIDTH = 16;

	protected static final int DEFAULT_SHADOW_COLOR = 0x22000000;

	protected static final boolean SHOW_SIDEBAR_BY_DEFAULT = false;

	private static final int MIN_DRAG_SENSITIVITY = 10;

	private static final int MAX_DRAG_SENSITIVITY = 260;

	private SidebarLocation location;

	private int animationDuration;

	private float sidebarWidth;

	private int maxSidebarWidth;

	private float sidebarOffset;

	private int maxSidebarOffset;

	private float scrollRatio;

	private float dragThreshold;

	private float dragSensitivity;

	private DragMode dragModeWhenHidden;

	private DragMode dragModeWhenShown;

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
			obtainDragModeWhenHidden(typedArray);
			obtainDragModeWhenShown(typedArray);
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
		sidebarBackground = typedArray.getResourceId(
				R.styleable.Sidebar_android_background, DEFAULT_BACKGROUND);
	}

	private void obtainContentOverlayColor(TypedArray typedArray) {
		setContentOverlayColor(typedArray.getColor(
				R.styleable.Sidebar_contentOverlayColor,
				DEFAULT_CONTENT_OVERLAY_COLOR));
	}

	private void obtainContentOverlayTransparency(TypedArray typedArray) {
		setContentOverlayTransparency(typedArray.getFraction(
				R.styleable.Sidebar_contentOverlayTransparency, 1, 1,
				DEFAULT_CONTENT_OVERLAY_TRANSPARENCY));
	}

	private void obtainShadowColor(TypedArray typedArray) {
		setShadowColor(typedArray.getColor(R.styleable.Sidebar_shadowColor,
				DEFAULT_SHADOW_COLOR));
	}

	private void obtainShadowWidth(TypedArray typedArray) {
		setShadowWidthInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_shadowWidth, DisplayUtil.convertDpToPixels(
						getContext(), DEFAULT_SHADOW_WIDTH)));
	}

	private void obtainSidebarView(TypedArray typedArray) {
		try {
			setSidebarView(typedArray.getResourceId(
					R.styleable.Sidebar_sidebarView, -1));
		} catch (NotFoundException e) {
			return;
		}
	}

	private void obtainContentView(TypedArray typedArray) {
		try {
			setContentView(typedArray.getResourceId(
					R.styleable.Sidebar_contentView, -1));
		} catch (NotFoundException e) {
			return;
		}
	}

	private void obtainLocation(TypedArray typedArray) {
		setLocation(SidebarLocation.fromValue(typedArray.getInt(
				R.styleable.Sidebar_location, DEFAULT_LOCATION.getValue())));
	}

	private void obtainAnimationDuration(TypedArray typedArray) {
		setAnimationDuration(typedArray.getInt(
				R.styleable.Sidebar_animationDuration,
				DEFAULT_ANIMATION_DURATION));
	}

	private void obtainSidebarWidth(TypedArray typedArray) {
		setSidebarWidth(typedArray.getFraction(
				R.styleable.Sidebar_sidebarWidth, 1, 1, DEFAULT_SIDEBAR_WIDTH));
	}

	private void obtainMaxSidebarWidth(TypedArray typedArray) {
		setMaxSidebarWidthInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_maxSidebarWidth, DEFAULT_MAX_SIDEBAR_WIDTH));
	}

	private void obtainSidebarOffset(TypedArray typedArray) {
		setSidebarOffset(typedArray
				.getFraction(R.styleable.Sidebar_sidebarOffset, 1, 1,
						DEFAULT_SIDEBAR_OFFSET));
	}

	private void obtainMaxSidebarOffset(TypedArray typedArray) {
		setMaxSidebarOffsetInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_maxSidebarOffset,
				DEFAULT_MAX_SIDEBAR_OFFSET));
	}

	private void obtainScrollRatio(TypedArray typedArray) {
		setScrollRatio(typedArray.getFraction(R.styleable.Sidebar_scrollRatio,
				1, 1, DEFAULT_SCROLL_RATIO));
	}

	private void obtainDragModeWhenHidden(TypedArray typedArray) {
		setDragModeWhenHidden(DragMode.fromValue(typedArray.getInt(
				R.styleable.Sidebar_dragModeWhenHidden,
				DEFAULT_DRAG_MODE_WHEN_HIDDEN.getValue())));
	}

	private void obtainDragModeWhenShown(TypedArray typedArray) {
		setDragModeWhenShown(DragMode.fromValue(typedArray.getInt(
				R.styleable.Sidebar_dragModeWhenShown,
				DEFAULT_DRAG_MODE_WHEN_SHOWN.getValue())));
	}

	private void obtainDragThreshold(TypedArray typedArray) {
		setDragThreshold(typedArray
				.getFraction(R.styleable.Sidebar_dragThreshold, 1, 1,
						DEFAULT_DRAG_THRESHOLD));
	}

	private void obtainDragSensitivity(final TypedArray typedArray) {
		setDragSensitivity(typedArray.getFraction(
				R.styleable.Sidebar_dragSensitivity, 1, 1,
				DEFAULT_DRAG_SENSITIVITY));
	}

	private void obtainHideOnBackButton(final TypedArray typedArray) {
		hideOnBackButton(typedArray.getBoolean(
				R.styleable.Sidebar_hideOnBackButton,
				DEFAULT_HIDE_ON_BACK_BUTTON));
	}

	private void obtainHideOnContentClick(final TypedArray typedArray) {
		hideOnContentClick(typedArray.getBoolean(
				R.styleable.Sidebar_hideOnContentClick,
				DEFAULT_HIDE_ON_CONTENT_CLICK));
	}

	private void obtainShowOnSidebarClick(TypedArray typedArray) {
		showOnSidebarClick(typedArray.getBoolean(
				R.styleable.Sidebar_showOnSidebarClick,
				DEFAULT_SHOW_ON_SIDEBAR_CLICKED));
	}

	private void obtainShowSidebar(TypedArray typedArray) {
		if (typedArray.getBoolean(R.styleable.Sidebar_showSidebar,
				SHOW_SIDEBAR_BY_DEFAULT)) {
			showSidebar();
		}
	}

	private void inflateSidebarView(Inflater inflater) {
		if (mSidebarView != null) {
			removeView(mSidebarView);
		}

		mSidebarView = new SidebarView(getContext(), inflater, getLocation(),
				sidebarBackground, shadowWidth, shadowColor);
		addView(mSidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	private void inflateContentView(Inflater inflater) {
		if (mContentView != null) {
			removeView(mContentView);
		}

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
			Animation sidebarViewAnimation = new SidebarViewAnimation(toXDelta,
					duration, animationListener);

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
		DragMode currentDragMode = dragModeWhenHidden;

		if (isSidebarShown()) {
			currentDragMode = dragModeWhenShown;
		}

		if (currentDragMode == DragMode.DISABLED) {
			return false;
		} else if (currentDragMode == DragMode.SIDEBAR_ONLY) {
			return isSidebarClicked(x);
		} else if (currentDragMode == DragMode.CONTENT_ONLY) {
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

		if (mSidebarView != null && mContentView != null) {
			mSidebarView.setLocation(location);
		}

		requestLayout();
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
		requestLayout();
	}

	public final int getMaxSidebarWidth() {
		if (maxSidebarWidth != -1) {
			return DisplayUtil.convertPixelsToDp(getContext(), maxSidebarWidth);
		} else {
			return -1;
		}
	}

	public final void setMaxSidebarWidth(final int maxSidebarWidth) {
		if (maxSidebarWidth != -1) {
			setMaxSidebarWidthInPixels(DisplayUtil.convertDpToPixels(
					getContext(), maxSidebarWidth));
		} else {
			setMaxSidebarWidthInPixels(-1);
		}
	}

	private final void setMaxSidebarWidthInPixels(final int maxSidebarWidth) {
		ensureAtLeast(maxSidebarWidth, -1,
				"The max sidebar width must be at least -1");
		this.maxSidebarWidth = maxSidebarWidth;

		if (maxSidebarWidth != -1) {
			mSidebarWidth = Math.max(mSidebarWidth, maxSidebarWidth);
		}

		requestLayout();
	}

	public final float getSidebarOffset() {
		return sidebarOffset;
	}

	public final void setSidebarOffset(final float sidebarOffset) {
		ensureAtLeast(sidebarOffset, 0, "The sidebar offset must be at least 0");
		ensureAtMaximum(sidebarOffset, 100,
				"The sidebar offset must be at maximum 100");
		this.sidebarOffset = sidebarOffset;
		requestLayout();
	}

	public final int getMaxSidebarOffset() {
		if (maxSidebarOffset != -1) {
			return DisplayUtil
					.convertPixelsToDp(getContext(), maxSidebarOffset);
		} else {
			return -1;
		}
	}

	public final void setMaxSidebarOffset(final int maxSidebarOffset) {
		if (maxSidebarOffset != -1) {
			setMaxSidebarOffsetInPixels(DisplayUtil.convertDpToPixels(
					getContext(), maxSidebarOffset));
		} else {
			setMaxSidebarOffsetInPixels(-1);
		}
	}

	private void setMaxSidebarOffsetInPixels(final int maxSidebarOffset) {
		ensureAtLeast(maxSidebarOffset, -1,
				"The max sidebar offset must be at least -1");
		this.maxSidebarOffset = maxSidebarOffset;

		if (maxSidebarOffset != -1) {
			mOffset = Math.min(mOffset, maxSidebarOffset);
		}

		requestLayout();
	}

	public final float getScrollRatio() {
		return scrollRatio;
	}

	public final void setScrollRatio(final float scrollRatio) {
		ensureAtLeast(scrollRatio, 0, "The scroll ratio must be at least 0");
		ensureAtMaximum(scrollRatio, 1, "The scroll ratio must be at maximum 1");
		this.scrollRatio = scrollRatio;
		requestLayout();
	}

	public final DragMode getDragModeWhenHidden() {
		return dragModeWhenHidden;
	}

	public final void setDragModeWhenHidden(final DragMode dragMode) {
		ensureNotNull(dragMode, "The drag mode may not be null");
		this.dragModeWhenHidden = dragMode;
	}

	public final DragMode getDragModeWhenShown() {
		return dragModeWhenShown;
	}

	public final void setDragModeWhenShown(final DragMode dragMode) {
		ensureNotNull(dragMode, "The drag mode may not be null");
		this.dragModeWhenShown = dragMode;
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
		int range = MAX_DRAG_SENSITIVITY - MIN_DRAG_SENSITIVITY;
		return Math.round((1 - getDragSensitivity()) * range
				+ MIN_DRAG_SENSITIVITY);
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

		if (mContentView != null) {
			mContentView.setOverlayColor(contentOverlayColor);
		}
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
		requestLayout();
	}

	public final int getShadowColor() {
		return shadowColor;
	}

	public final void setShadowColor(final int shadowColor) {
		this.shadowColor = shadowColor;

		if (mSidebarView != null) {
			mSidebarView.setShadowColor(shadowColor);
		}
	}

	public final int getShadowWidth() {
		return DisplayUtil.convertPixelsToDp(getContext(), shadowWidth);
	}

	public final void setShadowWidth(final int shadowWidth) {
		setShadowWidthInPixels(DisplayUtil.convertDpToPixels(getContext(),
				shadowWidth));
	}

	private void setShadowWidthInPixels(final int shadowWidth) {
		ensureAtLeast(shadowWidth, 0, "The shadow width must be at least 0");
		this.shadowWidth = shadowWidth;

		if (mSidebarView != null) {
			mSidebarView.setShadowWidth(shadowWidth);
		}
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

			if (maxSidebarWidth != -1) {
				mSidebarWidth = Math.min(maxSidebarWidth, mSidebarWidth);
			}

			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child, MeasureSpec.makeMeasureSpec(mSidebarWidth
					+ shadowWidth, mode), parentHSpec);
		} else if (child == mContentView) {
			mOffset = Math.round(getMeasuredWidth() * sidebarOffset);

			if (maxSidebarOffset != -1) {
				mOffset = Math.min(maxSidebarOffset, mOffset);
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