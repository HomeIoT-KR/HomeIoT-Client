package xyz.neonkid.homeiot.base.command;

/**
 * 자연어 명렁 <--> 서버 명령으로 변환하기 위한 공통 클래스
 *
 * 메시지 변환 양식
 *      --> <SENSOR NAME>DATA<END>
 *          ex) <WINDOWS>1<END> : 창문 여는 메시지
 *
 * Created by neonkid on 6/29/17.
 */

public abstract class BaseCommand {

    // 서버 전송 최대 길이
    private static final int MAX_NUM = 20;

    // 메시지 생성을 위한 StringBuilder
    protected StringBuilder tmpBuilder = new StringBuilder(MAX_NUM);

    // 보낼 메시지, 받을 메시지를 보관할 변수
    protected String sendMsg, recvMsg;

    // 서버와 통신할 메시지의 끝문자 (검증용)
    public static final String endTAG = "<END>";
}
