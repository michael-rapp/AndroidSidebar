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