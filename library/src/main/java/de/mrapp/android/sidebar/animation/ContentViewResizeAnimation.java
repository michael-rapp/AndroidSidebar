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

import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.view.ContentView;

import static de.mrapp.android.sidebar.util.Condition.ensureNotNull;

/**
 * An animation, which allows to resize a sidebar's content view, when the sidebar should be shown
 * or hidden.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class ContentViewResizeAnimation extends AnimationSet {

    /**
     * Creates a new animation, which allows to resize a sidebar's content view, when the sidebar
     * should be shown or hidden.
     *
     * @param contentView
     *         The content view, which should be moved, as an instance of the class {@link
     *         ContentView}. The content view may not be null
     * @param duration
     *         The duration of the animation in milliseconds, as a {@link Long} value. The duration
     *         may not be less than 0
     * @param distance
     *         The distance, the sidebar view should be moved by, as a {@link Float} value. If the
     *         value is negative, the view will be moved to the left, if it is positive, the view
     *         will be moved to the right
     * @param location
     *         The location of the sidebar as a value of the enum {@link Location}. The location may
     *         either be <code>LEFT</code> or <code>RIGHT</code>
     * @param overlayTransparency
     *         The transparency of the overlay, which should be applied, when the sidebar is shown,
     *         as a {@link Float} value. If set to 0.0, the overlay will be completely transparent,
     *         if set to 1.0, the overlay will not be transparent at all
     * @param show
     *         True, if the sidebar should be shown at the end of the animation, false otherwise
     */
    public ContentViewResizeAnimation(@NonNull final ContentView contentView, final long duration,
                                      final float distance, @NonNull final Location location,
                                      final float overlayTransparency, final boolean show) {
        super(true);
        ensureNotNull(location, "The location may not be null");
        setDuration(duration);

        Animation overlayAnimation =
                new ContentOverlayAnimation(contentView, overlayTransparency, show);
        addAnimation(overlayAnimation);

        if (location == Location.LEFT) {
            Animation resizeAnimation = new ResizeAnimation(contentView, -distance);
            addAnimation(resizeAnimation);

            Animation translateAnimation = new TranslateAnimation(0, distance, 0, 0);
            addAnimation(translateAnimation);
        } else {
            Animation resizeAnimation = new ResizeAnimation(contentView, distance);
            addAnimation(resizeAnimation);
        }
    }

}