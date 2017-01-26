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

import android.support.annotation.NonNull;
import android.view.View;

/**
 * An utility class, which offers factory methods, which allow to initialize instances of the type
 * {@link Inflater}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public final class InflaterFactory {

    /**
     * Creates a new utility class, which offers factory methods, which allow to initialize
     * instances of the type {@link Inflater}.
     */
    private InflaterFactory() {

    }

    /**
     * Creates and returns an inflater, which allows to inflate view, which are present as an
     * instance of the class {@link View}.
     *
     * @param view
     *         The view, which should be inflated, as an instance of the class {@link View}. The
     *         view may not be null
     * @return The inflater, which has been created, as an instance of the type {@link Inflater}
     */
    public static Inflater createInflater(@NonNull final View view) {
        return new InstanceInflater(view);
    }

    /**
     * Creates and returns an inflater, which allows to inflate views, which may be referenced by a
     * specific resource id.
     *
     * @param viewId
     *         The resource id of the view, which should be inflated by the inflater, as an {@link
     *         Integer} value. The id must correspond to a valid view resource
     * @return The inflater, which has been created, as an instance of the type {@link Inflater}
     */
    public static Inflater createInflater(final int viewId) {
        return new IdInflater(viewId);
    }

}