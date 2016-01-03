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
package de.mrapp.android.sidebar;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the functionality of the class {@link DragMode}.
 *
 * @author Michael Rapp
 */
public class DragModeTest extends TestCase {

    /**
     * Tests the functionality of the getValue-method.
     */
    public final void testGetValue() {
        assertEquals(0, DragMode.BOTH.getValue());
        assertEquals(1, DragMode.SIDEBAR_ONLY.getValue());
        assertEquals(2, DragMode.CONTENT_ONLY.getValue());
        assertEquals(3, DragMode.DISABLED.getValue());
        assertEquals(4, DragMode.EDGE.getValue());
    }

    /**
     * Tests the functionality of the fromValue-method.
     */
    public final void testFromValue() {
        assertEquals(DragMode.BOTH, DragMode.fromValue(0));
        assertEquals(DragMode.SIDEBAR_ONLY, DragMode.fromValue(1));
        assertEquals(DragMode.CONTENT_ONLY, DragMode.fromValue(2));
        assertEquals(DragMode.DISABLED, DragMode.fromValue(3));
        assertEquals(DragMode.EDGE, DragMode.fromValue(4));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is throw by the fromValue-method, if the
     * given value is invalid.
     */
    public final void testFromValueThrowsException() {
        try {
            DragMode.fromValue(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

}