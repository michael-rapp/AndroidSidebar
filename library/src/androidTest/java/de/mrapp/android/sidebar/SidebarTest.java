/*
 * Copyright 2014 - 2018 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.sidebar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;

import junit.framework.Assert;

import org.xmlpull.v1.XmlPullParser;

import de.mrapp.android.sidebar.savedstate.SidebarSavedState;
import de.mrapp.android.util.ElevationUtil;

import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link Sidebar}.
 *
 * @author Michael Rapp
 */
public class SidebarTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context as a
     * parameter.
     */
    public final void testConstructorWithContextParameter() {
        Context context = getContext();
        Sidebar sidebar = new Sidebar(context);
        assertEquals(context, sidebar.getContext());
        assertEquals(Sidebar.DEFAULT_LOCATION, sidebar.getLocation());
        assertEquals(Sidebar.DEFAULT_ANIMATION_SPEED, sidebar.getAnimationSpeed());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_WIDTH, sidebar.getSidebarWidth());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_WIDTH, sidebar.getMaxSidebarWidth());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_OFFSET, sidebar.getSidebarOffset());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_OFFSET, sidebar.getMaxSidebarOffset());
        assertEquals(Sidebar.DEFAULT_CONTENT_MODE, sidebar.getContentMode());
        assertEquals(Sidebar.DEFAULT_SCROLL_RATIO, sidebar.getScrollRatio());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_HIDDEN, sidebar.getDragModeWhenHidden());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_SHOWN, sidebar.getDragModeWhenShown());
        assertEquals(Sidebar.DEFAULT_DRAG_THRESHOLD, sidebar.getDragThreshold());
        assertEquals(Sidebar.DEFAULT_DRAG_SENSITIVITY, sidebar.getDragSensitivity());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_BACK_BUTTON, sidebar.isHiddenOnBackButton());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_CONTENT_CLICK, sidebar.isHiddenOnContentClick());
        assertEquals(Sidebar.DEFAULT_SHOW_ON_SIDEBAR_CLICK, sidebar.isShownOnSidebarClick());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_COLOR, sidebar.getContentOverlayColor());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_TRANSPARENCY,
                sidebar.getContentOverlayTransparency());
        assertNull(sidebar.getContentView());
        assertNull(sidebar.getSidebarView());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_ELEVATION, sidebar.getSidebarElevation());
        assertEquals(Sidebar.SHOW_SIDEBAR_BY_DEFAULT, sidebar.isShown());
        assertNull(sidebar.getSidebarBackground());
        assertFalse(sidebar.isAnimationRunning());
        assertFalse(sidebar.isDragging());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context and an
     * attribute set as parameters.
     */
    public final void testConstructorWithContextAndAttributeSetParameters() {
        Context context = getContext();
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.sidebar);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        Sidebar sidebar = new Sidebar(context, attributeSet);
        assertEquals(context, sidebar.getContext());
        assertEquals(Sidebar.DEFAULT_LOCATION, sidebar.getLocation());
        assertEquals(Sidebar.DEFAULT_ANIMATION_SPEED, sidebar.getAnimationSpeed());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_WIDTH, sidebar.getSidebarWidth());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_WIDTH, sidebar.getMaxSidebarWidth());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_OFFSET, sidebar.getSidebarOffset());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_OFFSET, sidebar.getMaxSidebarOffset());
        assertEquals(Sidebar.DEFAULT_CONTENT_MODE, sidebar.getContentMode());
        assertEquals(Sidebar.DEFAULT_SCROLL_RATIO, sidebar.getScrollRatio());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_HIDDEN, sidebar.getDragModeWhenHidden());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_SHOWN, sidebar.getDragModeWhenShown());
        assertEquals(Sidebar.DEFAULT_DRAG_THRESHOLD, sidebar.getDragThreshold());
        assertEquals(Sidebar.DEFAULT_DRAG_SENSITIVITY, sidebar.getDragSensitivity());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_BACK_BUTTON, sidebar.isHiddenOnBackButton());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_CONTENT_CLICK, sidebar.isHiddenOnContentClick());
        assertEquals(Sidebar.DEFAULT_SHOW_ON_SIDEBAR_CLICK, sidebar.isShownOnSidebarClick());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_COLOR, sidebar.getContentOverlayColor());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_TRANSPARENCY,
                sidebar.getContentOverlayTransparency());
        assertNull(sidebar.getContentView());
        assertNull(sidebar.getSidebarView());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_ELEVATION, sidebar.getSidebarElevation());
        assertEquals(Sidebar.SHOW_SIDEBAR_BY_DEFAULT, sidebar.isShown());
        assertNull(sidebar.getSidebarBackground());
        assertFalse(sidebar.isAnimationRunning());
        assertFalse(sidebar.isDragging());
    }

    /**
     * Tests, if all properties are set correctly by the constructor, which expects a context, an
     * attribute set and a default style as parameters.
     */
    public final void testConstructorWithContextAttributeSetAndDefaultStyleParameters() {
        Context context = getContext();
        int defaultStyle = 0;
        XmlPullParser xmlPullParser = context.getResources().getXml(R.xml.sidebar);
        AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        Sidebar sidebar = new Sidebar(context, attributeSet, defaultStyle);
        assertEquals(context, sidebar.getContext());
        assertEquals(Sidebar.DEFAULT_LOCATION, sidebar.getLocation());
        assertEquals(Sidebar.DEFAULT_ANIMATION_SPEED, sidebar.getAnimationSpeed());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_WIDTH, sidebar.getSidebarWidth());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_WIDTH, sidebar.getMaxSidebarWidth());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_OFFSET, sidebar.getSidebarOffset());
        assertEquals(Sidebar.DEFAULT_MAX_SIDEBAR_OFFSET, sidebar.getMaxSidebarOffset());
        assertEquals(Sidebar.DEFAULT_CONTENT_MODE, sidebar.getContentMode());
        assertEquals(Sidebar.DEFAULT_SCROLL_RATIO, sidebar.getScrollRatio());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_HIDDEN, sidebar.getDragModeWhenHidden());
        assertEquals(Sidebar.DEFAULT_DRAG_MODE_WHEN_SHOWN, sidebar.getDragModeWhenShown());
        assertEquals(Sidebar.DEFAULT_DRAG_THRESHOLD, sidebar.getDragThreshold());
        assertEquals(Sidebar.DEFAULT_DRAG_SENSITIVITY, sidebar.getDragSensitivity());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_BACK_BUTTON, sidebar.isHiddenOnBackButton());
        assertEquals(Sidebar.DEFAULT_HIDE_ON_CONTENT_CLICK, sidebar.isHiddenOnContentClick());
        assertEquals(Sidebar.DEFAULT_SHOW_ON_SIDEBAR_CLICK, sidebar.isShownOnSidebarClick());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_COLOR, sidebar.getContentOverlayColor());
        assertEquals(Sidebar.DEFAULT_CONTENT_OVERLAY_TRANSPARENCY,
                sidebar.getContentOverlayTransparency());
        assertNull(sidebar.getContentView());
        assertNull(sidebar.getSidebarView());
        assertEquals(Sidebar.DEFAULT_SIDEBAR_ELEVATION, sidebar.getSidebarElevation());
        assertEquals(Sidebar.SHOW_SIDEBAR_BY_DEFAULT, sidebar.isShown());
        assertNull(sidebar.getSidebarBackground());
        assertFalse(sidebar.isAnimationRunning());
        assertFalse(sidebar.isDragging());
    }

    /**
     * Tests the functionality of the method, which allows to set the content view and expects a
     * resource id as a parameter.
     */
    public final void testSetContentViewWithResourceIdParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setContentView(R.layout.view);
        assertNotNull(sidebar.getContentView());
    }

    /**
     * Tests the functionality of the method, which allows to set the content view and expects a
     * view as a parameter.
     */
    public final void testSetContentViewWithViewParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        View view = new View(getContext());
        sidebar.setContentView(view);
        assertEquals(view, sidebar.getContentView());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * content view, if the view is null.
     */
    public final void testSetContentViewThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentView(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar view and expects a
     * resource id as a parameter.
     */
    public final void testSetSidebarViewWithResourceIdParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setSidebarView(R.layout.view);
        assertNotNull(sidebar.getSidebarView());
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar view and expects a
     * view as a parameter.
     */
    public final void testSetSidebarViewWithViewParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        View view = new View(getContext());
        sidebar.setSidebarView(view);
        assertEquals(view, sidebar.getSidebarView());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * sidebar view, if the view is null.
     */
    public final void testSetSidebarViewThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarView(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar's background and
     * expects a drawable as a parameter.
     */
    public final void testSetSidebarBackgroundWithDrawableParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        Drawable background = new ColorDrawable(Color.BLACK);
        sidebar.setSidebarBackground(background);
        assertEquals(background, sidebar.getSidebarBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar's background and
     * expects a drawable as a parameter, if the background is set to null.
     */
    public final void testSetSidebarBackgroundWithDrawableParameterToNull() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setSidebarBackground(null);
        assertNull(sidebar.getSidebarBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar's background and
     * expects a resource id as a parameter.
     */
    public final void testSetSidebarBackgroundWithResourceIdParameter() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setSidebarBackground(R.drawable.sidebar_background_left_dark);
        assertNotNull(sidebar.getSidebarBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the sidebar's background and
     * expects a resource id as a parameter, if the resource id is -1.
     */
    public final void testSetSidebarBackgroundWithResourceIdParameterToNegativeOne() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setSidebarBackground(-1);
        assertNull(sidebar.getSidebarBackground());
    }

    /**
     * Tests the functionality of the method, which allows to set the location of the sidebar.
     */
    public final void testSetLocation() {
        Sidebar sidebar = new Sidebar(getContext());
        Location location = Location.LEFT;
        sidebar.setLocation(location);
        assertEquals(location, sidebar.getLocation());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * location of the sidebar, if the location is null.
     */
    public final void testSetLocationThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setLocation(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the animation speed.
     */
    public final void testSetAnimationSpeed() {
        Sidebar sidebar = new Sidebar(getContext());
        float animationSpeed = 1.0f;
        sidebar.setAnimationSpeed(animationSpeed);
        assertEquals(animationSpeed, sidebar.getAnimationSpeed());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the animation speed, if the animation speed is not greater than 0.
     */
    public final void testSetAnimationSpeedThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setAnimationSpeed(0.0f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the width of the sidebar.
     */
    public final void testSetSidebarWidth() {
        Sidebar sidebar = new Sidebar(getContext());
        float sidebarWidth = 0.5f;
        sidebar.setSidebarWidth(sidebarWidth);
        assertEquals(sidebarWidth, sidebar.getSidebarWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the width of the sidebar, if the width is less than 0.
     */
    public final void testSetSidebarWidthThrowsExceptionIfWidthIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarWidth(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the width of the sidebar, if the width is greater than 1.
     */
    public final void testSetSidebarWidthThrowsExceptionIfWidthIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarWidth(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the width of the sidebar, if the width is not greater than the offset.
     */
    public final void testSetSidebarWidthThrowsExceptionIfWidthIsNotGreaterThanOffset() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarWidth(12.125f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the maximum width of the sidebar.
     */
    public final void testSetMaxSidebarWidth() {
        Sidebar sidebar = new Sidebar(getContext());
        int maxSidebarWidth = 1;
        sidebar.setMaxSidebarWidth(maxSidebarWidth);
        assertEquals(maxSidebarWidth, sidebar.getMaxSidebarWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the maximum width of the sidebar, if the width is not greater than 0 and unlike -1.
     */
    public final void testSetMaxSidebarWidthThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setMaxSidebarWidth(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the offset of the sidebar.
     */
    public final void testSetSidebarOffset() {
        Sidebar sidebar = new Sidebar(getContext());
        float sidebarOffset = 0.1f;
        sidebar.setSidebarOffset(sidebarOffset);
        assertEquals(sidebarOffset, sidebar.getSidebarOffset());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the offset of the sidebar, if the offset is less than 0.
     */
    public final void testSetSidebarOffsetThrowsExceptionIfOffsetIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarOffset(-0.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the offset of the sidebar, if the offset is greater than 1.
     */
    public final void testSetSidebarOffsetThrowsExceptionIfOffsetIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarOffset(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the offset of the sidebar, if the offset is not less than the width.
     */
    public final void testSetSidebarOffsetThrowsExceptionIfOffsetIsNotLessThanWidth() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarOffset(0.75f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the maximum offset of the
     * sidebar.
     */
    public final void testSetMaxSidebarOffset() {
        Sidebar sidebar = new Sidebar(getContext());
        int maxSidebarOffset = 1;
        sidebar.setMaxSidebarOffset(maxSidebarOffset);
        assertEquals(maxSidebarOffset, sidebar.getMaxSidebarOffset());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the maximum offset of the sidebar, if the offset is not greater than 0 and unlike -1.
     */
    public final void testSetMaxSidebarOffsetThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setMaxSidebarOffset(0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the content mode.
     */
    public final void testSetContentMode() {
        Sidebar sidebar = new Sidebar(getContext());
        ContentMode contentMode = ContentMode.RESIZE;
        sidebar.setContentMode(contentMode);
        assertEquals(contentMode, sidebar.getContentMode());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * content mode, if the content mode is null.
     */
    public final void testSetContentModeThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentMode(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the scroll ratio.
     */
    public final void testSetScrollRatio() {
        Sidebar sidebar = new Sidebar(getContext());
        float scrollRatio = 0.75f;
        sidebar.setScrollRatio(scrollRatio);
        assertEquals(scrollRatio, sidebar.getScrollRatio());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the scroll ratio, if the scroll ratio is less than 0.
     */
    public final void testSetScrollRatioThrowsExceptionIfScrollRatioIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setScrollRatio(-0.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the scroll ratio, if the scroll ratio is greater than 1.
     */
    public final void testSetScrollRatioThrowsExceptionIfScrollRatioIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setScrollRatio(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the drag mode, which is used when
     * the sidebar is hidden.
     */
    public final void testSetDragModeWhenHidden() {
        Sidebar sidebar = new Sidebar(getContext());
        DragMode dragMode = DragMode.DISABLED;
        sidebar.setDragModeWhenHidden(dragMode);
        assertEquals(dragMode, sidebar.getDragModeWhenHidden());
    }

    /**
     * Ensures, that a {@link NullPointerException} is throw by the method, which allows to set the
     * drag mode, which is used when the sidebar is hidden, if the drag mode is null.
     */
    public final void testSetDragModeWhenHiddenThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragModeWhenHidden(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the drag mode, which is used when
     * the sidebar is shown.
     */
    public final void testSetDragModeWhenShown() {
        Sidebar sidebar = new Sidebar(getContext());
        DragMode dragMode = DragMode.DISABLED;
        sidebar.setDragModeWhenShown(dragMode);
        assertEquals(dragMode, sidebar.getDragModeWhenShown());
    }

    /**
     * Ensures, that a {@link NullPointerException} is throw by the method, which allows to set the
     * drag mode, which is used when the sidebar is shown, if the drag mode is null.
     */
    public final void testSetDragModeWhenShownThrowsExceptionWhenNull() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragModeWhenShown(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is throw by the method, which allows to set
     * the drag mode, which is used when the sidebar is shown, if the drag mode
     * <code>DragMode.EDGE</code>.
     */
    public final void testSetDragModeWhenShownThrowsExceptionWhenEdge() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragModeWhenShown(DragMode.EDGE);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the drag threshold.
     */
    public final void testSetDragThreshold() {
        Sidebar sidebar = new Sidebar(getContext());
        float dragThreshold = 0.33f;
        sidebar.setDragThreshold(dragThreshold);
        assertEquals(dragThreshold, sidebar.getDragThreshold());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the drag threshold, if the threshold is less than 0.
     */
    public final void testSetDragThresholdThrowsExceptionIfThresholdIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragThreshold(-0.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the drag threshold, if the threshold is greater than 1.
     */
    public final void testSetDragThresholdThrowsExceptionIfThresholdIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragThreshold(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the drag sensitivity.
     */
    public final void testSetDragSensitivity() {
        Sidebar sidebar = new Sidebar(getContext());
        float dragSensitivity = 0.33f;
        sidebar.setDragSensitivity(dragSensitivity);
        assertEquals(dragSensitivity, sidebar.getDragSensitivity());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the drag sensitivity, if the sensitivity is less than 0.
     */
    public final void testSetDragSensitivityThrowsExceptionIfSensitivityIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragSensitivity(-0.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the drag sensitivity, if the sensitivity is greater than 1.
     */
    public final void testSetDragSensitivityThrowsExceptionIfSensitivityIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setDragSensitivity(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the sidebar should be
     * hidden, when the device's back button is clicked.
     */
    public final void testHideOnBackButton() {
        Sidebar sidebar = new Sidebar(getContext());
        boolean hideOnBackButton = false;
        sidebar.hideOnBackButton(hideOnBackButton);
        assertEquals(hideOnBackButton, sidebar.isHiddenOnBackButton());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the sidebar should be
     * hidden, when the content is clicked.
     */
    public final void testHideOnContentClick() {
        Sidebar sidebar = new Sidebar(getContext());
        boolean hideOnContentClick = false;
        sidebar.hideOnContentClick(hideOnContentClick);
        assertEquals(hideOnContentClick, sidebar.isHiddenOnContentClick());
    }

    /**
     * Tests the functionality of the method, which allows to set, whether the sidebar should be
     * shown, when it is clicked.
     */
    public final void testShowOnSidebarClick() {
        Sidebar sidebar = new Sidebar(getContext());
        boolean showOnSidebarClick = false;
        sidebar.showOnSidebarClick(showOnSidebarClick);
        assertEquals(showOnSidebarClick, sidebar.isShownOnSidebarClick());
    }

    /**
     * Tests the functionality of the method, which allows to set the color of the content overlay.
     */
    public final void testSetContentOverlayColor() {
        Sidebar sidebar = new Sidebar(getContext());
        int contentOverlayColor = Color.RED;
        sidebar.setContentOverlayColor(contentOverlayColor);
        assertEquals(contentOverlayColor, sidebar.getContentOverlayColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the transparency of the content
     * overlay.
     */
    public final void testSetContentOverlayTransparency() {
        Sidebar sidebar = new Sidebar(getContext());
        float contentOverlayTransparency = 0.33f;
        sidebar.setContentOverlayTransparency(contentOverlayTransparency);
        assertEquals(contentOverlayTransparency, sidebar.getContentOverlayTransparency());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the transparency of the content overlay, if the transparency is less than 0.
     */
    public final void testSetContentOverlayTransparencyThrowsExceptionIfTransparencyIsLessThanZero() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentOverlayTransparency(-0.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the transparency of the content overlay, if the transparency is greater than 1.
     */
    public final void testSetContentOverlayTransparencyThrowsExceptionIfTransparencyIsGreaterThanOne() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentOverlayTransparency(1.1f);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the elevation of the sidebar.
     */
    public final void testSetSidebarElevation() {
        Sidebar sidebar = new Sidebar(getContext());
        int elevation = 2;
        sidebar.setSidebarElevation(elevation);
        assertEquals(elevation, sidebar.getSidebarElevation());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the elevation of the sidebar, if the elevation is less than 0.
     */
    public final void testSetSidebarElevationThrowsExceptionWhenElevationIsLessThan0() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarElevation(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the elevation of the sidebar, if the elevation is greater than the maximum.
     */
    public final void testSetSidebarElevationThrowsExceptioWhenElevationIsGreaterThanMaximum() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarElevation(ElevationUtil.MAX_ELEVATION + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to add a listener, which should be
     * notified when the sidebar becomes shown or hidden.
     */
    public final void testAddSidebarListener() {
        Sidebar sidebar = new Sidebar(getContext());
        SidebarListener listener = mock(SidebarListener.class);
        sidebar.addSidebarListener(listener);
        sidebar.addSidebarListener(listener);
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to add a
     * listener, which should be notified when the sidebar becomes shown or hidden, if the listener
     * is null.
     */
    public final void testAddSidebarListenerThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.addSidebarListener(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to remove a listener, which should not be
     * notified when the sidebar becomes shown or hidden, anymore.
     */
    public final void testRemoveSidebarListener() {
        Sidebar sidebar = new Sidebar(getContext());
        SidebarListener listener = mock(SidebarListener.class);
        sidebar.removeSidebarListener(listener);
        sidebar.addSidebarListener(listener);
        sidebar.removeSidebarListener(listener);
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to remove
     * a listener, which should not be notified when the sidebar becomes shown or hidden, anymore,
     * if the listener is null.
     */
    public final void testRemoveSidebarListenerThrowsException() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.removeSidebarListener(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to show the sidebar.
     */
    public final void testShowSidebar() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setContentView(R.layout.view);
        sidebar.setSidebarView(R.layout.view);
        sidebar.showSidebar();
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to show
     * the sidebar, if the content view is not set.
     */
    public final void testShowSidebarThrowsExceptionIfContentViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarView(R.layout.view);
            sidebar.showSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to show
     * the sidebar, if the sidebar view is not set.
     */
    public final void testShowSidebarThrowsExceptionIfSidebarViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentView(R.layout.view);
            sidebar.showSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to hide the sidebar.
     */
    public final void testHideSidebar() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setContentView(R.layout.view);
        sidebar.setSidebarView(R.layout.view);
        sidebar.hideSidebar();
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to hide
     * the sidebar, if the content view is not set.
     */
    public final void testHideSidebarThrowsExceptionIfContentViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarView(R.layout.view);
            sidebar.hideSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to hide
     * the sidebar, if the sidebar view is not set.
     */
    public final void testHideSidebarThrowsExceptionIfSidebarViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentView(R.layout.view);
            sidebar.hideSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to toggle the sidebar.
     */
    public final void testToggleSidebar() {
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setContentView(R.layout.view);
        sidebar.setSidebarView(R.layout.view);
        sidebar.toggleSidebar();
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to
     * toggle the sidebar, if the content view is not set.
     */
    public final void testToggleSidebarThrowsExceptionIfContentViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setSidebarView(R.layout.view);
            sidebar.toggleSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalStateException} is thrown by the method, which allows to
     * toggle the sidebar, if the sidebar view is not set.
     */
    public final void testToggleSidebarThrowsExceptionIfSidebarViewIsNotSet() {
        try {
            Sidebar sidebar = new Sidebar(getContext());
            sidebar.setContentView(R.layout.view);
            sidebar.toggleSidebar();
            Assert.fail();
        } catch (IllegalStateException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the onSaveInstanceState-method.
     */
    public final void testOnSaveInstanceState() {
        Sidebar sidebar = new Sidebar(getContext());
        SidebarSavedState savedState = (SidebarSavedState) sidebar.onSaveInstanceState();
        assertEquals(savedState.getLocation(), sidebar.getLocation());
        assertEquals(savedState.getAnimationSpeed(), sidebar.getAnimationSpeed());
        assertEquals(savedState.getSidebarWidth(), sidebar.getSidebarWidth());
        assertEquals(savedState.getMaxSidebarWidth(), sidebar.getMaxSidebarWidth());
        assertEquals(savedState.getSidebarOffset(), sidebar.getSidebarOffset());
        assertEquals(savedState.getMaxSidebarOffset(), sidebar.getMaxSidebarOffset());
        assertEquals(savedState.getContentMode(), sidebar.getContentMode());
        assertEquals(savedState.getScrollRatio(), sidebar.getScrollRatio());
        assertEquals(savedState.getDragThreshold(), sidebar.getDragThreshold());
        assertEquals(savedState.getDragSensitivity(), sidebar.getDragSensitivity());
        assertEquals(savedState.getDragModeWhenHidden(), sidebar.getDragModeWhenHidden());
        assertEquals(savedState.getDragModeWhenShown(), sidebar.getDragModeWhenShown());
        assertEquals(savedState.isHideOnBackButton(), sidebar.isHiddenOnBackButton());
        assertEquals(savedState.isHideOnContentClick(), sidebar.isHiddenOnContentClick());
        assertEquals(savedState.isShowOnSidebarClick(), sidebar.isShownOnSidebarClick());
        assertEquals(savedState.getContentOverlayColor(), sidebar.getContentOverlayColor());
        assertEquals(savedState.getContentOverlayTransparency(),
                sidebar.getContentOverlayTransparency());
        assertEquals(savedState.getSidebarElevation(), sidebar.getSidebarElevation());
        assertEquals(savedState.isShown(), sidebar.isShown());
    }

    /**
     * Tests the functionality of the onRestoreInstanceState-method.
     */
    public final void testRestoreInstanceState() {
        Location location = Location.LEFT;
        float animationSpeed = 0.33f;
        float sidebarWidth = 0.33f;
        int maxSidebarWidth = 2;
        float sidebarOffset = 0.22f;
        int maxSidebarOffset = 1;
        ContentMode contentMode = ContentMode.RESIZE;
        float scrollRatio = 0.33f;
        float dragThreshold = 0.33f;
        float dragSensitivity = 0.33f;
        DragMode dragModeWhenHidden = DragMode.DISABLED;
        DragMode dragModeWhenShown = DragMode.DISABLED;
        boolean hideOnBackButton = false;
        boolean hideOnContentClick = false;
        boolean showOnSidebarClick = false;
        int contentOverlayColor = Color.RED;
        float contentOverlayTransparency = 0.33f;
        int sidebarElevation = 2;
        Sidebar sidebar = new Sidebar(getContext());
        sidebar.setLocation(location);
        sidebar.setAnimationSpeed(animationSpeed);
        sidebar.setSidebarWidth(sidebarWidth);
        sidebar.setMaxSidebarWidth(maxSidebarWidth);
        sidebar.setSidebarOffset(sidebarOffset);
        sidebar.setMaxSidebarOffset(maxSidebarOffset);
        sidebar.setContentMode(contentMode);
        sidebar.setScrollRatio(scrollRatio);
        sidebar.setDragThreshold(dragThreshold);
        sidebar.setDragSensitivity(dragSensitivity);
        sidebar.setDragModeWhenHidden(dragModeWhenHidden);
        sidebar.setDragModeWhenShown(dragModeWhenShown);
        sidebar.hideOnBackButton(hideOnBackButton);
        sidebar.hideOnContentClick(hideOnContentClick);
        sidebar.showOnSidebarClick(showOnSidebarClick);
        sidebar.setContentOverlayColor(contentOverlayColor);
        sidebar.setContentOverlayTransparency(contentOverlayTransparency);
        sidebar.setSidebarElevation(sidebarElevation);
        Parcelable parcelable = sidebar.onSaveInstanceState();
        Sidebar restoredSidebar = new Sidebar(getContext());
        restoredSidebar.onRestoreInstanceState(parcelable);
        assertEquals(location, restoredSidebar.getLocation());
        assertEquals(animationSpeed, restoredSidebar.getAnimationSpeed());
        assertEquals(sidebarWidth, restoredSidebar.getSidebarWidth());
        assertEquals(maxSidebarWidth, restoredSidebar.getMaxSidebarWidth());
        assertEquals(sidebarOffset, restoredSidebar.getSidebarOffset());
        assertEquals(maxSidebarOffset, restoredSidebar.getMaxSidebarOffset());
        assertEquals(contentMode, restoredSidebar.getContentMode());
        assertEquals(scrollRatio, restoredSidebar.getScrollRatio());
        assertEquals(dragThreshold, restoredSidebar.getDragThreshold());
        assertEquals(dragSensitivity, restoredSidebar.getDragSensitivity());
        assertEquals(dragModeWhenHidden, restoredSidebar.getDragModeWhenHidden());
        assertEquals(dragModeWhenShown, restoredSidebar.getDragModeWhenShown());
        assertEquals(hideOnBackButton, restoredSidebar.isHiddenOnBackButton());
        assertEquals(hideOnContentClick, restoredSidebar.isHiddenOnContentClick());
        assertEquals(showOnSidebarClick, restoredSidebar.isShownOnSidebarClick());
        assertEquals(contentOverlayColor, restoredSidebar.getContentOverlayColor());
        assertEquals(contentOverlayTransparency, restoredSidebar.getContentOverlayTransparency());
        assertEquals(sidebarElevation, restoredSidebar.getSidebarElevation());
    }

}