package xyz.neonkid.homeiot.main.view.floatButton_menu.left.activities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import butterknife.BindArray;
import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.SensorRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorStat;
import xyz.neonkid.homeiot.base.view.BaseActivity;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;
import xyz.neonkid.homeiot.main.presenter.floatButton_menu.left.MonitoringPresenter;
import xyz.neonkid.homeiot.main.presenter.view.floatButton_menu.left.MonitoringPresenterView;

/**
 * 센서 모니터링 Activity
 *
 * Created by neonkid on 8/1/17.
 */

public class MonitoringActivity extends BaseActivity implements MonitoringPresenterView, SwipeRefreshLayout.OnRefreshListener {
    private MonitoringPresenter monitoringPresenter;
    private SensorRecyclerAdapter sensorRecyclerAdapter;

    @BindView(R.id.swipe_monitoring)
    SwipeRefreshLayout swipemonitoring;

    @BindView(R.id.monitoring_view)
    RecyclerView monitorview;

    @BindArray(R.array.sensorList)
    String[] sensorList;

    protected int getContentLayoutResource() {
        return R.layout.activity_monitoring;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.monitoring_Menu;
    }

    @Override
    protected void onCreate() {
        view.setPadding(0, 80, 0, 0);
        sensorRecyclerAdapter = new SensorRecyclerAdapter(this);
        monitorview.setAdapter(sensorRecyclerAdapter);
        swipemonitoring.setOnRefreshListener(this);
        swipemonitoring.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        monitoringPresenter = new MonitoringPresenter(this, this);
    }

    /**
     * 화면을 다시 불러올 경우, 센서 값 갱신
     */
    @Override
    protected void onResume() {
        super.onResume();
        refreshSensorvalue();
    }

    /**
     * Swipe 할 경우, 센서 값 갱신
     */
    @Override
    public void onRefresh() {
        swipemonitoring.setRefreshing(true);
        new Handler().postDelayed(() -> {
            refreshSensorvalue();
            swipemonitoring.setRefreshing(false);
        }, 2000);
    }

    /**
     * PresenterView Method recvView
     *
     * 센서 값을 가져온 뒤, View 에 렌더링
     * @param stat 센서 상태 모델
     */
    @Override
    public void recvView(SensorStat stat) {
        runOnUiThread(() -> sensorRecyclerAdapter.addStatus(stat));
    }

    /**
     * PresenterView Method setErrorMessage
     *
     * 토스트 메시지 형태로, 오류가 발생한 경우,
     * 내용을 표시해줍니다.
     * @param message 오류 메시지
     */
    @Override
    public void setErrorMessage(String message) {
        runOnUiThread(() -> setToastMessage(message));
    }

    private void refreshSensorvalue() {
        PrefManager pref = new PrefManager(this);
        sensorRecyclerAdapter.clear();
        for(String sensor : sensorList)
            monitoringPresenter.dataCheck(pref.getPrefString(sensor), sensor.substring(sensor.indexOf("_") + 1));
    }
}
