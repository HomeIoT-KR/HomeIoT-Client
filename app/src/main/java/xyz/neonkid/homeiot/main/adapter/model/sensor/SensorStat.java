package xyz.neonkid.homeiot.main.adapter.model.sensor;

import android.support.annotation.DrawableRes;

/**
 * 센서 모니터링 View 에 나오는 센서 Model
 *
 * Created by neonkid on 8/20/17.
 */

public class SensorStat {
    // 센서 모니터링에 보여질 각 센서 이미지
    @DrawableRes public final int img;

    // 센서에 들어가는 정보
    public final String message;

    // 뷰에 보여지는 센서 이름
    public final String title;

    // 센서 부가 설명란
    public final String comment;

    public SensorStat(String title, String message, @DrawableRes int img, String comment) {
        this.title = title;
        this.message = message;
        this.img = img;
        this.comment = comment;
    }
}
