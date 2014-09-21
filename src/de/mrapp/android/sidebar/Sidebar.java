/*
 * AndroidSidebar Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
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
import de.mrapp.android.sidebar.animation.ContentViewResizeAnimation;
import de.mrapp.android.sidebar.animation.ContentViewScrollAnimation;
import de.mrapp.android.sidebar.animation.SidebarViewAnimation;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.sidebar.savedstate.SidebarSavedState;
import de.mrapp.android.sidebar.util.DisplayUtil;
import de.mrapp.android.sidebar.util.DragHelper;
import de.mrapp.android.sidebar.view.ContentView;
import de.mrapp.android.sidebar.view.SidebarView;

/**
 * A custom view, which allows to show a sidebar, which overlaps the view's main
 * content at a specific edge. The sidebar can be shown and hidden in an
 * animated manner by either calling an appropriate method or via dragging on
 * the device's touch screen. Furthermore there are a lot of attributes, which
 * allow to specify the appearance and behavior of the sidebar.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public class Sidebar extends ViewGroup {

	/**
	 * The default location of the sidebar.
	 */
	protected static final Location DEFAULT_LOCATION = Location.RIGHT;

	/**
	 * The default speed of the animation, which is used to show or hide the
	 * sidebar, in dp per millisecond.
	 */
	protected static final float DEFAULT_ANIMATION_SPEED = 1.5f;

	/**
	 * The default width of the sidebar in relation to the width of its parent
	 * view.
	 */
	protected static final float DEFAULT_SIDEBAR_WIDTH = 0.75f;

	/**
	 * The default maximum width of the sidebar in dp or -1, if the sidebar's
	 * width should not be restricted.
	 */
	protected static final int DEFAULT_MAX_SIDEBAR_WIDTH = -1;

	/**
	 * The default amount of space in relation to the width of the parent view,
	 * the sidebar is visible, even if it is currently hidden.
	 */
	protected static final float DEFAULT_SIDEBAR_OFFSET = 0.125f;

	/**
	 * The default maximum offset of the sidebar in dp or -1, if the sidebar's
	 * offset should not be restricted.
	 */
	protected static final int DEFAULT_MAX_SIDEBAR_OFFSET = -1;

	/**
	 * The default content mode, which specifies how the content view is handled
	 * when the sidebar becomes shown or hidden.
	 */
	protected static final ContentMode DEFAULT_CONTENT_MODE = ContentMode.SCROLL;

	/**
	 * The default ratio between the distance, the sidebar is moved by, when it
	 * becomes shown or hidden, in relation to the distance, the content is
	 * moved by.
	 */
	protected static final float DEFAULT_SCROLL_RATIO = 0.5f;

	/**
	 * The default drag mode, which specifies the region, where drag gestures
	 * are recognized, when the sidebar is currently hidden.
	 */
	protected static final DragMode DEFAULT_DRAG_MODE_WHEN_HIDDEN = DragMode.SIDEBAR_ONLY;

	/**
	 * The default drag mode, which specifies the region, where drag gestures
	 * are recognized, when the sidebar is currently shown.
	 */
	protected static final DragMode DEFAULT_DRAG_MODE_WHEN_SHOWN = DragMode.BOTH;

	/**
	 * The default distance, the sidebar has to be dragged until its state
	 * changes, in relation to the whole distance.
	 */
	protected static final float DEFAULT_DRAG_THRESHOLD = 0.25f;

	/**
	 * The default sensitivity, which specifies the distance after which
	 * dragging has an effect on the sidebar, in relation to an internal value
	 * range.
	 */
	protected static final float DEFAULT_DRAG_SENSITIVITY = 0.25f;

	/**
	 * Specifies, whether by default the sidebar should be hidden, when the
	 * device's back button is clicked, or not.
	 */
	protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

	/**
	 * Specifies, whether by default the sidebar should be hidden, when the main
	 * content is clicked by the user, or not.
	 */
	protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

	/**
	 * Specifies, whether by default the sidebar should be shown, when it is
	 * clicked by the user, or not.
	 */
	protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICKED = true;

	/**
	 * The default color of the overlay, which is shown in front of the main
	 * content, when the sidebar is shown.
	 */
	protected static final int DEFAULT_CONTENT_OVERLAY_COLOR = Color.BLACK;

	/**
	 * The default transparency of the overlay, which is shown in front of the
	 * main content, when the sidebar is shown.
	 */
	protected static final float DEFAULT_CONTENT_OVERLAY_TRANSPARENCY = 0.5f;

	/**
	 * The default width of the sidebar's shadow in dp.
	 */
	protected static final int DEFAULT_SHADOW_WIDTH = 16;

	/**
	 * The default color of the sidebar's shadow.
	 */
	protected static final int DEFAULT_SHADOW_COLOR = 0x22000000;

	/**
	 * True, if the sidebar should be shown by default, false otherwise.
	 */
	protected static final boolean SHOW_SIDEBAR_BY_DEFAULT = false;

	/**
	 * The minimum value of the internal value range, which specifies after
	 * which distance dragging has an effect on the sidebar.
	 */
	private static final int MIN_DRAG_SENSITIVITY = 10;

	/**
	 * The maximum value of the internal value range, which specifies after
	 * which distance dragging has an effect on the sidebar.
	 */
	private static final int MAX_DRAG_SENSITIVITY = 260;

	/**
	 * The location of the sidebar.
	 */
	private Location location;

	/**
	 * The speed of the animation, which is used to show or hide the sidebar, in
	 * pixels per millisecond.
	 */
	private float animationSpeed;

	/**
	 * The width of the sidebar in relation to the width of its parent view.
	 */
	private float sidebarWidth;

	/**
	 * The maximum width of the sidebar in dp or -1, if the sidebar's width
	 * should not be restricted.
	 */
	private int maxSidebarWidth;

	/**
	 * The amount of space in relation to the width of the parent view, the
	 * sidebar is visible, even if it is currently hidden.
	 */
	private float sidebarOffset;

	/**
	 * The maximum offset of the sidebar in dp or -1, if the sidebar's offset
	 * should not be restricted.
	 */
	private int maxSidebarOffset;

	/**
	 * The content mode, which specifies how the content view is handled when
	 * the sidebar becomes shown or hidden.
	 */
	private ContentMode contentMode;

	/**
	 * The ratio between the distance, the sidebar is moved by, when it becomes
	 * shown or hidden, in relation to the distance, the content is moved by. If
	 * set to 1.0, the content will be moved exactly as far as the sidebar, if
	 * set to 0.0, the content will not moved at all and each value in between
	 * causes the content to be moved by a fraction of the distance, the sidebar
	 * is moved.
	 */
	private float scrollRatio;

	/**
	 * The drag mode, which specifies the region, where drag gestures are
	 * recognized, when the sidebar is currently hidden.
	 */
	private DragMode dragModeWhenHidden;

	/**
	 * The drag mode, which specifies the region, where drag gestures are
	 * recognized, when the sidebar is currently shown.
	 */
	private DragMode dragModeWhenShown;

	/**
	 * The distance, the sidebar has to be dragged until its state changes, in
	 * relation to the whole distance.
	 */
	private float dragThreshold;

	/**
	 * The sensitivity, which specifies the distance after which dragging has an
	 * effect on the sidebar, in relation to an internal value range.
	 */
	private float dragSensitivity;

	/**
	 * True, if the sidebar should be hidden, when the device's back button is
	 * clicked, false otherwise.
	 */
	private boolean hideOnBackButton;

	/**
	 * True, if the sidebar should be hidden, when the main content is clicked
	 * by the user, false otherwise.
	 */
	private boolean hideOnContentClick;

	/**
	 * True, if the sidebar should be shown, when it is clicked by the user,
	 * false otherwise.
	 */
	private boolean showOnSidebarClick;

	/**
	 * The color of the overlay, which is shown in front of the main content,
	 * when the sidebar is shown.
	 */
	private int contentOverlayColor;

	/**
	 * The transparency of the overlay, which is shown in front of the main
	 * content, when the sidebar is shown.
	 */
	private float contentOverlayTransparency;

	/**
	 * The width of the sidebar's shadow in dp.
	 */
	private int shadowWidth;

	/**
	 * The color of the sidebar's shadow.
	 */
	private int shadowColor;

	/**
	 * True, if the sidebar is currently shown, false otherwise.
	 */
	private boolean shown;

	/**
	 * The background of the sidebar or null, if the default background is used.
	 */
	private transient Drawable sidebarBackground;

	/**
	 * A set, which contains the listeners, which have registered to be
	 * notified, when the sidebar becomes shown or hidden.
	 */
	private transient Set<SidebarListener> listeners;

	/**
	 * The view, which contains the sidebar and its shadow.
	 */
	private transient SidebarView sidebarView;

	/**
	 * The view, which contains the content and its overlay.
	 */
	private transient ContentView contentView;

	/**
	 * A helper variable, which contains the sidebar's width in pixels.
	 */
	private transient int mSidebarWidth;

	/**
	 * A helper variable, which contains the content's width in pixels.
	 */
	private transient int mContentWidth;

	/**
	 * A helper variable, which contains the sidebar's offset in pixels.
	 */
	private transient int mOffset;

	/**
	 * An instance of the class {@link DragHelper}, which is used to recognize
	 * drag gestures.
	 */
	private transient DragHelper dragHelper;

	/**
	 * Initializes the sidebar.
	 * 
	 * @param context
	 *            The context, the sidebar should belong to, as an instance of
	 *            the class {@link Context}
	 * @param attributeSet
	 *            The attribute set, the attributes should be obtained from, as
	 *            an instance of the type {@link AttributeSet}
	 */
	private void initialize(final Context context,
			final AttributeSet attributeSet) {
		this.listeners = new LinkedHashSet<>();
		this.shown = false;
		this.dragHelper = new DragHelper(calculateDragSensitivity());
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
			obtainContentMode(typedArray);
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

	/**
	 * Obtains the background of the sidebar from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the background of the sidebar should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainSidebarBackground(final TypedArray typedArray) {
		setSidebarBackground(typedArray.getResourceId(
				R.styleable.Sidebar_sidebarBackground, -1));
	}

	/**
	 * Obtains the color of the overlay, which is shown in front of the main
	 * content, when the sidebar is shown, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the color of the overlay should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainContentOverlayColor(final TypedArray typedArray) {
		setContentOverlayColor(typedArray.getColor(
				R.styleable.Sidebar_contentOverlayColor,
				DEFAULT_CONTENT_OVERLAY_COLOR));
	}

	/**
	 * Obtains the transparency of the overlay, which is shown in front of the
	 * main content, when the sidebar is shown, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the transparency of the overlay should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainContentOverlayTransparency(final TypedArray typedArray) {
		setContentOverlayTransparency(typedArray.getFraction(
				R.styleable.Sidebar_contentOverlayTransparency, 1, 1,
				DEFAULT_CONTENT_OVERLAY_TRANSPARENCY));
	}

	/**
	 * Obtains the color of the sidebar's shadow from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the color of the sidebar's shadow should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainShadowColor(final TypedArray typedArray) {
		setShadowColor(typedArray.getColor(R.styleable.Sidebar_shadowColor,
				DEFAULT_SHADOW_COLOR));
	}

	/**
	 * Obtains the width of the sidebar's shadow from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the width of the sidebar's shadow should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainShadowWidth(final TypedArray typedArray) {
		setShadowWidthInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_shadowWidth, DisplayUtil.convertDpToPixels(
						getContext(), DEFAULT_SHADOW_WIDTH)));
	}

	/**
	 * Obtains the sidebar view from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the sidebar view should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainSidebarView(final TypedArray typedArray) {
		try {
			setSidebarView(typedArray.getResourceId(
					R.styleable.Sidebar_sidebarView, -1));
		} catch (NotFoundException e) {
			return;
		}
	}

	/**
	 * Obtains the content view from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the content view should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainContentView(final TypedArray typedArray) {
		try {
			setContentView(typedArray.getResourceId(
					R.styleable.Sidebar_contentView, -1));
		} catch (NotFoundException e) {
			return;
		}
	}

	/**
	 * Obtains the location of the sidebar from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the location of the sidebar should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainLocation(final TypedArray typedArray) {
		setLocation(Location.fromValue(typedArray.getInt(
				R.styleable.Sidebar_location, DEFAULT_LOCATION.getValue())));
	}

	/**
	 * Obtains the speed of the animation, which is used to show or hide the
	 * sidebar, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the speed of the animation should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainAnimationSpeed(final TypedArray typedArray) {
		setAnimationSpeed(typedArray.getFloat(
				R.styleable.Sidebar_animationSpeed, DEFAULT_ANIMATION_SPEED));
	}

	/**
	 * Obtains the width of the sidebar in relation to the width of its parent
	 * view, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the width of the sidebar should be obtained
	 *            from, as an instance of the class {@link TypedArray}
	 */
	private void obtainSidebarWidth(final TypedArray typedArray) {
		setSidebarWidth(typedArray.getFraction(
				R.styleable.Sidebar_sidebarWidth, 1, 1, DEFAULT_SIDEBAR_WIDTH));
	}

	/**
	 * Obtains the maximum width of the sidebar from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the maximum width of the sidebar should be
	 *            obtained from, as an instance of the class {@link TypedArray}
	 */
	private void obtainMaxSidebarWidth(final TypedArray typedArray) {
		setMaxSidebarWidthInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_maxSidebarWidth, DEFAULT_MAX_SIDEBAR_WIDTH));
	}

	/**
	 * Obtains the amount of space in relation to the width of the parent view,
	 * the sidebar is visible, even if it is currently hidden, from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the offset should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainSidebarOffset(final TypedArray typedArray) {
		setSidebarOffset(typedArray
				.getFraction(R.styleable.Sidebar_sidebarOffset, 1, 1,
						DEFAULT_SIDEBAR_OFFSET));
	}

	/**
	 * Obtains the maximum offset of the sidebar from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the maximum offset should be obtained from,
	 *            as an instance of the class {@link TypedArray}
	 */
	private void obtainMaxSidebarOffset(final TypedArray typedArray) {
		setMaxSidebarOffsetInPixels(typedArray.getDimensionPixelSize(
				R.styleable.Sidebar_maxSidebarOffset,
				DEFAULT_MAX_SIDEBAR_OFFSET));
	}

	/**
	 * Obtains the content mode, which specifies how the content view is handled
	 * when the sidebar becomes shown or hidden, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the content mode should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainContentMode(final TypedArray typedArray) {
		setContentMode(ContentMode.fromValue(typedArray.getInt(
				R.styleable.Sidebar_contentMode,
				DEFAULT_CONTENT_MODE.getValue())));
	}

	/**
	 * Obtains the ratio between the distance, the sidebar is moved by when it
	 * becomes shown or hidden, in relation to the distance, the content is
	 * moved by, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the scroll ratio should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainScrollRatio(final TypedArray typedArray) {
		setScrollRatio(typedArray.getFraction(R.styleable.Sidebar_scrollRatio,
				1, 1, DEFAULT_SCROLL_RATIO));
	}

	/**
	 * Obtains the drag mode, which specifies the region, where drag gestures
	 * are recognized, when the sidebar is currently hidden, from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the drag mode should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDragModeWhenHidden(final TypedArray typedArray) {
		setDragModeWhenHidden(DragMode.fromValue(typedArray.getInt(
				R.styleable.Sidebar_dragModeWhenHidden,
				DEFAULT_DRAG_MODE_WHEN_HIDDEN.getValue())));
	}

	/**
	 * Obtains the drag mode, which specifies the region, where drag gestures
	 * are recognized, when the sidebar is currently shown, from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the drag mode should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDragModeWhenShown(final TypedArray typedArray) {
		setDragModeWhenShown(DragMode.fromValue(typedArray.getInt(
				R.styleable.Sidebar_dragModeWhenShown,
				DEFAULT_DRAG_MODE_WHEN_SHOWN.getValue())));
	}

	/**
	 * Obtains the distance, the sidebar has to be dragged until its state
	 * changes, in relation to the whole distance, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the threshold should be obtained from, as an
	 *            instance of the class {@link TypedArray}
	 */
	private void obtainDragThreshold(final TypedArray typedArray) {
		setDragThreshold(typedArray
				.getFraction(R.styleable.Sidebar_dragThreshold, 1, 1,
						DEFAULT_DRAG_THRESHOLD));
	}

	/**
	 * Obtains the sensitivity, which specifies the distance after which
	 * dragging has an effect on the sidebar, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the sensitivity should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainDragSensitivity(final TypedArray typedArray) {
		setDragSensitivity(typedArray.getFraction(
				R.styleable.Sidebar_dragSensitivity, 1, 1,
				DEFAULT_DRAG_SENSITIVITY));
	}

	/**
	 * Obtains the boolean value, which specifies, whether the sidebar should be
	 * hidden, when the device's back button is clicked, from a specific typed
	 * array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainHideOnBackButton(final TypedArray typedArray) {
		hideOnBackButton(typedArray.getBoolean(
				R.styleable.Sidebar_hideOnBackButton,
				DEFAULT_HIDE_ON_BACK_BUTTON));
	}

	/**
	 * Obtains the boolean value, which specifies, whether the sidebar should be
	 * hidden, when the main content is clicked by the user, from a specific
	 * typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainHideOnContentClick(final TypedArray typedArray) {
		hideOnContentClick(typedArray.getBoolean(
				R.styleable.Sidebar_hideOnContentClick,
				DEFAULT_HIDE_ON_CONTENT_CLICK));
	}

	/**
	 * Obtains the boolean value, which specifies, whether the sidebar should be
	 * shown, when it is clicked by the user, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainShowOnSidebarClick(final TypedArray typedArray) {
		showOnSidebarClick(typedArray.getBoolean(
				R.styleable.Sidebar_showOnSidebarClick,
				DEFAULT_SHOW_ON_SIDEBAR_CLICKED));
	}

	/**
	 * Obtains the boolean value, which specifies, whether the sidebar should be
	 * shown by default, from a specific typed array.
	 * 
	 * @param typedArray
	 *            The typed array, the boolean value should be obtained from, as
	 *            an instance of the class {@link TypedArray}
	 */
	private void obtainShowSidebar(final TypedArray typedArray) {
		if (typedArray.getBoolean(R.styleable.Sidebar_showSidebar,
				SHOW_SIDEBAR_BY_DEFAULT)) {
			showSidebar();
		}
	}

	/**
	 * Sets the width of the sidebar's shadow in pixels.
	 * 
	 * @param shadowWidth
	 *            The width, which should be set, in pixels as an
	 *            {@link Integer} value
	 */
	private void setShadowWidthInPixels(final int shadowWidth) {
		ensureAtLeast(shadowWidth, 0, "The shadow width must be at least 0");
		this.shadowWidth = shadowWidth;

		if (sidebarView != null) {
			sidebarView.setShadowWidth(shadowWidth);
		}
	}

	/**
	 * Sets the maximum width of the sidebar in pixels.
	 * 
	 * @param maxSidebarWidth
	 *            The maximum width, which should be set, in pixels as an
	 *            {@link Integer} value
	 */
	private void setMaxSidebarWidthInPixels(final int maxSidebarWidth) {
		ensureAtLeast(maxSidebarWidth, -1,
				"The max sidebar width must be at least -1");
		this.maxSidebarWidth = maxSidebarWidth;

		if (maxSidebarWidth != -1) {
			mSidebarWidth = Math.max(mSidebarWidth, maxSidebarWidth);
		}

		requestLayout();
	}

	/**
	 * Sets the maximum offset of the sidebar in pixels.
	 * 
	 * @param maxSidebarOffset
	 *            The maximum offset, which should be set, in pixels as an
	 *            {@link Integer} value
	 */
	private void setMaxSidebarOffsetInPixels(final int maxSidebarOffset) {
		ensureAtLeast(maxSidebarOffset, -1,
				"The max sidebar offset must be at least -1");
		this.maxSidebarOffset = maxSidebarOffset;

		if (maxSidebarOffset != -1) {
			mOffset = Math.min(mOffset, maxSidebarOffset);
		}

		requestLayout();
	}

	/**
	 * Inflates and adds the sidebar view by using a specific inflater.
	 * 
	 * @param inflater
	 *            The inflater, which should be used, as an instance of the type
	 *            {@link Inflater}
	 */
	private void inflateSidebarView(final Inflater inflater) {
		if (sidebarView != null) {
			removeView(sidebarView);
		}

		sidebarView = new SidebarView(getContext(), inflater, getLocation(),
				sidebarBackground, shadowWidth, shadowColor);
		addView(sidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	/**
	 * Inflates and adds the content view by using a specific inflater.
	 * 
	 * @param inflater
	 *            The inflater, which should be used, as an instance of the type
	 *            {@link Inflater}
	 */
	private void inflateContentView(final Inflater inflater) {
		if (contentView != null) {
			removeView(contentView);
		}

		contentView = new ContentView(getContext(), inflater,
				getContentOverlayColor());
		addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		bringSidebarToFront();
	}

	/**
	 * Brings the sidebar view to the front, if it has already been inflated.
	 */
	private void bringSidebarToFront() {
		if (sidebarView != null) {
			sidebarView.bringToFront();
		}
	}

	/**
	 * Animates the sidebar to become shown.
	 * 
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value
	 */
	private void animateShowSidebar(final float distance) {
		animateShowSidebar(distance, animationSpeed);
	}

	/**
	 * Animates the sidebar to become shown.
	 * 
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value
	 * @param animationSpeed
	 *            The speed of the animation in pixels per milliseconds as a
	 *            {@link Float} value
	 */
	private void animateShowSidebar(final float distance,
			final float animationSpeed) {
		animateSidebar(true, distance, animationSpeed,
				createAnimationListener(true));
	}

	/**
	 * Animates the sidebar to become hidden.
	 * 
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value
	 */
	private void animateHideSidebar(final float distance) {
		animateHideSidebar(distance, animationSpeed);
	}

	/**
	 * Animates the sidebar to become hidden.
	 * 
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value
	 * @param animationSpeed
	 *            The speed of the animation in pixels per millisecond as a
	 *            {@link Float} value
	 */
	private void animateHideSidebar(final float distance,
			final float animationSpeed) {
		animateSidebar(false, distance, animationSpeed,
				createAnimationListener(false));
	}

	/**
	 * Animates the sidebar to be moved by a specific distance.
	 * 
	 * @param show
	 *            True, if the sidebar should be shown at the end of the
	 *            animation, false otherwise
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value. If the distance is negative, the sidebar
	 *            will be moved to the left, if the distance is positive, it
	 *            will be moved to the right
	 * @param animationSpeed
	 *            The speed of the animation in pixels per millisecond as a
	 *            {@link Float} value
	 * @param animationListener
	 *            The listener, which should be notified about the animation's
	 *            progress, as an instance of the type {@link AnimationListener}
	 */
	private void animateSidebar(final boolean show, final float distance,
			final float animationSpeed,
			final AnimationListener animationListener) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			long duration = calculateAnimationDuration(distance, animationSpeed);
			Animation contentViewAnimation;

			if (getContentMode() == ContentMode.SCROLL) {
				contentViewAnimation = new ContentViewScrollAnimation(
						contentView, duration, distance, scrollRatio,
						getContentOverlayTransparency(), show);
			} else {
				contentViewAnimation = new ContentViewResizeAnimation(
						contentView, duration, distance, getLocation(),
						getContentOverlayTransparency(), show);
			}

			Animation sidebarViewAnimation = new SidebarViewAnimation(distance,
					duration, animationListener);
			contentView.startAnimation(contentViewAnimation);
			sidebarView.startAnimation(sidebarViewAnimation);
		}
	}

	/**
	 * Creates and returns a listener, which allows to handle the end of an
	 * animation, which has been used to show or hide the sidebar.
	 * 
	 * @param show
	 *            True, if the sidebar should be shown at the end of the
	 *            animation, false otherwise
	 * @return The listener, which has been created, as an instance of the type
	 *         {@link AnimationListener}
	 */
	private AnimationListener createAnimationListener(final boolean show) {
		return new AnimationListener() {

			@Override
			public void onAnimationStart(final Animation animation) {
				return;
			}

			@Override
			public void onAnimationRepeat(final Animation animation) {
				return;
			}

			@Override
			public void onAnimationEnd(final Animation animation) {
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

	/**
	 * Notifies all listeners, which have been registered to be notified, when
	 * the sidebar becomes shown or hidden, about when the sidebar has become
	 * shown.
	 */
	private void notifyOnSidebarShown() {
		for (SidebarListener listener : listeners) {
			listener.onSidebarShown(this);
		}
	}

	/**
	 * Notifies all listeners, which have been registered to be notified, when
	 * the sidebar becomes show or hidden, about when the sidebar has become
	 * hidden.
	 */
	private void notifyOnSidebarHidden() {
		for (SidebarListener listener : listeners) {
			listener.onSidebarHidden(this);
		}
	}

	/**
	 * Calculates the duration of the animation, which is used to hide or show
	 * the sidebar, depending on a specific distance and speed.
	 * 
	 * @param distance
	 *            The distance, the sidebar has to be moved by, as a
	 *            {@link Float} value
	 * @param animationSpeed
	 *            The speed of the animation in pixels per millisecond as a
	 *            {@link Float} value
	 * @return The duration of the animation in milliseconds as an
	 *         {@link Integer} value
	 */
	private int calculateAnimationDuration(final float distance,
			final float animationSpeed) {
		return Math.round(Math.abs(distance) / animationSpeed);
	}

	/**
	 * Calculates and returns the position of the sidebar's left and right edge,
	 * depending on its location and whether it is currently shown or not.
	 * 
	 * @return The position of the sidebar's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateSidebarConstraints() {
		return calculateSidebarConstraints(isSidebarShown());
	}

	/**
	 * Calculates and returns the position of the sidebar's left and right edge,
	 * depending on its location and a specific boolean value, which specifies,
	 * whether it should be shown or not.
	 * 
	 * @param shown
	 *            True, if the sidebar should be shown, false otherwise
	 * @return The position of the sidebar's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateSidebarConstraints(
			final boolean shown) {
		int leftEdge;
		int rightEdge;

		if (getLocation() == Location.LEFT) {
			if (shown) {
				leftEdge = 0;
			} else {
				leftEdge = mOffset - mSidebarWidth;
			}
		} else {
			if (shown) {
				leftEdge = getWidth() - mSidebarWidth - shadowWidth;
			} else {
				leftEdge = getWidth() - mOffset - shadowWidth;
			}
		}

		rightEdge = leftEdge + mSidebarWidth + shadowWidth;
		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location, whether the sidebar is currently
	 * shown and the current content mode.
	 * 
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateContentConstraints() {
		if (getContentMode() == ContentMode.SCROLL) {
			return calculateScrolledContentConstraints();
		} else {
			return calculateResizedContentConstraints();
		}
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location and whether the sidebar is currently
	 * shown, using the content mode <code>SCROLL</code>.
	 * 
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateScrolledContentConstraints() {
		int leftEdge;
		int rightEdge;

		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				leftEdge = mOffset
						+ Math.round((mSidebarWidth - mOffset) * scrollRatio);
			} else {
				leftEdge = mOffset;
			}
		} else {
			if (isSidebarShown()) {
				leftEdge = Math.round((-mSidebarWidth + mOffset) * scrollRatio);
			} else {
				leftEdge = 0;
			}
		}

		rightEdge = leftEdge + mContentWidth;
		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location and whether the sidebar is currently
	 * shown, using the content mode <code>RESIZE</code>.
	 * 
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateResizedContentConstraints() {
		int leftEdge;
		int rightEdge;

		if (getLocation() == Location.LEFT) {
			rightEdge = getWidth();

			if (isSidebarShown()) {
				leftEdge = mSidebarWidth;
			} else {
				leftEdge = mOffset;
			}
		} else {
			leftEdge = 0;

			if (isSidebarShown()) {
				rightEdge = getWidth() - mSidebarWidth;
			} else {
				rightEdge = getWidth() - mOffset;
			}
		}

		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Calculates and returns the position of the sidebar's left and right edge,
	 * depending on its location, while the user performs a drag gesture.
	 * 
	 * @return The position of the sidebar's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateSidebarConstraintsWhileDragging() {
		Pair<Integer, Integer> shownSidebarConstraints = calculateSidebarConstraints(true);
		Pair<Integer, Integer> hiddenSidebarConstraints = calculateSidebarConstraints(false);

		int leftEdge = calculateSidebarConstraints().first
				+ dragHelper.getDistance();

		if (getLocation() == Location.LEFT) {
			leftEdge = Math.max(hiddenSidebarConstraints.first, leftEdge);
			leftEdge = Math.min(shownSidebarConstraints.first, leftEdge);
		} else {
			leftEdge = Math.max(shownSidebarConstraints.first, leftEdge);
			leftEdge = Math.min(hiddenSidebarConstraints.first, leftEdge);
		}

		int rightEdge = leftEdge + mSidebarWidth + shadowWidth;
		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location and the current content mode, while
	 * the user performs a drag gesture.
	 * 
	 * @param sidebarConstraints
	 *            The current position of the sidebar's left and right edge, as
	 *            an instance of the class {@link Pair}
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateContentConstraintsWhileDragging(
			final Pair<Integer, Integer> sidebarConstraints) {
		if (getContentMode() == ContentMode.SCROLL) {
			return calculateScrolledContentConstraintsWhileDragging(sidebarConstraints);
		} else {
			return calculateResizedContentConstraintsWhileDragging(sidebarConstraints);
		}
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location, using the content mode
	 * <code>SCROLL</code>, while the user performs a drag gesture.
	 * 
	 * @param sidebarConstraints
	 *            The current position of the sidebar's left and right edge, as
	 *            an instance of the class {@link Pair}
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateScrolledContentConstraintsWhileDragging(
			final Pair<Integer, Integer> sidebarConstraints) {
		int leftEdge;
		int rightEdge;

		if (getLocation() == Location.LEFT) {
			leftEdge = mOffset
					+ Math.round((sidebarConstraints.second - shadowWidth - mOffset)
							* scrollRatio);
		} else {
			leftEdge = Math
					.round((sidebarConstraints.first + shadowWidth - mContentWidth)
							* scrollRatio);
		}

		rightEdge = leftEdge + mContentWidth;
		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Calculates and returns the position of the content's left and right edge,
	 * depending on the sidebar's location, using the content mode
	 * <code>RESIZE</code>, while the user performs a drag gesture.
	 * 
	 * @param sidebarConstraints
	 *            The current position of the sidebar's left and right edge, as
	 *            an instance of the class {@link Pair}
	 * @return The position of the content's left and right edge as an instance
	 *         of the class {@link Pair}
	 */
	private Pair<Integer, Integer> calculateResizedContentConstraintsWhileDragging(
			final Pair<Integer, Integer> sidebarConstraints) {
		int leftEdge;
		int rightEdge;

		if (getLocation() == Location.LEFT) {
			leftEdge = sidebarConstraints.second - shadowWidth;
			rightEdge = getWidth();
		} else {
			leftEdge = 0;
			rightEdge = sidebarConstraints.first + shadowWidth;
		}

		return new Pair<Integer, Integer>(leftEdge, rightEdge);
	}

	/**
	 * Handles when a drag gesture is performed by the user.
	 * 
	 * @param dragPosition
	 *            The current horizontal position of the drag gesture as a
	 *            {@link Float} value
	 * @return True, if the sidebar has been moved by the drag gesture, false
	 *         otherwise
	 */
	private boolean handleDrag(final float dragPosition) {
		if (contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			dragHelper.update(dragPosition);

			if (dragHelper.hasThresholdBeenReached()
					&& isDraggingAllowed(dragHelper.getStartPosition())) {
				Pair<Integer, Integer> sidebarPos = calculateSidebarConstraintsWhileDragging();
				Pair<Integer, Integer> contentPos = calculateContentConstraintsWhileDragging(sidebarPos);

				sidebarView.layout(sidebarPos.first, sidebarView.getTop(),
						sidebarPos.second, sidebarView.getBottom());

				contentView
						.setOverlayTransparency(calculateContentOverlayTransparency());
				contentView.getLayoutParams().width = contentPos.second
						- contentPos.first;
				contentView.layout(contentPos.first, contentView.getTop(),
						contentPos.second, contentView.getBottom());
				contentView.requestLayout();

				return true;
			}
		}

		return false;
	}

	/**
	 * Handles when a drag gesture has been ended by the user.
	 */
	private void handleRelease() {
		dragHelper.reset();

		float thresholdPosition = calculatePositionWhereDragThresholdIsReached();
		float speed = Math.max(dragHelper.getDragSpeed(), animationSpeed);

		if (getLocation() == Location.LEFT) {
			if (sidebarView.getRight() - shadowWidth > thresholdPosition) {
				animateShowSidebar(calculateSnapDistance(true), speed);
			} else {
				animateHideSidebar(calculateSnapDistance(false), speed);
			}
		} else {
			if (sidebarView.getLeft() + shadowWidth < thresholdPosition) {
				animateShowSidebar(calculateSnapDistance(true), speed);
			} else {
				animateHideSidebar(calculateSnapDistance(false), speed);
			}
		}
	}

	/**
	 * Handles when the sidebar or content is clicked by the user.
	 * 
	 * @param clickPosition
	 *            The horizontal position of the click as a {@link Float} value
	 */
	private void handleClick(final float clickPosition) {
		dragHelper.reset();

		if (isSidebarClicked(clickPosition)) {
			if (showOnSidebarClick) {
				showSidebar();
			}
		} else if (isContentClicked(clickPosition)) {
			if (hideOnContentClick) {
				hideSidebar();
			}
		}
	}

	/**
	 * Returns, whether a click at a specific position targets the content, or
	 * not.
	 * 
	 * @param clickPosition
	 *            The horizontal position of the click as a {@link Float} value
	 * @return True, if the content has been clicked, false otherwise
	 */
	private boolean isContentClicked(final float clickPosition) {
		return !isSidebarClicked(clickPosition);
	}

	/**
	 * Returns, whether a click at a specific position targets the sidebar, or
	 * not.
	 * 
	 * @param clickPosition
	 *            The horizontal position of the click as a {@link Float} value
	 * @return True, if the sidebar has been clicked, false otherwise
	 */
	private boolean isSidebarClicked(final float clickPosition) {
		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				return clickPosition < mSidebarWidth;
			} else {
				return clickPosition < mOffset;
			}
		} else {
			if (isSidebarShown()) {
				return clickPosition > getWidth() - mSidebarWidth;
			} else {
				return clickPosition > mContentWidth;
			}
		}
	}

	/**
	 * Returns, whether dragging at a specific position is allowed, depending on
	 * the current drag mode and whether the sidebar is currently shown or
	 * hidden, or not.
	 * 
	 * @param dragPosition
	 *            The horizontal position of the drag gesture as a {@link Float}
	 *            value
	 * @return True, if dragging is allowed, false otherwise
	 */
	private boolean isDraggingAllowed(final float dragPosition) {
		DragMode currentDragMode = dragModeWhenHidden;

		if (isSidebarShown()) {
			currentDragMode = dragModeWhenShown;
		}

		if (currentDragMode == DragMode.DISABLED) {
			return false;
		} else if (currentDragMode == DragMode.SIDEBAR_ONLY) {
			return isSidebarClicked(dragPosition);
		} else if (currentDragMode == DragMode.CONTENT_ONLY) {
			return isContentClicked(dragPosition);
		}

		return true;
	}

	/**
	 * Calculates the position, where the drag threshold is reached, depending
	 * on whether the sidebar's location and whether it is currently shown or
	 * hidden.
	 * 
	 * @return The horizontal position, where the drag threshold is reached, as
	 *         a {@link Float} value
	 */
	private float calculatePositionWhereDragThresholdIsReached() {
		float position;

		if (getLocation() == Location.LEFT) {
			if (isSidebarShown()) {
				position = mSidebarWidth
						- ((mSidebarWidth - mOffset) * dragThreshold);
			} else {
				position = mOffset
						+ ((mSidebarWidth - mOffset) * dragThreshold);
			}
		} else {
			if (isSidebarShown()) {
				position = getWidth() - mSidebarWidth
						+ ((mSidebarWidth - mOffset) * dragThreshold);

			} else {
				position = getWidth() - mOffset
						- ((mSidebarWidth - mOffset) * dragThreshold);
			}
		}

		return position;
	}

	private float calculateSnapDistance(final boolean shouldBeShown) {
		float distance = 0;

		if (getLocation() == Location.LEFT) {
			if (shouldBeShown) {
				distance = mSidebarWidth + shadowWidth - sidebarView.getRight();
			} else {
				distance = mOffset + shadowWidth - sidebarView.getRight();
			}
		} else {
			if (shouldBeShown) {
				distance = getWidth() - mSidebarWidth - shadowWidth
						- sidebarView.getLeft();
			} else {
				distance = mContentWidth - shadowWidth - sidebarView.getLeft();
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

		if (getLocation() == Location.LEFT) {
			distance = distance * -1;
		}

		return distance;
	}

	/**
	 * Calculates and returns the distance after which dragging has an effect on
	 * the sidebar in pixels. The distance depends on the current set drag
	 * sensitivity, which corresponds to an internal value range.
	 * 
	 * @return The distance after which dragging has an effect on the sidebar in
	 *         pixels as an {@link Integer} value
	 */
	private int calculateDragSensitivity() {
		int range = MAX_DRAG_SENSITIVITY - MIN_DRAG_SENSITIVITY;
		return Math.round((1 - getDragSensitivity()) * range
				+ MIN_DRAG_SENSITIVITY);
	}

	/**
	 * Calculates and returns the transparency of the content overlay, depending
	 * on the sidebar's current position and the transparency, which should be
	 * applied, when the sidebar is shown.
	 * 
	 * @return The transparency of the content overlay as a {@link Float} value.
	 *         If the transparency is 0.0, the overlay will be completely
	 *         transparent, if it is 1.0, the overlay will be not transparent at
	 *         all
	 */
	private float calculateContentOverlayTransparency() {
		float totalDistance = mSidebarWidth - mOffset;
		float distance = Math.abs(calculateSnapDistance(false));
		return getContentOverlayTransparency() * (distance / totalDistance);
	}

	public Sidebar(final Context context) {
		this(context, null);
	}

	public Sidebar(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}

	public Sidebar(final Context context, final AttributeSet attrs,
			final int defStyle) {
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

	public final void setContentView(final View contentView) {
		inflateContentView(InflaterFactory.createInflater(contentView));
	}

	public final Drawable getSidebarBackground() {
		return sidebarBackground;
	}

	public final void setSidebarBackground(final Drawable background) {
		this.sidebarBackground = background;

		if (sidebarView != null) {
			sidebarView.setSidebarBackground(sidebarBackground);
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
			sidebarView.setSidebarBackground(sidebarBackground);
		}
	}

	public final Location getLocation() {
		return location;
	}

	public final void setLocation(final Location location) {
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

	public final ContentMode getContentMode() {
		return contentMode;
	}

	public final void setContentMode(final ContentMode contentMode) {
		this.contentMode = contentMode;
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
		this.dragHelper = new DragHelper(calculateDragSensitivity());
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

	public final void addSidebarListener(final SidebarListener listener) {
		ensureNotNull(listener, "The listener may not be null");
		listeners.add(listener);
	}

	public final void removeSidebarListener(final SidebarListener listener) {
		listeners.remove(listener);
	}

	@Override
	public final boolean onKeyPreIme(final int keyCode, final KeyEvent event) {
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
			handled = handleDrag(event.getX());
			break;
		case MotionEvent.ACTION_UP:
			if (dragHelper.hasThresholdBeenReached()) {
				handleRelease();
			} else {
				handleClick(event.getX());
			}

			break;
		default:
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
			handleDrag(event.getX());
			return true;
		case MotionEvent.ACTION_UP:

			if (dragHelper.hasThresholdBeenReached()) {
				handleRelease();
			} else {
				handleClick(event.getX());
			}

			performClick();
			return true;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}

	@Override
	public final boolean performClick() {
		super.performClick();
		return true;
	}

	@Override
	protected final void onSizeChanged(final int width, final int height,
			final int oldWidth, final int oldHeigth) {
		super.onSizeChanged(width, height, oldWidth, oldHeigth);

		mSidebarWidth = Math.round(width * sidebarWidth);

		if (maxSidebarWidth != -1) {
			mSidebarWidth = Math.min(maxSidebarWidth, mSidebarWidth);
		}

		mOffset = Math.round(width * sidebarOffset);

		if (maxSidebarOffset != -1) {
			mOffset = Math.min(maxSidebarOffset, mOffset);
		}

		mContentWidth = width - mOffset;

		sidebarView.getLayoutParams().width = mSidebarWidth + shadowWidth;
		contentView.getLayoutParams().width = mContentWidth;
	}

	@Override
	protected final void onLayout(final boolean changed, final int l,
			final int t, final int r, final int b) {
		if (dragHelper.isResetted() && contentView.getAnimation() == null
				&& sidebarView.getAnimation() == null) {
			Pair<Integer, Integer> sidebarPos = calculateSidebarConstraints();
			sidebarView.layout(sidebarPos.first, t, sidebarPos.second, b);

			Pair<Integer, Integer> contentPos = calculateContentConstraints();
			contentView
					.setOverlayTransparency(calculateContentOverlayTransparency());
			contentView.getLayoutParams().width = contentPos.second
					- contentPos.first;
			contentView.layout(contentPos.first, t, contentPos.second, b);
			contentView.requestLayout();
		}
	}

	@Override
	protected final void onMeasure(final int w, final int h) {
		super.onMeasure(w, h);
		super.measureChildren(w, h);
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
		savedState.setContentMode(getContentMode());
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
			setContentMode(savedState.getContentMode());
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