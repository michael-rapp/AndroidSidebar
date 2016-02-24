/*
 * Copyright 2014 - 2016 Michael Rapp
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
 * Tests the functionality of the {@link ContentOverlayAnimation}.
 *
 * @author Michael Rapp
 */
public class ContentOverlayAnimationTest extends AndroidTestCase {

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the content
     * view is null.
     */
    public final void testConstructorThrowsExceptionWhenContentViewIsNull() {
        try {
            new ContentOverlayAnimation(null, 1.0f, true);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Ensures, that a {@link IllegalArgumentException} is thrown by the constructor, if the overlay
     * transparency is less then 0.
     */
    public final void testConstructorThrowsExceptionWhenOverlayTransparencyIsLessThanZero() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentOverlayAnimation(contentView, -1.0f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that a {@link IllegalArgumentException} is thrown by the constructor, if the overlay
     * transparency is greater than 1.
     */
    public final void testConstructorThrowsExceptionWhenOverlayTransparencyIsGreaterThanOne() {
        try {
            ContentView contentView =
                    new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                            Color.BLACK);
            new ContentOverlayAnimation(contentView, 1.1f, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the applyTransformation-method, if the sidebar should be shown at
     * the end of the animation.
     */
    public final void testApplyTransformationWhenSidebarShouldBeShown() {
        float contentTransparency = 0.5f;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Color.BLACK);
        ContentOverlayAnimation contentOverlayAnimation =
                new ContentOverlayAnimation(contentView, contentTransparency, true);
        contentOverlayAnimation.applyTransformation(0.0f, null);
        assertEquals(0.0f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.25f, null);
        assertEquals(0.125f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.5f, null);
        assertEquals(0.25f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.75f, null);
        assertEquals(0.375f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(1.0f, null);
        assertEquals(0.5f, contentView.getOverlayTransparency());
    }

    /**
     * Tests the functionality of the applyTransformation-method, if the sidebar should be hidden at
     * the end of the animation.
     */
    public final void testApplyTransformationWhenSidebarShouldBeHidden() {
        float contentTransparency = 0.5f;
        ContentView contentView =
                new ContentView(getContext(), InflaterFactory.createInflater(R.layout.view),
                        Color.BLACK);
        contentView.setOverlayTransparency(contentTransparency);
        ContentOverlayAnimation contentOverlayAnimation =
                new ContentOverlayAnimation(contentView, contentTransparency, false);
        contentOverlayAnimation.applyTransformation(0.0f, null);
        assertEquals(0.5f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.25f, null);
        assertEquals(0.375f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.5f, null);
        assertEquals(0.25f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(0.75f, null);
        assertEquals(0.125f, contentView.getOverlayTransparency());
        contentOverlayAnimation.applyTransformation(1.0f, null);
        assertEquals(0.0f, contentView.getOverlayTransparency());
    }

}