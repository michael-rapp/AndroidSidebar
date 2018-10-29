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

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * An activity, which allows to adapt the settings of the example app.
 *
 * @author Michael Rapp
 */
public class PreferenceActivity extends AppCompatActivity {

    /**
     * The fragment, which contains the preferences.
     */
    private Fragment preferenceFragment;

    /**
     * Shows the preference fragment, which contains the activity's content.
     */
    private void showPreferenceFragment() {
        if (preferenceFragment == null) {
            preferenceFragment = Fragment.instantiate(this, PreferenceFragment.class.getName());
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack("preferenceBackStack");
        transaction.replace(R.id.fragment, preferenceFragment);
        transaction.commit();
    }

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        if (savedInstanceState != null) {
            preferenceFragment =
                    getFragmentManager().getFragment(savedInstanceState, "preferenceFragment");
        }

        showPreferenceFragment();
    }

    @Override
    protected final void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        getFragmentManager().putFragment(outState, "preferenceFragment", preferenceFragment);
    }

}