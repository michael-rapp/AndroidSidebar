/*
 * Copyright 2014 - 2017 Michael Rapp
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
package de.mrapp.android.sidebar.inflater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An inflater, which allows to inflate views, which are present as an instance of the class {@link
 * View}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class InstanceInflater implements Inflater {

    /**
     * The view, which should be inflated.
     */
    private final View view;

    /**
     * Creates a new inflater, which allows to inflate view, which are present as an instance of the
     * class {@link View}.
     *
     * @param view
     *         The view, which should be inflated, as an instance of the class {@link View}. The
     *         view may not be null
     */
    public InstanceInflater(@NonNull final View view) {
        ensureNotNull(view, "The view may not be null");
        this.view = view;
    }

    @Override
    public final View inflate(@NonNull final Context context, @Nullable final ViewGroup parent,
                              final boolean attachToRoot) {
        return view;
    }

}