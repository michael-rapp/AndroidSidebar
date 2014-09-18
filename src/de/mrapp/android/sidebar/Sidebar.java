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
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
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
import de.mrapp.android.sidebar.savedstate.SidebarSavedState;
import de.mrapp.android.sidebar.util.DisplayUtil;
import de.mrapp.android.sidebar.util.DragHelper;
import de.mrapp.android.sidebar.view.ContentView;
import de.mrapp.android.sidebar.view.SidebarView;

public class Sidebar extends ViewGroup {

	protected static final Location DEFAULT_LOCATION = Location.RIGHT;

	protected static final float DEFAULT_ANIMATION_SPEED = 1.5f;

	protected static final float DEFAULT_SIDEBAR_WIDTH = 0.75f;

	protected static final int DEFAULT_MAX_SIDEBAR_WIDTH = -1;

	protected static final float DEFAULT_SIDEBAR_OFFSET = 0.125f;

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

	private Location location;

	private float animationSpeed = 1.0f;

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

	private int contentOverlayColor;

	private float contentOverlayTransparency;

	private int shadowWidth;

	private int shadowColor;

	private boolean shown;

	private transient Drawable sidebarBackground;

	private transient Set<SidebarListener> listeners;

	private transient SidebarView sidebarView;

	private transient ContentView contentView;

	private transient int currentSidebarWidth;

	private transient int currentContentWidth;

	private transient int currentOffset;

	private transient DragHelper dragHelper;

	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		this.listeners = new LinkedHashSet<>();
		this.shown = false;
		this.dragHelper = new DragHelper(getDragSensitivityInPixels());
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
			obtainLocation(typedArray);
			obtainSidebarBackground(typedArray);
			obtainSidebarView(typedArray);
			obtainContentView(typedArray);
			obtainAnimationSpeed(typedArray);
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

	private void obtainSidebarBackground(TypedArray typedArray) {
		setSidebarBackground(typedArray.getResourceId(
				R.styleable.Sidebar_sidebarBackground, -1));
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
		setLocation(Location.fromValue(typedArray.getInt(
				R.styleable.Sidebar_location, DEFAULT_LOCATION.getValue())));
	}

