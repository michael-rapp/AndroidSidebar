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