/*
 * Copyright 2014 - 2018 Michael Rapp
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

import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.view.ContentView;

import static de.mrapp.android.util.Condition.ensureNotNull;

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