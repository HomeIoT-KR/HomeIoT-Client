package xyz.neonkid.homeiot.main.view.effect;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

/**
 * 이미지에 어두운 효과를 주는 클래스
 *
 * Created by neonkid on 7/15/17.
 */

public class DarknessBuilder {
    private final static PorterDuffColorFilter darkness = new PorterDuffColorFilter(Color.parseColor("#E0E0E0"), PorterDuff.Mode.MULTIPLY);

    public static void darkness(Bitmap originalImage) {
        Canvas canvas = new Canvas();
        canvas.setBitmap(originalImage);    // 캔버스 위에 darknessImage 를...

        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setColorFilter(darkness);

        canvas.drawBitmap(originalImage, 0, 0, paint);  // 인자로 준 이미지에 darkness 를 씌움.
    }
}
