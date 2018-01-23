package xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities;

import android.preference.Preference;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BasePrefActivity;
import xyz.neonkid.homeiot.main.presenter.floatButton_menu.left.SettingsPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.left.SettingsPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.fragments.SettingsFragment;

/**
 * 앱 설정 Activity
 *
 * Created by neonkid on 5/19/17.
 */

public class SettingsActivity extends BasePrefActivity<SettingsFragment> implements SettingsPresenterView {
    private SettingsPresenter settingsPresenter;
    private SettingsFragment settingsFragment;

    @Override
    protected int getContentXmlResource() {
        return R.xml.pref_headers;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.settings_App;
    }

    @Override
    protected void onCreate() {
        settingsPresenter = new SettingsPresenter(this, this);
        settingsFragment = new SettingsFragment();
    }

    @Override
    protected SettingsFragment getFragment() {
        return settingsFragment;
    }

    /**
     * 일부 설정 변경시, View 에 알림 표시
     *
     * @param pref 변경 확인 대상 Preference
     * @param key 대상 Preference 에 해당하는 key
     */
    @Override
    protected void onSharedPreferenceChangeListener(Preference pref, String key) {
        switch(key) {
            case "keepAlive_value":
                setToastMessage(getString(R.string.noti_change_keepAlive));
                break;
        }
    }
}
