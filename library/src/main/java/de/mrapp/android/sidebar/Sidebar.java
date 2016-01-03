/*
 * AndroidSidebar Copyright 2014 - 2015 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.sidebar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.LinkedHashSet;
import java.util.Set;

import de.mrapp.android.sidebar.animation.ContentViewResizeAnimation;
import de.mrapp.android.sidebar.animation.ContentViewScrollAnimation;
import de.mrapp.android.sidebar.animation.SidebarViewAnimation;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.sidebar.savedstate.SidebarSavedState;
import de.mrapp.android.sidebar.util.DragHelper;
import de.mrapp.android.sidebar.view.ContentView;
import de.mrapp.android.sidebar.view.SidebarView;
import de.mrapp.android.util.ElevationUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureGreater;
import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.Condition.ensureSmaller;
import static de.mrapp.android.util.DisplayUtil.dpToPixels;
import static de.mrapp.android.util.DisplayUtil.pixelsToDp;

/**
 * A custom view, which allows to show a sidebar, which overlaps the view's main content and can be
 * shown or hidden in an animated manner. The sidebar may be located at left or right edge of the
 * parent view and its state can be changed by either calling an appropriate method or via dragging
 * on the device's touch screen. Furthermore there are a lot of attributes, which allow to specify
 * the appearance and behavior of the sidebar.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class Sidebar extends ViewGroup {

    /**
     * The default location of the sidebar.
     */
    protected static final Location DEFAULT_LOCATION = Location.RIGHT;

    /**
     * The default speed of the animation, which is used to show or hide the sidebar, in dp per
     * millisecond.
     */
    protected static final float DEFAULT_ANIMATION_SPEED = 1.5f;

    /**
     * The default width of the sidebar in relation to the width of its parent view.
     */
    protected static final float DEFAULT_SIDEBAR_WIDTH = 0.75f;

    /**
     * The default maximum width of the sidebar in dp or -1, if the sidebar's width should not be
     * restricted.
     */
    protected static final int DEFAULT_MAX_SIDEBAR_WIDTH = -1;

    /**
     * The default amount of space in relation to the width of the parent view, the sidebar is
     * visible, even if it is currently hidden.
     */
    protected static final float DEFAULT_SIDEBAR_OFFSET = 0.125f;

    /**
     * The default maximum offset of the sidebar in dp or -1, if the sidebar's offset should not be
     * restricted.
     */
    protected static final int DEFAULT_MAX_SIDEBAR_OFFSET = -1;

    /**
     * The default content mode, which specifies how the content view is handled when the sidebar
     * becomes shown or hidden.
     */
    protected static final ContentMode DEFAULT_CONTENT_MODE = ContentMode.SCROLL;

    /**
     * The default ratio between the distance, the sidebar is moved by, when it becomes shown or
     * hidden, in relation to the distance, the content is moved by.
     */
    protected static final float DEFAULT_SCROLL_RATIO = 0.5f;

    /**
     * The default drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently hidden.
     */
    protected static final DragMode DEFAULT_DRAG_MODE_WHEN_HIDDEN = DragMode.SIDEBAR_ONLY;

    /**
     * The default drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently shown.
     */
    protected static final DragMode DEFAULT_DRAG_MODE_WHEN_SHOWN = DragMode.BOTH;

    /**
     * The default distance, the sidebar has to be dragged until its state changes, in relation to
     * the whole distance.
     */
    protected static final float DEFAULT_DRAG_THRESHOLD = 0.25f;

    /**
     * The default sensitivity, which specifies the distance after which dragging has an effect on
     * the sidebar, in relation to an internal value range.
     */
    protected static final float DEFAULT_DRAG_SENSITIVITY = 0.25f;

    /**
     * Specifies, whether by default the sidebar should be hidden, when the device's back button is
     * clicked, or not.
     */
    protected static final boolean DEFAULT_HIDE_ON_BACK_BUTTON = true;

    /**
     * Specifies, whether by default the sidebar should be hidden, when the main content is clicked
     * by the user, or not.
     */
    protected static final boolean DEFAULT_HIDE_ON_CONTENT_CLICK = true;

    /**
     * Specifies, whether by default the sidebar should be shown, when it is clicked by the user, or
     * not.
     */
    protected static final boolean DEFAULT_SHOW_ON_SIDEBAR_CLICK = true;

    /**
     * The default color of the overlay, which is shown in front of the main content, when the
     * sidebar is shown.
     */
    protected static final int DEFAULT_CONTENT_OVERLAY_COLOR = Color.BLACK;

    /**
     * The default transparency of the overlay, which is shown in front of the main content, when
     * the sidebar is shown.
     */
    protected static final float DEFAULT_CONTENT_OVERLAY_TRANSPARENCY = 0.5f;

    /**
     * The default elevation of the sidebar in dp.
     */
    protected static final int DEFAULT_SIDEBAR_ELEVATION = 16;

    /**
     * True, if the sidebar should be shown by default, false otherwise.
     */
    protected static final boolean SHOW_SIDEBAR_BY_DEFAULT = false;

    /**
     * The minimum value of the internal value range, which specifies after which distance dragging
     * has an effect on the sidebar.
     */
    private static final int MIN_DRAG_SENSITIVITY = 10;

    /**
     * The maximum value of the internal value range, which specifies after which distance dragging
     * has an effect on the sidebar.
     */
    private static final int MAX_DRAG_SENSITIVITY = 260;

    /**
     * The tolerance in dp, which is used to determine, if a drag gesture has been started at the
     * edge of the sidebar's parent view, when using the drag mode <code>DragMode.EDGE</code>.
     */
    private static final int EDGE_DRAGGING_TOLERANCE = 4;

    /**
     * The location of the sidebar.
     */
    private Location location;

    /**
     * The speed of the animation, which is used to show or hide the sidebar, in pixels per
     * millisecond.
     */
    private float animationSpeed;

    /**
     * The width of the sidebar in relation to the width of its parent view.
     */
    private float sidebarWidth;

    /**
     * The maximum width of the sidebar in dp or -1, if the sidebar's width should not be
     * restricted.
     */
    private int maxSidebarWidth;

    /**
     * The amount of space in relation to the width of the parent view, the sidebar is visible, even
     * if it is currently hidden.
     */
    private float sidebarOffset;

    /**
     * The maximum offset of the sidebar in dp or -1, if the sidebar's offset should not be
     * restricted.
     */
    private int maxSidebarOffset;

    /**
     * The content mode, which specifies how the content view is handled when the sidebar becomes
     * shown or hidden.
     */
    private ContentMode contentMode;

    /**
     * The ratio between the distance, the sidebar is moved by, when it becomes shown or hidden, in
     * relation to the distance, the content is moved by.
     */
    private float scrollRatio;

    /**
     * The drag mode, which specifies the region, where drag gestures are recognized, when the
     * sidebar is currently hidden.
     */
    private DragMode dragModeWhenHidden;

    /**
     * The drag mode, which specifies the region, where drag gestures are recognized, when the
     * sidebar is currently shown.
     */
    private DragMode dragModeWhenShown;

    /**
     * The distance, the sidebar has to be dragged until its state changes, in relation to the whole
     * distance.
     */
    private float dragThreshold;

    /**
     * The sensitivity, which specifies the distance after which dragging has an effect on the
     * sidebar, in relation to an internal value range.
     */
    private float dragSensitivity;

    /**
     * True, if the sidebar should be hidden, when the device's back button is clicked, false
     * otherwise.
     */
    private boolean hideOnBackButton;

    /**
     * True, if the sidebar should be hidden, when the main content is clicked by the user, false
     * otherwise.
     */
    private boolean hideOnContentClick;

    /**
     * True, if the sidebar should be shown, when it is clicked by the user, false otherwise.
     */
    private boolean showOnSidebarClick;

    /**
     * The color of the overlay, which is shown in front of the main content, when the sidebar is
     * shown.
     */
    private int contentOverlayColor;

    /**
     * The transparency of the overlay, which is shown in front of the main content, when the
     * sidebar is shown.
     */
    private float contentOverlayTransparency;

    /**
     * The elevation of the sidebar in dp.
     */
    private int sidebarElevation;

    /**
     * True, if the sidebar is currently shown, false otherwise.
     */
    private boolean shown;

    /**
     * The background of the sidebar or null, if the default background is used.
     */
    private transient Drawable sidebarBackground;

    /**
     * A set, which contains the listeners, which have registered to be notified, when the sidebar
     * becomes shown or hidden.
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
     * An instance of the class {@link DragHelper}, which is used to recognize drag gestures.
     */
    private transient DragHelper dragHelper;

    /**
     * Initializes the sidebar.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void initialize(@Nullable final AttributeSet attributeSet) {
        this.listeners = new LinkedHashSet<>();
        this.shown = false;
        this.dragHelper = new DragHelper(calculateDragSensitivity());
        this.setFocusableInTouchMode(true);
        obtainStyledAttributes(attributeSet);
    }

    /**
     * Obtains all attributes from a specific attribute set.
     *
     * @param attributeSet
     *         The attribute set, the attributes should be obtained from, as an instance of the type
     *         {@link AttributeSet} or null, if no attributes should be obtained
     */
    private void obtainStyledAttributes(@Nullable final AttributeSet attributeSet) {
        TypedArray typedArray =
                getContext().obtainStyledAttributes(attributeSet, R.styleable.Sidebar);

        try {
            obtainContentOverlayColor(typedArray);
            obtainContentOverlayTransparency(typedArray);
            obtainSidebarElevation(typedArray);
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
     *         The typed array, the background of the sidebar should be obtained from, as an
     *         instance of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainSidebarBackground(@NonNull final TypedArray typedArray) {
        setSidebarBackground(typedArray.getResourceId(R.styleable.Sidebar_sidebarBackground, -1));
    }

    /**
     * Obtains the color of the overlay, which is shown in front of the main content, when the
     * sidebar is shown, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the color of the overlay should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainContentOverlayColor(@NonNull final TypedArray typedArray) {
        setContentOverlayColor(typedArray
                .getColor(R.styleable.Sidebar_contentOverlayColor, DEFAULT_CONTENT_OVERLAY_COLOR));
    }

    /**
     * Obtains the transparency of the overlay, which is shown in front of the main content, when
     * the sidebar is shown, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the transparency of the overlay should be obtained from, as an
     *         instance of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainContentOverlayTransparency(@NonNull final TypedArray typedArray) {
        setContentOverlayTransparency(typedArray
                .getFraction(R.styleable.Sidebar_contentOverlayTransparency, 1, 1,
                        DEFAULT_CONTENT_OVERLAY_TRANSPARENCY));
    }

    /**
     * Obtains the elevation of the sidebar from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the elevation of the sidebar should be obtained from, as an instance
     *         of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainSidebarElevation(@NonNull final TypedArray typedArray) {
        setSidebarElevation(pixelsToDp(getContext(), typedArray
                .getDimensionPixelSize(R.styleable.Sidebar_sidebarElevation,
                        dpToPixels(getContext(), DEFAULT_SIDEBAR_ELEVATION))));
    }

    /**
     * Obtains the sidebar view from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the sidebar view should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainSidebarView(@NonNull final TypedArray typedArray) {
        try {
            setSidebarView(typedArray.getResourceId(R.styleable.Sidebar_sidebarView, -1));
        } catch (NotFoundException e) {

        }
    }

    /**
     * Obtains the content view from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the content view should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainContentView(@NonNull final TypedArray typedArray) {
        try {
            setContentView(typedArray.getResourceId(R.styleable.Sidebar_contentView, -1));
        } catch (NotFoundException e) {

        }
    }

    /**
     * Obtains the location of the sidebar from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the location of the sidebar should be obtained from, as an instance
     *         of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainLocation(@NonNull final TypedArray typedArray) {
        setLocation(Location.fromValue(
                typedArray.getInt(R.styleable.Sidebar_location, DEFAULT_LOCATION.getValue())));
    }

    /**
     * Obtains the speed of the animation, which is used to show or hide the sidebar, from a
     * specific typed array.
     *
     * @param typedArray
     *         The typed array, the speed of the animation should be obtained from, as an instance
     *         of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainAnimationSpeed(@NonNull final TypedArray typedArray) {
        setAnimationSpeed(
                typedArray.getFloat(R.styleable.Sidebar_animationSpeed, DEFAULT_ANIMATION_SPEED));
    }

    /**
     * Obtains the width of the sidebar in relation to the width of its parent view, from a specific
     * typed array.
     *
     * @param typedArray
     *         The typed array, the width of the sidebar should be obtained from, as an instance of
     *         the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainSidebarWidth(@NonNull final TypedArray typedArray) {
        setSidebarWidth(typedArray
                .getFraction(R.styleable.Sidebar_sidebarWidth, 1, 1, DEFAULT_SIDEBAR_WIDTH));
    }

    /**
     * Obtains the maximum width of the sidebar from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the maximum width of the sidebar should be obtained from, as an
     *         instance of the class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMaxSidebarWidth(@NonNull final TypedArray typedArray) {
        setMaxSidebarWidthInPixels(typedArray
                .getDimensionPixelSize(R.styleable.Sidebar_maxSidebarWidth,
                        DEFAULT_MAX_SIDEBAR_WIDTH));
    }

    /**
     * Obtains the amount of space in relation to the width of the parent view, the sidebar is
     * visible, even if it is currently hidden, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the offset should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainSidebarOffset(@NonNull final TypedArray typedArray) {
        setSidebarOffset(typedArray
                .getFraction(R.styleable.Sidebar_sidebarOffset, 1, 1, DEFAULT_SIDEBAR_OFFSET));
    }

    /**
     * Obtains the maximum offset of the sidebar from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the maximum offset should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainMaxSidebarOffset(@NonNull final TypedArray typedArray) {
        setMaxSidebarOffsetInPixels(typedArray
                .getDimensionPixelSize(R.styleable.Sidebar_maxSidebarOffset,
                        DEFAULT_MAX_SIDEBAR_OFFSET));
    }

    /**
     * Obtains the content mode, which specifies how the content view is handled when the sidebar
     * becomes shown or hidden, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the content mode should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainContentMode(@NonNull final TypedArray typedArray) {
        setContentMode(ContentMode.fromValue(typedArray
                .getInt(R.styleable.Sidebar_contentMode, DEFAULT_CONTENT_MODE.getValue())));
    }

    /**
     * Obtains the ratio between the distance, the sidebar is moved by when it becomes shown or
     * hidden, in relation to the distance, the content is moved by, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the scroll ratio should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainScrollRatio(@NonNull final TypedArray typedArray) {
        setScrollRatio(typedArray
                .getFraction(R.styleable.Sidebar_scrollRatio, 1, 1, DEFAULT_SCROLL_RATIO));
    }

    /**
     * Obtains the drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently hidden, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the drag mode should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDragModeWhenHidden(@NonNull final TypedArray typedArray) {
        setDragModeWhenHidden(DragMode.fromValue(typedArray
                .getInt(R.styleable.Sidebar_dragModeWhenHidden,
                        DEFAULT_DRAG_MODE_WHEN_HIDDEN.getValue())));
    }

    /**
     * Obtains the drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently shown, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the drag mode should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDragModeWhenShown(@NonNull final TypedArray typedArray) {
        setDragModeWhenShown(DragMode.fromValue(typedArray
                .getInt(R.styleable.Sidebar_dragModeWhenShown,
                        DEFAULT_DRAG_MODE_WHEN_SHOWN.getValue())));
    }

    /**
     * Obtains the distance, the sidebar has to be dragged until its state changes, in relation to
     * the whole distance, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the threshold should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDragThreshold(@NonNull final TypedArray typedArray) {
        setDragThreshold(typedArray
                .getFraction(R.styleable.Sidebar_dragThreshold, 1, 1, DEFAULT_DRAG_THRESHOLD));
    }

    /**
     * Obtains the sensitivity, which specifies the distance after which dragging has an effect on
     * the sidebar, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the sensitivity should be obtained from, as an instance of the class
     *         {@link TypedArray}. The typed array may not be null
     */
    private void obtainDragSensitivity(@NonNull final TypedArray typedArray) {
        setDragSensitivity(typedArray
                .getFraction(R.styleable.Sidebar_dragSensitivity, 1, 1, DEFAULT_DRAG_SENSITIVITY));
    }

    /**
     * Obtains the boolean value, which specifies, whether the sidebar should be hidden, when the
     * device's back button is clicked, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainHideOnBackButton(@NonNull final TypedArray typedArray) {
        hideOnBackButton(typedArray
                .getBoolean(R.styleable.Sidebar_hideOnBackButton, DEFAULT_HIDE_ON_BACK_BUTTON));
    }

    /**
     * Obtains the boolean value, which specifies, whether the sidebar should be hidden, when the
     * main content is clicked by the user, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainHideOnContentClick(@NonNull final TypedArray typedArray) {
        hideOnContentClick(typedArray
                .getBoolean(R.styleable.Sidebar_hideOnContentClick, DEFAULT_HIDE_ON_CONTENT_CLICK));
    }

    /**
     * Obtains the boolean value, which specifies, whether the sidebar should be shown, when it is
     * clicked by the user, from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowOnSidebarClick(@NonNull final TypedArray typedArray) {
        showOnSidebarClick(typedArray
                .getBoolean(R.styleable.Sidebar_showOnSidebarClick, DEFAULT_SHOW_ON_SIDEBAR_CLICK));
    }

    /**
     * Obtains the boolean value, which specifies, whether the sidebar should be shown by default,
     * from a specific typed array.
     *
     * @param typedArray
     *         The typed array, the boolean value should be obtained from, as an instance of the
     *         class {@link TypedArray}. The typed array may not be null
     */
    private void obtainShowSidebar(@NonNull final TypedArray typedArray) {
        if (typedArray.getBoolean(R.styleable.Sidebar_showSidebar, SHOW_SIDEBAR_BY_DEFAULT)) {
            showSidebar();
        }
    }

    /**
     * Sets the maximum width of the sidebar in pixels.
     *
     * @param maxSidebarWidth
     *         The maximum width, which should be set, in pixels as an {@link Integer} value
     */
    private void setMaxSidebarWidthInPixels(final int maxSidebarWidth) {
        ensureAtLeast(maxSidebarWidth, -1, "The max sidebar width must be at least -1");
        this.maxSidebarWidth = maxSidebarWidth;
        measureSidebarWidth();
        requestLayout();
    }

    /**
     * Sets the maximum offset of the sidebar in pixels.
     *
     * @param maxSidebarOffset
     *         The maximum offset, which should be set, in pixels as an {@link Integer} value
     */
    private void setMaxSidebarOffsetInPixels(final int maxSidebarOffset) {
        ensureAtLeast(maxSidebarOffset, -1, "The max sidebar offset must be at least -1");
        this.maxSidebarOffset = maxSidebarOffset;
        measureSidebarOffset();
        requestLayout();
    }

    /**
     * Inflates and adds the sidebar view by using a specific inflater.
     *
     * @param inflater
     *         The inflater, which should be used, as an instance of the type {@link Inflater}. The
     *         inflater may not be null
     */
    private void inflateSidebarView(@NonNull final Inflater inflater) {
        if (sidebarView != null) {
            removeView(sidebarView);
        }

        sidebarView = new SidebarView(getContext(), inflater, getLocation(), sidebarBackground,
                sidebarElevation);
        addView(sidebarView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        bringSidebarToFront();
    }

    /**
     * Inflates and adds the content view by using a specific inflater.
     *
     * @param inflater
     *         The inflater, which should be used, as an instance of the type {@link Inflater}. The
     *         inflater may not be null
     */
    private void inflateContentView(@NonNull final Inflater inflater) {
        if (contentView != null) {
            removeView(contentView);
        }

        contentView = new ContentView(getContext(), inflater, getContentOverlayColor());
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
     *         The distance, the sidebar has to be moved by, as a {@link Float} value
     * @param interpolator
     *         The interpolator, which should be used by the animation, as an instance of the type
     *         {@link Interpolator}. The interpolator may not be null
     */
    private void animateShowSidebar(final float distance,
                                    @NonNull final Interpolator interpolator) {
        animateShowSidebar(distance, animationSpeed, interpolator);
    }

    /**
     * Animates the sidebar to become shown.
     *
     * @param distance
     *         The distance, the sidebar has to be moved by, as a {@link Float} value
     * @param animationSpeed
     *         The speed of the animation in pixels per milliseconds as a {@link Float} value
     * @param interpolator
     *         The interpolator, which should be used by the animation, as an instance of the type
     *         {@link Interpolator}. The interpolator may not be null
     */
    private void animateShowSidebar(final float distance, final float animationSpeed,
                                    @NonNull final Interpolator interpolator) {
        animateSidebar(true, distance, animationSpeed, createAnimationListener(true), interpolator);
    }

    /**
     * Animates the sidebar to become hidden.
     *
     * @param distance
     *         The distance, the sidebar has to be moved by, as a {@link Float} value
     * @param interpolator
     *         The interpolator, which should be used by the animation, as an instance of the type
     *         {@link Interpolator}. The interpolator may not be null
     */
    private void animateHideSidebar(final float distance,
                                    @NonNull final Interpolator interpolator) {
        animateHideSidebar(distance, animationSpeed, interpolator);
    }

    /**
     * Animates the sidebar to become hidden.
     *
     * @param distance
     *         The distance, the sidebar has to be moved by, as a {@link Float} value
     * @param animationSpeed
     *         The speed of the animation in pixels per millisecond as a {@link Float} value
     * @param interpolator
     *         The interpolator, which should be used by the animation, as an instance of the type
     *         {@link Interpolator}. The interpolator may not be null
     */
    private void animateHideSidebar(final float distance, final float animationSpeed,
                                    @NonNull final Interpolator interpolator) {
        animateSidebar(false, distance, animationSpeed, createAnimationListener(false),
                interpolator);
    }

    /**
     * Animates the sidebar to be moved by a specific distance.
     *
     * @param show
     *         True, if the sidebar should be shown at the end of the animation, false otherwise
     * @param distance
     *         The distance, the sidebar has to be moved by, as a {@link Float} value. If the
     *         distance is negative, the sidebar will be moved to the left, if the distance is
     *         positive, it will be moved to the right
     * @param animationSpeed
     *         The speed of the animation in pixels per millisecond as a {@link Float} value
     * @param animationListener
     *         The listener, which should be notified about the animation's progress, as an instance
     *         of the type {@link AnimationListener}. The listener may not be null
     * @param interpolator
     *         The interpolator, which should be used by the animation, as an instance of the type
     *         {@link Interpolator}. The interpolator may not be null
     */
    private void animateSidebar(final boolean show, final float distance,
                                final float animationSpeed,
                                @NonNull final AnimationListener animationListener,
                                @NonNull final Interpolator interpolator) {
        if (!isDragging() && !isAnimationRunning()) {
            long duration = calculateAnimationDuration(distance, animationSpeed);
            Animation contentViewAnimation;

            if (getContentMode() == ContentMode.SCROLL) {
                contentViewAnimation =
                        new ContentViewScrollAnimation(contentView, duration, distance, scrollRatio,
                                1 - getContentOverlayTransparency(), show);
            } else {
                contentViewAnimation =
                        new ContentViewResizeAnimation(contentView, duration, distance,
                                getLocation(), 1 - getContentOverlayTransparency(), show);
            }

            Animation sidebarViewAnimation =
                    new SidebarViewAnimation(distance, duration, animationListener);
            contentViewAnimation.setInterpolator(interpolator);
            sidebarViewAnimation.setInterpolator(interpolator);
            contentView.startAnimation(contentViewAnimation);
            sidebarView.startAnimation(sidebarViewAnimation);
        }
    }

    /**
     * Creates and returns a listener, which allows to handle the end of an animation, which has
     * been used to show or hide the sidebar.
     *
     * @param show
     *         True, if the sidebar should be shown at the end of the animation, false otherwise
     * @return The listener, which has been created, as an instance of the type {@link
     * AnimationListener}
     */
    private AnimationListener createAnimationListener(final boolean show) {
        return new AnimationListener() {

            @Override
            public void onAnimationStart(final Animation animation) {

            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

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
     * Notifies all listeners, which have been registered to be notified, when the sidebar becomes
     * shown or hidden, about when the sidebar has become shown.
     */
    private void notifyOnSidebarShown() {
        for (SidebarListener listener : listeners) {
            listener.onSidebarShown(this);
        }
    }

    /**
     * Notifies all listeners, which have been registered to be notified, when the sidebar becomes
     * show or hidden, about when the sidebar has become hidden.
     */
    private void notifyOnSidebarHidden() {
        for (SidebarListener listener : listeners) {
            listener.onSidebarHidden(this);
        }
    }

    /**
     * Measures the sidebar's actual width in pixels, depending on the width of the parent view.
     */
    private void measureSidebarWidth() {
        mSidebarWidth = Math.round(getWidth() * sidebarWidth);

        if (maxSidebarWidth != -1) {
            mSidebarWidth = Math.min(maxSidebarWidth, mSidebarWidth);
        }

        if (sidebarView != null) {
            sidebarView.getLayoutParams().width = mSidebarWidth + sidebarView.getShadowWidth();
        }
    }

    /**
     * Measures the sidebar's actual offset in pixels, depending on the width of the parent view.
     */
    private void measureSidebarOffset() {
        mOffset = Math.round(getWidth() * sidebarOffset);

        if (maxSidebarOffset != -1) {
            mOffset = Math.min(maxSidebarOffset, mOffset);
        }

        mContentWidth = getWidth() - mOffset;

        if (contentView != null) {
            contentView.getLayoutParams().width = mContentWidth;
        }
    }

    /**
     * Calculates and returns the distance, the sidebar has to be moved by when it should become
     * shown or hidden, depending on its current position.
     *
     * @param show
     *         True, if the sidebar should become shown, false otherwise
     * @return The distance, the sidebar has to be moved by, as a {@link Float} value
     */
    private float calculateAnimationDistance(final boolean show) {
        float distance;

        if (getLocation() == Location.LEFT) {
            if (show) {
                distance = mSidebarWidth + sidebarView.getShadowWidth() - sidebarView.getRight();
            } else {
                distance = mOffset + sidebarView.getShadowWidth() - sidebarView.getRight();
            }
        } else {
            if (show) {
                distance = getWidth() - mSidebarWidth - sidebarView.getShadowWidth() -
                        sidebarView.getLeft();
            } else {
                distance = mContentWidth - sidebarView.getShadowWidth() - sidebarView.getLeft();
            }
        }

        return distance;
    }

    /**
     * Calculates the duration of the animation, which is used to hide or show the sidebar,
     * depending on a specific distance and speed.
     *
     * @param distance
     *         The distance, the sidebar has to be moved by, as a {@link Float} value
     * @param animationSpeed
     *         The speed of the animation in pixels per millisecond as a {@link Float} value
     * @return The duration of the animation in milliseconds as an {@link Integer} value
     */
    private int calculateAnimationDuration(final float distance, final float animationSpeed) {
        return Math.round(Math.abs(distance) / animationSpeed);
    }

    /**
     * Calculates and returns the position of the sidebar's left and right edge, depending on its
     * location and whether it is currently shown or not.
     *
     * @return The position of the sidebar's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateSidebarConstraints() {
        return calculateSidebarConstraints(isSidebarShown());
    }

    /**
     * Calculates and returns the position of the sidebar's left and right edge, depending on its
     * location and a specific boolean value, which specifies, whether it should be shown or not.
     *
     * @param shown
     *         True, if the sidebar should be shown, false otherwise
     * @return The position of the sidebar's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateSidebarConstraints(final boolean shown) {
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
                leftEdge = getWidth() - mSidebarWidth - sidebarView.getShadowWidth();
            } else {
                leftEdge = getWidth() - mOffset - sidebarView.getShadowWidth();
            }
        }

        rightEdge = leftEdge + mSidebarWidth + sidebarView.getShadowWidth();
        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location, whether the sidebar is currently shown and the current content mode.
     *
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateContentConstraints() {
        if (getContentMode() == ContentMode.SCROLL) {
            return calculateScrolledContentConstraints();
        } else {
            return calculateResizedContentConstraints();
        }
    }

    /**
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location and whether the sidebar is currently shown, using the content mode
     * <code>SCROLL</code>.
     *
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateScrolledContentConstraints() {
        int leftEdge;
        int rightEdge;

        if (getLocation() == Location.LEFT) {
            if (isSidebarShown()) {
                leftEdge = mOffset + Math.round((mSidebarWidth - mOffset) * scrollRatio);
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
        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location and whether the sidebar is currently shown, using the content mode
     * <code>RESIZE</code>.
     *
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
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

        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Calculates and returns the position of the sidebar's left and right edge, depending on its
     * location, while the user performs a drag gesture.
     *
     * @return The position of the sidebar's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateSidebarConstraintsWhileDragging() {
        Pair<Integer, Integer> shownSidebarConstraints = calculateSidebarConstraints(true);
        Pair<Integer, Integer> hiddenSidebarConstraints = calculateSidebarConstraints(false);

        int leftEdge = calculateSidebarConstraints().first + dragHelper.getDistance();

        if (getLocation() == Location.LEFT) {
            leftEdge = Math.max(hiddenSidebarConstraints.first, leftEdge);
            leftEdge = Math.min(shownSidebarConstraints.first, leftEdge);
        } else {
            leftEdge = Math.max(shownSidebarConstraints.first, leftEdge);
            leftEdge = Math.min(hiddenSidebarConstraints.first, leftEdge);
        }

        int rightEdge = leftEdge + mSidebarWidth + sidebarView.getShadowWidth();
        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location and the current content mode, while the user performs a drag gesture.
     *
     * @param sidebarConstraints
     *         The current position of the sidebar's left and right edge, as an instance of the
     *         class {@link Pair}
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
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
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location, using the content mode <code>SCROLL</code>, while the user performs a
     * drag gesture.
     *
     * @param sidebarConstraints
     *         The current position of the sidebar's left and right edge, as an instance of the
     *         class {@link Pair}
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateScrolledContentConstraintsWhileDragging(
            final Pair<Integer, Integer> sidebarConstraints) {
        int leftEdge;
        int rightEdge;

        if (getLocation() == Location.LEFT) {
            leftEdge = mOffset + Math.round(
                    (sidebarConstraints.second - sidebarView.getShadowWidth() - mOffset) *
                            scrollRatio);
        } else {
            leftEdge = Math.round(
                    (sidebarConstraints.first + sidebarView.getShadowWidth() - mContentWidth) *
                            scrollRatio);
        }

        rightEdge = leftEdge + mContentWidth;
        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Calculates and returns the position of the content's left and right edge, depending on the
     * sidebar's location, using the content mode <code>RESIZE</code>, while the user performs a
     * drag gesture.
     *
     * @param sidebarConstraints
     *         The current position of the sidebar's left and right edge, as an instance of the
     *         class {@link Pair}
     * @return The position of the content's left and right edge as an instance of the class {@link
     * Pair}
     */
    private Pair<Integer, Integer> calculateResizedContentConstraintsWhileDragging(
            final Pair<Integer, Integer> sidebarConstraints) {
        int leftEdge;
        int rightEdge;

        if (getLocation() == Location.LEFT) {
            leftEdge = sidebarConstraints.second - sidebarView.getShadowWidth();
            rightEdge = getWidth();
        } else {
            leftEdge = 0;
            rightEdge = sidebarConstraints.first + sidebarView.getShadowWidth();
        }

        return new Pair<>(leftEdge, rightEdge);
    }

    /**
     * Handles when a drag gesture is performed by the user.
     *
     * @param dragPosition
     *         The current horizontal position of the drag gesture as a {@link Float} value
     * @return True, if the sidebar has been moved by the drag gesture, false otherwise
     */
    private boolean handleDrag(final float dragPosition) {
        if (!isAnimationRunning()) {
            dragHelper.update(dragPosition);

            if (dragHelper.hasThresholdBeenReached() &&
                    isDraggingAllowed(dragHelper.getStartPosition())) {
                Pair<Integer, Integer> sidebarPos = calculateSidebarConstraintsWhileDragging();
                Pair<Integer, Integer> contentPos =
                        calculateContentConstraintsWhileDragging(sidebarPos);

                sidebarView.layout(sidebarPos.first, sidebarView.getTop(), sidebarPos.second,
                        sidebarView.getBottom());

                contentView.setOverlayTransparency(calculateContentOverlayTransparency());
                contentView.getLayoutParams().width = contentPos.second - contentPos.first;
                contentView.layout(contentPos.first, contentView.getTop(), contentPos.second,
                        contentView.getBottom());

                if (getContentMode() == ContentMode.RESIZE) {
                    contentView.requestLayout();
                }

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
            if (sidebarView.getRight() - sidebarView.getShadowWidth() > thresholdPosition) {
                animateShowSidebar(calculateAnimationDistance(true), speed,
                        new DecelerateInterpolator());
            } else {
                animateHideSidebar(calculateAnimationDistance(false), speed,
                        new DecelerateInterpolator());
            }
        } else {
            if (sidebarView.getLeft() + sidebarView.getShadowWidth() < thresholdPosition) {
                animateShowSidebar(calculateAnimationDistance(true), speed,
                        new DecelerateInterpolator());
            } else {
                animateHideSidebar(calculateAnimationDistance(false), speed,
                        new DecelerateInterpolator());
            }
        }
    }

    /**
     * Handles when the sidebar or content is clicked by the user.
     *
     * @param clickPosition
     *         The horizontal position of the click as a {@link Float} value
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
     * Returns, whether a click at a specific position targets the content, or not.
     *
     * @param clickPosition
     *         The horizontal position of the click as a {@link Float} value
     * @return True, if the content has been clicked, false otherwise
     */
    private boolean isContentClicked(final float clickPosition) {
        return !isSidebarClicked(clickPosition);
    }

    /**
     * Returns, whether a click at a specific position targets the sidebar, or not.
     *
     * @param clickPosition
     *         The horizontal position of the click as a {@link Float} value
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
     * Returns, whether a click at a specific position targets the edge of the sidebar's parent
     * view, or not.
     *
     * @param clickPosition
     *         The horizontal position of the click as a {@link Float} value
     * @return True, if the edge of the sidebar's parent view has been clicked, false otherwise
     */
    private boolean isEdgeClicked(final float clickPosition) {
        int tolerance = dpToPixels(getContext(), EDGE_DRAGGING_TOLERANCE);

        if (getLocation() == Location.LEFT) {
            return clickPosition <= tolerance;
        } else {
            return clickPosition >= getWidth() - tolerance;
        }
    }

    /**
     * Returns, whether a drag gesture, which has been started at a specific position, is allowed,
     * depending on the used drag modes, or not.
     *
     * @param dragStartPosition
     *         The horizontal position, the drag gesture has been started at, as an {@link Integer}
     *         value
     * @return True, if the drag gesture is allowed, false otherwise
     */
    private boolean isDraggingAllowed(final int dragStartPosition) {
        DragMode currentDragMode = dragModeWhenHidden;

        if (isSidebarShown()) {
            currentDragMode = dragModeWhenShown;
        }

        if (currentDragMode == DragMode.DISABLED) {
            return false;
        } else if (currentDragMode == DragMode.SIDEBAR_ONLY) {
            return isSidebarClicked(dragStartPosition);
        } else if (currentDragMode == DragMode.CONTENT_ONLY) {
            return isContentClicked(dragStartPosition);
        } else if (currentDragMode == DragMode.EDGE) {
            return isEdgeClicked(dragStartPosition);
        }

        return true;
    }

    /**
     * Calculates the position, where the drag threshold is reached, depending on whether the
     * sidebar's location and whether it is currently shown or hidden.
     *
     * @return The horizontal position, where the drag threshold is reached, as a {@link Float}
     * value
     */
    private float calculatePositionWhereDragThresholdIsReached() {
        float position;

        if (getLocation() == Location.LEFT) {
            if (isSidebarShown()) {
                position = mSidebarWidth - ((mSidebarWidth - mOffset) * dragThreshold);
            } else {
                position = mOffset + ((mSidebarWidth - mOffset) * dragThreshold);
            }
        } else {
            if (isSidebarShown()) {
                position = getWidth() - mSidebarWidth + ((mSidebarWidth - mOffset) * dragThreshold);

            } else {
                position = getWidth() - mOffset - ((mSidebarWidth - mOffset) * dragThreshold);
            }
        }

        return position;
    }

    /**
     * Calculates and returns the distance after which dragging has an effect on the sidebar in
     * pixels. The distance depends on the current set drag sensitivity, which corresponds to an
     * internal value range.
     *
     * @return The distance after which dragging has an effect on the sidebar in pixels as an {@link
     * Integer} value
     */
    private int calculateDragSensitivity() {
        int range = MAX_DRAG_SENSITIVITY - MIN_DRAG_SENSITIVITY;
        return Math.round((1 - getDragSensitivity()) * range + MIN_DRAG_SENSITIVITY);
    }

    /**
     * Calculates and returns the transparency of the content overlay, depending on the sidebar's
     * current position and the transparency, which should be applied, when the sidebar is shown.
     *
     * @return The transparency of the content overlay as a {@link Float} value. If the transparency
     * is 0.0, the overlay will be completely transparent, if it is 1.0, the overlay will be not
     * transparent at all
     */
    private float calculateContentOverlayTransparency() {
        float totalDistance = mSidebarWidth - mOffset;
        float distance = Math.abs(calculateAnimationDistance(false));
        return (1 - getContentOverlayTransparency()) * (distance / totalDistance);
    }

    /**
     * Creates a new custom view, which allows to show a sidebar, which overlaps the view's main
     * content and can be shown or hidden in an animated manner.
     *
     * @param context
     *         The context, the sidebar should belong to, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public Sidebar(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new custom view, which allows to show a sidebar, which overlaps the view's main
     * content and can be shown or hidden in an animated manner. This constructor is called when a
     * preference is being constructed from an XML file, supplying attributes that were specified in
     * the XML file. This version uses a default style of 0, so the only attribute values applied
     * are those in the context's theme and the given attribute set.
     *
     * @param context
     *         The context, the sidebar should belong to, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public Sidebar(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(attributeSet);
    }

    /**
     * Creates a new custom view, which allows to show a sidebar, which overlaps the view's main
     * content and can be shown or hidden in an animated manner. This constructor allows subclasses
     * to use their own base style when they are inflating.
     *
     * @param context
     *         The context, the sidebar should belong to, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     */
    public Sidebar(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize(attributeSet);
    }

    /**
     * Creates a new custom view, which allows to show a sidebar, which overlaps the view's main
     * content and can be shown or hidden in an animated manner. This constructor allows subclasses
     * to use their own base style when they are inflating.
     *
     * @param context
     *         The context, the sidebar should belong to, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this preference. If 0, no style will be applied (beyond
     *         what is included in the theme). This may either be an attribute resource, whose value
     *         will be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the
     *         preference, used only if the default style is 0 or can not be found in the theme. Can
     *         be 0 to not look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Sidebar(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   final int defaultStyle, final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize(attributeSet);
    }

    /**
     * Returns, whether the sidebar is currently shown, or not.
     *
     * @return True, if the sidebar is currently shown, false otherwise
     */
    public final boolean isSidebarShown() {
        return shown;
    }

    /**
     * Shows the sidebar, if it is currently hidden.
     */
    public final void showSidebar() {
        if (contentView == null) {
            throw new IllegalStateException("The content view is not set");
        } else if (sidebarView == null) {
            throw new IllegalStateException("The sidebar view is not set");
        } else if (!isSidebarShown()) {
            animateShowSidebar(calculateAnimationDistance(true),
                    new AccelerateDecelerateInterpolator());
        }
    }

    /**
     * Hides the sidebar, if it is currently shown.
     */
    public final void hideSidebar() {
        if (contentView == null) {
            throw new IllegalStateException("The content view is not set");
        } else if (sidebarView == null) {
            throw new IllegalStateException("The sidebar view is not set");
        } else if (isSidebarShown()) {
            animateHideSidebar(calculateAnimationDistance(false),
                    new AccelerateDecelerateInterpolator());
        }
    }

    /**
     * Hides the sidebar, if it is currently shown, or shows it, if it is currently hidden.
     */
    public final void toggleSidebar() {
        if (isSidebarShown()) {
            hideSidebar();
        } else {
            showSidebar();
        }
    }

    /**
     * Returns the view, which is contained by the sidebar.
     *
     * @return The view, which is contained by the sidebar, as an instance of the class {@link View}
     * or null, if no sidebar view is set
     */
    public final View getSidebarView() {
        if (sidebarView != null) {
            return sidebarView.getSidebarView();
        } else {
            return null;
        }
    }

    /**
     * Sets the view, which should be contained by the sidebar.
     *
     * @param sidebarViewId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The id
     *         must be a valid resource id
     */
    public final void setSidebarView(final int sidebarViewId) {
        inflateSidebarView(InflaterFactory.createInflater(sidebarViewId));
    }

    /**
     * Sets the view, which should be contained by the sidebar.
     *
     * @param sidebarView
     *         The view, which should be set, as an instance of the class {@link View}. The view may
     *         not be null
     */
    public final void setSidebarView(final View sidebarView) {
        inflateSidebarView(InflaterFactory.createInflater(sidebarView));
    }

    /**
     * Returns the view, which is used as the main content.
     *
     * @return The view, which is used as the main content, as an instance of the class {@link View}
     */
    public final View getContentView() {
        if (contentView != null) {
            return contentView.getContentView();
        } else {
            return null;
        }
    }

    /**
     * Sets the view, which should be used as the main content.
     *
     * @param contentViewId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The id
     *         must be a valid resource id
     */
    public final void setContentView(final int contentViewId) {
        inflateContentView(InflaterFactory.createInflater(contentViewId));
    }

    /**
     * Sets the view, which should be used as the main content.
     *
     * @param contentView
     *         The view, which should be set, as an instance of the class {@link View}. The view may
     *         not be null
     */
    public final void setContentView(final View contentView) {
        inflateContentView(InflaterFactory.createInflater(contentView));
    }

    /**
     * Returns the background of the sidebar.
     *
     * @return The background of the sidebar as an instance of the class {@link Drawable} or null,
     * if the default background is used
     */
    public final Drawable getSidebarBackground() {
        return sidebarBackground;
    }

    /**
     * Sets the background of the sidebar.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if the default background should be used
     */
    public final void setSidebarBackground(@Nullable final Drawable background) {
        this.sidebarBackground = background;

        if (sidebarView != null) {
            sidebarView.setSidebarBackground(sidebarBackground);
        }
    }

    /**
     * Sets the background of the sidebar.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The id must be a valid resource id
     */
    @SuppressWarnings("deprecation")
    public final void setSidebarBackground(@DrawableRes final int resourceId) {
        if (resourceId == -1) {
            this.sidebarBackground = null;
        } else {
            this.sidebarBackground = getContext().getResources().getDrawable(resourceId);
        }

        if (sidebarView != null) {
            sidebarView.setSidebarBackground(sidebarBackground);
        }
    }

    /**
     * Returns the location of the sidebar.
     *
     * @return The location of the sidebar as a value of the enum {@link Location}. The location may
     * either be <code>LEFT</code> or <code>RIGHT</code>
     */
    public final Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the sidebar.
     *
     * @param location
     *         The location, which should be set, as a value of the enum {@link Location}. The
     *         location may either be <code>LEFT</code> or <code>RIGHT</code>
     */
    public final void setLocation(@NonNull final Location location) {
        ensureNotNull(location, "The location may not be null");
        this.location = location;

        if (sidebarView != null && contentView != null) {
            sidebarView.setLocation(location);
        }

        requestLayout();
    }

    /**
     * Returns the speed of the animation, which is used to show or hide the sidebar.
     *
     * @return The speed of the animation in dp per millisecond as a {@link Float} value. The speed
     * must be greater than 0
     */
    public final float getAnimationSpeed() {
        return pixelsToDp(getContext(), animationSpeed);
    }

    /**
     * Sets the speed of the animation, which is used to show or hide the sidebar.
     *
     * @param animationSpeed
     *         The speed, which should be set, in dp per millisecond as a {@link Float} value. The
     *         speed must be greater than 0
     */
    public final void setAnimationSpeed(final float animationSpeed) {
        ensureGreater(animationSpeed, 0, "The animation speed must be greater than 0");
        this.animationSpeed = dpToPixels(getContext(), animationSpeed);
    }

    /**
     * Returns the width of the sidebar in relation to the width of the parent view.
     *
     * @return The width of the sidebar in relation to the width of the parent view, as a {@link
     * Float} value. The width must be at least 0 and at maximum 1
     */
    public final float getSidebarWidth() {
        return sidebarWidth;
    }

    /**
     * Sets the width of the sidebar in relation to the width of the parent view.
     *
     * @param sidebarWidth
     *         The width, which should be set, as a {@link Float} value. The width must be at least
     *         0 and at maximum 1
     */
    public final void setSidebarWidth(final float sidebarWidth) {
        ensureAtLeast(sidebarWidth, 0, "The sidebar width must be at least 0");
        ensureAtMaximum(sidebarWidth, 1, "The sidebar width must be at maximum 1");
        ensureGreater(sidebarWidth, sidebarOffset,
                "The sidebar width must be greater than the sidebar offset");
        this.sidebarWidth = sidebarWidth;
        measureSidebarWidth();
        requestLayout();
    }

    /**
     * Returns the maximum width of the sidebar.
     *
     * @return The maximum width of the sidebar in dp as an {@link Integer} value or -1, if the
     * sidebar's width is not restricted
     */
    public final int getMaxSidebarWidth() {
        if (maxSidebarWidth != -1) {
            return pixelsToDp(getContext(), maxSidebarWidth);
        } else {
            return -1;
        }
    }

    /**
     * Sets the maximum width of the sidebar.
     *
     * @param maxSidebarWidth
     *         The maximum width, which should be set, in dp as an {@link Integer} value or -1, if
     *         the sidebar's width should not be restricted
     */
    public final void setMaxSidebarWidth(final int maxSidebarWidth) {
        if (maxSidebarWidth != -1) {
            ensureGreater(maxSidebarWidth, 0, "The maximum sidebar width must be greater than 0");
            setMaxSidebarWidthInPixels(dpToPixels(getContext(), maxSidebarWidth));
        } else {
            setMaxSidebarWidthInPixels(-1);
        }
    }

    /**
     * Returns the amount of space in relation to the width of the parent view, the sidebar is
     * visible, even if it is currently hidden.
     *
     * @return The offset of the sidebar as a {@link Float} value. The offset must be at least 0 and
     * at maximum 1
     */
    public final float getSidebarOffset() {
        return sidebarOffset;
    }

    /**
     * Sets the amount of space in relation to the width of the parent view, the sidebar should be
     * visible, even if it is currently hidden.
     *
     * @param sidebarOffset
     *         The offset of the sidebar, which should be set, as a {@link Float} value. The offset
     *         must be at least 0 and at maximum 1
     */
    public final void setSidebarOffset(final float sidebarOffset) {
        ensureAtLeast(sidebarOffset, 0, "The sidebar offset must be at least 0");
        ensureAtMaximum(sidebarOffset, 1, "The sidebar offset must be at maximum 1");
        ensureSmaller(sidebarOffset, sidebarWidth,
                "The sidebar offset must be less than the sidebar width");
        this.sidebarOffset = sidebarOffset;
        measureSidebarOffset();
        requestLayout();
    }

    /**
     * Returns the maximum offset of the sidebar.
     *
     * @return The maximum offset of the sidebar in dp as an {@link Integer} value or -1, if the
     * sidebar's offset should not be restricted
     */
    public final int getMaxSidebarOffset() {
        if (maxSidebarOffset != -1) {
            return pixelsToDp(getContext(), maxSidebarOffset);
        } else {
            return -1;
        }
    }

    /**
     * Sets the maximum offset of the sidebar.
     *
     * @param maxSidebarOffset
     *         The maximum offset of the sidebar, which should be set, in dp as an {@link Integer}
     *         value or -1, if the sidebar's offset should not be restricted
     */
    public final void setMaxSidebarOffset(final int maxSidebarOffset) {
        if (maxSidebarOffset != -1) {
            ensureGreater(maxSidebarOffset, 0, "The maximum sidebar offset must be greater than 0");
            setMaxSidebarOffsetInPixels(dpToPixels(getContext(), maxSidebarOffset));
        } else {
            setMaxSidebarOffsetInPixels(-1);
        }
    }

    /**
     * Returns the content mode, which specifies how the main content is handled, when the sidebar
     * becomes shown or hidden.
     *
     * @return The content mode as a value of the enum {@link ContentMode}. The content mode may
     * either be <code>SCROLL</code> or <code>RESIZE</code>
     */
    public final ContentMode getContentMode() {
        return contentMode;
    }

    /**
     * Sets the content mode, which specifies how the main content should be handled, when the
     * sidebar becomes shown or hidden.
     *
     * @param contentMode
     *         The content mode, which should be set, as a value of the enum {@link ContentMode}.
     *         The content mode may either be <code>SCROLL</code> or <code>RESIZE</code>
     */
    public final void setContentMode(@NonNull final ContentMode contentMode) {
        ensureNotNull(contentMode, "The content mode may not be null");
        this.contentMode = contentMode;
        requestLayout();
    }

    /**
     * Returns the ratio between the distance, the sidebar is moved by, when it becomes shown or
     * hidden, in relation to the distance, the content is moved by.
     *
     * @return The scroll ratio as a {@link Float} value. The scroll ratio must be at least 0 and at
     * maximum 1. If set to 1.0, the content is moved exactly as far as the sidebar, if set to 0.0,
     * the content is not moved at all. The scroll ratio does only apply, if the content mode is set
     * to <code>SCROLL</code>
     */
    public final float getScrollRatio() {
        return scrollRatio;
    }

    /**
     * Sets the ratio between the distance, the sidebar is moved by, when it becomes shown or
     * hidden, in relation to the distance, the content is moved by.
     *
     * @param scrollRatio
     *         The scroll ratio, which should be set, as a {@link Float} value. The scroll ratio
     *         must be at least 0 and at maximum 1. If set to 1.0, the content will be moved exactly
     *         as far as the sidebar, if set to 0.0, the content will not be moved at all. The
     *         scroll ratio will only apply, if the content mode is set to <code>SCROLL</code>
     */
    public final void setScrollRatio(final float scrollRatio) {
        ensureAtLeast(scrollRatio, 0, "The scroll ratio must be at least 0");
        ensureAtMaximum(scrollRatio, 1, "The scroll ratio must be at maximum 1");
        this.scrollRatio = scrollRatio;
        requestLayout();
    }

    /**
     * Returns the drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently hidden.
     *
     * @return The drag mode as a value of the enum {@link DragMode}. The drag mode may either be
     * <code>BOTH</code>, <code>SIDEBAR_ONLY</code>, <code>CONTENT_ONLY</code>,
     * <code>DISABLED</code> or <code>EDGE</code>
     */
    public final DragMode getDragModeWhenHidden() {
        return dragModeWhenHidden;
    }

    /**
     * Sets the drag mode, which specifies the region, where drag gestures should be recognized,
     * when the sidebar is currently hidden.
     *
     * @param dragMode
     *         The drag mode as a value of the enum {@link DragMode}. The drag mode may either be
     *         <code>BOTH</code>, <code>SIDEBAR_ONLY</code>, <code>CONTENT_ONLY</code>,
     *         <code>DISABLED</code> or <code>EDGE</code>
     */
    public final void setDragModeWhenHidden(@NonNull final DragMode dragMode) {
        ensureNotNull(dragMode, "The drag mode may not be null");
        this.dragModeWhenHidden = dragMode;
    }

    /**
     * Returns the drag mode, which specifies the region, where drag gestures are recognized, when
     * the sidebar is currently shown.
     *
     * @return The drag mode as a value of the enum {@link DragMode}. The drag mode may either be
     * <code>BOTH</code>, <code>SIDEBAR_ONLY</code>, <code>CONTENT_ONLY</code> or
     * <code>DISABLED</code>
     */
    public final DragMode getDragModeWhenShown() {
        return dragModeWhenShown;
    }

    /**
     * Sets the drag mode, which specifies the region, where drag gestures should be recognized,
     * when the sidebar is currently shown.
     *
     * @param dragMode
     *         The drag mode as a value of the enum {@link DragMode}. The drag mode may either be
     *         <code>BOTH</code>, <code>SIDEBAR_ONLY</code>, <code>CONTENT_ONLY</code> or
     *         <code>DISABLED</code>
     */
    public final void setDragModeWhenShown(@NonNull final DragMode dragMode) {
        ensureNotNull(dragMode, "The drag mode may not be null");

        if (dragMode == DragMode.EDGE) {
            throw new IllegalArgumentException(
                    "The drag mode when shown may not be " + DragMode.EDGE);
        }

        this.dragModeWhenShown = dragMode;
    }

    /**
     * Returns the distance, the sidebar has to be dragged until its state changes, in relation to
     * the whole distance.
     *
     * @return The drag threshold as a {@link Float} value. The drag threshold must be at least 0
     * and at maximum 1
     */
    public final float getDragThreshold() {
        return dragThreshold;
    }

    /**
     * Sets the distance, the sidebar has to be dragged until its state changes, in relation to the
     * whole distance.
     *
     * @param dragThreshold
     *         The drag threshold, which should be set, as a {@link Float} value. The drag threshold
     *         must be at least 0 and at maximum 1
     */
    public final void setDragThreshold(final float dragThreshold) {
        ensureAtLeast(dragThreshold, 0, "The drag threshold must be at least 0");
        ensureAtMaximum(dragThreshold, 1, "The drag threshold must be at maximum 1");
        this.dragThreshold = dragThreshold;
    }

    /**
     * Returns the sensitivity, which specifies the distance after which dragging has an effect on
     * the sidebar, in relation to an internal value range.
     *
     * @return The drag sensitivity as a {@link Float} value. The drag sensitivity must be at lest 0
     * and at maximum 1
     */
    public final float getDragSensitivity() {
        return dragSensitivity;
    }

    /**
     * Sets the sensitivity, which specifies the distance after which dragging has an effect on the
     * sidebar, in relation to an internal value range.
     *
     * @param dragSensitivity
     *         The drag sensitivity, which should be set, as a {@link Float} value. The drag
     *         sensitivity must be at lest 0 and at maximum 1
     */
    public final void setDragSensitivity(final float dragSensitivity) {
        ensureAtLeast(dragSensitivity, 0, "The drag sensitivity must be at least 0");
        ensureAtMaximum(dragSensitivity, 1, "The drag sensitivity must be at maximum 1");
        this.dragSensitivity = dragSensitivity;
        this.dragHelper = new DragHelper(calculateDragSensitivity());
    }

    /**
     * Returns, whether the sidebar is hidden, when the device's back button is clicked, or not.
     *
     * @return True, if the sidebar is hidden, when the device's back button is clicked, false
     * otherwise
     */
    public final boolean isHiddenOnBackButton() {
        return hideOnBackButton;
    }

    /**
     * Sets, whether the sidebar should be hidden, when the device's back button is clicked, or
     * not.
     *
     * @param hideOnBackButton
     *         True, if the sidebar should be hidden, when the device's back button is clicked,
     *         false otherwise
     */
    public final void hideOnBackButton(final boolean hideOnBackButton) {
        this.hideOnBackButton = hideOnBackButton;
    }

    /**
     * Returns, whether the sidebar is hidden, when the content is clicked by the user, or not.
     *
     * @return True, if the sidebar is hidden, when the content is clicked by the user, false
     * otherwise
     */
    public final boolean isHiddenOnContentClick() {
        return hideOnContentClick;
    }

    /**
     * Sets, whether the sidebar should be hidden, when the content is clicked by the user, or not.
     *
     * @param hideOnContentClick
     *         True, if the sidebar should be hidden, when the content is clicked by the user, false
     *         otherwise
     */
    public final void hideOnContentClick(final boolean hideOnContentClick) {
        this.hideOnContentClick = hideOnContentClick;
    }

    /**
     * Returns, whether the sidebar is shown, when it is clicked by the user, or not.
     *
     * @return True, if the sidebar is shown, when it is clicked by the user, false otherwise
     */
    public final boolean isShownOnSidebarClick() {
        return showOnSidebarClick;
    }

    /**
     * Sets, whether the sidebar should be shown, when it is clicked by the user, or not.
     *
     * @param showOnSidebarClick
     *         True, if the sidebar should be shown, when it is clicked by the user, false
     *         otherwise
     */
    public final void showOnSidebarClick(final boolean showOnSidebarClick) {
        this.showOnSidebarClick = showOnSidebarClick;
    }

    /**
     * Returns the color of the overlay, which is shown in front of the main content, when the
     * sidebar is shown.
     *
     * @return The color of the overlay, which is shown in front of the main content, when the
     * sidebar is shown, as an {@link Integer} value
     */
    public final int getContentOverlayColor() {
        return contentOverlayColor;
    }

    /**
     * Sets the color of the overlay, which is shown in front of the main content, when the sidebar
     * is shown.
     *
     * @param contentOverlayColor
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setContentOverlayColor(@ColorInt final int contentOverlayColor) {
        this.contentOverlayColor = contentOverlayColor;

        if (contentView != null) {
            contentView.setOverlayColor(contentOverlayColor);
        }
    }

    /**
     * Returns the transparency of the overlay, which is shown in front of the main content, when
     * the sidebar is shown.
     *
     * @return The transparency of the overlay, which is shown in front of the main content, when
     * the sidebar is shown, as a {@link Float} value. The transparency must be at least 0 and at
     * maximum 1. If the transparency is 1.0, the overlay is completely transparent, if it is 0.0,
     * the overlay is not transparent at all
     */
    public final float getContentOverlayTransparency() {
        return contentOverlayTransparency;
    }

    /**
     * Sets the transparency of the overlay, which is shown in front of the main content, when the
     * sidebar is shown.
     *
     * @param contentOverlayTransparency
     *         The transparency, which should be set, as a {@link Float} value. The transparency
     *         must be at least 0 and at maximum 1. If the transparency is 1.0, the overlay will be
     *         completely transparent, if it is 0.0, the overlay will not be transparent at all
     */
    public final void setContentOverlayTransparency(final float contentOverlayTransparency) {
        ensureAtLeast(contentOverlayTransparency, 0, "The transparency must be at least 0");
        ensureAtMaximum(contentOverlayTransparency, 1, "The transparency must be at maximum 1");
        this.contentOverlayTransparency = contentOverlayTransparency;
        requestLayout();
    }

    /**
     * Returns the elevation of the sidebar.
     *
     * @return The elevation of the sidebar in dp as an {@link Integer} value
     */
    public final int getSidebarElevation() {
        return sidebarElevation;
    }

    /**
     * Sets the elevation of the sidebar.
     *
     * @param elevation
     *         The elevation, which should be set, in dp as an {@link Integer} value. The elevation
     *         must be at least 0 and at maximum 16
     */
    public final void setSidebarElevation(final int elevation) {
        ensureAtLeast(elevation, 0, "The sidebar elevation must be at least 0");
        ensureAtMaximum(elevation, ElevationUtil.MAX_ELEVATION,
                "The sidebar elevation must be at least " + ElevationUtil.MAX_ELEVATION);
        this.sidebarElevation = elevation;

        if (sidebarView != null) {
            sidebarView.setSidebarElevation(elevation);
        }

        measureSidebarWidth();
        requestLayout();
    }

    /**
     * Adds a new listener, which should be notified, when the sidebar becomes shown or hidden.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link
     *         SidebarListener}. The listener may not be null
     */
    public final void addSidebarListener(@NonNull final SidebarListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.add(listener);
    }

    /**
     * Removes a specific listener, which should not be notified, when the sidebar becomes shown or
     * hidden, anymore.
     *
     * @param listener
     *         The listener, which should be removed, as an instance of the type {@link
     *         SidebarListener}. The listener may not be null
     */
    public final void removeSidebarListener(@NonNull final SidebarListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        listeners.remove(listener);
    }

    /**
     * Returns, whether a drag gesture, which moves the sidebar, is currently performed, or not.
     *
     * @return True, if a drag gesture, which moves the sidebar, is currently performed, false
     * otherwise
     */
    public final boolean isDragging() {
        return !dragHelper.isReseted() && dragHelper.hasThresholdBeenReached();
    }

    /**
     * Returns, whether an animation, which moves the sidebar, is currently running, or not.
     *
     * @return True, if an animation, which moves the sidebar, is currently running, false otherwise
     */
    public final boolean isAnimationRunning() {
        return sidebarView != null && sidebarView.getAnimation() != null;
    }

    @Override
    public final boolean onKeyPreIme(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP &&
                isSidebarShown() && hideOnBackButton) {
            hideSidebar();
            return true;
        }

        return false;
    }

    @Override
    public final boolean dispatchTouchEvent(final MotionEvent event) {
        boolean handled = false;

        if (isSidebarClicked(event.getX()) && !isSidebarShown() && isShownOnSidebarClick()) {
            handled = true;
        } else if (isContentClicked(event.getX()) && isSidebarShown() && isHiddenOnContentClick()) {
            handled = true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                handled = handleDrag(event.getX());
                break;
            case MotionEvent.ACTION_UP:
                if (dragHelper.hasThresholdBeenReached() &&
                        isDraggingAllowed(dragHelper.getStartPosition())) {
                    handleRelease();
                } else {
                    handleClick(event.getX());
                }

                break;
            default:
                break;
        }

        return handled || super.dispatchTouchEvent(event);
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

                if (dragHelper.hasThresholdBeenReached() &&
                        isDraggingAllowed(dragHelper.getStartPosition())) {
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
    protected final void onSizeChanged(final int width, final int height, final int oldWidth,
                                       final int oldHeigth) {
        super.onSizeChanged(width, height, oldWidth, oldHeigth);
        measureSidebarWidth();
        measureSidebarOffset();
    }

    @Override
    protected final void onLayout(final boolean changed, final int l, final int t, final int r,
                                  final int b) {
        if (!isDragging() && !isAnimationRunning()) {
            Pair<Integer, Integer> sidebarPos = calculateSidebarConstraints();
            sidebarView.layout(sidebarPos.first, t, sidebarPos.second, b);

            Pair<Integer, Integer> contentPos = calculateContentConstraints();
            contentView.setOverlayTransparency(calculateContentOverlayTransparency());
            contentView.getLayoutParams().width = contentPos.second - contentPos.first;
            contentView.layout(contentPos.first, t, contentPos.second, b);

            if (getContentMode() == ContentMode.RESIZE) {
                contentView.requestLayout();
            }
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
        savedState.setContentOverlayTransparency(getContentOverlayTransparency());
        savedState.setSidebarElevation(getSidebarElevation());
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
            setContentOverlayTransparency(savedState.getContentOverlayTransparency());
            setSidebarElevation(savedState.getSidebarElevation());
            shown = savedState.isShown();
            requestLayout();
            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

}