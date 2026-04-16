package InvestTrack.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppPreferences {

    private static final String PREFS_NAME = "investtrack_preferences";

    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_GUEST_MODE = "guest_mode";
    private static final String KEY_CURRENT_PRICE = "current_price";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_IS_FAVORITE = "is_favorite";

    private static final float DEFAULT_CURRENT_PRICE = 248.32f;
    private static final float DEFAULT_QUANTITY = 12.0f;
    private static final float DEFAULT_AVERAGE_PRICE = 229.10f;

    private AppPreferences() {
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUser(Context context, String name, String email) {
        preferences(context)
                .edit()
                .putString(KEY_USER_NAME, name.trim())
                .putString(KEY_USER_EMAIL, email.trim())
                .apply();
    }

    public static String getUserName(Context context) {
        return preferences(context).getString(KEY_USER_NAME, "Alicia Torres");
    }

    public static String getGreetingName(Context context) {
        String name = getUserName(context).trim();
        if (name.isEmpty()) {
            return "Investor";
        }

        int separator = name.indexOf(' ');
        return separator > 0 ? name.substring(0, separator) : name;
    }

    public static String getUserEmail(Context context) {
        return preferences(context).getString(KEY_USER_EMAIL, "alicia.torres@investtrack.app");
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, loggedIn)
                .putBoolean(KEY_GUEST_MODE, false)
                .apply();
    }

    public static boolean isLoggedIn(Context context) {
        return preferences(context).getBoolean(KEY_LOGGED_IN, false);
    }

    public static void enableGuestMode(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, true)
                .apply();
    }

    public static boolean isGuestMode(Context context) {
        return preferences(context).getBoolean(KEY_GUEST_MODE, false);
    }

    public static boolean hasActiveSession(Context context) {
        return isLoggedIn(context) || isGuestMode(context);
    }

    public static void logout(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, false)
                .apply();
    }

    public static void saveInvestment(Context context, double currentPrice, double quantity) {
        preferences(context)
                .edit()
                .putFloat(KEY_CURRENT_PRICE, (float) currentPrice)
                .putFloat(KEY_QUANTITY, (float) quantity)
                .apply();
    }

    public static double getCurrentPrice(Context context) {
        return preferences(context).getFloat(KEY_CURRENT_PRICE, DEFAULT_CURRENT_PRICE);
    }

    public static double getQuantity(Context context) {
        return preferences(context).getFloat(KEY_QUANTITY, DEFAULT_QUANTITY);
    }

    public static double getAveragePrice() {
        return DEFAULT_AVERAGE_PRICE;
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
