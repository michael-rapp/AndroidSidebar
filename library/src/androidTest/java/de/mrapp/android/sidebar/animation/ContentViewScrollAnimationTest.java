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