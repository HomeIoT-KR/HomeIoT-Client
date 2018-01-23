package xyz.neonkid.homeiot.base.command;

import android.content.Context;

/**
 * 창문, 블라인드 등 모터를 이용하여 움직이는 센서의
 * 명령 처리를 위한 기본 공통 클래스
 *
 * @see BaseCommand
 * Created by neonkid on 7/2/17.
 */

public abstract class MoveCommand extends BaseCommand {

    /**
     * 제어에 대한 열거형 정의
     * Open, Close 는 자연어
     * 파라메터로 들어간 code 는 변환 값
     */
    public enum switchType {
        Open(1), Close(0);

        private int code;
        switchType(int code) { this.code = code; }
        public String getCode() { return String.valueOf(code); }
    }

    /**
     * Abstract Method conSendexecute for {@link MoveCommand}
     *
     * 서버로 보내는 메시지를 반환하는 코드
     * 반드시 메시지 마지막에는 endTAG 를 포함해야 한다
     *
     * @param type 행동 Flag
     * @return 서버로 보낼 메시지 반환
     */
    public abstract String conSendexecute(switchType type);

    /**
     * Abstract Method conRecvexecute for {@link MoveCommand}
     *
     * 서버로부터 받은 메시지를 변환하는 메소드
     * Context 는 수동 제어의 Preference 와 동기화 하기 위해 사용
     *
     * @param msg 서버로부터 받은 메시지
     * @param context context
     * @return 최종 변환된 메시지
     */
    public abstract String conRecvexecute(String msg, Context context);
}
