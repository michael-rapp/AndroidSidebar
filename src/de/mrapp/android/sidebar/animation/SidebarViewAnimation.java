package de.mrapp.android.sidebar.animation;

import android.view.animation.TranslateAnimation;

public class SidebarViewAnimation extends TranslateAnimation {

	public SidebarViewAnimation(float toXDelta, long duration,
			AnimationListener listener) {
		super(0, toXDelta, 0, 0);
		setDuration(duration);
		setAnimationListener(listener);
	}

}