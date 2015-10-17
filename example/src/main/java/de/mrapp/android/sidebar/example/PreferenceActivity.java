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
package de.mrapp.android.sidebar.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

import de.mrapp.android.sidebar.ContentMode;

/**
 * An activity, which allows to adapt the settings of the example app.
 *
 * @author Michael Rapp
 */
public class PreferenceActivity extends android.preference.PreferenceActivity {

    /**
     * The preference, which allows to set the scroll ratio.
     */
    private Preference scrollRatioPreference;

    /**
     * The preference, which allows to set the content mode.
     */
    private Preference contentModePreference;

    /**
     * Creates and returns a listener, which allows to enable or disable the preference, which
     * allows to set the scroll ratio, when the content mode has been changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnPreferenceChangeListener}
     */
    private OnPreferenceChangeListener createContentModeChangeListener() {
        return new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                int contentMode = Integer.valueOf((String) newValue);
                scrollRatioPreference.setEnabled(contentMode != ContentMode.RESIZE.getValue());
                return true;
            }

        };
    }

    @SuppressWarnings("deprecation")
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected final void onStart() {
        super.onStart();
        String scrollRatioKey = getString(R.string.scroll_ratio_preference_key);
        scrollRatioPreference = findPreference(scrollRatioKey);
        String contentModeKey = getString(R.string.content_mode_preference_key);
        String contentModeDefaultValue = getString(R.string.content_mode_preference_default_value);
        contentModePreference = findPreference(contentModeKey);
        contentModePreference.setOnPreferenceChangeListener(createContentModeChangeListener());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int contentMode = Integer.valueOf(
                sharedPreferences.getString(contentModeKey, contentModeDefaultValue));
        scrollRatioPreference.setEnabled(contentMode != ContentMode.RESIZE.getValue());
    }

}