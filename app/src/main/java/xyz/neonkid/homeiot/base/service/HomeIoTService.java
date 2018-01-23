package xyz.neonkid.homeiot.base.service;

import android.support.annotation.WorkerThread;
import android.util.Log;

import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * HomeIoT 서버 송신 서비스 클래스
 * (오직 송신만 가능한 서비스 클래스)
 *
 * @see  MqttService
 *
 * Created by neonkid on 8/12/17.
 */

public abstract class HomeIoTService extends MqttService implements MqttCallbackExtended, IMqttActionListener, IMqttMessageListener {
    private PrefManager prefManager;
    private String ip_addr;
    private String clientID;
    public MqttAsyncClient hIoTAndroid;
    private static final String TAG = "[HOMEIoT_SERVICE]";

    /**
     * Constructor {@link HomeIoTService}
     *
     * MQTT Broker 에 사용할 ID 를 정의
     * @param clientID  사용할 ID
     */
    public HomeIoTService(String clientID) {
        this.clientID = clientID;
    }

    /**
     * HomeIoT 서버 연결 절차 및 연결 옵션
     *
     * Preference 에 정의된 IP 주소로, 연결을 시도하며,
     * 연결이 끊어졌을 경우를 대비해, 자동으로 재연결을 시도한다.
     * 기존 디바이스의 정보 기억을 위해 연결이 끊어진 경우에도 세션은 유지하며,
     * Keep-Alive 는 30으로 지정하였다.
     *
     * MqttVersion 은 3.1.1 버전을 사용한다.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        prefManager = new PrefManager(this);
        ip_addr = prefManager.getPrefString("ip_address").trim().length() > 15 ?
                "tcp://" + prefManager.getPrefString("ip_address") + ":1883" : "tcp://" + prefManager.getPrefString("ip_address");
        MqttConnectOptions hIoTConnectionOptions = new MqttConnectOptions();
        hIoTConnectionOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        hIoTConnectionOptions.setAutomaticReconnect(true);
        hIoTConnectionOptions.setCleanSession(false);
        hIoTConnectionOptions.setKeepAliveInterval(getPrefManager().getPrefInt(getString(R.string.keepAlive_value)));
        hIoTConnectionOptions.setUserName(clientID);
        try {
            hIoTAndroid = new MqttAsyncClient(ip_addr, clientID, new MemoryPersistence());
            hIoTAndroid.connect(null, this);
        } catch (MqttException ex) {
            ex.printStackTrace();
            setToastMessage(ex.getMessage());
        }
    }

    /**
     * 서비스 클래스가 소멸되는 경우, 연결을 끊는다.
     */
    @Override
    public void onDestroy() {
        try {
            hIoTAndroid.disconnect();
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Callback Method connectComplete
     *
     * 재연결 시도시, 호출되는 메소드
     *
     * @param reconnect 재연결 상태 (재연결에 성공했으면 true, 그렇지 않으면 false)
     * @param serverURI 서버 URI 주소
     */
    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if(reconnect)
            Log.d(TAG, "Reconnection Success !!");
        else
            Log.d(TAG, "Connection is established....");
    }

    /**
     * Callback Method connectionLost
     *
     * 연결이 끊어졌을 경우, 호출되는 메소드
     *
     * @param cause 장애 상세 내용
     */
    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "The Connection was lost: " + cause.getCause());
        setToastMessage(cause.getMessage());
        cause.printStackTrace();
    }

    /**
     * Callback Method deliveryComplete
     *
     * 메시지 전송이 성공적으로 이루어지는 경우, 호출되는 메소드
     *
     * @param token 메시지 전송 token 값
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "Sent Message !");
    }

    /**
     * Callback Method onFailure
     *
     * 연결에 실패하는 경우, 호출되는 메소드
     *
     * @param asyncActionToken  연결 Token
     * @param exception Exception 내용
     */
    @WorkerThread
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.e(TAG, "Failed Connection");
    }

    /**
     * Method publishMessage
     *
     * 동기 방식을 사용하여 메시지를 순차적으로 전송 (동시성 방지)
     *
     * @param topic MQTT Topic
     * @param command   전달할 메시지
     * @throws MqttException : 서버와 연결 끊김 등 메시지 전송에 실패하는 경우.
     */
    protected synchronized void publishMessage(String topic, String command) throws MqttException {
        MqttMessage message = new MqttMessage(command.getBytes());
        message.setQos(prefManager.getPrefInt(getString(R.string.qos_value)));
        Log.d(TAG, "Client IP: " + ip_addr);
        Log.d(TAG, "publishTopic: " + topic);
        Log.d(TAG, "publishMessage: " + message);
        hIoTAndroid.publish(topic, message);
    }

    /**
     * Method publishMessage non Topic
     *
     * Topic 을 정하지 않은 경우, 기본 Topic 인 HomeIoT_Server 를 사용.
     *
     * @param command   전달할 메시지
     * @throws MqttException : 서버와 연결 끊김 등 메시지 전송에 실패하는 경우
     */
    protected void publishMessage(String command) throws MqttException {
        publishMessage(getString(R.string.pub_Topic), command);
    }

    /**
     * @see PrefManager
     * @return Preference Manager 객체 반환
     */
    protected final PrefManager getPrefManager() { return prefManager; }

    protected abstract void setToastMessage(String msg);
}
