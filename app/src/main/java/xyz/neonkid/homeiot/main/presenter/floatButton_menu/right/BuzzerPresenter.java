package xyz.neonkid.homeiot.main.presenter.floatButton_menu.right;

import android.preference.Preference;
import android.support.annotation.NonNull;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.presenter.BasePrefPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.right.BuzzerPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.right.activities.BuzzerActivity;

/**
 * Created by neonkid on 7/7/17.
 */

public class BuzzerPresenter extends BasePrefPresenter<BuzzerPresenterView, BuzzerActivity> {

    public BuzzerPresenter(BuzzerPresenterView view, BuzzerActivity context) { super(view, context); }

    /**
     * Method makeMessage 1
     *
     * 아무런 조건 없이, 벨을 울릴 때 사용하는 메소드
     *
     * @param val 벨 울림/멈춤 값
     * @param key 일반 벨 울림/멈춤 key
     * @param pref 변경하고자 하는 Preference
     */
    public void makeMessage(boolean val, String key, Preference pref) {
        String message;
        if(val) {
            message = getContext().getString(R.string.buzzer_command) + key + getContext().getString(R.string.buzzer_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_On));
        } else {
            message = getContext().getString(R.string.buzzer_command) + getContext().getString(R.string.buzzer_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_Off));
        }
    }

    /**
     * Method makeMessage 2
     *
     * 데이터 수치가 없는 측정 센서에서 사용하는 메소드
     *
     * @param val 벨 울림/멈춤 값
     * @param key 조건 센서에 의한 벨 울림/멈춤 key
     * @param pref 변경하고자 하는 Preference
     * @param option 조건 센서
     */
    public void makeMessage(boolean val, String key, Preference pref, @NonNull String option) {
        String message;
        if(val) {
            message = option + " " + getContext().getString(R.string.buzzer_command) + key + getContext().getString(R.string.buzzer_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_On));
        } else {
            message = option + " " + getContext().getString(R.string.buzzer_command) + getContext().getString(R.string.buzzer_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_Off));
        }
    }

    /**
     * Method makeMessage 3
     *
     * 데이터 수치가 필요한 측정 센서에서 사용하는 메소드
     *
     * @param val 벨 울림/멈춤 값
     * @param key 조건 센서에 의한 벨 울림/멈춤 key
     * @param pref 변경하고자 하는 Preference
     * @param option 조건 센서
     * @param data 센서 수치 값
     */
    public void makeMessage(boolean val, String key, Preference pref, @NonNull String option, @NonNull String data) {
        String message;
        if(val) {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + getContext().getString(R.string.buzzer_command) + key + getContext().getString(R.string.buzzer_on_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_On));
        } else {
            message = option + " " + data + getContext().getString(R.string.whenTEM_ifelse_command) + " " + getContext().getString(R.string.buzzer_command) + getContext().getString(R.string.buzzer_off_command);
            getView().changePreferences(message, pref, getContext().getString(R.string.buzzer_onOff_summary_Off));
        }
    }
}
