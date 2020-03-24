package org.xtimms.ridebus.util;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;

import org.xtimms.ridebus.R;

public abstract class ThemeUtils {

	private static final int[] APP_THEMES = new int[]{
			R.style.AppTheme_Default,
			R.style.AppTheme_Red,
			R.style.AppTheme_Orange,
			R.style.AppTheme_LightGreen,
			R.style.AppTheme_Green,
			R.style.AppTheme_Teal,
			R.style.AppTheme_Cyan,
			R.style.AppTheme_LightBlue,
			R.style.AppTheme_Blue,
			R.style.AppTheme_Indigo,
			R.style.AppTheme_DeepPurple,
			R.style.AppTheme_Purple,
			R.style.AppTheme_Pink,
			R.style.AppTheme_Black
	};

	@ColorInt
	public static int getThemeAttrColor(Context context, @AttrRes int resId) {
		TypedArray a = context.getTheme().obtainStyledAttributes(getAppThemeRes(context), new int[] { resId });
		int color = a.getColor(0, 0);
		a.recycle();
		return color;
	}

	private static int getAppThemeRes(Context context) {
		return APP_THEMES[getAppTheme(context)];
	}

	@StyleRes
	public static int getAppThemeRes(int index) {
		return APP_THEMES[index];
	}

	public static int getAppTheme(Context context) {
		return AppSettings.get(context).getAppTheme();
	}

	public static boolean isAppThemeDark(Context context) {
		return getAppTheme(context) > 13;
	}

	public static boolean isAppThemeNotDark(Context context) {
		return getAppTheme(context) < 13;
	}

}