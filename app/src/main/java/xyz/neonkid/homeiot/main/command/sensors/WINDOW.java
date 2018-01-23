package xyz.neonkid.homeiot.main.command.sensors;

import android.content.Context;
import android.util.Log;

import xyz.neonkid.homeiot.base.command.MoveCommand;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 창문 모터를 조작하기 위한 클래스
 *
 * @see MoveCommand
 * Created by neonkid on 5/24/17.
 */

public class WINDOW extends MoveCommand {
    private static final String WINDOW_CODE = "<WINDOWS>";
    private static final String TAG = "[WINDOW_COMMAND]";

    /**
     * Override Method conSendexecute
     *
     * 창문을 열고자 하는 경우: Open
     * 창문을 닫고자 하는 경우: Close
     *
     * @see MoveCommand
     * @param type 행동 Flag
     * @return 서버로 전송할 최종 메시지
     */
    @Override
    public String conSendexecute(switchType type) {
        tmpBuilder.append(WINDOW_CODE);
        Log.i(TAG, "WINDOW 기본 코드 생성 완료..");
        switch(type) {
            case Open:
                tmpBuilder.append(switchType.Open.getCode());
                Log.i(TAG, "WINDOW OPEN 코드가 삽입되었음..");
                break;

            case Close:
                tmpBuilder.append(switchType.Close.getCode());
                Log.i(TAG, "WINDOW CLOSE 코드가 삽입되었음..");
                break;

            default:
                Log.e(TAG, "WINDOW OPEN/CLOSE 신호 코드 삽입이 이루어지지 않음..");
                break;
        }
        tmpBuilder.append(endTAG);
        sendMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return sendMsg;
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
        if(!msg.contains(WINDOW_CODE))
            tmpBuilder.append("잘못된 메시지를 전달 받았습니다. ");
        else {
            tmpBuilder.append("창문 ");
            if(msg.contains(switchType.Open.getCode())) {
                tmpBuilder.append("열어 드렸습니다.");
                pref.putPrefBoolean("window_Stat", true);
            }
            else if(msg.contains(switchType.Close.getCode())) {
                tmpBuilder.append("닫아 드렸습니다. ");
                pref.putPrefBoolean("window_Stat", false);
            }
        }
        recvMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return recvMsg;
    }
}
