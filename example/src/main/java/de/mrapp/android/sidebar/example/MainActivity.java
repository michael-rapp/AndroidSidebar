/*
 * AndroidSidebar Copyright 2014 - 2016 Michael Rapp
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
package de.mrapp.android.sidebar.example;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import de.mrapp.android.sidebar.ContentMode;
import de.mrapp.android.sidebar.DragMode;
import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.Sidebar;
import de.mrapp.android.sidebar.SidebarListener;

/**
 * The example app's main activity.
 *
 * @author Michael Rapp
 */
public class MainActivity extends Activity implements SidebarListener {

    /**
     * The sidebar.
     */
    private Sidebar sidebar;

    /**
     * Initializes the sidebar.
     */
    private void initializeSidebar() {
        sidebar = (Sidebar) findViewById(R.id.sidebar);
        sidebar.addSidebarListener(this);
    }

    /**
     * Initializes the location of the sidebar by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeSidebarLocation(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.location_preference_key);
        String defaultValue = getString(R.string.location_preference_default_value);
        int location = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setLocation(Location.fromValue(location));
    }

    /**
     * Initializes the width of the sidebar by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeSidebarWidth(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.sidebar_width_preference_key);
        String defaultValue = getString(R.string.sidebar_width_default_value);
        float sidebarWidth = Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setSidebarWidth(sidebarWidth);
    }

    /**
     * Initializes the maximum width of the sidebar by retrieving the appropriate setting from the
     * shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeMaxSidebarWidth(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.max_sidebar_width_preference_key);
        String defaultValue = getString(R.string.max_sidebar_width_preference_default_value);
        int maxSidebarWidth = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setMaxSidebarWidth(maxSidebarWidth);
    }

    /**
     * Initializes the offset of the sidebar by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeSidebarOffset(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.sidebar_offset_preference_key);
        String defaultValue = getString(R.string.sidebar_offset_preference_default_value);
        float sidebarOffset = Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setSidebarOffset(sidebarOffset);
    }

    /**
     * Initializes the maximum offset of the sidebar by retrieving the appropriate setting from the
     * shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeMaxSidebarOffset(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.max_sidebar_offset_preference_key);
        String defaultValue = getString(R.string.max_sidebar_offset_preference_default_value);
        int maxSidebarOffset = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setMaxSidebarOffset(maxSidebarOffset);
    }

    /**
     * Initializes, whether the sidebar should be shown by default, by retrieving the appropriate
     * setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeShowByDefault(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.show_preference_key);
        String defaultValue = getString(R.string.show_preference_default_value);
        boolean show = sharedPreferences.getBoolean(key, Boolean.valueOf(defaultValue));

        if (show) {
            sidebar.showSidebar();
        }
    }

    /**
     * Initializes the content mode by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeContentMode(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.content_mode_preference_key);
        String defaultValue = getString(R.string.content_mode_preference_default_value);
        int contentMode = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setContentMode(ContentMode.fromValue(contentMode));
    }

    /**
     * Initializes the scroll ratio by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeScrollRatio(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.scroll_ratio_preference_key);
        String defaultValue = getString(R.string.scroll_ratio_preference_default_value);
        float scrollRatio = Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setScrollRatio(scrollRatio);
    }

    /**
     * Initializes, whether the sidebar should be hidden when the back button is clicked, by
     * retrieving the appropriate setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeHideOnBackButton(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.hide_on_back_button_preference_key);
        String defaultValue = getString(R.string.hide_on_back_button_default_value);
        boolean hideOnBackButton = sharedPreferences.getBoolean(key, Boolean.valueOf(defaultValue));
        sidebar.hideOnBackButton(hideOnBackButton);
    }

    /**
     * Initializes, whether the sidebar should be hidden when the content is clicked, by retrieving
     * the appropriate setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeHideOnContentClick(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.hide_on_content_click_preference_key);
        String defaultValue = getString(R.string.hide_on_content_click_preference_default_value);
        boolean hideOnContentClick =
                sharedPreferences.getBoolean(key, Boolean.valueOf(defaultValue));
        sidebar.hideOnContentClick(hideOnContentClick);
    }

    /**
     * Initializes, whether the sidebar should be shown when it is clicked, by retrieving the
     * appropriate setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeShowOnSidebarClick(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.show_on_sidebar_click_preference_key);
        String defaultValue = getString(R.string.show_on_sidebar_click_preference_default_value);
        boolean showOnSidebarClick =
                sharedPreferences.getBoolean(key, Boolean.valueOf(defaultValue));
        sidebar.showOnSidebarClick(showOnSidebarClick);
    }

    /**
     * Initializes the animation speed by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeAnimationSpeed(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.animation_speed_preference_key);
        String defaultValue = getString(R.string.animation_speed_preference_default_value);
        float animationSpeed = Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setAnimationSpeed(animationSpeed);
    }

    /**
     * Initializes the drag threshold by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeDragThreshold(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.drag_threshold_preference_key);
        String defaultValue = getString(R.string.drag_threshold_preference_default_value);
        float dragThreshold = Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setDragThreshold(dragThreshold);
    }

    /**
     * Initializes the drag mode, which is used when the sidebar is hidden, by retrieving the
     * appropriate setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeDragModeWhenHidden(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.drag_mode_when_hidden_preference_key);
        String defaultValue = getString(R.string.drag_mode_when_hidden_preference_default_value);
        int dragMode = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setDragModeWhenHidden(DragMode.fromValue(dragMode));
    }

    /**
     * Initializes the drag mode, which is used when the sidebar is shown, by retrieving the
     * appropriate setting from the shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeDragModeWhenShown(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.drag_mode_when_shown_preference_key);
        String defaultValue = getString(R.string.drag_mode_when_shown_preference_default_value);
        int dragMode = Integer.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setDragModeWhenShown(DragMode.fromValue(dragMode));
    }

    /**
     * Initializes the sidebar elevation by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeSidebarElevation(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.sidebar_elevation_preference_key);
        String defaultValue = getString(R.string.sidebar_elevation_preference_default_value);
        String sidebarElevation = sharedPreferences.getString(key, defaultValue);
        sidebar.setSidebarElevation(Integer.valueOf(sidebarElevation));
    }

    /**
     * Initializes the content overlay color by retrieving the appropriate setting from the shared
     * preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeContentOverlayColor(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.content_overlay_color_preference_key);
        String defaultValue = getString(R.string.content_overlay_color_preference_default_value);
        String contentOverlayColor = sharedPreferences.getString(key, defaultValue);
        sidebar.setContentOverlayColor(Color.parseColor(contentOverlayColor));
    }

    /**
     * Initializes the content overlay transparency by retrieving the appropriate setting from the
     * shared preferences.
     *
     * @param sharedPreferences
     *         The shared preferences, the settings should be retrieved from, as an instance of the
     *         type {@link SharedPreferences}
     */
    private void initializeContentOverlayTransparency(final SharedPreferences sharedPreferences) {
        String key = getString(R.string.content_overlay_transparency_key);
        String defaultValue = getString(R.string.content_overlay_transparency_default_value);
        float contentOverlayTransparency =
                Float.valueOf(sharedPreferences.getString(key, defaultValue));
        sidebar.setContentOverlayTransparency(contentOverlayTransparency);
    }

