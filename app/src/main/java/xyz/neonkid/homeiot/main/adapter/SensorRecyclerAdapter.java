package xyz.neonkid.homeiot.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorModel;
import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorStat;
import xyz.neonkid.homeiot.main.adapter.view.SensorRecyclerView;
import xyz.neonkid.homeiot.base.adapter.BaseRecyclerAdapter;
import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;

/**
 * Created by neonkid on 8/20/17.
 */

public class SensorRecyclerAdapter extends BaseRecyclerAdapter<SensorStat> implements SensorModel {
    public SensorRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SensorRecyclerView(parent, this);
    }

    @Override
    protected void addItem(SensorStat data) {
        addItem(data, true);
    }

    @Override
    public void addStatus(@NonNull SensorStat object) {
        addItem(object);
    }

    public void clear() {
        getItemList().clear();
    }
}
