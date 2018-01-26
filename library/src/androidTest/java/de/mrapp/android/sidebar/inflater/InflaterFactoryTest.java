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
package de.mrapp.android.sidebar.inflater;

import android.content.Context;
import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.mrapp.android.sidebar.R;

/**
 * Tests the functionality of the class {@link InflaterFactory}.
 *
 * @author Michael Rapp
 */
public class InflaterFactoryTest extends InstrumentationTestCase {

    /**
     * Tests the functionality of the createInflater-method, if it is used to create an inflater,
     * which allows to inflate view, which are present as an instance of the class {@link View}.
     */
    public final void testCreateViewInflaterByViewInstance() {
        Context context = getInstrumentation().getContext();
        View view = new View(context);
        ViewGroup viewGroup = new LinearLayout(context);
        Inflater inflater = InflaterFactory.createInflater(view);
        View inflatedView = inflater.inflate(context, viewGroup, false);
        assertEquals(view, inflatedView);
    }

    /**
     * Tests the functionality of the createInflater-method, if it is used to create an inflater,
     * which allows to inflate views, which may be referenced by a resource id.
     */
    public final void testCreateViewInflaterByViewId() {
        int viewId = R.layout.view;
        ViewGroup viewGroup = new LinearLayout(getInstrumentation().getContext());
        Inflater inflater = InflaterFactory.createInflater(viewId);
        View inflatedView = inflater.inflate(getInstrumentation().getContext(), viewGroup, false);
        assertNotNull(inflatedView);
    }

}