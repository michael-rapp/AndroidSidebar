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
package de.mrapp.android.sidebar.view;

import android.graphics.Color;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.InflaterFactory;

/**
 * Tests the functionality of the class {@link ContentView}.
 *
 * @author Michael Rapp
 */
public class ContentViewTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor.
     */
    public final void testConstructor() {
        int overlayColor = Color.BLACK;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        overlayColor);
        assertEquals(0.0f, contentView.getOverlayTransparency());
        assertEquals(overlayColor, contentView.getOverlayColor());
        assertNotNull(contentView.getContentView());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the context is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenContextIsNull() {
        try {
            new ContentView(null, InflaterFactory.createInflater(R.layout.view), Color.BLACK);
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
            new ContentView(getContext(), null, Color.BLACK);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the method, which allows to set the color of the overlay.
     */
    public final void testSetOverlayColor() {
        int overlayColor = Color.RED;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Color.BLACK);
        contentView.setOverlayColor(overlayColor);
        assertEquals(overlayColor, contentView.getOverlayColor());
    }

    /**
     * Tests the functionality of the method, which allows to set the transparency of the overlay.
     */
    public final void testSetOverlayTransparency() {
        float overlayTransparency = 0.5f;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Color.BLACK);
        contentView.setOverlayTransparency(overlayTransparency);
        assertEquals(overlayTransparency, contentView.getOverlayTransparency());
    }

}