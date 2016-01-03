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