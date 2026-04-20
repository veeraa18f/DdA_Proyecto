package InvestTrack.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPreferences {

    private static final String PREFS_NAME = "investtrack_preferences";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_GUEST_MODE = "guest_mode";
    private static final String KEY_IS_FAVORITE = "is_favorite";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_PROFILE_PHOTO_INDEX = "profile_photo_index";
    private static final String KEY_LANGUAGE_CODE = "language_code";

    private AppPreferences() {
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, loggedIn)
                .putBoolean(KEY_GUEST_MODE, false)
                .apply();
    }

    public static void enableGuestMode(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, true)
                .apply();
    }

    public static void logout(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, false)
                .apply();
    }

    public static boolean isFavorite(Context context) {
        return preferences(context).getBoolean(KEY_IS_FAVORITE, true);
    }

    public static boolean toggleFavorite(Context context) {
        boolean updatedValue = !isFavorite(context);
        preferences(context)
                .edit()
                .putBoolean(KEY_IS_FAVORITE, updatedValue)
                .apply();
        return updatedValue;
    }

    public static boolean isDarkModeEnabled(Context context) {
        return preferences(context).getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkModeEnabled(Context context, boolean enabled) {
        preferences(context)
                .edit()
                .putBoolean(KEY_DARK_MODE, enabled)
                .apply();
    }

    public static int getProfilePhotoIndex(Context context) {
        return preferences(context).getInt(KEY_PROFILE_PHOTO_INDEX, 1);
    }

    public static void setProfilePhotoIndex(Context context, int profilePhotoIndex) {
        preferences(context)
                .edit()
                .putInt(KEY_PROFILE_PHOTO_INDEX, profilePhotoIndex)
                .apply();
    }

    public static String getLanguageCode(Context context) {
        return preferences(context).getString(KEY_LANGUAGE_CODE, LanguageHelper.LANGUAGE_ENGLISH);
    }

    public static void setLanguageCode(Context context, String languageCode) {
        preferences(context)
                .edit()
                .putString(KEY_LANGUAGE_CODE, LanguageHelper.normalizeLanguageCode(languageCode))
                .apply();
    }
}
