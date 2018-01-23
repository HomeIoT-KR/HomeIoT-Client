package xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.left;

import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorStat;
import xyz.neonkid.homeiot.base.presenter.view.BasePresenterView;

/**
 * Created by neonkid on 8/2/17.
 */

public interface MonitoringPresenterView extends BasePresenterView {
    void recvView(SensorStat stat);
    void setErrorMessage(String message);
}
