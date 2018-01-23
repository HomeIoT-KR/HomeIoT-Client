package xyz.neonkid.homeiot.main.adapter.model.chat;

/**
 * 음성 인식 메시지 모델
 *
 * Created by neonkid on 5/17/17.
 */

public class ChatMessage {
    // 서버에서 받은 것인지, 사용자가 요청한 것인지를 구분
    public boolean left;

    // 표시할 메시지
    public final String message;

    public ChatMessage(boolean left, String message) {
        this.left = left;
        this.message = message;
    }
}
