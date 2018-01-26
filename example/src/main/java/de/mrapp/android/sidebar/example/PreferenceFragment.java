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
package de.mrapp.android.sidebar.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import de.mrapp.android.sidebar.ContentMode;

/**
 * A preference fragment, which contains the example app's settings.
 *
 * @author Michael Rapp
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

    /**
     * The preference, which allows to set the scroll ratio.
     */
    private Preference scrollRatioPreference;

    /**
     * Creates and returns a listener, which allows to enable or disable the preference, which
     * allows to set the scroll ratio, when the content mode has been changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * Preference.OnPreferenceChangeListener}
     */
    private Preference.OnPreferenceChangeListener createContentModeChangeListener() {
        return new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                int contentMode = Integer.valueOf((String) newValue);
                scrollRatioPreference.setEnabled(contentMode != ContentMode.RESIZE.getValue());
                return true;
            }

        };
    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        String scrollRatioKey = getString(R.string.scroll_ratio_preference_key);
        scrollRatioPreference = findPreference(scrollRatioKey);
        String contentModeKey = getString(R.string.content_mode_preference_key);
        String contentModeDefaultValue = getString(R.string.content_mode_preference_default_value);
        Preference contentModePreference = findPreference(contentModeKey);
        contentModePreference.setOnPreferenceChangeListener(createContentModeChangeListener());
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        int contentMode = Integer.valueOf(
                sharedPreferences.getString(contentModeKey, contentModeDefaultValue));
        scrollRatioPreference.setEnabled(contentMode != ContentMode.RESIZE.getValue());
    }

}