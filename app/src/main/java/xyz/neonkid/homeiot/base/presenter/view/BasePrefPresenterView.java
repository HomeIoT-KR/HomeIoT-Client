package xyz.neonkid.homeiot.base.presenter.view;

import android.preference.Preference;

/**
 * Created by neonkid on 7/5/17.
 */

public interface BasePrefPresenterView {
    void changePreferences(String sendMsg, Preference pref, String summary);
}
