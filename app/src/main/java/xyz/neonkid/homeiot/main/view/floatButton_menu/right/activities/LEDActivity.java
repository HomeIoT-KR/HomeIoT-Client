package xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities;

import android.preference.Preference;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BasePrefActivity;
import xyz.neonkid.homeiot.main.presenter.floatButton_menu.right.LEDPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.LEDPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.fragments.LEDFragment;

/**
 * 조명 수동 제어 Activity
 *
 * @see BasePrefActivity
 *
 * Created by neonkid on 7/7/17.
 */

public class LEDActivity extends BasePrefActivity<LEDFragment> implements LEDPresenterView {
    private LEDPresenter ledPresenter;
    private LEDFragment ledFragment;

    @Override
    protected int getContentXmlResource() { return R.xml.led_headers; }

    @Override
    protected int getToolbarTitle() {
        return R.string.setting_LED;
    }

    @Override
    protected void onCreate() {
        ledPresenter = new LEDPresenter(this, this);
        ledFragment = new LEDFragment();
    }

    /**
     * LED_x 별로 조명 색을 구분합니다.
     *
     * ex) <LED_R>: 빨간색 조명
     *
     * whenXXX 별로 각 데이터 측정 센터에 대한 값을 지정합니다.
     *
     * ex) whenRain: 비가 올 경우,
     *
     * @param pref 변경 확인 대상 Preference
     * @param key 대상 Preference 에 해당하는 key
     */
    @Override
    protected void onSharedPreferenceChangeListener(Preference pref, String key) {
        boolean val;
        try {
            switch(key) {
                case "<LED_R>":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_r_command));
                    break;

                case "<LED_G>":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_g_command));
                    break;

                case "<LED_B>":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_b_command));
                    break;

                case "LED_R_whenRain":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_r_command), getString(R.string.whenRain_command));
                    break;

                case "LED_G_whenRain":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_g_command), getString(R.string.whenRain_command));
                    break;

                case "LED_B_whenRain":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_b_command), getString(R.string.whenRain_command));
                    break;

                case "LED_R_whenDUST":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_r_command), getString(R.string.whenDUST_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_DUSTNum))));
                    break;

                case "LED_G_whenDUST":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_g_command), getString(R.string.whenDUST_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_DUSTNum))));
                    break;

                case "LED_B_whenDUST":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_b_command), getString(R.string.whenDUST_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_DUSTNum))));
                    break;

                case "LED_R_whenTEM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_r_command), getString(R.string.whenTEM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_TEMNum))));
                    break;

                case "LED_G_whenTEM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_g_command), getString(R.string.whenTEM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_TEMNum))));
                    break;

                case "LED_B_whenTEM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_b_command), getString(R.string.whenTEM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_TEMNum))));
                    break;

                case "LED_R_whenHUM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_r_command), getString(R.string.whenHUM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_HUMNum))));
                    break;

                case "LED_G_whenHUM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_g_command), getString(R.string.whenHUM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_HUMNum))));
                    break;

                case "LED_B_whenHUM":
                    val = prefManager.getPrefBoolean(key, true);
                    ledPresenter.makeMessage(val, pref, getString(R.string.led_b_command), getString(R.string.whenHUM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.led_HUMNum))));
                    break;
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            setToastMessage(ex.getMessage());
        }
    }

    @Override
    protected LEDFragment getFragment() {
        return ledFragment;
    }
}
