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
        int shadowWidth = 1;
        int shadowColor = Color.BLACK;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        location, sidebarBackground, shadowWidth, shadowColor);
        assertEquals(location, sidebarView.getLocation());
        assertEquals(sidebarBackground, sidebarView.getSidebarBackground());
        assertEquals(shadowWidth, sidebarView.getShadowWidth());
        assertEquals(shadowColor, sidebarView.getShadowColor());
        assertNotNull(sidebarView.getSidebarView());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the context is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenContextIsNull() {
        try {
            new SidebarView(null, InflaterFactory.createInflater(R.layout.view), Location.LEFT,
                    null, 0, Color.BLACK);
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
            new SidebarView(getContext(), null, Location.LEFT, null, 0, Color.BLACK);
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
                    0, Color.BLACK);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the shadow
     * width is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenShadowWidthIsLessThanZero() {
        try {
            new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                    Location.LEFT, null, -1, Color.BLACK);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the shadow color.
     */
    public final void testSetShadowColor() {
        int shadowColor = Color.RED;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 0, Color.BLACK);
        sidebarView.setShadowColor(shadowColor);
        assertEquals(shadowColor, sidebarView.getShadowColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the location of the sidebar.
     */
    public final void testSetLocation() {
        Location location = Location.RIGHT;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 0, Color.BLACK);
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
                            Location.LEFT, null, 0, Color.BLACK);
            sidebarView.setLocation(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the width of the shadow.
     */
    public final void testSetShadowWidth() {
        int shadowWidth = 2;
        SidebarView sidebarView =
                new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Location.LEFT, null, 0, Color.BLACK);
        sidebarView.setShadowWidth(shadowWidth);
        assertEquals(shadowWidth, sidebarView.getShadowWidth());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the method, which allows to
     * set the width of the shadow, if the width is less than 0.
     */
    public final void testSetShadowWidthThrowsException() {
        try {
            SidebarView sidebarView =
                    new SidebarView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Location.LEFT, null, 0, Color.BLACK);
            sidebarView.setShadowWidth(-1);
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
                        Location.LEFT, null, 0, Color.BLACK);
        sidebarView.setSidebarBackground(sidebarBackground);
        assertEquals(sidebarBackground, sidebarView.getSidebarBackground());
    }

}