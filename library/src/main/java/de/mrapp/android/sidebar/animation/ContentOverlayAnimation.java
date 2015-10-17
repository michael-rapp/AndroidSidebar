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

import de.mrapp.android.sidebar.view.ContentView;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;
import static de.mrapp.android.sidebar.util.Condition.ensureAtLeast;
import static de.mrapp.android.sidebar.util.Condition.ensureAtMaximum;

/**
 * An animation, which allows to fade in or out the overlay, which is shown in
 * front of a sidebar's content view, while the sidebar is shown.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class ContentOverlayAnimation extends Animation {

	/**
	 * The content view, the overlay should be shown in front of.
	 */
	private final ContentView contentView;

	/**
	 * The transparency of the overlay, which should be applied, when the
	 * sidebar is shown.
	 */
	private final float overlayTransparency;

	/**
	 * True, if the sidebar should be shown at the end of the animation, false
	 * otherwise.
	 */
	private final boolean show;

	/**
	 * The transparency of the overlay, which was applied, when the animation
	 * has been started or -1, if the animation has not been started yet.
	 */
	private float initialTransparency;

	/**
	 * Creates a new animation, which allows to fade in or out the overlay,
	 * which is shown in front of a sidebar's content view, while the sidebar is
	 * shown.
	 * 
	 * @param contentView
	 *            The content view, the overlay should be shown in front of, as
	 *            an instance of the class {@link ContentView}. The content view
	 *            may not be null
	 * @param overlayTransparency
	 *            The transparency of the overlay, which should be applied, when
	 *            the sidebar is shown, as a {@link Float} value. If set to 0.0,
	 *            the overlay will be completely transparent, if set to 1.0, the
	 *            overlay will not be transparent at all
	 * @param show
	 *            True, if the sidebar should be shown at the end of the
	 *            animation, false otherwise
	 */
	public ContentOverlayAnimation(final ContentView contentView,
			final float overlayTransparency, final boolean show) {
		ensureNotNull(contentView, "The content view may not be null");
		ensureAtLeast(overlayTransparency, 0,
				"The transparency must be at least 0");
		ensureAtMaximum(overlayTransparency, 1,
				"The transparency must be at maximum 1");
		this.contentView = contentView;
		this.overlayTransparency = overlayTransparency;
		this.show = show;
		this.initialTransparency = -1.0f;
	}

	@Override
	protected final void applyTransformation(final float interpolatedTime,
			final Transformation transformation) {
		super.applyTransformation(interpolatedTime, transformation);

		if (initialTransparency == -1.0f) {
			initialTransparency = contentView.getOverlayTransparency();
		}

		if (show) {
			contentView.setOverlayTransparency(initialTransparency
					+ interpolatedTime
					* (overlayTransparency - initialTransparency));
		} else {
			contentView.setOverlayTransparency(initialTransparency
					- interpolatedTime * (initialTransparency - 0));
		}
	}

}