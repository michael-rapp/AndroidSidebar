/*
 * AndroidSidebar Copyright 2014 - 2015 Michael Rapp
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