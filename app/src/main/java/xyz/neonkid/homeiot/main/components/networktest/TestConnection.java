package xyz.neonkid.homeiot.main.components.networktest;

import android.support.annotation.WorkerThread;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by neonkid on 11/21/17.
 */

public class TestConnection {
    private String inputIP;

    public TestConnection(String ip_address) {
        inputIP = ip_address.trim().length() > 15 ? "tcp://" + ip_address + ":1883" : "tcp://" + ip_address;
    }

    @WorkerThread
    public String connect() {
        try {
            MqttClient testClient = new MqttClient(inputIP, "Sample", new MemoryPersistence());
            MqttConnectOptions conOpts = new MqttConnectOptions();
            conOpts.setCleanSession(true);
            testClient.connect();
            return "OK";
        } catch (MqttException | IllegalArgumentException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}
