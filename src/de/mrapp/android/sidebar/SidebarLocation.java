package de.mrapp.android.sidebar;

/**
 * Contains all possible locations of a sidebar.
 * 
 * @author Michael Rapp
 * 
 * @since 1.0.0
 */
public enum SidebarLocation {

	/**
	 * If the sidebar is located at the left edge of the display.
	 */
	LEFT(0),

	/**
	 * If the sidebar is located at the right edge of the display.
	 */
	RIGHT(1);

	private int value;

	private SidebarLocation(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static SidebarLocation fromValue(final int value) {
		for (SidebarLocation location : values()) {
			if (location.value == value)
				return location;
		}

		throw new IllegalArgumentException("Invalid enum value: " + value);
	}

}