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
 * Contains all possible drag modes, which can be applied on a sidebar to specify, on which regions
 * dragging should be recognized.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public enum DragMode {

    /**
     * If dragging should be recognized on the sidebar, as well as on the content.
     */
    BOTH(0),

    /**
     * If dragging should only be recognized on the sidebar.
     */
    SIDEBAR_ONLY(1),

    /**
     * If dragging should only be recognized on the content.
     */
    CONTENT_ONLY(2),

    /**
     * If dragging should be not recognized at all.
     */
    DISABLED(3),

    /**
     * If dragging should only be recognized when started at the edge of the sidebar's parent view.
     */
    EDGE(4);

    /**
     * The value of the drag mode.
     */
    private int value;

    /**
     * Creates a new drag mode.
     *
     * @param value
     *         The value of the drag mode
     */
    DragMode(final int value) {
        this.value = value;
    }

    /**
     * Returns the value of the drag mode.
     *
     * @return The value of the drag mode
     */
    public final int getValue() {
        return value;
    }

    /**
     * Returns the drag mode, which belongs to a specific value.
     *
     * @param value
     *         The value of the drag mode, which should be returned
     * @return The drag mode, which belongs to the given value
     */
    public static DragMode fromValue(final int value) {
        for (DragMode dragMode : values()) {
            if (dragMode.value == value) {
                return dragMode;
            }
        }

        throw new IllegalArgumentException();
    }

}