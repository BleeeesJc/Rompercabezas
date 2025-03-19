package com.example.myapplication.login;

import android.content.Context;
import android.content.SharedPreferences;

public class Sesion {
    private static final String PREFS_NAME = "usuario";
    private static final String KEY_USUARIO_ID = "usuarioId";

    public static int getUsuarioId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_USUARIO_ID, -1);
    }
    public static void setUsuarioId(Context context, int usuarioId) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_USUARIO_ID, usuarioId);
        editor.apply();
    }
}