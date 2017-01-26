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

import android.test.InstrumentationTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import de.mrapp.android.sidebar.R;

/**
 * Tests the functionality of the class {@link IdInflater}.
 *
 * @author Michael Rapp
 */
public class IdInflaterTest extends InstrumentationTestCase {

    /**
     * Tests the functionality of the inflate-method.
     */
    public final void testInflate() {
        int viewId = R.layout.view;
        ViewGroup viewGroup = new LinearLayout(getInstrumentation().getContext());
        IdInflater inflater = new IdInflater(viewId);
        View inflatedView = inflater.inflate(getInstrumentation().getContext(), viewGroup, false);
        assertNotNull(inflatedView);
    }

}