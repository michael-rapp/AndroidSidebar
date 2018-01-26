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