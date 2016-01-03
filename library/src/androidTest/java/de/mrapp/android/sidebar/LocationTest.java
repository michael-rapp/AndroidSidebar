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
 * Tests the functionality of the class {@link Location}.
 *
 * @author Michael Rapp
 */
public class LocationTest extends TestCase {

    /**
     * Tests the functionality of the getValue-method.
     */
    public final void testGetValue() {
        assertEquals(0, Location.LEFT.getValue());
        assertEquals(1, Location.RIGHT.getValue());
    }

    /**
     * Tests the functionality of the fromValue-method.
     */
    public final void testFromValue() {
        assertEquals(Location.LEFT, Location.fromValue(0));
        assertEquals(Location.RIGHT, Location.fromValue(1));
    }

    /**
     * Ensures, that an {@link IllegalArgumentException} is throw by the fromValue-method, if the
     * given value is invalid.
     */
    public final void testFromValueThrowsException() {
        try {
            Location.fromValue(-1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            return;
        }
    }

}