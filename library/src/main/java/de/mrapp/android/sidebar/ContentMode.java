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
 * Contains all possible content modes, which specify how a sidebar's content view is handled when
 * the sidebar becomes shown or hidden.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public enum ContentMode {

    /**
     * If the content view should be moved along with the sidebar.
     */
    SCROLL(0),

    /**
     * If the content view should be resized to release space for the sidebar.
     */
    RESIZE(1);

    /**
     * The value of the content mode.
     */
    private int value;

    /**
     * Creates a new content mode.
     *
     * @param value
     *         The value of the content mode
     */
    ContentMode(final int value) {
        this.value = value;
    }

    /**
     * Returns the value of the content mode.
     *
     * @return The value of the content mode
     */
    public final int getValue() {
        return value;
    }

    /**
     * Returns the content mode, which belongs to a specific value.
     *
     * @param value
     *         The value of the content mode, which should be returned
     * @return The content mode, which belongs to the given value
     */
    public static ContentMode fromValue(final int value) {
        for (ContentMode contentMode : values()) {
            if (contentMode.value == value) {
                return contentMode;
            }
        }

        throw new IllegalArgumentException();
    }

}