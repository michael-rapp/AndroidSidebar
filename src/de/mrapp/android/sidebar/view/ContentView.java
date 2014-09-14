package de.mrapp.android.sidebar.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import de.mrapp.android.sidebar.inflater.Inflater;

public class ContentView extends FrameLayout {

	private View contentView;

	private View overlayView;

	private float overlayTransparency;

	private void inflateContentView(final Inflater inflater) {
		contentView = inflater.inflate(getContext(), null);
		addView(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
	}

	private void inflateOverlayView(final int overlayColor) {
		overlayView = new View(getContext());
		overlayView.setBackgroundColor(overlayColor);
		addView(overlayView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		setOverlayTransparency(0.0f);
	}

	public ContentView(Context context, Inflater inflater,
			final int overlayColor) {
		super(context, null);
		inflateContentView(inflater);
		inflateOverlayView(overlayColor);
	}

	public final void setOverlayTransparency(final float transparency) {
		this.overlayTransparency = transparency;
		AlphaAnimation animation = new AlphaAnimation(transparency,
				transparency);
		animation.setDuration(0);
		animation.setFillAfter(true);
		overlayView.startAnimation(animation);
	}

	public final float getOverlayTransparency() {
		return overlayTransparency;
	}

	public final View getContentView() {
		return contentView;
	}

	public final void setOverlayColor(final int overlayColor) {
		overlayView.setBackgroundColor(overlayColor);
	}

}