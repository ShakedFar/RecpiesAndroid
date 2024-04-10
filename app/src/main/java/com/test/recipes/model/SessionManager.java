package com.test.recipes.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // All Shared Preferences Keys
    public static final String KEY_IS_LOGIN = "is_login";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        editor = pref.edit();
        editor.apply();
    }
    public void setLogin(boolean is_login){
        editor.putBoolean(KEY_IS_LOGIN, is_login);
        editor.apply();
    }
    public boolean IsLogin() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

}
