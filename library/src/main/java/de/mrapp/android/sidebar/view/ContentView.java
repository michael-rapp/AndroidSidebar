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
package de.mrapp.android.sidebar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.util.Condition;

/**
 * A custom view, which contains the content view of a sidebar, as well as a view, which is used as
 * an overlay, while the sidebar is shown.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
@SuppressLint("ViewConstructor")
public class ContentView extends FrameLayout {

    /**
     * The content view.
     */
    private View contentView;

    /**
     * The view, which is used as an overlay of the content view.
     */
    private View overlayView;

    /**
     * The current transparency of the overlay.
     */
    private float overlayTransparency;

    /**
     * The current color of the overlay.
     */
    private int overlayColor;

    /**
     * Inflates the content view.
     *
     * @param inflater
     *         The inflater, which should be used to inflate the content view, as an instance of the
     *         type {@link Inflater}. The inflater may not be null
     */
    private void inflateContentView(@NonNull final Inflater inflater) {
        contentView = inflater.inflate(getContext(), null, false);
        addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Inflates the view, which is used as an overlay of the content view.
     */
    private void inflateOverlayView() {
        overlayView = new View(getContext());
        addView(overlayView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Creates a new custom view, which contains the content view of a sidebar, as well as a view,
     * which is used as an overlay, while the sidebar is shown.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     * @param inflater
     *         The inflater, which should be used to inflate the content view, as an instance of the
     *         type {@link Inflater}. The inflater may not be null
     * @param overlayColor
     *         The color of the overlay as an {@link Integer} value
     */
    public ContentView(@NonNull final Context context, @NonNull final Inflater inflater,
                       @ColorInt final int overlayColor) {
        super(context, null);
        Condition.INSTANCE.ensureNotNull(inflater, "The inflater may not be null");
        inflateContentView(inflater);
        inflateOverlayView();
        setOverlayColor(overlayColor);
        setOverlayTransparency(0.0f);
    }

    /**
     * Returns the current transparency of the overlay.
     *
     * @return The current transparency of the overlay as a {@link Float} value. If set to 0.0, the
     * overlay is completely transparent, if set to 1.0, the overlay is not transparent at all
     */
    public final float getOverlayTransparency() {
        return overlayTransparency;
    }

    /**
     * Sets the transparency of the overlay.
     *
     * @param transparency
     *         The transparency, which should be set, as a {@link Float} value. If set to 0.0, the
     *         overlay will be completely transparent, if set to 1.0, the overlay will not be
     *         transparent at all
     */
    public final void setOverlayTransparency(final float transparency) {
        this.overlayTransparency = transparency;
        AlphaAnimation animation = new AlphaAnimation(transparency, transparency);
        animation.setDuration(0);
        animation.setFillAfter(true);
        overlayView.startAnimation(animation);
    }

    /**
     * Returns the current color of the overlay.
     *
     * @return The current color of the overlay as an {@link Integer} value
     */
    public final int getOverlayColor() {
        return overlayColor;
    }

    /**
     * Sets the color of the overlay.
     *
     * @param overlayColor
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setOverlayColor(@ColorInt final int overlayColor) {
        this.overlayColor = overlayColor;
        overlayView.setBackgroundColor(overlayColor);
    }

    /**
     * Returns the content view.
     *
     * @return The content view, as an instance of the class {@link View}. The content view may not
     * be null
     */
    public final View getContentView() {
        return contentView;
    }

}