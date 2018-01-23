package xyz.neonkid.homeiot.main.command;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import xyz.neonkid.homeiot.base.command.BaseCommand;
import xyz.neonkid.homeiot.base.command.DataAlarmCommand;
import xyz.neonkid.homeiot.base.command.MoveCommand;
import xyz.neonkid.homeiot.base.command.OnOffCommand;
import xyz.neonkid.homeiot.main.command.sensors.BUZZER;
import xyz.neonkid.homeiot.main.command.sensors.DUST;
import xyz.neonkid.homeiot.main.command.sensors.HUM;
import xyz.neonkid.homeiot.main.command.sensors.LED;
import xyz.neonkid.homeiot.main.command.sensors.RAIN;
import xyz.neonkid.homeiot.main.command.sensors.TEM;
import xyz.neonkid.homeiot.main.command.sensors.WINDOW;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 명령어를 처리하는 매니저 클래스
 * 사용자의 메시지를 전달 받아, 토크나이징 하여,
 * 해당하는 각 명령 클래스에 전달한 후, 메시지 변환
 *
 * Created by neonkid on 7/5/17.
 */

public class CommandManager {
    private static final String TAG = "[COMMAND_MANAGER]";
    private Context context;

    // 메시지 전송 최대 길이
    private static final int MAX_NUM = 20;

    // 메시지 생성 빌더..
    private StringBuilder commandBuilder = new StringBuilder(MAX_NUM);

    // 송신, 수신 메시지 저장 변수
    private String sendMsg, recvMsg;

    // 데이터 측정 센서를 모아놓은 Map
    private Map<String, String> measureMap = new HashMap<>();

    // 이동, On/Off 센서를 모아놓은 Map
    private Map<String, String> moveMap = new HashMap<>();

    // 메시지에 들어가는 제일 첫번째 문자
    public static final String sendHeader = "<";

    // OK 신호
    private static final String OK = "OK:";

    // Preference Manager
    private PrefManager prefManager;

    /**
     * Constructor
     *
     * Context 를 인자로 받아, Preference 저장에 사용
     * CommandManager 를 생성할시, 센서에 사용할 Map 을 생성
     * @param context context
     */
    public CommandManager(Context context) {
        this.context = context;
        prefManager = new PrefManager(context);
        if(measureMap.isEmpty()) {
            measureMap.put("<DUST>", "먼지"); measureMap.put("<TEMPERATURE>", "온도");
            measureMap.put("<HUMIDITY>", "습도"); measureMap.put("<RAIN>", "비");
        }
        if(moveMap.isEmpty()) {
            moveMap.put("<LED_R>", "빨간 불"); moveMap.put("<LED_G>", "초록 불");
            moveMap.put("<LED_B>", "파란 불"); moveMap.put("<BUZZER>", "벨"); moveMap.put("<WINDOWS>", "창문");
        }
    }

    /**
     * Method recvFilterMessage
     *
     * 서버로부터 받은 메시지를 Tokenize 하여
     * 해당 클래스에 전달 후, 변환된 메시지를 반환
     *
     * 1. 메시지를 받은 후, 센서 태그를 검사한다.
     * 2. moveMap 은, 서버에서 메시지가 들어오므로, 반드시 OK 사인을 검사한다.
     * 3. 센서 태그가 확인되면, 각 센서에 맞는 클래스로 메시지를 보낸 후, 변환한다.
     * 4. 센서 태그가 확인되지 않으면, NULL 을 반환하고, 서비스에게 처리를 맡긴다.
     *
     * @param message 서버로부터 받은 메시지
     * @return 사용자에게 보여줄 메시지
     */
    public String recvFilterMessage(String message) {
        Iterator<Map.Entry<String, String>> measure_Iterator = measureMap.entrySet().iterator();
        Iterator<Map.Entry<String, String>> move_Iterator = moveMap.entrySet().iterator();

        while(measure_Iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)measure_Iterator.next();
            if(message.contains(entry.getKey().toString())) {
                switch(entry.getValue().toString()) {
                    case "비":
                        RAIN rain = new RAIN(context);
                        recvMsg = rain.conRecvexecute(message);
                        break;

                    case "온도":
                        TEM tem = new TEM(context);
                        recvMsg = tem.conRecvexecute(message);
                        break;

                    case "습도":
                        HUM hum = new HUM(context);
                        recvMsg = hum.conRecvexecute(message);
                        break;

                    case "먼지":
                        DUST dust = new DUST(context);
                        recvMsg = dust.conRecvexecute(message);
                        break;
                }
            }
        }

