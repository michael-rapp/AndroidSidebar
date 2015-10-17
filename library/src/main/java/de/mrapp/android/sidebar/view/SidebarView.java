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
package de.mrapp.android.sidebar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.Inflater;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A custom view, which contains the sidebar view of a sidebar, as well as a view, which is used to
 * visualize a shadow.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class SidebarView extends LinearLayout {

    /**
     * The sidebar view.
     */
    private View sidebarView;

    /**
     * The view, which is used to visualize a shadow.
     */
    private View shadowView;

    /**
     * The location of the sidebar.
     */
    private Location location;

    /**
     * The color of the shadow.
     */
    private int shadowColor;

    /**
     * The width of the shadow in pixels.
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
        shadowView = new View(getContext());
        addShadowView();
    }

    /**
     * Adds the view, which is used to visualize a shadow.
     */
    private void addShadowView() {
        LayoutParams layoutParams = new LayoutParams(shadowWidth, LayoutParams.MATCH_PARENT);
        layoutParams.weight = 0;
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
     * @param shadowWidth
     *         The width of the shadow in pixels as an {@link Integer} value. The width must be at
     *         least 0
     * @param shadowColor
     *         The color of the shadow as an {@link Integer} value
     */
    public SidebarView(@NonNull final Context context, @NonNull final Inflater inflater,
                       @NonNull final Location location, @Nullable final Drawable sidebarBackground,
                       final int shadowWidth, @ColorInt final int shadowColor) {
        super(context, null);
        ensureNotNull(location, "The location may not be null");
        ensureNotNull(inflater, "The inflater may not be null");
        ensureAtLeast(shadowWidth, 0, "The shadow width must be at least 0");
        this.location = location;
        this.shadowWidth = shadowWidth;
        this.shadowColor = shadowColor;
        this.sidebarBackground = sidebarBackground;
        setOrientation(LinearLayout.HORIZONTAL);
        inflateViews(inflater);
        setShadowColor(shadowColor);
    }

    /**
     * Returns the color of the shadow.
     *
     * @return The color of the shadow as an {@link Integer} value
     */
    public final int getShadowColor() {
        return shadowColor;
    }

    /**
     * Sets the color of the shadow.
     *
     * @param shadowColor
     *         The color, which should be set, as an {@link Integer} value
     */
    @SuppressWarnings("deprecation")
    public final void setShadowColor(@ColorInt final int shadowColor) {
        this.shadowColor = shadowColor;
        Orientation orientation = Orientation.LEFT_RIGHT;

        if (location == Location.LEFT) {
            orientation = Orientation.RIGHT_LEFT;
        }

        GradientDrawable gradient =
                new GradientDrawable(orientation, new int[]{Color.TRANSPARENT, shadowColor});
        shadowView.setBackgroundDrawable(gradient);
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
        setShadowColor(shadowColor);
    }

    /**
     * Returns the width of the shadow in pixels.
     *
     * @return The width of the shadow in pixels as an {@link Integer} value
     */
    public final int getShadowWidth() {
        return shadowWidth;
    }

    /**
     * Sets the width of the shadow in pixels.
     *
     * @param shadowWidth
     *         The width, which should be set, as an {@link Integer} value. The width must be at
     *         least 0
     */
    public final void setShadowWidth(final int shadowWidth) {
        ensureAtLeast(shadowWidth, 0, "The shadow width must be at least 0");
        this.shadowWidth = shadowWidth;
        setLocation(location);
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
    @SuppressWarnings("deprecation")
    public final void setSidebarBackground(@Nullable final Drawable background) {
        this.sidebarBackground = background;

        if (sidebarBackground == null) {
            if (location == Location.LEFT) {
                sidebarView.setBackgroundResource(R.drawable.sidebar_background_left_light);
            } else {
                sidebarView.setBackgroundResource(R.drawable.sidebar_background_right_light);
            }
        } else {
            sidebarView.setBackgroundDrawable(sidebarBackground);
        }
    }

    /**
     * Returns the sidebar view.
     *
     * @return The sidebar view as an instance of the class {@link View}. The sidebar view may not
     * be null
     */
    public final View getSidebarView() {
        return sidebarView;
    }

}