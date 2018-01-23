package xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities;

import android.preference.Preference;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BasePrefActivity;
import xyz.neonkid.homeiot.main.presenter.floatButton_menu.right.WindowPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.WindowPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.fragments.WindowFragment;

/**
 * 창문 수동 제어 Activity
 *
 * @see BasePrefActivity
 *
 * Created by neonkid on 5/19/17.
 */

public class WindowActivity extends BasePrefActivity<WindowFragment> implements WindowPresenterView {
    private WindowPresenter windowPresenter;
    private WindowFragment windowFragment;

    @Override
    protected int getContentXmlResource() { return R.xml.window_headers; }

    @Override
    protected int getToolbarTitle() {
        return R.string.setting_Window;
    }

    @Override
    protected void onCreate() {
        windowPresenter = new WindowPresenter(this, this);
        windowFragment = new WindowFragment();
    }

    /**
     * window_Stat: 창문 열림/닫힘
     * window_whenRain: 비가 올 경우,
     * 나머지 key 들은 whenRain 과 비슷한 형태로 나아갑니다.
     *
     * @param pref 변경 확인 대상 Preference
     * @param key 대상 Preference 에 해당하는 key
     */
    @Override
    protected void onSharedPreferenceChangeListener(Preference pref, String key) {
        boolean val;
        try {
            switch(key) {
                case "window_Stat":
                    val = prefManager.getPrefBoolean(key, true);
                    windowPresenter.makeMessage(val, key, pref);
                    break;

                case "window_whenRain":
                    val = prefManager.getPrefBoolean(key, true);
                    windowPresenter.makeMessage(val, key, pref, getString(R.string.whenRain_command));
                    break;

                case "window_whenDUST":
                    val = prefManager.getPrefBoolean(key, true);
                    windowPresenter.makeMessage(val, key, pref, getString(R.string.whenDUST_command), String.valueOf(prefManager.getPrefInt(getString(R.string.window_DUSTNum))));
                    break;

                case "window_whenTEM":
                    val = prefManager.getPrefBoolean(key, true);
                    windowPresenter.makeMessage(val, key, pref, getString(R.string.whenTEM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.window_TEMNum))));
                    break;

                case "window_whenHUM":
                    val = prefManager.getPrefBoolean(key, true);
                    windowPresenter.makeMessage(val, key, pref, getString(R.string.whenHUM_command), String.valueOf(prefManager.getPrefInt(getString(R.string.window_HUMNum))));
                    break;
            }
        } catch (NullPointerException | StringIndexOutOfBoundsException ex) {
            ex.printStackTrace();
            setToastMessage(ex.getMessage());
        }
    }

    @Override
    protected WindowFragment getFragment() {
        return windowFragment;
    }
}
