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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * An inflater, which allows to inflate views, which may be referenced by a resource id.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class IdInflater implements Inflater {

    /**
     * The resource id of the view, which should be inflated.
     */
    private final int viewId;

    /**
     * Creates a new inflater, which allows to inflate views, which may be referenced by a resource
     * id.
     *
     * @param viewId
     *         The resource id of the view, which should be inflated, as an {@link Integer} value.
     *         The id must correspond to a valid view resource
     */
    public IdInflater(final int viewId) {
        this.viewId = viewId;
    }

    @Override
    public final View inflate(@NonNull final Context context, @Nullable final ViewGroup parent,
                              final boolean attachToRoot) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(viewId, parent, attachToRoot);
    }

}