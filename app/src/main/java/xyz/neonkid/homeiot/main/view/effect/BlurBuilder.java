package xyz.neonkid.homeiot.main.view.effect;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * 블러 효과를 입히는 클래스
 *
 * Created by neonkid on 7/11/17.
 */

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.3f;
    private static final float BLUR_RADIUS = 25.0f;

    /**
     * 블러 효과를 줍니다.
     * 이 메소드는 Android 4.2 버전 이상에서 동작합니다
     *
     * @param context 현재 Activity
     * @param image 효과를 줄 이미지
     * @return 블러 효과가 적용된 이미지를 Bitmap 형태로 반환
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputImage = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap resultImage = Bitmap.createBitmap(inputImage);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputImage);
        Allocation tmpOut = Allocation.createFromBitmap(rs, resultImage);

        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(resultImage);

        return resultImage;
    }
}
