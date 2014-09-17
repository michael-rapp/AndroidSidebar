package de.mrapp.android.sidebar.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * An utility class, which offers methods for calculations, depending on the
 * device's display metrics.
 * 
 * @author Michael Rapp
 */
public final class DisplayUtil {

	/**
	 * The ratio between pixel and dp measurement.
	 */
	private static final float PIXEL_DP_RATIO = 160f;

	/**
	 * Creates a new utility class, which offers methods for calculations,
	 * depending on the device's display metrics.
	 */
	private DisplayUtil() {

	}

	/**
	 * Converts a pixel value to a dp value.
	 * 
	 * @param context
	 *            The context, which should be used to retrieve the device's
	 *            display metrics
	 * @param pixels
	 *            The pixel value, which should be converted
	 * @return The calculated dp value
	 */
	public static int convertPixelsToDp(final Context context, final int pixels) {
		return Math.round(convertPixelsToDp(context, (float) pixels));
	}

	public static float convertPixelsToDp(final Context context,
			final float pixels) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return pixels / (metrics.densityDpi / PIXEL_DP_RATIO);
	}

	/**
	 * Converts a dp value to a pixel value.
	 * 
	 * @param context
	 *            The context, which should be used to retrieve the device's
	 *            display metrics
	 * @param dp
	 *            The dp value, which should be converted
	 * @return The calculated pixel value
	 */
	public static int convertDpToPixels(final Context context, final int dp) {
		return Math.round(convertDpToPixels(context, (float) dp));
	}

	public static float convertDpToPixels(final Context context, final float dp) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return dp * (metrics.densityDpi / PIXEL_DP_RATIO);
	}

}