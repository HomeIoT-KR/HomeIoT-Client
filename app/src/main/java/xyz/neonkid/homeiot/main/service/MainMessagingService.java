package xyz.neonkid.homeiot.main.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.WorkerThread;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.command.BaseCommand;
import xyz.neonkid.homeiot.base.command.DataAlarmCommand;
import xyz.neonkid.homeiot.base.service.HomeIoTService;
import xyz.neonkid.homeiot.main.command.CommandManager;
import xyz.neonkid.homeiot.main.components.notification.NotificationHelper;
import xyz.neonkid.homeiot.main.components.notification.NotificationHelperGreater26;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * HomeIoT 서버와 메시지를 주고 받는 서비스
 *
 * @see HomeIoTService
 * Created by neonkid on 5/15/17.
 */

public class MainMessagingService extends HomeIoTService {
    private static final String TAG = "[MESSAGING_SERVICE]";
    private String[] sensorList;

    public MainMessagingService() {
        super("HomeIoT_Android");
    }

    /**
     * 해당 서비스 클래스와 바인딩 하도록 도와주는 클래스
     */
    public class MqttServiceBinder extends Binder {
        public MainMessagingService getService() { return MainMessagingService.this; }
    }

    /**
     * 상대 Activity / Fragment 클래스의 메소드를
     * 호출하고자 하는 인터페이스
     */
    public interface ICallback {
        void recvMessage(final String message);
        void recvToastMessage(final String message);
    }

    // 인터페이스 객체 생성
    private ICallback mCallback;

    // 해당 서비스에 해당하는 Callback 등록
    public void registerCallback(ICallback callback) {
        mCallback = callback;
    }

    private final IBinder mBinder = new MqttServiceBinder();

    /**
     * Callback Method onCreate
     *
     * 서비스가 최초로 실행되엇을 경우 발생하는 메소드
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if(sensorList == null)
            sensorList = getApplicationContext().getResources().getStringArray(R.array.sensorList);
    }

    /**
     * Callback Method onBind
     *
     * 화면단(Activity, Fragment) 와 바안딩 되었을 경우, 발생하는 메소드
     * (정확히는 bindService() 메소드를 호춯하는 경우 발생)
     * @param intent 바인딩된 Intent
     * @return binder
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind() !");
        return mBinder;
    }

    /**
     * Callback Method onStartCommand
     *
     * startService() 메소드를 호출하는 경우 발생하는 메소드
     * 실제 이 앱에서는 SplashActivity 에서 해당 메소드를 호출
     * 앱이 종료된 이후에도, 서버에서 들어오는 메시지를 수신하기 위해 사용 (Push 알림)
     *
     * 백그라운드에서 앱이 돌고 있음을 알려주기 위해 Notification 사용
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification());
        return START_STICKY;
    }

    /**
     * Method stackVerCheck
     *
     * Task 스케줄러를 사용하여, 메시지를 화면에 표시할지,
     * Push 알림으로 표시할지를 구분해주는 메소드
     *
     * @param message 전달할 메시지
     */
    public void stackVerCheck(String message) {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo>runList = activityManager.getRunningAppProcesses();
        if(runList != null) {
            for(RunningAppProcessInfo rap : runList) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(rap.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                        mCallback.recvMessage(message);
                    else
                        notifyMessageGreater22(message);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(rap.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                        mCallback.recvMessage(message);
                    else
                        notifyMessageGreater26(message);
                } else {
                    if(rap.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                        mCallback.recvMessage(message);
                    else
                        notifyMessageUnder21(message);
                }
            }
        }
    }

    /**
     * Method notifyMessageGreater26
     *
     * Android 8.0 이상 플랫폼에서 Notification 사용
     * @param message 전달할 메시지
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifyMessageGreater26(String message) {
        Uri ringtoneUri = Uri.parse(getPrefManager().getPrefString(getString(R.string.noti_ringtone)));
        NotificationHelperGreater26 helper = new NotificationHelperGreater26(this, ringtoneUri);
        Notification.Builder nb = helper.getNotification(getString(R.string.app_name), message);
        nb.notify();
    }

    /**
     * Method notifyMessageGreater22
     *
     * Android 5.0 이상 플랫폼에서 Notification 사용
     * @param message 전달할 메시지
     */
    @SuppressWarnings("deprecation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void notifyMessageGreater22(String message) {
        Uri ringtoneUri = Uri.parse(getPrefManager().getPrefString(getString(R.string.noti_ringtone)));
        NotificationHelper helper = new NotificationHelper(this);
        helper.setNotificationText(getString(R.string.app_name), message);
        helper.setRingtoneUri(ringtoneUri);
        helper.setCategoryAndVisibility(Notification.CATEGORY_MESSAGE, Notification.VISIBILITY_PUBLIC);

        if(getPrefManager().getPrefBoolean(getString(R.string.noti_vibrate)))
            helper.setVibrate();
        helper.notify(1412);
    }

    /**
     * Method notifyMessageUnder21
     *
     * Android 5.0 이하 플랫폼에서 Notification 사용
     * @param message 전달할 메시지
     */
    @SuppressWarnings("deprecation")
    private void notifyMessageUnder21(String message) {
        Uri ringtoneUri = Uri.parse(getPrefManager().getPrefString(getString(R.string.noti_ringtone)));
        NotificationHelper helper = new NotificationHelper(this);
        helper.setNotificationText(getString(R.string.app_name), message);
        helper.setRingtoneUri(ringtoneUri);

        if(getPrefManager().getPrefBoolean(getString(R.string.noti_vibrate)))
            helper.setVibrate();
    }

    /**
     * Callback Method onSuccess
     *
     * HomeIoT 서버와 성공적으로 연결된 경우..
     * @param asyncActionToken mqttToken
     */
    @WorkerThread
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        Log.d(TAG, "onSuccess");
        hIoTAndroid.setCallback(this);
        for(final String sensor : sensorList) {
            try {
                hIoTAndroid.subscribe(sensor, getPrefManager().getPrefInt(getString(R.string.qos_value)), this);
            } catch (Exception ex) {
                ex.printStackTrace();
                setToastMessage(ex.getMessage());
            }
        }
    }

