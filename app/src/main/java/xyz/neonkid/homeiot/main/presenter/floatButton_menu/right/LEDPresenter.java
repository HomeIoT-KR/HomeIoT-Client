package xyz.neonkid.homeiot.main.presenter.floatButton_menu.right;

import android.preference.Preference;
import android.support.annotation.NonNull;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.presenter.BasePrefPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.LEDPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.LEDActivity;

/**
 * Created by neonkid on 7/7/17.
 */

public class LEDPresenter extends BasePrefPresenter<LEDPresenterView, LEDActivity> {
    public LEDPresenter(LEDPresenterView view, LEDActivity context) { super(view, context); }

    /**
     * Method makeMessage 1
     *
     * 아무런 조건 없이 조명을 켜고 끌 떄 사용하는 메소드
     *
     * @param val Preference 값 value
     * @param pref 변경하고자 하는 Preference
     * @param ledColor 조명 색깔 값
     */
    public void makeMessage(boolean val, Preference pref, @NonNull String ledColor) {
        String message;
        if(val) {
            message = ledColor + " " + getContext().getString(R.string.led_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_On));
        } else {
            message = ledColor + " " + getContext().getString(R.string.led_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_Off));
        }
    }

    /**
     * Method makeMessage 2
     *
     * 데이터 수치가 없는 측정 센서에서 사용하는 메소드
     *
     * @param val Preference 값 value
     * @param pref 변경하고자 하는 Preference
     * @param ledColor 조명 색깔 값
     * @param option 조건 센서
     */
    public void makeMessage(boolean val, Preference pref, @NonNull String ledColor, @NonNull String option) {
        String message;
        if(val) {
            message = option + " " + ledColor + " " + getContext().getString(R.string.led_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_On));
        } else {
            message = option + " " + ledColor + " " + getContext().getString(R.string.led_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_Off));
        }
    }

    /**
     * Method makeMessage 3
     *
     * 데이터 수치가 필요한 측정 센서에서 사용하는 메소드
     *
     * @param val Preference 값 value
     * @param pref 변경하고자 하는 Preference
     * @param ledColor 조명 색깔 값
     * @param option 조건 센서
     * @param data 센서 수치 값
     */
    public void makeMessage(boolean val, Preference pref, @NonNull String ledColor, @NonNull String option, @NonNull String data) {
        String message;
        if(val) {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + ledColor + " " + getContext().getString(R.string.led_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_On));
        } else {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + ledColor + " " + getContext().getString(R.string.led_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.led_onOff_summary_Off));
        }
    }
}
