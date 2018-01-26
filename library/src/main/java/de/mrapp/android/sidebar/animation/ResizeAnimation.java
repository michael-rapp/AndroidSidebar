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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An animation, which allows to resize a view horizontally.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class ResizeAnimation extends Animation {

    /**
     * The view, which should be resized.
     */
    private View view;

    /**
     * The offset, the view should be resized by.
     */
    private int widthOffset;

    /**
     * The view's original width.
     */
    private int originalWidth;

    /**
     * The width, the view should be resized to.
     */
    private int targetWidth;

    /**
     * Creates a new animation, which allows to resize a view horizontally.
     *
     * @param view
     *         The view, which should be resized, as an instance of the class {@link View}. The view
     *         may not be null
     * @param widthOffset
     *         The offset, the view should be resized by, as a {@link Float} value. If the offset is
     *         negative, the view's width will become decreased, if the offset is positive, the
     *         width will become increased
     */
    public ResizeAnimation(@NonNull final View view, final float widthOffset) {
        ensureNotNull(view, "The view may not be null");
        this.view = view;
        this.widthOffset = Math.round(widthOffset);
        this.originalWidth = view.getWidth();
        this.targetWidth = originalWidth + this.widthOffset;
    }

    @Override
    protected final void applyTransformation(final float interpolatedTime,
                                             final Transformation transformation) {
        super.applyTransformation(interpolatedTime, transformation);
        view.getLayoutParams().width = Math.round(originalWidth + widthOffset * interpolatedTime);
        view.layout(view.getLeft(), view.getTop(),
                view.getLeft() + Math.max(targetWidth, originalWidth), view.getBottom());
        view.requestLayout();
    }

    @Override
    public final boolean willChangeBounds() {
        return true;
    }

}