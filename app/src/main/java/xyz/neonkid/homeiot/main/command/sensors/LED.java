package xyz.neonkid.homeiot.main.command.sensors;

import android.content.Context;
import android.util.Log;

import xyz.neonkid.homeiot.base.command.OnOffCommand;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 조명 LED 를 조작하기 위한 클래스
 *
 * @see OnOffCommand
 * Created by neonkid on 5/20/17.
 */

public class LED extends OnOffCommand {
    private static final String LED_CODE_R = "<LED_R>";
    private static final String LED_CODE_G = "<LED_G>";
    private static final String LED_CODE_B = "<LED_B>";
    private static final String TAG = "[LED_COMMAND]";

    public LED() {
        Log.i(TAG, "Call LED... ");
    }

    /**
     * Override Method conSendexecute
     *
     * 조명을 켜고자하는 경우: On
     * 조명을 끄고자하는 경우: Off
     *
     * @param data 사용자로 부터 받은 메시지
     * @param type 제어 타입
     * @return 서버로 전송할 최종 메시지
     */
    @Override
    public String conSendexecute(String data, switchType type) {
        if(data.contains("빨강") || data.contains("빨간"))
            tmpBuilder.append(LED_CODE_R);
        else if(data.contains("초록") || data.contains("녹색"))
            tmpBuilder.append(LED_CODE_G);
        else if(data.contains("파란") || data.contains("파랑"))
            tmpBuilder.append(LED_CODE_B);
        Log.i(TAG, "LED 기본 코드 생성 완료..");
        switch(type) {
            case On:
                tmpBuilder.append(switchType.On.getCode());
                break;

            case Off:
                tmpBuilder.append(switchType.Off.getCode());
                break;

            default:
                Log.e(TAG, "LED ON/OFF 코드 삽입이 이루어지지 않음...");
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
        if(!msg.contains(LED_CODE_R) && !msg.contains(LED_CODE_G) && !msg.contains(LED_CODE_B))
            tmpBuilder.append("잘못된 메시지를 전달받았습니다. ");
        else {
            String command = msg.contains(switchType.On.getCode()) ? "켜졌습니다. " : "꺼졌습니다. ";
            String whatLED = msg.substring(3, 10);
            switch(whatLED) {
                case LED_CODE_R:
                    tmpBuilder.append("빨간 불이 ");
                    tmpBuilder.append(command);
                    pref.putPrefBoolean(LED_CODE_R, msg.contains(switchType.On.getCode()));
                    break;

                case LED_CODE_G:
                    tmpBuilder.append("초록 불이 ");
                    tmpBuilder.append(command);
                    pref.putPrefBoolean(LED_CODE_G, msg.contains(switchType.On.getCode()));
                    break;

                case LED_CODE_B:
                    tmpBuilder.append("파란 불이 ");
                    tmpBuilder.append(command);
                    pref.putPrefBoolean(LED_CODE_B, msg.contains(switchType.On.getCode()));
                    break;

                default:
                    Log.e(TAG, "LED ON/OFF 코드 해석이 이루어지지 않음...");
                    break;
            }
        }
        recvMsg = tmpBuilder.toString();
        tmpBuilder.setLength(0);
        return recvMsg;
    }
}
