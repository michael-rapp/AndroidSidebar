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

import android.test.AndroidTestCase;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import junit.framework.Assert;

/**
 * Tests the functionality of the class {@link InstanceInflater}.
 *
 * @author Michael Rapp
 */
public class InstanceInflaterTest extends AndroidTestCase {

    /**
     * Tests the functionality of the inflate-method.
     */
    public final void testInflateView() {
        View view = new View(getContext());
        ViewGroup viewGroup = new LinearLayout(getContext());
        InstanceInflater inflater = new InstanceInflater(view);
        View inflatedView = inflater.inflate(getContext(), viewGroup, false);
        assertEquals(view, inflatedView);
    }

    /**
     * Ensures, that a {@link NullPointerException} is thrown, if the view is set to null.
     */
    public final void testSetViewToNullThrowsException() {
        try {
            new InstanceInflater(null);
            Assert.fail();
        } catch (NullPointerException e) {
            return;
        }
    }

}