package de.mrapp.android.sidebar.animation;

import de.mrapp.android.sidebar.view.ContentView;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ContentOverlayAnimation extends Animation {

	private final ContentView contentView;

	private final float overlayTransparency;

	private float initialTransparency;

	private final boolean show;

	public ContentOverlayAnimation(final ContentView contentView,
			final float overlayTransparency, final boolean show) {
		this.contentView = contentView;
		this.overlayTransparency = overlayTransparency;
		this.show = show;
		this.initialTransparency = -1.0f;
	}

	@Override
	protected void applyTransformation(float interpolatedTime,
			Transformation transformation) {
		super.applyTransformation(interpolatedTime, transformation);

		if (initialTransparency == -1.0f) {
			initialTransparency = contentView.getOverlayTransparency();
		}

		if (show) {
			contentView.setOverlayTransparency(initialTransparency
					+ interpolatedTime
					* (overlayTransparency - initialTransparency));
		} else {
			contentView.setOverlayTransparency(initialTransparency
					- interpolatedTime * (initialTransparency - 0));
		}
	}

}