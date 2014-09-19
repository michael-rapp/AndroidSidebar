/*
 * AndroidSidebar Copyright 2014 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>. 
 */
package de.mrapp.android.sidebar;

/**
 * Contains all possible content modes, which specify how a sidebar's content
 * view is handled when the sidebar becomes shown or hidden.
 * 
 * @author Michael Rapp
 * 
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
	 *            The value of the content mode
	 */
	private ContentMode(final int value) {
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
	 *            The value of the content mode, which should be returned
	 * @return The content mode, which belongs to the given value
	 */
	public static final ContentMode fromValue(final int value) {
		for (ContentMode contentMode : values()) {
			if (contentMode.value == value) {
				return contentMode;
			}
		}

		throw new IllegalArgumentException();
	}

}