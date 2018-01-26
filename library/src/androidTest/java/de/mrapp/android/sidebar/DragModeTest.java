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