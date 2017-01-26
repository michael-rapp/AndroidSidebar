/*
 * Copyright 2014 - 2017 Michael Rapp
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