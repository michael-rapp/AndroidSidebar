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