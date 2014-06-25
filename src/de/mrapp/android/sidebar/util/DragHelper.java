package de.mrapp.android.sidebar.util;

public class DragHelper {

	private final int threshold;

	private int distance;

	private int startValue;

	private boolean recycled;

	private boolean dragging;

	public DragHelper(final int threshold) {
		this.threshold = threshold;
		reset();
		stopDragging();
	}

	public void reset() {
		recycled = true;
		distance = 0;
		startValue = 0;
	}
	
	public void stopDragging() {
		dragging = false;
	}

	public final boolean isDragging() {
		return dragging;
	}

	public final void update(float value) {
		int roundedValue = Math.round(value);

		if (recycled) {
			recycled = false;
			startValue = roundedValue;
			distance = 0;
		} else {
			distance = roundedValue - startValue;
			
			if (reachedThreshold()) {
				dragging = true;
			}
		}
	}

	private boolean reachedThreshold() {
		return Math.abs(distance) >= threshold;
	}

	public final int getDistance() {
		return distance;
	}

}