        if(message.contains(OK)) {
            while(move_Iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)move_Iterator.next();
                if(message.contains(entry.getKey().toString())) {
                    switch(entry.getValue().toString()) {
                        case "벨":
                            BUZZER buzzer = new BUZZER();
                            recvMsg = buzzer.conRecvexecute(message, context);
                            break;

                        case "빨간 불":
                        case "파란 불":
                        case "초록 불":
                            LED led = new LED();
                            recvMsg = led.conRecvexecute(message, context);
                            break;

                        case "창문":
                            WINDOW window = new WINDOW();
                            recvMsg = window.conRecvexecute(message, context);
                            break;
                    }
                }
            }
        }
        return recvMsg;
    }

    /**
     * Method sendFilterMessage
     *
     * 사용자가 요청한 메시지를 Tokenize 하여
     * 해당 클래스에 전달 후, 변환된 메시지를 반환
     *
     * 1. 메시지를 받은 후, 키워드를 검사한다.
     * 2. 키워드가 발견되면, 행동 명령을 검사한 뒤, 각 클래스에 보낸다.
     * 3. 각 클래스에서 endTAG 태그가 붙었으면, 종료하지만, 그렇지 않으면
     *    검사를 계속한다.
     * 4. 어디에도 해당되지 않았거나, 행동 명령에 문제가 있을 경우, 완벽한
     *    메시지로 변환되지 않은 상태로 반환된다. 이 경우, 서비스에서 처리한다.
     *
     * @param message 사용자가 요청한 메시지
     * @return 서버로 전송할 메시지
     */
    public String sendFilterMessage(String message) {
        try {
            for(String keyword : measureMap.values()) {
                if(message.contains(keyword)) {
                    switch(keyword) {
                        case "먼지":
                            DUST dust = new DUST(context);
                            if(message.indexOf("얼만큼") > 0 || message.indexOf("얼마") > 0 || message.indexOf("몇") > 0)
                                commandBuilder.append(dust.conSendexecute(null,
                                        DataAlarmCommand.controlType.howMuch, null));
                            else if(message.indexOf("때") > 0 || message.indexOf("면") > 0)
                                commandBuilder.append(dust.conSendexecute(message,
                                        DataAlarmCommand.controlType.when, getMovSensor(message)));
                            break;

                        case "비":
                            RAIN rain = new RAIN(context);
                            if(message.indexOf("오니") > 0 || message.indexOf("오냐") > 0 || message.indexOf("와") > 0)
                                commandBuilder.append(rain.conSendexecute(null,
                                        DataAlarmCommand.controlType.howMuch, null));
                            else if(message.indexOf("면") > 0)
                                commandBuilder.append(rain.conSendexecute(message,
                                        DataAlarmCommand.controlType.when, getMovSensor(message)));
                            break;

                        case "온도":
                            TEM tem = new TEM(context);
                            if (message.indexOf("몇") > 0)
                                commandBuilder.append(tem.conSendexecute(null,
                                        DataAlarmCommand.controlType.howMuch, null));
                            else if(message.indexOf("면") > 0 || message.indexOf("때") > 0)
                                commandBuilder.append(tem.conSendexecute(message,
                                        DataAlarmCommand.controlType.when, getMovSensor(message)));
                            break;

                        case "습도":
                            HUM hum = new HUM(context);
                            if(message.indexOf("몇") > 0)
                                commandBuilder.append(hum.conSendexecute(null,
                                        DataAlarmCommand.controlType.howMuch, null));
                            else if(message.indexOf("면") > 0 || message.indexOf("때") > 0)
                                commandBuilder.append(hum.conSendexecute(message,
                                        DataAlarmCommand.controlType.when, getMovSensor(message)));
                            break;
                        default:
                            Log.e(TAG, "일치하는 어휘가 없음 !");
                            break;
                    }
                }
            }

            if(!commandBuilder.toString().endsWith(BaseCommand.endTAG)) {
                for(String keyword : moveMap.values()) {
                    if(message.contains(keyword) && !commandBuilder.toString().endsWith(BaseCommand.endTAG)) {
                        switch(keyword) {
                            case "빨간 불":
                            case "파란 불":
                            case "초록 불":
                                LED led = new LED();
                                int ledIdx = message.indexOf(keyword);
                                String ledStr = message.substring(ledIdx, ledIdx + 3);
                                if(message.indexOf("켜") > 0 || message.indexOf("키") > 0)
                                    commandBuilder.append(led.conSendexecute(ledStr, OnOffCommand.switchType.On));
                                else if(message.indexOf("꺼") > 0 || message.indexOf("끄") > 0)
                                    commandBuilder.append(led.conSendexecute(ledStr, OnOffCommand.switchType.Off));
                                break;

                            case "창문":
                                WINDOW window = new WINDOW();
                                if(message.indexOf("열") > 0)
                                    commandBuilder.append(window.conSendexecute(MoveCommand.switchType.Open));
                                else if(message.indexOf("닫") > 0)
                                    commandBuilder.append(window.conSendexecute(MoveCommand.switchType.Close));
                                break;

                            case "벨":
                                BUZZER buzzer = new BUZZER();
                                if(message.indexOf("울") > 0 || message.indexOf("켜") > 0) {
                                    int buzzerIdx = message.indexOf(keyword);
                                    String num = message.substring(buzzerIdx, buzzerIdx + 3).replaceAll("\\D", "");
                                    String count = num.equals("") ? String.valueOf(prefManager.getPrefInt("default_buzzer_Count")) : num;
                                    commandBuilder.append(buzzer.conSendexecute(count, OnOffCommand.switchType.On));
                                }
                                else if(message.indexOf("멈") > 0 || message.indexOf("꺼") > 0)
                                    commandBuilder.append(buzzer.conSendexecute(null,
                                            OnOffCommand.switchType.Off));
                                break;
                        }
                    }
                }
            }
        } catch (StringIndexOutOfBoundsException ex) {
            Log.e(TAG, "메시지 필터링 길이 초과 오류 발생 ! (각도 추출 길이 확인 바람)");
        } catch (NullPointerException ex) {
            Log.e(TAG, "메시지 필터링 중, NULL 값 발생 !");
        } finally {
            sendMsg = commandBuilder.toString();
            commandBuilder.setLength(0);
        }
        return sendMsg;
    }

    /**
     * Method getMovSensor
     *
     * 메시지에서 창문/블라인드 등 이동 수단의 센서의
     * 이름을 추출하는 메소드
     *
     * @param msg 사용자가 요청한 메시지
     * @return 센서 이름
     */
    private String getMovSensor(String msg) {
        for(String mov : moveMap.values()) {
            if(msg.contains(mov))
                return mov;
        }
        return null;
    }

    /**
     * Method isCorrectValue
     *
     * 음성으로 가져온 문장 중, 키워드와
     * 가장 일치하는 부분의 문장을 추출
     *
     * @param msg 메시지 내용
     * @return 올바른 문장 판단
     */
    public boolean isCorrectValue(String msg) {
        for(String measure : measureMap.values()) {
            if(msg.contains(measure))
                return true;
        }

        for(String mov : moveMap.values()) {
            if(msg.contains(mov))
                return true;
        }
        return false;
    }
}
