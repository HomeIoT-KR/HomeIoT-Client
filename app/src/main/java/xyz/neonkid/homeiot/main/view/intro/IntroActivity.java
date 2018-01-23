package xyz.neonkid.homeiot.main.view.intro;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import butterknife.BindArray;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BaseIntroActivity;
import xyz.neonkid.homeiot.base.view.BaseSlide;
import xyz.neonkid.homeiot.main.presenter.intro.IntroPresenter;
import xyz.neonkid.homeiot.main.presenter.view.intro.IntroPresenterView;
import xyz.neonkid.homeiot.main.service.MainMessagingService;
import xyz.neonkid.homeiot.main.view.MainActivity;

/**
 * HomeIoT 소개 화면 Activity
 *
 * @see BaseIntroActivity
 *
 * Created by neonkid on 7/24/17.
 */

public class IntroActivity extends BaseIntroActivity implements IntroPresenterView {
    private IntroPresenter introPresenter;

    @BindArray(R.array.intro_Title)
    String[] introTitle;

    @BindArray(R.array.intro_Json)
    String[] introJson;

    @BindArray(R.array.intro_Desc)
    String[] introDesc;

    /**
     * 각 슬라이드에 들어갈 내용을 res/arrays.xml 파일에 정의
     * 마지막 바로 이전의 슬라이드를 IP 설정 슬라이드로 정의하였음 (항상 고정 권장)
     *
     * 음성 인식 부분 소개 화면에서 마이크 권한을 요구하도록 하였음
     * swipe 로 넘길 수 있도록 구현
     */
    @Override
    protected void onCreate() {
        introPresenter = new IntroPresenter(this);
        for(int i = 0; i < introJson.length; i++) {
            if(i == introJson.length - 2)
                introPresenter.createInputSlide(introTitle[i], introJson[i], introDesc[i]);
            else
                introPresenter.createLottieSlide(introTitle[i], introJson[i], introDesc[i]);
        }
        askForPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, introJson.length - 2);
        setSwipeLock(false);
        showSkipButton(false);
    }

    /**
     * Callback Method onSkipPressed
     *
     * 만약, skip 버튼을 터치했을 경우, Activity 가 종료되도록 함
     * @param currentFragment 현재 Fragment
     */
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    /**
     * Callback Method onDonePressed
     *
     * 설정 완료 버튼을 눌럿을 경우,
     * 서버와 연결이 되어 있자 않다면, 다시 시도 or 연결된 경우, MainActivity 로 이동
     *
     * MainActivity 로 이동할 때, MainMessagingService 가 백그라운드에서 동작하도록 함
     *
     * @param currentFragment 현재 Fragment
     */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(pref.getBoolean(getString(R.string.hIoT_init), false)) {
            startService(new Intent(this, MainMessagingService.class));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else
            pager.setCurrentItem(introJson.length - 2);
    }

    /**
     * PresenterView Method addLottieSlide
     *
     * Presenter 에서 View 작업을 할 수 있도록 구현
     * @param slide 추가할 슬라이드
     */
    @Override
    public void addLottieSlide(BaseSlide slide) {
        addSlide(slide);
    }
}
