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
package de.mrapp.android.sidebar.animation;

import android.graphics.Color;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.InflaterFactory;
import de.mrapp.android.sidebar.view.ContentView;

/**
 * Tests the functionality of the class {@link ContentViewScrollAnimation}.
 *
 * @author Michael Rapp
 */
public class ContentViewScrollAnimationTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor.
     */
    public final void testConstructor() {
        long duration = 1L;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Color.BLACK);
        ContentViewScrollAnimation contentViewAnimation =
                new ContentViewScrollAnimation(contentView, duration, 1.0f, 1.0f, 1.0f, true);
        assertEquals(duration, contentViewAnimation.getDuration());
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the content
     * view is null.
     */
    public final void testConstructorThrowsExceptionWhenContentViewIsNull() {
        try {
            new ContentViewScrollAnimation(null, 1L, 1.0f, 1.0f, 1.0f, true);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * duration is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenDurationIsLessThanZero() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentViewScrollAnimation(contentView, -1L, 1.0f, 1.0f, 1.0f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the scroll
     * ratio is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenScrollRatioIsLessThanZero() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentViewScrollAnimation(contentView, -1L, 1.0f, -1.0f, 1.0f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the scroll
     * ratio is greater than 1.
     */
    public final void testConstructorThrowsExceptionWhenScrollRatioIsGreaterThanOne() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentViewScrollAnimation(contentView, -1L, 1.0f, 1.1f, 1.0f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * overlay transparency is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenOverlayTransparencyIsLessThanZero() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentViewScrollAnimation(contentView, -1L, 1.0f, 1.0f, -1.0f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * overlay transparency is greater than 1.
     */
    public final void testConstructorThrowsExceptionWhenOverlayTransparencyIsGreaterThanOne() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentViewScrollAnimation(contentView, -1L, 1.0f, 1.0f, 1.1f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

}