    /**
     * Initializes the button, which allows to show the sidebar.
     */
    private void initializeShowSidebarButton() {
        Button showSidebarButton = (Button) findViewById(R.id.show_sidebar_button);
        showSidebarButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                sidebar.showSidebar();
            }

        });
    }

    /**
     * Initializes the button, which allows to hide the sidebar.
     */
    private void initializeHideSidebarButton() {
        Button hideSidebarButton = (Button) findViewById(R.id.hide_sidebar_button);
        hideSidebarButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                sidebar.hideSidebar();
            }

        });
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeSidebar();
        initializeShowSidebarButton();
        initializeHideSidebarButton();

        if (savedInstanceState == null) {
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(this);
            initializeSidebarLocation(sharedPreferences);
            initializeSidebarWidth(sharedPreferences);
            initializeMaxSidebarWidth(sharedPreferences);
            initializeSidebarOffset(sharedPreferences);
            initializeMaxSidebarOffset(sharedPreferences);
            initializeShowByDefault(sharedPreferences);
            initializeContentMode(sharedPreferences);
            initializeScrollRatio(sharedPreferences);
            initializeHideOnBackButton(sharedPreferences);
            initializeHideOnContentClick(sharedPreferences);
            initializeShowOnSidebarClick(sharedPreferences);
            initializeAnimationSpeed(sharedPreferences);
            initializeDragThreshold(sharedPreferences);
            initializeDragModeWhenHidden(sharedPreferences);
            initializeDragModeWhenShown(sharedPreferences);
            initializeSidebarElevation(sharedPreferences);
            initializeContentOverlayColor(sharedPreferences);
            initializeContentOverlayTransparency(sharedPreferences);
        }
    }

    @Override
    public final void onResume() {
        super.onResume();

        if (sidebar.isSidebarShown()) {
            ActionBar actionBar = getActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            setTitle(R.string.sidebar_title);
        }
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public final void onSidebarShown(final Sidebar sidebar) {
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(R.string.sidebar_title);
    }

    @Override
    public final void onSidebarHidden(final Sidebar sidebar) {
        ActionBar actionBar = getActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        setTitle(R.string.app_name);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (sidebar.isSidebarShown()) {
                    sidebar.hideSidebar();
                    return true;
                }

                break;
            case R.id.settings:
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}