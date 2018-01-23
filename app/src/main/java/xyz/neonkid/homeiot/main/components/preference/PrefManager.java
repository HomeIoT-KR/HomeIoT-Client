package xyz.neonkid.homeiot.main.components.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Android Preference 사용시, 공통으로 들어가는
 * 코드를 정리해 놓은 클래스
 *
 * Created by neonkid on 7/28/17.
 */

public class PrefManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;

    public PrefManager(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        prefEditor = pref.edit();
    }

    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        pref.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        pref.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public void putPrefString(String pref_Name, @Nullable String val) {
        prefEditor.putString(pref_Name, val != null ? val : "");
        prefEditor.apply();
    }

    public String getPrefString(String pref_Name) {
        return pref.getString(pref_Name, "");
    }

    public String getPrefString(String pref_Name, @Nullable String val) {
        return pref.getString(pref_Name, val == null ? "" : val);
    }

    public void putPrefBoolean(String pref_Name, boolean val) {
        prefEditor.putBoolean(pref_Name, val);
        prefEditor.apply();
    }

    public boolean getPrefBoolean(String pref_Name) {
        return pref.getBoolean(pref_Name, false);
    }

    public boolean getPrefBoolean(String pref_Name, boolean val) {
        return pref.getBoolean(pref_Name, val);
    }

    public void putPrefInt(String pref_Name, int val) {
        prefEditor.putInt(pref_Name, val);
        prefEditor.apply();
    }

    public void putPrefFloat(String pref_Name, float val) {
        prefEditor.putFloat(pref_Name, val);
        prefEditor.apply();
    }

    public int getPrefInt(String pref_Name) {
        return pref.getInt(pref_Name, 0);
    }

    public int getPrefInt(String pref_Name, int val) {
        return pref.getInt(pref_Name, val);
    }
}
