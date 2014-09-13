package de.mrapp.android.sidebar.animation;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import de.mrapp.android.sidebar.view.ContentView;

public class ContentViewAnimation extends AnimationSet {

	public ContentViewAnimation(final ContentView contentView,
			final long duration, final float toXDelta, final float scrollRatio,
			final float overlayTransparency, final boolean show) {
		super(true);
		setDuration(duration);
		Animation translateAnimation = new TranslateAnimation(0, toXDelta
				* scrollRatio, 0, 0);
		addAnimation(translateAnimation);
		Animation overlayAnimation = new ContentOverlayAnimation(contentView,
				overlayTransparency, show);
		addAnimation(overlayAnimation);
	}

}