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