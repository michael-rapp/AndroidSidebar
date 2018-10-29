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

import androidx.annotation.NonNull;
import android.view.animation.TranslateAnimation;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An animation, which allows to move a sidebar's sidebar view, when the sidebar should be shown or
 * hidden.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SidebarViewAnimation extends TranslateAnimation {

    /**
     * Creates a new animation, which allows to move a sidebar's sidebar view, when the sidebar
     * should be shown or hidden.
     *
     * @param distance
     *         The distance, the sidebar view should be moved by, as a {@link Float} value. If the
     *         value is negative, the view will be moved to the left, if it is positive, the view
     *         will be moved to the right
     * @param duration
     *         The duration of the animation in milliseconds, as a {@link Long} value. The duration
     *         may not be less than 0
     * @param listener
     *         The listener, which should be notified about the animation's progress, as an instance
     *         of the type {@link AnimationListener} . The listener may not be null
     */
    public SidebarViewAnimation(final float distance, final long duration,
                                @NonNull final AnimationListener listener) {
        super(0, distance, 0, 0);
        ensureNotNull(listener, "The animation listener may not be null");
        setDuration(duration);
        setAnimationListener(listener);
    }

}