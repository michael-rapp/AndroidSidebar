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