	private void obtainAnimationSpeed(TypedArray typedArray) {
		setAnimationSpeed(typedArray.getFloat(
				R.styleable.Sidebar_animationSpeed, DEFAULT_ANIMATION_SPEED));
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
		if (sidebarView != null) {
			removeView(sidebarView);
		}

		sidebarView = new SidebarView(getContext(), inflater, getLocation(),
				sidebarBackground, shadowWidth, shadowColor);
		addView(sidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	private void inflateContentView(Inflater inflater) {
		if (contentView != null) {
			removeView(contentView);
		}

		contentView = new ContentView(getContext(), inflater,
				getContentOverlayColor());
		addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	private void bringSidebarToFront() {
		if (sidebarView != null) {
			sidebarView.bringToFront();
		}
	}

	private void animateShowSidebar(final float toXDelta) {
		animateShowSidebar(toXDelta, animationSpeed);
	}

	private void animateShowSidebar(final float toXDelta,
			final float animationSpeed) {
		animateSidebar(true, toXDelta, animationSpeed,
				createAnimationListener(true));
	}

	private void animateHideSidebar(final float toXDelta) {
		animateHideSidebar(toXDelta, animationSpeed);
	}

	private void animateHideSidebar(final float toXDelta,
			final float animationSpeed) {
		animateSidebar(false, toXDelta, animationSpeed,
				createAnimationListener(false));
	}

	private void animateSidebar(final boolean show, final float toXDelta,
			final float animationSpeed,
			final AnimationListener animationListener) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			long duration = calculateAnimationDuration(toXDelta, animationSpeed);

			Animation contentViewAnimation = new ContentViewAnimation(
					contentView, duration, toXDelta, scrollRatio,
					getContentOverlayTransparency(), show);
			Animation sidebarViewAnimation = new SidebarViewAnimation(toXDelta,
					duration, animationListener);

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

	private int calculateAnimationDuration(final float distance,
			final float animationSpeed) {
		return Math.round(Math.abs(distance) / animationSpeed);
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

	private boolean handleMove(float x) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			dragHelper.update(x);

			if (dragHelper.hasThresholdBeenReached()
					&& checkDragMode(dragHelper.getStartPosition())) {
				Pair<Integer, Integer> sidebarPos = calculateSidebarDragPosition();
				Pair<Integer, Integer> contentPos = calculateContentDragPosition(sidebarPos);

				sidebarView.layout(sidebarPos.first, sidebarView.getTop(),
						sidebarPos.second, sidebarView.getBottom());
				contentView.layout(contentPos.first, contentView.getTop(),
						contentPos.second, contentView.getBottom());
				contentView
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
				+ dragHelper.getDistance();

		if (getLocation() == Location.LEFT) {
			sidebarX = Math.max(hiddenSidebarPos.first, sidebarX);
			sidebarX = Math.min(shownSidebarPos.first, sidebarX);
		} else {
			sidebarX = Math.max(shownSidebarPos.first, sidebarX);
			sidebarX = Math.min(hiddenSidebarPos.first, sidebarX);
		}

		return new Pair<Integer, Integer>(sidebarX, sidebarX
				+ currentSidebarWidth + shadowWidth);
	}

	private Pair<Integer, Integer> calculateContentDragPosition(
			Pair<Integer, Integer> sidebarPosition) {
		int contentX = 0;

		if (getLocation() == Location.LEFT) {
			contentX = currentOffset
					+ Math.round((sidebarPosition.second - shadowWidth - currentOffset)
							* scrollRatio);
		} else {
			contentX = Math
					.round((sidebarPosition.first + shadowWidth - currentContentWidth)
							* scrollRatio);
		}

		return new Pair<Integer, Integer>(contentX, contentX
				+ currentContentWidth);
	}

	private void handleRelease() {
		dragHelper.reset();

		float threshold = calculateDragThreshold();
		float speed = Math.max(dragHelper.getDragSpeed(), animationSpeed);

		if (getLocation() == Location.LEFT) {
			if (sidebarView.getRight() - shadowWidth > threshold) {
				animateShowSidebar(calculateSnapDistance(true), speed);
			} else {
				animateHideSidebar(calculateSnapDistance(false), speed);
			}
		} else {
			if (sidebarView.getLeft() + shadowWidth < threshold) {
				animateShowSidebar(calculateSnapDistance(true), speed);
			} else {
				animateHideSidebar(calculateSnapDistance(false), speed);
			}
		}
	}

	private float calculateDragThreshold() {
		float threshold = 0;

		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				threshold = currentSidebarWidth
						- ((currentSidebarWidth - currentOffset) * dragThreshold);
			} else {
				threshold = currentOffset
						+ ((currentSidebarWidth - currentOffset) * dragThreshold);
			}
		} else {
			if (isSidebarShown()) {
				threshold = getWidth()
						- currentSidebarWidth
						+ ((currentSidebarWidth - currentOffset) * dragThreshold);

			} else {
				threshold = getWidth()
						- currentOffset
						- ((currentSidebarWidth - currentOffset) * dragThreshold);
			}
		}

		return threshold;
	}

	private float calculateSnapDistance(boolean shouldBeShown) {
		float distance = 0;

		if (getLocation() == Location.LEFT) {
			if (shouldBeShown) {
				distance = currentSidebarWidth + shadowWidth
						- sidebarView.getRight();
			} else {
				distance = currentOffset + shadowWidth - sidebarView.getRight();
			}
		} else {
			if (shouldBeShown) {
				distance = getWidth() - currentSidebarWidth - shadowWidth
						- sidebarView.getLeft();
			} else {
				distance = currentContentWidth - shadowWidth
						- sidebarView.getLeft();
			}
		}

		return distance;
	}

	private float calculateAnimationDistance() {
		float distance = 0.0f;

		distance = currentSidebarWidth - currentOffset;

		if (!isSidebarShown()) {
			distance = distance * -1;
		}

		if (getLocation() == Location.LEFT) {
			distance = distance * -1;
		}

		return distance;
	}

	private void handleClick(float x) {
		dragHelper.reset();

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
		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				return x < currentSidebarWidth;
			} else {
				return x < currentOffset;
			}
		} else {
			if (isSidebarShown()) {
				return x > getWidth() - currentSidebarWidth;
			} else {
				return x > currentContentWidth;
			}
		}
	}

	private boolean isContentClicked(float x) {
		return !isSidebarClicked(x);
	}

	private float calculateContentOverlayTransparency() {
		float totalDistance = currentSidebarWidth - currentOffset;
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
		return shown;
	}

	public final void showSidebar() {
		if (!isSidebarShown()) {
			if (location == Location.LEFT) {
				animateShowSidebar(calculateAnimationDistance());
			} else {
				animateShowSidebar(calculateAnimationDistance());
			}
		}
	}

