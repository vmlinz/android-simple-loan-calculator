package ee.smkv.calc.loan;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class ThemeResolver {

    private static final String LIGHT = "light";
    private static final String DARK = "dark";

    private static final int ACTIVITY_THEME_LIGHT = R.style.Theme_Sherlock_Light;
    private static final int DIALOG_THEME_LIGHT = R.style.Theme_Sherlock_Light_Dialog;
    private static final int ACTIVITY_THEME_DARK = R.style.Theme_Sherlock;
    private static final int DIALOG_THEME_DARK = R.style.Theme_Sherlock_Dialog;


    private static final int TEXT_COLOR_LIGHT = R.color.abs__primary_text_holo_light;
    private static final int TEXT_COLOR_DARK = R.color.abs__primary_text_holo_dark;


    private static final int BACKGROUND_COLOR_LIGHT = R.color.backgroundLight;
    private static final int BACKGROUND_COLOR_DARK = R.color.abs__background_holo_dark;


    public static int getActivityTheme(Context context) {
        String theme = getThemeName(context);
        if (LIGHT.equals(theme)) {
            return ACTIVITY_THEME_LIGHT;
        } else {
            return ACTIVITY_THEME_DARK;
        }
    }

    public static int getTextColor(Context context) {
        String theme = getThemeName(context);
        if (LIGHT.equals(theme)) {
            return context.getResources().getColor(TEXT_COLOR_LIGHT);
        } else {
            return context.getResources().getColor(TEXT_COLOR_DARK);
        }
    }

    public static int getBackgroundColor(Context context) {
        String theme = getThemeName(context);
        if (LIGHT.equals(theme)) {
            return context.getResources().getColor(BACKGROUND_COLOR_LIGHT);
        } else {
            return context.getResources().getColor(BACKGROUND_COLOR_DARK);
        }
    }

    private static String getThemeName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("theme", LIGHT);
    }

    public static boolean isLight(Context context) {
        String theme = getThemeName(context);
        return LIGHT.equals(theme);
    }

  public static int getAddToCompareIcon(Context context) {
    return isLight(context) ? R.drawable.ic_action_add : R.drawable.ic_action_add_dark ;
  }

  public static int getCalculateIcon(Context context) {
    return isLight(context) ? R.drawable.ic_action_calc : R.drawable.ic_action_calc_dark ;
  }

    public static int getDialogTheme(Context context) {
        String theme = getThemeName(context);
        if (LIGHT.equals(theme)) {
            return DIALOG_THEME_LIGHT;
        } else {
            return DIALOG_THEME_DARK;
        }
    }
}
