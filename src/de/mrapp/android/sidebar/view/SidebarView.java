package de.mrapp.android.sidebar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.View;
import android.widget.LinearLayout;
import de.mrapp.android.sidebar.Location;
import de.mrapp.android.sidebar.R;
import de.mrapp.android.sidebar.inflater.Inflater;

public class SidebarView extends LinearLayout {

	private View sidebarView;

	private View shadowView;

	private Location location;

	private int shadowColor;

	private int shadowWidth;

	private Drawable sidebarBackground;

	private void inflateViews(Inflater inflater) {
		if (location == Location.LEFT) {
			inflateSidebarView(inflater);
			inflateShadowView(shadowColor, shadowWidth);
		} else {
			inflateShadowView(shadowColor, shadowWidth);
			inflateSidebarView(inflater);
		}
	}

	private void inflateShadowView(final int shadowColor, final int shadowWidth) {
		shadowView = new View(getContext());
		addShadowView(shadowWidth);
	}

	private void addShadowView(final int shadowWidth) {
		LayoutParams layoutParams = new LayoutParams(shadowWidth,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 0;
		addView(shadowView, layoutParams);
	}

	private void inflateSidebarView(Inflater inflater) {
		sidebarView = inflater.inflate(getContext(), null);
		setBackgroundDrawable(sidebarBackground);
		addSidebarView();
	}

	private void addSidebarView() {
		LayoutParams layoutParams = new LayoutParams(0,
				LayoutParams.MATCH_PARENT);
		layoutParams.weight = 1;
		addView(sidebarView, layoutParams);
	}

	public SidebarView(Context context, Inflater inflater,
			final Location location, final Drawable sidebarBackground,
			final int shadowWidth, final int shadowColor) {
		super(context, null);
		this.location = location;
		this.shadowWidth = shadowWidth;
		this.shadowColor = shadowColor;
		this.sidebarBackground = sidebarBackground;
		setOrientation(LinearLayout.HORIZONTAL);
		inflateViews(inflater);
		setShadowColor(shadowColor);
	}

	@SuppressWarnings("deprecation")
	public final void setShadowColor(final int shadowColor) {
		this.shadowColor = shadowColor;
		Orientation orientation = Orientation.LEFT_RIGHT;

		if (location == Location.LEFT) {
			orientation = Orientation.RIGHT_LEFT;
		}

		GradientDrawable gradient = new GradientDrawable(orientation,
				new int[] { Color.TRANSPARENT, shadowColor });
		shadowView.setBackgroundDrawable(gradient);
	}

	public final void setLocation(final Location location) {
		this.location = location;
		removeAllViews();

		if (location == Location.LEFT) {
			addSidebarView();
			addShadowView(shadowWidth);
		} else {
			addShadowView(shadowWidth);
			addSidebarView();
		}

		setBackgroundDrawable(sidebarBackground);
		setShadowColor(shadowColor);
	}

	public final void setShadowWidth(final int shadowWidth) {
		this.shadowWidth = shadowWidth;
		setLocation(location);
	}

	public View getSidebarView() {
		return sidebarView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public final void setBackgroundDrawable(final Drawable background) {
		this.sidebarBackground = background;

		if (sidebarBackground == null) {
			if (location == Location.LEFT) {
				sidebarView
						.setBackgroundResource(R.drawable.sidebar_left_light);
			} else {
				sidebarView
						.setBackgroundResource(R.drawable.sidebar_right_light);

			}
		} else {
			sidebarView.setBackgroundDrawable(sidebarBackground);
		}
	}

}