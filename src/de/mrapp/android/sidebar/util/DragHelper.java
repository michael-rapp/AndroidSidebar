package de.mrapp.android.sidebar.util;

public class DragHelper {

	private final int threshold;

	private int distance;

	private int thresholdReachedPosition;

	private int dragStartPosition;

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
		thresholdReachedPosition = 0;
		dragStartPosition = 0;
		reachedThreshold = false;
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
			thresholdReachedPosition = roundedValue;
			dragStartPosition = roundedValue;
			distance = 0;
			reachedThreshold = false;
		} else {
			if (!reachedThreshold) {
				int tmpDistance = roundedValue - thresholdReachedPosition;

				if (reachedThreshold(tmpDistance)) {
					reachedThreshold = true;
					thresholdReachedPosition = roundedValue;
					dragging = true;
				}
			} else {
				distance = roundedValue - thresholdReachedPosition;
			}
		}
	}

	private boolean reachedThreshold;

	private boolean reachedThreshold(final int distance) {
		return Math.abs(distance) >= threshold;
	}

	public final int getDistance() {
		return distance;
	}

	public final int getStartPosition() {
		return dragStartPosition;
	}

}