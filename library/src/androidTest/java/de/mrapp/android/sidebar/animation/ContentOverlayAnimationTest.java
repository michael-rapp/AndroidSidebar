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