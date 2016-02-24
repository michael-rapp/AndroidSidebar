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
package de.mrapp.android.sidebar.inflater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * Defines the interface, a class, which should allow to inflate views, must implement.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public interface Inflater {

    /**
     * Inflates and returns the view.
     *
     * @param context
     *         The context, which should be used, as an instance of the class {@link Context}. The
     *         context may not be null
     * @param parent
     *         The parent, that this view will eventually be attached to, as an instance of the
     *         class {@link ViewGroup} or null, if no parent is available
     * @param attachToRoot
     *         True, if the inflated view should also be attached to the parent, false otherwise
     * @return The view, which has been inflated, as an instance of the class {@link View} The view
     * may not be null
     */
    View inflate(@NonNull Context context, @Nullable ViewGroup parent, boolean attachToRoot);

}