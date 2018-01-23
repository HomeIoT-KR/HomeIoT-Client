package xyz.neonkid.homeiot.main.adapter.view;

import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.SensorRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.sensor.SensorStat;
import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;

/**
 * Created by neonkid on 8/20/17.
 */

public class SensorRecyclerView extends BaseRecyclerView<SensorRecyclerAdapter, SensorStat> {
    private static int DURATION = 500;

    @BindView(R.id.sensorTitle)
    TextView sensorTitle;

    @BindView(R.id.sensorImage)
    ImageView sensorImage;

    @BindView(R.id.sensorData)
    TextView sensorData;

    @BindView(R.id.sensorComment)
    TextView sensorComment;

    public SensorRecyclerView(ViewGroup parent, SensorRecyclerAdapter adapter) {
        super(R.layout.content_sensorcard, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable SensorStat item, final int position) {
        Animation right_Anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        right_Anim.setDuration(DURATION);
        Animation left_Anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
        left_Anim.setDuration(DURATION);

        if(item != null) {
            sensorTitle.setText(item.title);
            sensorImage.setBackgroundResource(item.img);
            sensorData.setText(item.message);
            sensorComment.setText(item.comment);
            sensorTitle.startAnimation(right_Anim);
            sensorData.startAnimation(left_Anim);
            sensorComment.startAnimation(left_Anim);
        }
    }
}
