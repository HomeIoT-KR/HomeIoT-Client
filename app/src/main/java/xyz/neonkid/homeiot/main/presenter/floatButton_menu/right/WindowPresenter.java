package xyz.neonkid.homeiot.main.presenter.floatButton_menu.right;

import android.preference.Preference;
import android.support.annotation.NonNull;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.presenter.BasePrefPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.WindowPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.WindowActivity;

/**
 * Created by neonkid on 7/4/17.
 */

public class WindowPresenter extends BasePrefPresenter<WindowPresenterView, WindowActivity> {

    public WindowPresenter(WindowPresenterView view, WindowActivity context) {
        super(view, context);
    }

    /**
     * Method makeMessage 1
     *
     * 아무런 조건 없이, 창문을 열고 닫을 떄 사용하는 메소드
     *
     * @param val 창문 열림/닫힘 값
     * @param key 일반 창문 열림/닫힘 key
     * @param pref 변경하고자 하는 Preference
     */
    public void makeMessage(boolean val, String key, Preference pref) {
        String message;
        if(val) {
            message = getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_open_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.window_openClose_summary_option_true));
        } else {
            message = getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_close_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.window_openClose_summary_option_false));
        }
    }

    /**
     * Method makeMessage 2
     *
     * 데이터 수치가 없는 측정 센서에서 사용하는 메소드
     *
     * @param val 창문 열림/닫힘 값
     * @param key 조건 센서에 의한 창문 열림/닫힘 key
     * @param pref 변경하고자 하는 Preference
     * @param option 조건 센서
     */
    public void makeMessage(boolean val, String key, Preference pref, @NonNull String option) {
        String message;
        if(val) {
            message = option + " " + getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_open_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.window_openClose_summary_option_true) + getContext().getString(R.string.window_whenRain_summary_option_true_2));
        } else {
            message = option + " " + getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_close_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.window_openClose_summary_option_false) + getContext().getString(R.string.window_whenRain_summary_option_false));
        }
    }

    /**
     * Method makeMessage 3
     *
     * 데이터 수치가 필요한 측정 센서에서 사용하는 메소드
     *
     * @param val 창문 열림/닫힘 값
     * @param key 조건 센서에 의한 창문 열림/닫힘 key
     * @param pref 변경하고자 하는 Preference
     * @param option 조건 센서
     * @param data 센서 수치 값
     */
    public void makeMessage(boolean val, String key, Preference pref, @NonNull String option, @NonNull String data) {
        String message;
        if(val) {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_open_command);
            getView().changePreferences(message, pref, option + data + " " + getContext().getString(R.string.whenDUST_ifelse_command) + getContext().getString(R.string.window_summary_option_open));
        } else {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + getContext().getString(R.string.window_command) + " " + getContext().getString(R.string.window_close_command);
            getView().changePreferences(message, pref, option + data + " " + getContext().getString(R.string.whenDUST_ifelse_command) + getContext().getString(R.string.window_summary_option_close));
        }
    }
}
