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
package de.mrapp.android.sidebar.animation;

import android.test.AndroidTestCase;
import android.view.animation.Animation.AnimationListener;

import junit.framework.Assert;

import static org.mockito.Mockito.mock;

/**
 * Tests the functionality of the class {@link SidebarViewAnimation}.
 *
 * @author Michael Rapp
 */
public class SidebarViewAnimationTest extends AndroidTestCase {

    /**
     * Tests, if all properties are set correctly by the constructor.
     */
    public final void testConstructor() {
        long duration = 0L;
        SidebarViewAnimation sidebarViewAnimation =
                new SidebarViewAnimation(1.0f, duration, mock(AnimationListener.class));
        assertEquals(duration, sidebarViewAnimation.getDuration());
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is thrown by the constructor, if the
     * duration is less than 0.
     */
    public final void testConstructorThrowsExceptionWhenDurationIsLessThanZero() {
        try {
            new SidebarViewAnimation(1.0f, -1L, mock(AnimationListener.class));
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown by the constructor, if the animation
     * listener is null.
     */
    public final void testConstructorThrowsExceptionWhenListenerIsNull() {
        try {
            new SidebarViewAnimation(1.0f, 1L, null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

}