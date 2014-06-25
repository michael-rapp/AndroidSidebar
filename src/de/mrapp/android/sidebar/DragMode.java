package de.mrapp.android.sidebar;

public enum DragMode {

	BOTH(0),

	SIDEBAR_ONLY(1),

	CONTENT_ONLY(2),

	DISABLED(3);

	private int value;

	private DragMode(final int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

	public static final DragMode fromValue(final int value) {
		for (DragMode dragMode : values()) {
			if (dragMode.value == value) {
				return dragMode;
			}
		}

		throw new IllegalArgumentException("Invalid enum value: " + value);
	}

}