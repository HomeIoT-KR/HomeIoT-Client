package xyz.neonkid.homeiot.base.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro2;

import butterknife.ButterKnife;

/**
 * HomeIoT 소개 화면 Activity 기본 공통 클래스
 *
 * @see AppIntro2
 *
 * Created by neonkid on 7/24/17.
 */

public abstract class BaseIntroActivity extends AppIntro2 {

    /**
     * 별도의 LayoutResource 는 사용하지 않으며,
     * 스타일의 일관성을 위해, BaseActivity 와 마찬가지로, FLAG_LAYOUT_NO_LIMITS 를 사용
     *
     * 나머지는 BaseActivity 와 비슷한 설계를 유지
     *
     * @see BaseActivity
     * @param savedInstanceState 자원 저장
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        onCreate();
    }

    /**
     * Callback Method onDonePressed
     *
     * App 소개 화면에서, 완료 버튼을 눌렀을 경우에 호출되는 메소드
     *
     * @param currentFragment 현재 Fragment
     */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
    }

    /**
     * Callback Method onSlideChanged
     *
     * App 소개 화면이 바뀌는 경우, 호출되는 메소드
     *
     * @param oldFragment   이전 Fragment
     * @param newFragment   다음 Fragment
     */
    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }

    protected abstract void onCreate();
}
