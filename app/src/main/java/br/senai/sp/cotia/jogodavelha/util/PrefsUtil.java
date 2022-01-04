package br.senai.sp.cotia.jogodavelha.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class PrefsUtil {
    public static String getSimboloJog1(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("simbolo_jog1", "X");
    }

    public static String getSimboloJog2(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("simbolo_jog2", "X");
    }
}
