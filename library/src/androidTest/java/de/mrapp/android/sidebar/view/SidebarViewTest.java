/*
 * Copyright 2014 - 2017 Michael Rapp
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