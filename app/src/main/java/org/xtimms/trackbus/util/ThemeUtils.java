package org.xtimms.trackbus.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Px;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;

import org.xtimms.trackbus.R;

/**
 * Created by koitharu on 24.12.17.
 */

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
			R.style.AppTheme_Black,
			R.style.AppThemeDark,
			R.style.AMOLED
	};

	public static ColorStateList getAttrColorStateList(Context context, @AttrRes int resId) {
		TypedArray a = context.getTheme().obtainStyledAttributes(new int[] {resId});
		int attributeResourceId = a.getResourceId(0, 0);
		return ContextCompat.getColorStateList(context, attributeResourceId);
	}

	public static Drawable[] getThemedIcons(Context context, @DrawableRes int... resIds) {
		boolean dark = isAppThemeDark(context);
		PorterDuffColorFilter cf = dark ?
				new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.white_overlay_85), PorterDuff.Mode.SRC_ATOP)
				: null;
		Drawable[] ds = new Drawable[resIds.length];
		for (int i=0;i<resIds.length;i++) {
			ds[i] = ContextCompat.getDrawable(context, resIds[i]);
			if (ds[i] != null && dark) {
				ds[i].setColorFilter(cf);
			}
		}
		return ds;
	}

	public static Drawable[] getAccentIcons(Context context, @DrawableRes int... resIds) {
		PorterDuffColorFilter cf = new PorterDuffColorFilter(getAccentColor(context), PorterDuff.Mode.SRC_ATOP);
		Drawable[] ds = new Drawable[resIds.length];
		for (int i=0;i<resIds.length;i++) {
			ds[i] = ContextCompat.getDrawable(context, resIds[i]);
			if (ds[i] != null) {
				ds[i].setColorFilter(cf);
			}
		}
		return ds;
	}

	@ColorInt
	public static int getAttrColor(Context context, @AttrRes int resId) {
		TypedValue typedValue = new TypedValue();
		TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { resId });
		int color = a.getColor(0, 0);
		a.recycle();
		return color;
	}

	@ColorInt
	public static int getThemeAttrColor(Context context, @AttrRes int resId) {
		TypedArray a = context.getTheme().obtainStyledAttributes(getAppThemeRes(context), new int[] { resId });
		int color = a.getColor(0, 0);
		a.recycle();
		return color;
	}

	public static Drawable getAttrDrawable(Context context, @AttrRes int resId) {
		TypedValue typedValue = new TypedValue();
		TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { resId });
		Drawable drawable = a.getDrawable(0);
		a.recycle();
		return drawable;
	}

	public static int getAppThemeRes(Context context) {
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

	public static Drawable getSelectableBackground(Context context) {
		return getAttrDrawable(context, R.attr.selectableItemBackground);
	}

	public static Drawable getSelectableBackgroundBorderless(Context context) {
		return getAttrDrawable(context, R.attr.selectableItemBackgroundBorderless);
	}

	@ColorInt
	public static int getAccentColor(Context context) {
		return getThemeAttrColor(context, R.attr.colorAccent);
	}

	public static Drawable getColoredDrawable(@NonNull Context context, @DrawableRes int resId, @AttrRes int colorAttrId) {
		final Drawable drawable = ContextCompat.getDrawable(context, resId);
		if (drawable != null) {
			drawable.setColorFilter(
					getThemeAttrColor(context, colorAttrId),
					PorterDuff.Mode.SRC_IN
			);
		}
		return drawable;
	}

	@StyleRes
	public static int getBottomSheetTheme(Context context) {
		return isAppThemeDark(context) ? R.style.AppDialogDark : R.style.AppDialogLight;
	}

	@Px
	public static int getAttrSizePx(Context context, @AttrRes int resId) {
		TypedValue typedValue = new TypedValue();
		TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { resId });
		int size = a.getDimensionPixelSize(0, 0);
		a.recycle();
		return size;
	}
}
