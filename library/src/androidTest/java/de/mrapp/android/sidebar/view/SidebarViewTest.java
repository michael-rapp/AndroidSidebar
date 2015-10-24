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
package de.mrapp.android.sidebar.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.util.ElevationUtil;

/**
 * Tests the functionality of the class {@link SidebarView}.
 *
 * @author Michael Rapp
 */
public class SidebarViewTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor.
     */
    public final void testConstructor() {
        Location location = Location.LEFT;
        Drawable sidebarBackground = null;
        int sidebarElevation = 1;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        location, sidebarBackground, sidebarElevation);
        assertEquals(location, sidebarView.getLocation());
        assertEquals(sidebarBackground, sidebarView.getSidebarBackground());
        assertEquals(sidebarElevation, sidebarView.getSidebarElevation());
        assertNotNull(sidebarView.getSidebarView());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the context is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenContextIsNull() {
        try {
            new SidebarView(null, InflaterFactory.createInflater(R.layout.view), Location.LEFT,
                    null, 1);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the inflater is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenInflaterIsNull() {
        try {
            new SidebarView(getContext(), null, Location.LEFT, null, 1);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the location is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenLocationIsNull() {
        try {
            new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view), null, null,
                    1);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * elevation is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenElevationIsLessThan0() {
        try {
            new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                    Location.LEFT, null, -1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * elevation is greater than the maximum elevation.
     */
    public final void testConstructorThrowsExceptionWhenElevationIsGreaterThanMaximum() {
        try {
            new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                    Location.LEFT, null, ElevationUtil.MAX_ELEVATION + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the location of the sidebar.
     */
    public final void testSetLocation() {
        Location location = Location.RIGHT;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 1);
        sidebarView.setLocation(location);
        assertEquals(location, sidebarView.getLocation());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the method, which allows to set the
     * location of the sidebar, if the location is null.
     */
    public final void testSetLocationThrowsException() {
        try {
            SidebarView sidebarView =
                    new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Location.LEFT, null, 1);
            sidebarView.setLocation(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the elevation of the sidebar.
     */
    public final void testSetSidebarElevation() {
        int elevation = 2;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 1);
        sidebarView.setSidebarElevation(elevation);
        assertEquals(elevation, sidebarView.getSidebarElevation());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the elevation of the sidebar, if the elevation is less than 0.
     */
    public final void testSetSidebarElevationThrowsExceptionWhenElevationIsLessThan0() {
        try {
            SidebarView sidebarView =
                    new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Location.LEFT, null, 1);
            sidebarView.setSidebarElevation(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the elevation of the sidebar, if the elevation is greater than the maximum value.
     */
    public final void testSetSidebarElevationThrowsExceptionWhenElevationIsGreaterThanMaximum() {
        try {
            SidebarView sidebarView =
                    new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Location.LEFT, null, 1);
            sidebarView.setSidebarElevation(ElevationUtil.MAX_ELEVATION + 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the background of the sidebar
     * view.
     */
    public final void testSetSidebarBackground() {
        Drawable sidebarBackground = new ColorDrawable(Color.BLACK);
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 1);
        sidebarView.setSidebarBackground(sidebarBackground);
        assertEquals(sidebarBackground, sidebarView.getSidebarBackground());
    }

}