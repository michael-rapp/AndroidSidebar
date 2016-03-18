/*
 * Copyright 2014 - 2016 Michael Rapp
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
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.Inflater;
import de.mrapp.android.util.ElevationUtil;
import de.mrapp.android.util.ElevationUtil.Orientation;
import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureAtMaximum;
import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.ElevationUtil.createElevationShadow;

/**
 * A custom view, which contains the sidebar view of a sidebar, as well as a view, which is used to
 * visualize a shadow.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
@SuppressLint("ViewConstructor")
public class SidebarView extends LinearLayout {

    /**
     * The sidebar view.
     */
    private View sidebarView;

    /**
     * The view, which is used to emulate the sidebar's elevation.
     */
    private ImageView shadowView;

    /**
     * The location of the sidebar.
     */
    private Location location;

    /**
     * The elevation of the sidebar in dp.
     */
    private int sidebarElevation;

    /**
     * The width of the view, which is used to emulate the sidebar's elevation, in pixels.
     */
    private int shadowWidth;

    /**
     * The background of the sidebar view or null, if the default background is used.
     */
    private Drawable sidebarBackground;

    /**
     * Inflates and adds all child views, depending on the location of the sidebar.
     *
     * @param inflater
     *         The inflater, which should be used to inflate the sidebar view, as an instance of the
     *         type {@link Inflater}. The inflater may not be null
     */
    private void inflateViews(@NonNull final Inflater inflater) {
        if (location == Location.LEFT) {
            inflateSidebarView(inflater);
            inflateShadowView();
        } else {
            inflateShadowView();
            inflateSidebarView(inflater);
        }
    }

    /**
     * Inflates and adds the view, which is used to visualize a shadow.
     */
    private void inflateShadowView() {
        shadowView = new ImageView(getContext());
        shadowView.setScaleType(ImageView.ScaleType.FIT_XY);
        shadowView.setContentDescription(null);
        addShadowView();
    }

    /**
     * Adds the view, which is used to visualize a shadow.
     */
    private void addShadowView() {
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(shadowView, layoutParams);
    }

    /**
     * Inflates and adds the sidebar view.
     *
     * @param inflater
     *         The inflater, which should be used to inflate the sidebar view, as an instance of the
     *         type {@link Inflater}. The inflater may not be null
     */
    private void inflateSidebarView(@NonNull final Inflater inflater) {
        sidebarView = inflater.inflate(getContext(), null, false);
        setSidebarBackground(sidebarBackground);
        addSidebarView();
    }

    /**
     * Adds the sidebar view.
     */
    private void addSidebarView() {
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        addView(sidebarView, layoutParams);
    }

    /**
     * Creates a new custom view, which contains the sidebar view of a sidebar, as well as a view,
     * which is used to visualize a shadow.
     *
     * @param context
     *         The context, the view should belong to, as an instance of the class {@link Context}.
     *         The context may not be null
     * @param inflater
     *         The inflater, which should be used to inflate the sidebar view, as an instance of the
     *         type {@link Inflater}. The inflater may not be null
     * @param location
     *         The location of the sidebar as a value of the enum {@link Location}. The location may
     *         either be <code>LEFT</code> or <code>RIGHT</code>
     * @param sidebarBackground
     *         The background of the sidebar view as an instance of the class {@link Drawable} or
     *         null, if the default background should be used
     * @param sidebarElevation
     *         The elevation of the sidebar in dp as an {@link Integer}. The elevation must be at
     *         least 0 and at maximum 16
     */
    public SidebarView(@NonNull final Context context, @NonNull final Inflater inflater,
                       @NonNull final Location location, @Nullable final Drawable sidebarBackground,
                       final int sidebarElevation) {
        super(context, null);
        ensureNotNull(location, "The location may not be null");
        ensureNotNull(inflater, "The inflater may not be null");
        this.location = location;
        this.sidebarBackground = sidebarBackground;
        setOrientation(LinearLayout.HORIZONTAL);
        inflateViews(inflater);
        setSidebarElevation(sidebarElevation);
    }

    /**
     * Returns the elevation of the sidebar.
     *
     * @return The elevation of the sidebar in dp as an {@link Integer} value
     */
    public final int getSidebarElevation() {
        return sidebarElevation;
    }

    /**
     * Sets the elevation of the sidebar.
     *
     * @param elevation
     *         The elevation, which should be set, in dp as an {@link Integer} value. The elevation
     *         must be at least 0 and at maximum 16
     */
    public final void setSidebarElevation(final int elevation) {
        ensureAtLeast(elevation, 0, "The sidebar elevation must be at least 0");
        ensureAtMaximum(elevation, ElevationUtil.MAX_ELEVATION,
                "The sidebar elevation must be at maximum " + ElevationUtil.MAX_ELEVATION);
        this.sidebarElevation = elevation;
        Bitmap shadow = createElevationShadow(getContext(), elevation,
                getLocation() == Location.LEFT ? Orientation.RIGHT : Orientation.LEFT);
        shadowView.setImageBitmap(shadow);
        shadowWidth = shadow.getWidth();
        setLocation(getLocation());
    }

    /**
     * Returns the location of the sidebar.
     *
     * @return The location of the sidebar as a value of the enum {@link Location}. The location may
     * either be <code>LEFT</code> or <code>RIGHT</code>
     */
    public final Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the sidebar.
     *
     * @param location
     *         The location, which should be set, as a value of the enum {@link Location}. The
     *         location may either be <code>LEFT</code> or <code>RIGHT</code>
     */
    public final void setLocation(@NonNull final Location location) {
        ensureNotNull(location, "The location may not be null");
        this.location = location;
        removeAllViews();

        if (location == Location.LEFT) {
            addSidebarView();
            addShadowView();
        } else {
            addShadowView();
            addSidebarView();
        }

        setSidebarBackground(sidebarBackground);
    }

    /**
     * Returns the background of the sidebar view.
     *
     * @return The background of the sidebar view as an instance of the class {@link Drawable} or
     * null, if the default background is used
     */
    public final Drawable getSidebarBackground() {
        return sidebarBackground;
    }

    /**
     * Sets the background of the sidebar view.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if the default background should be used
     */
    public final void setSidebarBackground(@Nullable final Drawable background) {
        this.sidebarBackground = background;

        if (sidebarBackground == null) {
            if (location == Location.LEFT) {
                sidebarView.setBackgroundResource(R.drawable.sidebar_background_left_light);
            } else {
                sidebarView.setBackgroundResource(R.drawable.sidebar_background_right_light);
            }
        } else {
            ViewUtil.setBackground(sidebarView, sidebarBackground);
        }
    }

    /**
     * Returns the sidebar view.
     *
     * @return The sidebar view as an instance of the class {@link View}
     */
    public final View getSidebarView() {
        return sidebarView;
    }

    /**
     * Returns the width of the view, which is used to visualize the sidebar's elevation.
     *
     * @return The width of the view, which is used to visualize the sidebar's elevation, in pixels
     * as an {@link Integer} value
     */
    public final int getShadowWidth() {
        return shadowWidth;
    }

}