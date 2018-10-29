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
import android.view.animation.Animation;
import android.view.animation.Transformation;

import de.mrapp.android.sidebar.view.ContentView;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An animation, which allows to fade in or out the overlay, which is shown in front of a sidebar's
 * content view, while the sidebar is shown.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class ContentOverlayAnimation extends Animation {

    /**
     * The content view, the overlay should be shown in front of.
     */
    private final ContentView contentView;

    /**
     * The transparency of the overlay, which should be applied, when the sidebar is shown.
     */
    private final float overlayTransparency;

    /**
     * True, if the sidebar should be shown at the end of the animation, false otherwise.
     */
    private final boolean show;

    /**
     * The transparency of the overlay, which was applied, when the animation has been started or
     * -1, if the animation has not been started yet.
     */
    private float initialTransparency;

    /**
     * Creates a new animation, which allows to fade in or out the overlay, which is shown in front
     * of a sidebar's content view, while the sidebar is shown.
     *
     * @param contentView
     *         The content view, the overlay should be shown in front of, as an instance of the
     *         class {@link ContentView}. The content view may not be null
     * @param overlayTransparency
     *         The transparency of the overlay, which should be applied, when the sidebar is shown,
     *         as a {@link Float} value. If set to 0.0, the overlay will be completely transparent,
     *         if set to 1.0, the overlay will not be transparent at all
     * @param show
     *         True, if the sidebar should be shown at the end of the animation, false otherwise
     */
    public ContentOverlayAnimation(@NonNull final ContentView contentView,
                                   final float overlayTransparency, final boolean show) {
        ensureNotNull(contentView, "The content view may not be null");
        ensureAtLeast(overlayTransparency, 0, "The transparency must be at least 0");
        ensureAtMaximum(overlayTransparency, 1, "The transparency must be at maximum 1");
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
            contentView.setOverlayTransparency(initialTransparency +
                    interpolatedTime * (overlayTransparency - initialTransparency));
        } else {
            contentView.setOverlayTransparency(
                    initialTransparency - interpolatedTime * (initialTransparency - 0));
        }
    }

}