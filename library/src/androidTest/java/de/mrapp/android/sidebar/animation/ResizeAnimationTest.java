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

import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import junit.framework.Assert;

/**
 * Tests the functionality of the class {@link ResizeAnimation}.
 *
 * @author Michael Rapp
 */
public class ResizeAnimationTest extends AndroidTestCase {

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the view is
     * null.
     */
    public final void testConstructorThrowsExceptionWhenViewIsNull() {
        try {
            new ResizeAnimation(null, 0.0f);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

    /**
     * Tests the functionality of the willChangeBounds-method.
     */
    public final void testWillChangeBounds() {
        ResizeAnimation resizeAnimation = new ResizeAnimation(new View(getContext()), 0.0f);
        assertTrue(resizeAnimation.willChangeBounds());
    }

    /**
     * Tests the functionality of the applyTransformation-method.
     */
    public final void testApplyTransformation() {
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        View view = new View(getContext());
        view.setLayoutParams(layoutParams);
        ResizeAnimation resizeAnimation = new ResizeAnimation(view, 4.0f);
        resizeAnimation.applyTransformation(0.0f, null);
        assertEquals(0, view.getLayoutParams().width);
        resizeAnimation.applyTransformation(0.25f, null);
        assertEquals(1, view.getLayoutParams().width);
        resizeAnimation.applyTransformation(0.5f, null);
        assertEquals(2, view.getLayoutParams().width);
        resizeAnimation.applyTransformation(0.75f, null);
        assertEquals(3, view.getLayoutParams().width);
        resizeAnimation.applyTransformation(1.0f, null);
        assertEquals(4, view.getLayoutParams().width);
    }

}