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
package de.mrapp.android.sidebar.util;

/**
 * A helper class, which may be used to recognize drag gestures.
 * 
 * @author Michael Rapp
 *
 * @since 1.0.0
 */
public class DragHelper {

	/**
	 * The distance in pixels, the gesture must last until it is recognized.
	 */
	private final int threshold;

	/**
	 * The distance, which has been passed while dragging, in pixels.
	 */
	private int distance;

	/**
	 * The position, where the threshold was reached or -1, if the threshold was
	 * not reached yet.
	 */
	private int thresholdReachedPosition;

	/**
	 * The position, where the gesture has been started at or -1, if no gesture
	 * has been started yet.
	 */
	private int dragStartPosition;

	/**
	 * The time, when the gesture has been started or -1, if no gesture has been
	 * started yet.
	 */
	private long dragStartTime;

	/**
	 * True, if the method <code>reset():void</code> has been called, false
	 * otherwise.
	 */
	private boolean resetted;

	/**
	 * True, if a gesture is currently recognized, false otherwise.
	 */
	private boolean dragging;

	/**
	 * True, if the threshold has already been reached, false otherwise.
	 */
	private boolean reachedThreshold;

	/**
	 * Returns, whether the threshold is reached by a specific distance.
	 * 
	 * @param distance
	 *            The distance, which should be checked, as an {@link Integer}
	 *            value
	 * @return True, if the threshold is reached by the given distance, false
	 *         otherwise
	 */
	private boolean reachedThreshold(final int distance) {
		return Math.abs(distance) >= threshold;
	}

	/**
	 * Creates a new helper class, which may be used to recognize drag gestures,
	 * which uses a specific threshold.
	 * 
	 * @param threshold
	 *            The distance in pixels, the gesture must last until it is
	 *            recognized, as an {@link Integer} value. The value must be at
	 *            least 0
	 */
	public DragHelper(final int threshold) {
		this.threshold = threshold;
		reset();
		stopDragging();
	}

	public void reset() {
		resetted = true;
		distance = 0;
		thresholdReachedPosition = -1;
		dragStartPosition = -1;
		dragStartTime = -1;
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

		if (resetted) {
			resetted = false;
			thresholdReachedPosition = roundedValue;
			dragStartPosition = roundedValue;
			dragStartTime = System.currentTimeMillis();
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

	public final int getDistance() {
		return distance;
	}

	public final int getStartPosition() {
		return dragStartPosition;
	}

	public final float getDragSpeed() {
		long interval = System.currentTimeMillis() - dragStartTime;
		return (float) Math.abs(getDistance()) / (float) interval;
	}

}