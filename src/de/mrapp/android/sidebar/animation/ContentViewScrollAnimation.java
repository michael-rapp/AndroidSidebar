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

import static de.mrapp.android.sidebar.util.Condition.ensureAtLeast;
import static de.mrapp.android.sidebar.util.Condition.ensureAtMaximum;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import de.mrapp.android.sidebar.view.ContentView;

/**
 * An animation, which allows to move a sidebar's content view, when the sidebar
 * should be shown or hidden.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class ContentViewScrollAnimation extends AnimationSet {

	/**
	 * Creates a new animation, which allows to move a sidebar's content view,
	 * when the sidebar should be shown or hidden.
	 * 
	 * @param contentView
	 *            The content view, which should be moved, as an instance of the
	 *            class {@link ContentView}. The content view may not be null
	 * @param duration
	 *            The duration of the animation in milliseconds, as a
	 *            {@link Long} value. The duration may not be less than 0
	 * @param distance
	 *            The distance, the sidebar view should be moved by, as a
	 *            {@link Float} value. If the value is negative, the view will
	 *            be moved to the left, if it is positive, the view will be
	 *            moved to the right
	 * @param scrollRatio
	 *            The ratio between the distance, the sidebar view and the
	 *            content view are moved by, as a {@link Float} value. The ratio
	 *            may be at least 0 and at maximum 1. The distance is multiplied
	 *            by the ratio to calculate the distance, the content view
	 *            should be moved by
	 * @param overlayTransparency
	 *            The transparency of the overlay, which should be applied, when
	 *            the sidebar is shown, as a {@link Float} value. If set to 0.0,
	 *            the overlay will be completely transparent, if set to 1.0, the
	 *            overlay will not be transparent at all
	 * @param show
	 *            True, if the sidebar should be shown at the end of the
	 *            animation, false otherwise
	 */
	public ContentViewScrollAnimation(final ContentView contentView,
			final long duration, final float distance, final float scrollRatio,
			final float overlayTransparency, final boolean show) {
		super(true);
		ensureAtLeast(scrollRatio, 0, "The scroll ratio must be at least 0");
		ensureAtMaximum(scrollRatio, 1, "The scroll ratio must be at maximum 1");
		setDuration(duration);
		Animation translateAnimation = new TranslateAnimation(0, distance
				* scrollRatio, 0, 0);
		addAnimation(translateAnimation);
		Animation overlayAnimation = new ContentOverlayAnimation(contentView,
				overlayTransparency, show);
		addAnimation(overlayAnimation);
	}

}