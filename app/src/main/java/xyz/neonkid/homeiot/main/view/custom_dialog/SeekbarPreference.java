package xyz.neonkid.homeiot.main.view.custom_dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BaseDialog;

/**
 * 각 수동 제어 Activity 에서,
 * 먼지, 온도 센서 등 데이터 수치를 필요로 하는 센서들의
 * 값 설정을 위한 컴포넌트 클래스
 *
 * Created by neonkid on 7/26/17.
 */

public class SeekbarPreference extends BaseDialog implements SeekBar.OnSeekBarChangeListener, ViewTreeObserver.OnPreDrawListener {
    private static final String androidns = "http://schemas.android.com/apk/res/android";

    private SeekBar mSeekBar;
    private TextView mSplashText, mValueText;
    private Context mContext;

    private String mDialogMessage, mSuffix;
    private int mDefault, mMax, mMin, mValue = 0;

    /**
     * Constructor
     *
     * Dialog 화면에 나타날 타이틀, 설명, 기본값, 최대값을 지정
     *
     * @param context 현재 Activity
     * @param attrs xml namespace 에서 지정한 값
     */
    public SeekbarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
        mSuffix = attrs.getAttributeValue(androidns, "message");
        mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", 0);
        mMin = attrs.getAttributeIntValue(androidns, "min", 0);
        mMax = attrs.getAttributeIntValue(androidns, "max", 100);
    }

    /**
     * 다이얼로그 뷰에 대한 속성 값 지정
     *
     * Seekbar 에 보여질, 수치 값의 폰트 크기를 고정
     * @return dialog 뷰 반환
     */
    @Override
    protected View onCreateDialogView() {
        LinearLayout.LayoutParams params;
        LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(6, 6, 6, 6);

        mSplashText = new TextView(mContext);
        if(mDialogMessage != null)
            mSplashText.setText(mDialogMessage);
        layout.addView(mSplashText);

        mValueText = new TextView(mContext);
        mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
        mValueText.setTextSize(32);

        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mValueText, params);

        mSeekBar = new SeekBar(mContext);

        mSeekBar.incrementProgressBy(1);
        mSeekBar.setOnSeekBarChangeListener(this);
        layout.addView(mSeekBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        if(shouldPersist())
            mValue = getPersistedInt(mDefault);

        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
        mSeekBar.getViewTreeObserver().addOnPreDrawListener(this);

        return layout;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mValue);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mSeekBar.setMin(mMin);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if(restorePersistedValue)
            mValue = shouldPersist() ? getPersistedInt(mDefault) : 0;
        else
            mValue = (Integer)defaultValue;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text = String.valueOf(progress);
        mValueText.setText(mSuffix == null ? text : text.concat(mSuffix));
        if(shouldPersist())
            persistInt(progress);
        callChangeListener(progress);
    }

    @Override
    public boolean onPreDraw() {
        if(mSeekBar.getHeight() > 0) {
            int height = mSeekBar.getMeasuredHeight() * 2;
            int width = height;

            Bitmap bmpOrg = ((BitmapDrawable)getThumb()).getBitmap();
            Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, width, height, true);
            Drawable newThumb = new BitmapDrawable(getContext().getResources(), bmpScaled);

            newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
            mSeekBar.setThumb(newThumb);
            mSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            mSeekBar.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        return true;
    }

    public void setMax(int max) { mMax = max; }

    public void setMin(int min) { mMin = min; }

    public int getMax() { return mMax; }

    public void setProgress(int progress) {
        mValue = progress;
        if(mSeekBar != null)
            mSeekBar.setProgress(progress);
    }

    public int getProgress() { return mValue; }

    private Drawable getThumb() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
            return getContext().getDrawable(R.mipmap.app_icon);
        else
            return getContext().getResources().getDrawable(R.mipmap.app_icon);
    }
}
