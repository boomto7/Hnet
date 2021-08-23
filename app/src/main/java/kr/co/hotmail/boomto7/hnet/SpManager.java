package kr.co.hotmail.boomto7.hnet;

import android.content.Context;
import android.content.SharedPreferences;

public class SpManager {
    private static final String PREF_NAME = "hnet_header_jwt";
    public static final String FIELD_NAME_CURRENT_JWT = "hnet_field_name_current_jwt";
    public static final String FIELD_NAME_REFRESH_JWT = "hnet_field_name_refresh_jwt";

    public static SpManager instance;
    private final SharedPreferences prefs;

    private SpManager(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SpManager getInstance(Context ctx) {
        if ( instance == null) {
            instance = new SpManager(ctx);
        }

        return instance;
    }

    public String getCurrentJwt() {
        if (prefs == null) return "";
        return prefs.getString(FIELD_NAME_CURRENT_JWT,"");
    }

    public String getRefreshJwt() {
        if (prefs == null) return "";
        return prefs.getString(FIELD_NAME_REFRESH_JWT,"");
    }

    public void setCurrentJwt(String token) {
        if ( prefs == null) return;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIELD_NAME_CURRENT_JWT, token);
        editor.apply();
    }

    public void setRefreshJwt(String token) {
        if ( prefs == null) return;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FIELD_NAME_REFRESH_JWT, token);
        editor.apply();
    }
}
