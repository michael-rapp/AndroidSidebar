/*
 * AndroidSidebar Copyright 2014 - 2016 Michael Rapp
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
package de.mrapp.android.sidebar.savedstate;

import android.os.Parcelable;

import junit.framework.TestCase;

import de.mrapp.android.sidebar.ContentMode;
import de.mrapp.android.sidebar.DragMode;
import de.mrapp.android.sidebar.Location;

import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link SidebarSavedState}.
 *
 * @author Michael Rapp
 */
public class SidebarSavedStateTest extends TestCase {

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "location".
     */
    public final void testSetLocation() {
        Location value = Location.LEFT;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setLocation(value);
        assertEquals(value, sidebarSavedState.getLocation());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "animationSpeed".
     */
    public final void testSetAnimationSpeed() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setAnimationSpeed(value);
        assertEquals(value, sidebarSavedState.getAnimationSpeed());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "sidebarWidth".
     */
    public final void testSetSidebarWidth() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setSidebarWidth(value);
        assertEquals(value, sidebarSavedState.getSidebarWidth());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "maxSidebarWidth".
     */
    public final void testSetMaxSidebarWidth() {
        int value = 1;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setMaxSidebarWidth(value);
        assertEquals(value, sidebarSavedState.getMaxSidebarWidth());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "sidebarOffset".
     */
    public final void testSetSidebarOffset() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setSidebarOffset(value);
        assertEquals(value, sidebarSavedState.getSidebarOffset());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "maxSidebarOffset".
     */
    public final void testSetMaxSidebarOffset() {
        int value = 1;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setMaxSidebarOffset(value);
        assertEquals(value, sidebarSavedState.getMaxSidebarOffset());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "contentMode".
     */
    public final void testSetContentMode() {
        ContentMode value = ContentMode.SCROLL;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setContentMode(value);
        assertEquals(value, sidebarSavedState.getContentMode());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "scrollRatio".
     */
    public final void testSetScrollRatio() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setScrollRatio(value);
        assertEquals(value, sidebarSavedState.getScrollRatio());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "dragThreshold".
     */
    public final void testSetDragThreshold() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setDragThreshold(value);
        assertEquals(value, sidebarSavedState.getDragThreshold());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "dragSensitivity".
     */
    public final void testSetDragSensitivity() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setDragSensitivity(value);
        assertEquals(value, sidebarSavedState.getDragSensitivity());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "dragModeWhenHidden".
     */
    public final void testSetDragModeWhenHidden() {
        DragMode value = DragMode.BOTH;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setDragModeWhenHidden(value);
        assertEquals(value, sidebarSavedState.getDragModeWhenHidden());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "dragModeWhenShown".
     */
    public final void testSetDragModeWhenShown() {
        DragMode value = DragMode.BOTH;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setDragModeWhenShown(value);
        assertEquals(value, sidebarSavedState.getDragModeWhenShown());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "hideOnBackButton".
     */
    public final void testSetHideOnBackButton() {
        boolean value = true;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setHideOnBackButton(value);
        assertEquals(value, sidebarSavedState.isHideOnBackButton());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "hideOnContentClick".
     */
    public final void testSetHideOnContentClick() {
        boolean value = true;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setHideOnContentClick(value);
        assertEquals(value, sidebarSavedState.isHideOnContentClick());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "showOnSidebarClick".
     */
    public final void testSetShowOnSidebarClick() {
        boolean value = true;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setShowOnSidebarClick(value);
        assertEquals(value, sidebarSavedState.isShowOnSidebarClick());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "contentOverlayColor".
     */
    public final void testSetContentOverlayColor() {
        int value = 1;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setContentOverlayColor(value);
        assertEquals(value, sidebarSavedState.getContentOverlayColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "contentOverlayTransparency".
     */
    public final void testSetContentOverlayTransparency() {
        float value = 1.0f;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setContentOverlayTransparency(value);
        assertEquals(value, sidebarSavedState.getContentOverlayTransparency());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "sidebarElevation".
     */
    public final void testSetSidebarElevation() {
        int value = 1;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setSidebarElevation(value);
        assertEquals(value, sidebarSavedState.getSidebarElevation());
    }

    /**
     * Tests the functionality of the method, which allows to set the saved value of the attribute
     * "shown".
     */
    public final void testSetShown() {
        boolean value = true;
        Parcelable parcelable = mock(Parcelable.class);
        SidebarSavedState sidebarSavedState = new SidebarSavedState(parcelable);
        sidebarSavedState.setShown(value);
        assertEquals(value, sidebarSavedState.isShown());
    }

}