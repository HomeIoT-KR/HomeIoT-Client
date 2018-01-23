package xyz.neonkid.homeiot.main.command.sensors;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import xyz.neonkid.homeiot.base.command.OnOffCommand;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 벨 센서 조작을 위한 클래스
 *
 * @see OnOffCommand
 * Created by neonkid on 6/28/17.
 */

public class BUZZER extends OnOffCommand {
    private static final String BUZZER_CODE = "<BUZZER>";
    private static final String TAG = "[BUZZER_COMMAND]";

    public BUZZER() {
        Log.i(TAG, "Call BUZZER... ");
    }

    /**
     * Override Method conSendexecute
     *
     * 벨을 울리는 경우, 얼마나 울릴 지에 대한 데이터 삽입
     * 벨을 멈출 경우, 멈춤 코드를 삽입
     *
     * @param data 받은 메시지 중, 벨의 시간 값
     * @param type On / Off 판단 Flag
     * @return 서버로 전송할 최종 메시지
     */
    @Override
    public String conSendexecute(@Nullable String data, switchType type) {
        tmpBuilder.append(BUZZER_CODE);
        Log.i(TAG, "BUZZER 기본 코드 생성 완료..");
        switch(type) {
            case On:
                tmpBuilder.append(data);
                break;
            case Off:
                tmpBuilder.append(switchType.Off.getCode());
                break;
            default:
                Log.e(TAG, "BUZZER 신호 코드 삽입이 이루어지지 않음...");
                break;
        }
        tmpBuilder.append(endTAG);
        recvMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return recvMsg;
    }

    /**
     * Override Method conRecvexecute
     *
     * 서버로부터 받은 메시지에 센서 코드가 들어가
     * 있지 않은 경우, 오류로 판정
     *
     * @param msg 서버로부터 받은 메시지
     * @param context context
     * @return 사용자에게 보여줄 메시지
     */
    @Override
    public String conRecvexecute(String msg, Context context) {
        PrefManager pref = new PrefManager(context);
        if(!msg.contains(BUZZER_CODE))
            tmpBuilder.append("잘못된 메시지를 전달받았습니다. ");
        else {
            tmpBuilder.append("벨이 ");
            if(!msg.contains("0")) {
                tmpBuilder.append(msg.replaceAll("\\D", ""));
                tmpBuilder.append("번 울립니다.");
                pref.putPrefBoolean("buzzer_Stat", true);
            }
            else if(msg.contains(switchType.Off.getCode())) {
                tmpBuilder.append("멈췄습니다.");
                pref.putPrefBoolean("buzzer_Stat", false);
            }
        }
        recvMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return recvMsg;
    }
}