	public final void hideSidebar() {
		if (isSidebarShown()) {
			if (location == Location.LEFT) {
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
		return sidebarView.getSidebarView();
	}

	public final void setSidebarView(final int sidebarViewId) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarViewId));
	}

	public final void setSidebarView(final View sidebarView) {
		inflateSidebarView(InflaterFactory.createInflater(sidebarView));
	}

	public final View getContentView() {
		return contentView.getContentView();
	}

	public final void setContentView(final int contentViewId) {
		inflateContentView(InflaterFactory.createInflater(contentViewId));
	}

	public void setContentView(final View contentView) {
		inflateContentView(InflaterFactory.createInflater(contentView));
	}

	public final Drawable getSidebarBackground() {
		return sidebarBackground;
	}

	public final void setSidebarBackground(final Drawable background) {
		this.sidebarBackground = background;

		if (sidebarView != null) {
			sidebarView.setBackgroundDrawable(sidebarBackground);
		}
	}

	public final void setSidebarBackground(final int resourceId) {
		if (resourceId == -1) {
			this.sidebarBackground = null;
		} else {
			this.sidebarBackground = getContext().getResources().getDrawable(
					resourceId);
		}

		if (sidebarView != null) {
			sidebarView.setBackgroundDrawable(sidebarBackground);
		}
	}

	public final Location getLocation() {
		return location;
	}

	public final void setLocation(Location location) {
		ensureNotNull(location, "The location may not be null");
		this.location = location;

		if (sidebarView != null && contentView != null) {
			sidebarView.setLocation(location);
		}

		requestLayout();
	}

	public final float getAnimationSpeed() {
		return DisplayUtil.convertPixelsToDp(getContext(), animationSpeed);
	}

	public final void setAnimationSpeed(final float animationSpeed) {
		this.animationSpeed = DisplayUtil.convertDpToPixels(getContext(),
				animationSpeed);
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
			currentSidebarWidth = Math
					.max(currentSidebarWidth, maxSidebarWidth);
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
			currentOffset = Math.min(currentOffset, maxSidebarOffset);
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
		this.dragHelper = new DragHelper(getDragSensitivityInPixels());
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

		if (contentView != null) {
			contentView.setOverlayColor(contentOverlayColor);
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
		this.contentOverlayTransparency = 1 - contentOverlayTransparency;
		requestLayout();
	}

	public final int getShadowColor() {
		return shadowColor;
	}

	public final void setShadowColor(final int shadowColor) {
		this.shadowColor = shadowColor;

		if (sidebarView != null) {
			sidebarView.setShadowColor(shadowColor);
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

		if (sidebarView != null) {
			sidebarView.setShadowWidth(shadowWidth);
		}
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

			if (dragHelper.hasThresholdBeenReached()) {
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

			if (dragHelper.hasThresholdBeenReached()) {
				handleRelease();
			} else {
				handleClick(event.getX());
			}

			performClick();
			return true;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public boolean performClick() {
		super.performClick();
		return true;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Pair<Integer, Integer> sidebarPos = calculateSidebarPosition();
		sidebarView.layout(sidebarPos.first, t, sidebarPos.second, b);

		Pair<Integer, Integer> contentPos = calculateContentPosition();
		contentView.layout(contentPos.first, t, contentPos.second, b);
		contentView
				.setOverlayTransparency(calculateContentOverlayTransparency());
	}

	private Pair<Integer, Integer> calculateSidebarPosition() {
		return calculateSidebarPosition(isSidebarShown());
	}

	private Pair<Integer, Integer> calculateSidebarPosition(final boolean shown) {
		int leftPos = 0;

		if (getLocation() == Location.LEFT) {
			if (shown) {
				leftPos = 0;
			} else {
				leftPos = currentOffset - currentSidebarWidth;
			}
		} else {
			if (shown) {
				leftPos = getWidth() - currentSidebarWidth - shadowWidth;
			} else {
				leftPos = getWidth() - currentOffset - shadowWidth;
			}
		}

		return new Pair<Integer, Integer>(leftPos, leftPos
				+ currentSidebarWidth + shadowWidth);
	}

	private Pair<Integer, Integer> calculateContentPosition() {
		int leftPos = 0;

		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				leftPos = currentOffset
						+ Math.round((currentSidebarWidth - currentOffset)
								* scrollRatio);
			} else {
				leftPos = currentOffset;
			}
		} else {
			if (isSidebarShown()) {
				leftPos = Math.round((-currentSidebarWidth + currentOffset)
						* scrollRatio);
			} else {
				leftPos = 0;
			}
		}

		return new Pair<>(leftPos, leftPos + currentContentWidth);
	}

	@Override
	protected final void onMeasure(final int w, final int h) {
		super.onMeasure(w, h);
		super.measureChildren(w, h);
	}

	@Override
	protected final void measureChild(final View child, final int parentWSpec,
			final int parentHSpec) {
		if (child == sidebarView) {
			currentSidebarWidth = Math.round(getMeasuredWidth() * sidebarWidth);

			if (maxSidebarWidth != -1) {
				currentSidebarWidth = Math.min(maxSidebarWidth,
						currentSidebarWidth);
			}

			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(
					child,
					MeasureSpec.makeMeasureSpec(currentSidebarWidth
							+ shadowWidth, mode), parentHSpec);
		} else if (child == contentView) {
			currentOffset = Math.round(getMeasuredWidth() * sidebarOffset);

			if (maxSidebarOffset != -1) {
				currentOffset = Math.min(maxSidebarOffset, currentOffset);
			}

			currentContentWidth = getMeasuredWidth() - currentOffset;
			int mode = MeasureSpec.getMode(parentWSpec);
			super.measureChild(child,
					MeasureSpec.makeMeasureSpec(currentContentWidth, mode),
					parentHSpec);
		} else {
			super.measureChild(child, parentWSpec, parentHSpec);
		}
	}

	@Override
	protected final Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SidebarSavedState savedState = new SidebarSavedState(superState);
		savedState.setLocation(getLocation());
		savedState.setAnimationSpeed(getAnimationSpeed());
		savedState.setSidebarWidth(getSidebarWidth());
		savedState.setMaxSidebarWidth(getMaxSidebarWidth());
		savedState.setSidebarOffset(getSidebarOffset());
		savedState.setMaxSidebarOffset(getMaxSidebarOffset());
		savedState.setScrollRatio(getScrollRatio());
		savedState.setDragThreshold(getDragThreshold());
		savedState.setDragSensitivity(getDragSensitivity());
		savedState.setDragModeWhenHidden(getDragModeWhenHidden());
		savedState.setDragModeWhenShown(getDragModeWhenShown());
		savedState.setHideOnBackButton(isHiddenOnBackButton());
		savedState.setHideOnContentClick(isHiddenOnContentClick());
		savedState.setShowOnSidebarClick(isShownOnSidebarClick());
		savedState.setContentOverlayColor(getContentOverlayColor());
		savedState
				.setContentOverlayTransparency(getContentOverlayTransparency());
		savedState.setShadowWidth(getShadowWidth());
		savedState.setShadowColor(getShadowColor());
		savedState.setShown(isSidebarShown());
		return savedState;
	}

	@Override
	protected final void onRestoreInstanceState(final Parcelable state) {
		if (state != null && state instanceof SidebarSavedState) {
			SidebarSavedState savedState = (SidebarSavedState) state;
			setLocation(savedState.getLocation());
			setAnimationSpeed(savedState.getAnimationSpeed());
			setSidebarWidth(savedState.getSidebarWidth());
			setMaxSidebarWidth(savedState.getMaxSidebarWidth());
			setSidebarOffset(savedState.getSidebarOffset());
			setMaxSidebarOffset(savedState.getMaxSidebarOffset());
			setScrollRatio(savedState.getScrollRatio());
			setDragThreshold(savedState.getDragThreshold());
			setDragSensitivity(savedState.getDragSensitivity());
			setDragModeWhenHidden(savedState.getDragModeWhenHidden());
			setDragModeWhenShown(savedState.getDragModeWhenShown());
			hideOnBackButton(savedState.isHideOnBackButton());
			hideOnContentClick(savedState.isHideOnContentClick());
			showOnSidebarClick(savedState.isShowOnSidebarClick());
			setContentOverlayColor(savedState.getContentOverlayColor());
			setContentOverlayTransparency(savedState
					.getContentOverlayTransparency());
			setShadowWidth(savedState.getShadowWidth());
			setShadowColor(savedState.getShadowColor());
			shown = savedState.isShown();
			requestLayout();
			super.onRestoreInstanceState(savedState.getSuperState());
		} else {
			super.onRestoreInstanceState(state);
		}
	}

}