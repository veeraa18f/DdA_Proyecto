package InvestTrack.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPreferences {

    private static final String PREFS_NAME = "investtrack_preferences";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_GUEST_MODE = "guest_mode";
    private static final String KEY_IS_FAVORITE = "is_favorite";

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
}
