package xyz.neonkid.homeiot.main.adapter.model.sensor;

/**
 * Created by neonkid on 8/20/17.
 */

import android.support.annotation.NonNull;

public interface SensorModel {
    void addStatus(@NonNull SensorStat object);
}
