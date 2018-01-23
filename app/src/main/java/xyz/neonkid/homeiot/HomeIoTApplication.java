package xyz.neonkid.homeiot;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * HomeIoT 앱 클래스
 *
 * Created by neonkid on 9/21/17.
 */

public class HomeIoTApplication extends Application {

    /**
     * LeakCanary 를 사용하여, 메모리 누수를 검사하고자 하는 경우,
     * LeakCanary.install 주석을 해제하여 주세요.
     *
     * (이 부분은 코드 테스트용으로민 사용하시기 바랍니다.)
     */
    @Override
    public void onCreate() {
        super.onCreate();
        // LeakCanary.install(this);
    }
}
