package xyz.neonkid.homeiot.main.command.sensors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import xyz.neonkid.homeiot.base.command.DataAlarmCommand;

/**
 * 습도 센서 조작을 위한 클래스
 *
 * @see DataAlarmCommand
 * Created by neonkid on 9/5/17.
 */

public class HUM extends DataAlarmCommand {
    private static final String HUM_CODE = "<HUMIDITY>";
    private static final String TAG = "[HUM_COMMAND]";
    private static final String HUM_NAME = "Humidity";

    public HUM(Context context) {
        super(context);
        Log.i(TAG, "Call HUM... ");
    }

    /**
     * Override Method conSendexecute
     *
     * 습도 값이 알고 싶은 경우: howMuch
     * 습도 값에 따른 센서 제어: when
     *
     * @param msg 사용자로부터 받은 메시지
     * @param type 제어 타입
     * @param mov 이동 센서값 유무
     * @return 서버로 전송할 최종 메시지
     */
    @Override
    public String conSendexecute(String msg, @NonNull controlType type, String mov) {
        switch(type) {
            case howMuch:
                String str = pref.getPrefString("HomeIoT_Humidity") + endTAG;
                tmpBuilder.append(HUM_CODE);
                Log.i(TAG, "HUM 기본 코드 생성 완료...");
                tmpBuilder.append(str);
                Log.i(TAG, "HUM 데이터가 성공적으로 완성되었음... ");
                break;

            case when:
                checkInsertRESV(HUM_NAME, msg.substring(msg.indexOf("습도"), msg.indexOf("습도") + 8).replaceAll("[^0-9.]", ""),
                        mov, msg.substring(msg.indexOf(mov)));
                tmpBuilder.append(reserveFlag.REST.getCode());
                tmpBuilder.append(endTAG);
                break;

            default:
                Log.e(TAG, "HUM 신호 코드 삽입이 이루어지지 않음...");
                break;
        }
        sendMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return sendMsg;
    }

    /**
     * Override conRecvexecute
     *
     * 서버로부터 받은 메시지에 센서 코드가 들어가
     * 있지 않은 경우, 오류로 판정
     *
     * @param command 서버로부터 받은 메시지
     * @return 사용자에게 보여줄 메시지
     */
    @Override
    public String conRecvexecute(String command) {
        if(!command.contains(HUM_CODE))
            tmpBuilder.append("잘못된 메시지를 전달받았습니다. ");
        else {
            String msg = "현재 습도는 " + command.replaceAll("[^0-9.]", "") + "% 입니다. ";
            tmpBuilder.append(msg);
        }
        recvMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return recvMsg;
    }
}
