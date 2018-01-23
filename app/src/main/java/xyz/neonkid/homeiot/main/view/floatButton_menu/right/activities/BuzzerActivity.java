package xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities;

import android.preference.Preference;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BasePrefActivity;
import xyz.neonkid.homeiot.main.presenter.floatButton_menu.right.BuzzerPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.BuzzerPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.fragments.BuzzerFragment;

/**
 * 벨 수동 제어 Activity
 *
 * @see BasePrefActivity
 *
 * Created by neonkid on 7/7/17.
 */

public class BuzzerActivity extends BasePrefActivity<BuzzerFragment> implements BuzzerPresenterView {
    private BuzzerPresenter buzzerPresenter;
    private BuzzerFragment buzzerFragment;

    @Override
    protected int getContentXmlResource() { return R.xml.buzzer_headers; }

    @Override
    protected int getToolbarTitle() {
        return R.string.setting_Buzzer;
    }

    @Override
    protected void onCreate() {
        buzzerPresenter = new BuzzerPresenter(this, this);
        buzzerFragment = new BuzzerFragment();
    }

    /**
     * buzzer_Stat: 벨 켜짐/꺼짐 스위치
     *
     * @param pref 변경 확인 대상 Preference
     * @param key 대상 Preference 에 해당하는 key
     */
    @Override
    protected void onSharedPreferenceChangeListener(Preference pref, String key) {
        boolean val;
        try {
            switch(key) {
                case "buzzer_Stat":
                    val = prefManager.getPrefBoolean(key, true);
                    buzzerPresenter.makeMessage(val, String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_Count))), pref);
                    break;

                case "buzzer_whenRain":
                    val = prefManager.getPrefBoolean(key, true);
                    buzzerPresenter.makeMessage(val, String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_Count_whenRain))), pref, getString(R.string.whenRain_command));
                    break;

                case "buzzer_whenDUST":
                    val = prefManager.getPrefBoolean(key, true);
                    buzzerPresenter.makeMessage(val, String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_Count_whenDUST))), pref, getString(R.string.whenDUST_command), String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_DUSTNum))));
                    break;

                case "buzzer_whenTEM":
                    val = prefManager.getPrefBoolean(key, true);
                    buzzerPresenter.makeMessage(val, String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_Count_whenTEM))), pref, getString(R.string.whenTEM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_TEMNum))));
                    break;

                case "buzzer_whenHUM":
                    val = prefManager.getPrefBoolean(key, true);
                    buzzerPresenter.makeMessage(val, String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_Count_whenHUM))), pref, getString(R.string.whenHUM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.buzzer_HUMNum))));
                    break;
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            setToastMessage(ex.getMessage());
        }
    }

    @Override
    protected BuzzerFragment getFragment() {
        return buzzerFragment;
    }
}
