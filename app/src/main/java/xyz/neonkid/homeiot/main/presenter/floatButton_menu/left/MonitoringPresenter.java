package xyz.neonkid.homeiot.main.presenter.floatButton_menu.left;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorStat;
import xyz.neonkid.homeiot.base.presenter.BasePresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.left.MonitoringPresenterView;
import xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities.MonitoringActivity;

/**
 * MonitoringActivity 의 Presenter
 *
 * Created by neonkid on 8/2/17.
 */

public class MonitoringPresenter extends BasePresenter<MonitoringPresenterView, MonitoringActivity> {
    public MonitoringPresenter(MonitoringPresenterView view, MonitoringActivity context) {
        super(view, context);
    }

    /**
     * Method dataCheck
     *
     * 센서 데이터의 값을 확인하여, 데이터에 해당하는 이미지를
     * 넣어준 후, 상태를 모델링하여, View 에게 넘겨줍니다
     *
     * @param message 센서 값
     * @param topic 센서
     */
    public void dataCheck(String message, String topic) {
        try {
            switch(topic) {
                case "Temperature":
                    if(Float.parseFloat(message) > 28)
                        getView().recvView(new SensorStat(topic, message + " °C", R.drawable.high_temperature, getContext().getString(R.string.high_temperature)));
                    else
                        getView().recvView(new SensorStat(topic, message + " °C", R.drawable.normal_temperature, getContext().getString(R.string.normal_temperature)));
                    break;
                case "Dust":
                    if(Float.parseFloat(message) > 30)
                        getView().recvView(new SensorStat(topic, message + " ㎍/㎥", R.drawable.high_dust, getContext().getString(R.string.high_dust)));
                    else
                        getView().recvView(new SensorStat(topic, message + " ㎍/㎥", R.drawable.normal_dust, getContext().getString(R.string.normal_dust)));
                    break;
                case "Humidity":
                    if(Float.parseFloat(message) > 80)
                        getView().recvView(new SensorStat(topic, message + " %", R.drawable.normal_humidity, getContext().getString(R.string.normal_humidity)));
                    else
                        getView().recvView(new SensorStat(topic, message + " %", R.drawable.normal_humidity, getContext().getString(R.string.normal_humidity)));
                    break;
                case "Rain":
                    if(Float.parseFloat(message) == 0)
                        getView().recvView(new SensorStat("Weather", getContext().getString(R.string.weather_sunny_Title), R.drawable.sunny_icon, getContext().getString(R.string.weather_sunny)));
                    else
                        getView().recvView(new SensorStat("Weather", getContext().getString(R.string.weather_rainny_Title), R.drawable.rainy_icon, getContext().getString(R.string.weather_rainny)));
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException ex) {
            getView().setErrorMessage(getContext().getString(R.string.monitoring_error));
            ex.printStackTrace();
        }
    }
}
