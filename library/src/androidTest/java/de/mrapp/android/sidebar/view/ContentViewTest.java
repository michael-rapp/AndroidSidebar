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