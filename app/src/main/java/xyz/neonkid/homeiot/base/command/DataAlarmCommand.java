package xyz.neonkid.homeiot.base.command;

import android.content.Context;
import android.support.annotation.NonNull;

import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 온도, 습도 등 데이터 값을 반환하는 센서의
 * 명령 처리를 위한 기본 공통 클래스
 *
 * @see BaseCommand
 * Created by neonkid on 6/29/17.
 */

public abstract class DataAlarmCommand extends BaseCommand {

    /**
     * 센서에 따른 행동 코드 저장 Flag 열거
     *
     * RESV = 센서 조건 값 : ex) 온도가 몇 도인지, 비가 오는지 안오는지의 여부
     * RACT = 조건에 따른 명령 : ex) 빨간 불 켜, 창문 열어 등
     * REST = 예약 명령어임을 알려주는 태그
     */
    public enum reserveFlag {
        RESV("-Rval"), RACT("-RAct"), REST("<RES>");

        private String code;
        reserveFlag(String code) { this.code = code; }
        public String getCode() { return code; }
    }

    /**
     * 제어에 대한 열거형 정의
     *
     * howMuch, when 은 자연어
     * (각각, 얼마나 있는지, 이만큼 있을 때)
     */
    public enum controlType { howMuch, when }

    // Preference 값을 저장하기 위해 사용할 객체
    protected PrefManager pref;

    /**
     * Constructor
     * @param context Activity 값, Preference 값을 다루기 위해 사용
     */
    public DataAlarmCommand(Context context) {
        pref = new PrefManager(context);
    }

    /**
     * Readonly Method checkInsertRESV
     *
     * 데이터 센서에 의하여, 움직일 센서들의 행동 값을 저장해주는 메소드
     *
     * ex) 온도가 31도 일 떄 창문 열어
     *
     * 위 메시지를 받게 되면,
     *
     * 온도 센서가 31도 = resVal 에 저장
     * 창문을 연다 = actVal 에 저장
     *
     * Preference 에서는 다음과 같이 설정됩니다.
     *
     * TEM_WINDOWS_Rval = 31
     * TEM_WINDOWS_RAct = 창문 열어
     *
     * @param sensor 데이터 센서 이름
     * @param data 센서 데이터 값
     * @param mov 움직일 센서 이름
     * @param act 행동 값
     */
    protected final void checkInsertRESV(String sensor, String data, String mov, String act) {
        String resVal, actVal;
        switch(mov) {
            case "창문":
                resVal = sensor + "-" + "WINDOWS" + reserveFlag.RESV.getCode();
                actVal = sensor + "-" + "WINDOWS" + reserveFlag.RACT.getCode();
                pref.putPrefString(resVal, data);
                pref.putPrefString(actVal, act);
                break;

            case "빨간 불":
                resVal = sensor + "-" + "LED_R" + reserveFlag.RESV.getCode();
                actVal = sensor + "-" + "LED_R" + reserveFlag.RACT.getCode();
                pref.putPrefString(resVal, data);
                pref.putPrefString(actVal, act);
                break;

            case "파란 불":
                resVal = sensor + "-" + "LED_B" + reserveFlag.RESV.getCode();
                actVal = sensor + "-" + "LED_B" + reserveFlag.RACT.getCode();
                pref.putPrefString(resVal, data);
                pref.putPrefString(actVal, act);
                break;

            case "초록 불":
                resVal = sensor + "-" + "LED_G" + reserveFlag.RESV.getCode();
                actVal = sensor + "-" + "LED_G" + reserveFlag.RACT.getCode();
                pref.putPrefString(resVal, data);
                pref.putPrefString(actVal, act);
                break;

            case "벨":
                resVal = sensor + "-" + "BUZZER" + reserveFlag.RESV.getCode();
                actVal = sensor + "-" + "BUZZER" + reserveFlag.RACT.getCode();
                pref.putPrefString(resVal, data);
                pref.putPrefString(actVal, act);
                break;
        }
    }

    /**
     * Abstract Method conSendexecute for {@link DataAlarmCommand}
     *
     * 서버로 보내는 메시지를 변환하는 메소드
     *
     * @param data 사용자가 말한 메시지
     * @param type 정해진 플래그 값 유무
     * @param mov 이동 센서값 유무
     * @return 최종 변환된 메시지
     */
    public abstract String conSendexecute(String data, @NonNull controlType type, String mov);

    /**
     * Abstract Method conRecvexecute for {@link DataAlarmCommand}
     *
     * 서버로부터 받은 메시지를 변환하는 메소드
     *
     * @param command 서버로부터 받은 메시지
     * @return 최종 변환된 메시지
     */
    public abstract String conRecvexecute(String command);
}
