package com.tuempresa.investtrack.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.tuempresa.investtrack.core.settings.LanguageHelper;
import com.tuempresa.investtrack.data.repository.InvestTrackRepository;
import com.tuempresa.investtrack.data.local.UserEntity;

public final class AppPreferences {

    private static final String PREFS_NAME = "investtrack_preferences";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_GUEST_MODE = "guest_mode";
    private static final String KEY_CURRENT_USER_ID = "current_user_id";
    private static final String KEY_IS_FAVORITE = "is_favorite";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_PROFILE_PHOTO_INDEX = "profile_photo_index";
    private static final String KEY_LANGUAGE_CODE = "language_code";
    private static final String KEY_PROFILE_USERNAME = "profile_username";
    private static final String KEY_PROFILE_PHONE = "profile_phone";
    private static final String KEY_PROFILE_EMAIL = "profile_email";

    private static final String DEFAULT_PROFILE_USERNAME = "Guest investor";
    private static final String DEFAULT_PROFILE_PHONE = "+34 600 000 000";
    private static final String DEFAULT_PROFILE_EMAIL = "demo@investtrack.app";

    private AppPreferences() {
    }

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        setLoggedIn(context, loggedIn, InvestTrackRepository.DEMO_USER_ID);
    }

    public static void setLoggedIn(Context context, boolean loggedIn, String userId) {
        SharedPreferences.Editor editor = preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, loggedIn)
                .putBoolean(KEY_GUEST_MODE, false);

        if (loggedIn) {
            editor.putString(KEY_CURRENT_USER_ID, userId);
        } else {
            editor.remove(KEY_CURRENT_USER_ID);
        }

        editor.apply();
    }

    public static String getActiveUserId(Context context) {
        String userId = preferences(context).getString(KEY_CURRENT_USER_ID, null);
        if (userId == null || userId.trim().isEmpty()) {
            return InvestTrackRepository.GUEST_USER_ID;
        }

        UserEntity user = InvestTrackRepository.getInstance(context).getUserById(userId);
        return user == null ? InvestTrackRepository.GUEST_USER_ID : user.id;
    }

    public static void enableGuestMode(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, true)
                .putString(KEY_CURRENT_USER_ID, InvestTrackRepository.GUEST_USER_ID)
                .apply();
    }

    public static void logout(Context context) {
        preferences(context)
                .edit()
                .putBoolean(KEY_LOGGED_IN, false)
                .putBoolean(KEY_GUEST_MODE, false)
                .remove(KEY_CURRENT_USER_ID)
                .apply();
    }

    public static boolean isGuestMode(Context context) {
        String userId = preferences(context).getString(KEY_CURRENT_USER_ID, null);
        return preferences(context).getBoolean(KEY_GUEST_MODE, false)
                || InvestTrackRepository.GUEST_USER_ID.equals(userId);
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

    public static boolean isFavorite(Context context, String assetId) {
        if (isGuestMode(context) || assetId == null || assetId.trim().isEmpty()) {
            return false;
        }
        return InvestTrackRepository
                .getInstance(context)
                .isFavorite(getActiveUserId(context), assetId);
    }

    public static boolean toggleFavorite(Context context, String assetId) {
        if (isGuestMode(context)) {
            return false;
        }
        return InvestTrackRepository
                .getInstance(context)
                .toggleFavorite(getActiveUserId(context), assetId);
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
        UserEntity user = getActiveUser(context);
        if (user != null) {
            return user.profilePhotoIndex;
        }
        return preferences(context).getInt(KEY_PROFILE_PHOTO_INDEX, 1);
    }

    public static void setProfilePhotoIndex(Context context, int profilePhotoIndex) {
        if (isGuestMode(context)) {
            return;
        }

        InvestTrackRepository
                .getInstance(context)
                .updateUserProfilePhoto(getActiveUserId(context), profilePhotoIndex);
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

    public static String getProfileUsername(Context context) {
        UserEntity user = getActiveUser(context);
        if (user != null) {
            return user.name;
        }
        return preferences(context).getString(KEY_PROFILE_USERNAME, DEFAULT_PROFILE_USERNAME);
    }

    public static String getProfilePhone(Context context) {
        UserEntity user = getActiveUser(context);
        if (user != null) {
            return user.phone;
        }
        return preferences(context).getString(KEY_PROFILE_PHONE, DEFAULT_PROFILE_PHONE);
    }

    public static String getProfileEmail(Context context) {
        UserEntity user = getActiveUser(context);
        if (user != null) {
            return user.email;
        }
        return preferences(context).getString(KEY_PROFILE_EMAIL, DEFAULT_PROFILE_EMAIL);
    }

    public static void setProfileData(
            Context context,
            String username,
            String phone,
            String email
    ) {
        if (isGuestMode(context)) {
            return;
        }

        InvestTrackRepository
                .getInstance(context)
                .updateUserProfile(getActiveUserId(context), username, phone, email);
        preferences(context)
                .edit()
                .putString(KEY_PROFILE_USERNAME, username)
                .putString(KEY_PROFILE_PHONE, phone)
                .putString(KEY_PROFILE_EMAIL, email)
                .apply();
    }

    private static UserEntity getActiveUser(Context context) {
        return InvestTrackRepository
                .getInstance(context)
                .getUserById(getActiveUserId(context));
    }
}
