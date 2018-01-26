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

/**
 * Contains all possible locations of a sidebar.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public enum Location {

    /**
     * If the sidebar is located at the left edge of the display.
     */
    LEFT(0),

    /**
     * If the sidebar is located at the right edge of the display.
     */
    RIGHT(1);

    /**
     * The value of the location.
     */
    private int value;

    /**
     * Creates a new location.
     *
     * @param value
     *         The value of the location
     */
    Location(final int value) {
        this.value = value;
    }

    /**
     * Returns the value of the location.
     *
     * @return The value of the location
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the location, which belongs to a specific value.
     *
     * @param value
     *         The value of the location, which should be returned
     * @return The location, which belongs to the given value
     */
    public static Location fromValue(final int value) {
        for (Location location : values()) {
            if (location.value == value) {
                return location;
            }
        }

        throw new IllegalArgumentException();
    }

}