    /**
     * Callback Method messageArrived
     *
     * HomeIoT 서버로부터 메시지를 받은 경우..
     * @param topic 메시지가 들어온 topic
     * @param message topic 에 해당하는 메시지
     * @throws Exception 연결이 끊어지거나, 메시지를 받는 중 오류가 발생한 경우
     */
    @WorkerThread
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(TAG, new String(message.getPayload()));
        PrefManager pref = new PrefManager(this);
        String[] moveList = getApplicationContext().getResources().getStringArray(R.array.moveCommand);
        switch(topic) {
            case "HomeIoT_Return":
                stackVerCheck(getRecvFilterMessage(new String(message.getPayload())));
                break;
            default:
                if(topic.contains(getApplication().getResources().getString(R.string.app_name))) {
                    pref.putPrefString(topic, new String(message.getPayload()));    // 서버에서 받아오는 센서 값 저장... (모니터링용...)
                    for(String sensor : moveList)
                        sensorTopicCheck(pref, sensor, topic);
                }
                break;
        }
    }

    /**
     * Method sensorTopicCheck
     *
     * 예약된 Preference 값들을 확인하여,
     * 모니터링하고 있는 센서 값들과 비교하여 메시지 전송
     *
     * 메시지 전송 이후에는 예약된 Preference 값 초기화 (필수)
     *
     * Topic: ($SENSOR_NAME)-($MOVE_SENSOR)-Rval
     * example: Rain-LED_R-RAct
     *
     * Topic 을 받은 후, 해당 Topic 의 Preference 에 값 존재 여부로,
     * 예약 메시지 전송 결정
     *
     * @see xyz.neonkid.homeiot.base.command.DataAlarmCommand
     * @param pref Preference Manager
     * @param sensor 센서 이름
     * @param topic 서버 topic
     * @throws Exception 메시지 전송 중 오류가 발생한 경우
     */
    private void sensorTopicCheck(PrefManager pref, String sensor, String topic) throws Exception {
        String prefVAL = topic.substring(topic.indexOf("_") + 1) + "-" + sensor + DataAlarmCommand.reserveFlag.RESV.getCode();
        String prefACT = topic.substring(topic.indexOf("_") + 1) + "-" + sensor + DataAlarmCommand.reserveFlag.RACT.getCode();
        String origin_sensor = pref.getPrefString(topic);
        String resv_sensor = pref.getPrefString(prefVAL);
        if(origin_sensor != null && resv_sensor != null && !origin_sensor.equals("") && !resv_sensor.equals("")) {
            if(Math.abs(Float.parseFloat(origin_sensor)) == Math.abs(Float.parseFloat(resv_sensor))) {
                getsendFilterMessage(pref.getPrefString(prefACT));
                pref.putPrefString(prefVAL, "");
                pref.putPrefString(prefACT, "");
            }
        }
    }

    @Override
    protected void setToastMessage(String msg) {
        mCallback.recvToastMessage(msg);
    }

    /**
     * Method getsendFilterMessage
     *
     * 사용자가 전달한 자연어 메시지를
     * 신호처리된 메시지로 변환 후,서버로 전송
     *
     * 각 센서 태그별로, 다르게 처리함
     *
     * @see CommandManager
     * @param message 사용자 메시지
     */
    public void getsendFilterMessage(String message) {
        CommandManager commandManager = new CommandManager(this);
        String finMsg = commandManager.sendFilterMessage(message);
        try {
            if(finMsg.indexOf(CommandManager.sendHeader) == 0 && finMsg.contains(BaseCommand.endTAG)) {
                if(finMsg.contains("<DUST>") || finMsg.contains("<TEMPERATURE>") || finMsg.contains("<HUMIDITY>") || finMsg.contains("<RAIN>"))
                    stackVerCheck(getRecvFilterMessage(finMsg));
                else {
                    if(finMsg.contains(DataAlarmCommand.reserveFlag.REST.getCode()))
                        stackVerCheck(getString(R.string.success_Reserve));
                    else
                        publishMessage(finMsg);
                }
            }
            else
                stackVerCheck(getString(R.string.recongnize_Error));
        } catch (MqttException ex) {
            ex.printStackTrace();
            stackVerCheck(getString(R.string.send_Error));
        }
    }

    /**
     * Method getPrefsendMessage
     *
     * 수동 제어에서 보내는 메시지를 변환하고,
     * 서버로 전송하기 위한 메소드
     *
     * 각 센서 태그별로, 다르게 처리함
     *
     * @param message 제어 메시지
     */
    public void getPrefsendMessage(String message) {
        CommandManager commandManager = new CommandManager(this);
        try {
            String sendMsg = commandManager.sendFilterMessage(message);
            if(sendMsg.indexOf(CommandManager.sendHeader) == 0 && sendMsg.contains(BaseCommand.endTAG)) {
                if(sendMsg.contains("<LED_R>") || sendMsg.contains("<LED_G>") || sendMsg.contains("<LED_B>") ||
                        sendMsg.contains("<WINDOWS>") || sendMsg.contains("<BUZZER>"))
                    publishMessage(sendMsg);
                else
                    setToastMessage(getString(R.string.success_Reserve));
            }
            else
                setToastMessage(getString(R.string.fail_Reserve));
        } catch (MqttException ex) {
            ex.printStackTrace();
            setToastMessage(ex.getMessage());
        }
    }

    /**
     * Method getRecvFilterMessage
     *
     * 서버로부터 받은 결과 메시지를
     * 자연어 메시지로 변환하는 메소드
     *
     * @param message 서버 수신 메시지
     */
    public String getRecvFilterMessage(String message) {
        CommandManager commandManager = new CommandManager(this);
        String msg = commandManager.recvFilterMessage(message);
        try {
            if(msg.equals(""))
                return getString(R.string.fail_Reserve);
            else
                return msg;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return getString(R.string.fail_Reserve);
        }
    }
}
