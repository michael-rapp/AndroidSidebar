package de.mrapp.android.sidebar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.LinearLayout;
import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.SidebarLocation;
import de.mrapp.android.sidebar.inflater.Inflater;

public class SidebarView extends LinearLayout {

	private View sidebarView;

	private View shadowView;

	private SidebarLocation location;

	private int shadowWidth;

	private void initialize(Inflater inflater, final int background,
			final int shadowColor) {
		setOrientation(LinearLayout.HORIZONTAL);

		if (location == SidebarLocation.LEFT) {
			inflateSidebarView(inflater);
			inflateShadowView(shadowColor);
		} else {
			inflateShadowView(shadowColor);
			inflateSidebarView(inflater);
		}

		setBackground(background);
		drawShadow(shadowColor);
	}

	private void inflateShadowView(final int shadowColor) {
		shadowView = new View(getContext());
		shadowView.setBackgroundColor(shadowColor);
		LayoutParams layoutParams = new LayoutParams(shadowWidth,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 0;
		addView(shadowView, layoutParams);
	}

	private void inflateSidebarView(Inflater inflater) {
		sidebarView = inflater.inflate(getContext(), null);
		LayoutParams layoutParams = new LayoutParams(0,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 1;
		addView(sidebarView, layoutParams);
	}

	private void setBackground(final int background) {
		if (sidebarView != null && background == -1) {
			if (location == SidebarLocation.LEFT) {
				sidebarView
						.setBackgroundResource(R.drawable.sidebar_left_light);

			} else {
				sidebarView
						.setBackgroundResource(R.drawable.sidebar_right_light);

			}
		}
	}

	@SuppressWarnings("deprecation")
	private void drawShadow(final int shadowColor) {
		Orientation orientation = Orientation.LEFT_RIGHT;

		if (location == SidebarLocation.LEFT) {
			orientation = Orientation.RIGHT_LEFT;
		}

		// GradientDrawable gradient = new GradientDrawable(orientation,
		// new int[] { Color.TRANSPARENT, shadowColor });

		GradientDrawable gradient = new GradientDrawable(orientation,
				new int[] { Color.RED, Color.RED });
		shadowView.setBackgroundDrawable(gradient);
	}

	public SidebarView(Context context, Inflater inflater,
			final SidebarLocation location, final int background,
			final int shadowWidth, final int shadowColor) {
		super(context, null);
		this.location = location;
		this.shadowWidth = shadowWidth;
		initialize(inflater, background, shadowColor);
	}

	public View getSidebarView() {
		return sidebarView;
	}

}