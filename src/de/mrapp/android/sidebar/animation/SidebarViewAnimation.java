/*
 * AndroidSidebar Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.sidebar.animation;

import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;
import android.view.animation.TranslateAnimation;

/**
 * An animation, which allows to move a sidebar's sidebar view, when the sidebar
 * should be shown or hidden.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class SidebarViewAnimation extends TranslateAnimation {

	/**
	 * Creates a new animation, which allows to move a sidebar's sidebar view,
	 * when the sidebar should be shown or hidden.
	 * 
	 * @param distance
	 *            The distance, the sidebar view should be moved by, as a
	 *            {@link Float} value. If the value is negative, the view will
	 *            be moved to the left, if it is positive, the view will be
	 *            moved to the right
	 * @param duration
	 *            The duration of the animation in milliseconds, as a
	 *            {@link Long} value. The duration may not be less than 0
	 * @param listener
	 *            The listener, which should be notified about the animation's
	 *            progress, as an instance of the type {@link AnimationListener}
	 *            . The listener may not be null
	 */
	public SidebarViewAnimation(final float distance, final long duration,
			final AnimationListener listener) {
		super(0, distance, 0, 0);
		ensureNotNull(listener, "The animation listener may not be null");
		setDuration(duration);
		setAnimationListener(listener);
	}
}