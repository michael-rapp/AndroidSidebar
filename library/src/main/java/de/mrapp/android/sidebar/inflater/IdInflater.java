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