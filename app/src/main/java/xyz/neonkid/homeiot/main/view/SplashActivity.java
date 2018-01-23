package xyz.neonkid.homeiot.main.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.UiThread;

import com.facebook.stetho.Stetho;

import xyz.neonkid.homeiot.base.view.BaseActivity;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.presenter.SplashPresenter;
import xyz.neonkid.homeiot.main.presenter.view.SplashPresenterView;
import xyz.neonkid.homeiot.main.service.MainMessagingService;
import xyz.neonkid.homeiot.main.view.intro.IntroActivity;

/**
 * HomeIoT Splash 화면 (앱 실행시, 맨 처음 나타나는 로고 화면)
 * @see BaseActivity
 *
 * Created by neonkid on 5/16/17.
 */

public class SplashActivity extends BaseActivity implements SplashPresenterView {

    @Override
    protected int getContentLayoutResource() { return R.layout.activity_splash; }

    /**
     * 서버 IP 주소를 확인합니다.
     * 존재 하지 않는 경우, 설정할 수 있도록 앱 소개 화면이 나타납니다.
     *
     * Facebook Stetho 를 사용하여, Preference, SQLite 를 추적할 수 있습니다.
     * 사용하려면 주석을 해제하시면 됩니다.
     * (이 부분은 코드 테스트용으로민 사용하시기 바랍니다.)
     */
    @Override
    protected void onCreate() {
        // Stetho.initializeWithDefaults(this);
        SplashPresenter splashPresenter = new SplashPresenter(this);
        splashPresenter.checkIP(this);
    }

    @Override
    protected int getToolbarTitle() {
        return 0;
    }

    /**
     * PresenterView Method loadApp
     *
     * MainActivity 가 띄워지는 메소드
     */
    @Override
    public void loadApp() {
        if(!isServiceRunning())
            startService(new Intent(SplashActivity.this, MainMessagingService.class));
        loadActivity(MainActivity.class, 1100);
    }

    /**
     * PresenterView Method loadIntro
     *
     * IntroActivity 가 띄워지는 메소드
     */
    @Override
    public void loadIntro() {
        loadActivity(IntroActivity.class, 1200);
    }

    /**
     * Method isServiceRunning
     *
     * MainMessagingService 가 백그라운드에서 작동 중인지 확인합니다
     * @return 작동 중이면 true, 그렇지 않으면 false 반환
     */
    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager)this.getSystemService(Activity.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if("xyz.neonkid.homeiot.main.service.MainMessagingService".equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    /**
     * Method loadActivity
     *
     * Activity 를 불러오는 메소드
     * 반드시 UI Thread 에서 동작 해야합니다.
     *
     * @param activity 실행할 Activity
     * @param delay delay 시간
     */
    @UiThread
    private void loadActivity(Class<?> activity, int delay) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, activity);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, delay);
    }
}
