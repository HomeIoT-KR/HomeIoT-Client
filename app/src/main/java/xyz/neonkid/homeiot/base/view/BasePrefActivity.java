package xyz.neonkid.homeiot.base.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.presenter.view.BasePrefPresenterView;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;
import xyz.neonkid.homeiot.main.service.MainMessagingService;

/**
 * 수동 제어 Activity 기본 공통 클래스
 *
 * PreferenceActivity 를 상속하였으며, 이 Activity 는 기본적으로 Fragment 와
 * 같이 사용하기 때문에, 같이 사용할 Fragment 를 템플릿에 넣도록 정의하였다
 *
 * @see BasePrefFragment
 *
 * Created by neonkid on 5/21/17.
 */

public abstract class BasePrefActivity<F extends BasePrefFragment> extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener, BasePrefPresenterView {
    protected MainMessagingService mService;
    protected PrefManager prefManager;

    /**
     * 수동 제어에 해당하는 모든 Activity 들은, 제어 메시지 전송을 위해,
     * MainMessagingService 와 Binding 되도록 설계
     *
     * MainActivity 와 마찬가지로, AIDL 인터페이스 채택
     * 그러나, Service 에서 Activity 의 메소드를 호출하지 않을 것이므로,
     * Interface Callback 은 사용하지 않음
     *
     * @see xyz.neonkid.homeiot.main.view.MainActivity
     */
    protected ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MainMessagingService.MqttServiceBinder binder = (MainMessagingService.MqttServiceBinder)service;
            mService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) { mService = null; }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setToolbar();
    }

    /**
     * Preference 값을 주고 받고, 읽고 쓰고 하는 동작이므로,
     * PrefManager 객체를 생성,
     *
     * 템플릿에 정의된 Fragment 만을 사용하도록 설계, 스타일은 BaseActivity 와 동일한 Window 적용
     *
     * @param savedInstanceState 자원 저장 값
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        View view = getWindow().getDecorView();
        view.setPadding(0, 80, 0, 0);
        onCreate();
        getFragmentManager().beginTransaction().replace(android.R.id.content, getFragment()).commit();
    }

    /**
     * Fragment xml Resource 정의. 태블릿 등의 화면이 큰 디바이스를 위해 정의
     * Android 3.0 이상의 플랫폼부터 필수 적용
     *
     * @param target Android Platforms
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(getContentXmlResource(), target);
    }

    /**
     * Activity 가 띄워지는 경우, MainMessagingService 와 바인딩을 시도.
     * 사용자가 값을 변경할 경우에 반영한 값을 적용할 수 있도록,
     * onSharedPreferenceChangeListener 등록
     */
    @Override
    protected void onResume() {
        super.onResume();
        prefManager.registerOnSharedPreferenceChangeListener(this);
        bindService(new Intent(this, MainMessagingService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Activity 에서 빠져나가는 경우, MainMessagingService 와 바인딩 해제,
     * 마찬가지로, onSharedPreferenceChangeListener 도 해제해줘야 한다.
     */
    @Override
    protected void onPause() {
        super.onPause();
        prefManager.unregisterOnSharedPreferenceChangeListener(this);
        unbindService(mConnection);
    }

    /**
     * Abstract Method getContentXmlResource
     *
     * 각 Activity 별 XmlResource 지정 메소드
     *
     * @return XmlResource
     */
    @XmlRes
    protected abstract int getContentXmlResource();

    /**
     * 수동 제어 Activity 에서는 xml 에서 정의하지 않아도,
     * 반드시 Toolbar 을 사용하도록 정의, 뒤로 버튼을 누르면,
     * Activity 를 종료하도록 함
     */
    protected void setToolbar() {
        LinearLayout root = (LinearLayout)findViewById(android.R.id.content).getParent();
        Toolbar toolbar = (Toolbar)getLayoutInflater().inflate(R.layout.manual_toolbar, root, false);
        if(toolbar != null) {
            if(getToolbarTitle() > 0)
                toolbar.setTitle(getToolbarTitle());
            root.addView(toolbar, 0);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    /**
     * Abstract Method getToolbarTitle
     *
     * 사용할 툴바 제목 정의
     *
     * @return Toolbar 제목 (String 형 x, StringRes 형태 사용)
     */
    @StringRes
    protected abstract int getToolbarTitle();

    /**
     * Callback Method onSharedPreferenceChanged
     *
     * Preference 의 내용이 변경되는 경우 호출되는 메소드
     *
     * @param sharedPreferences 대상 preference
     * @param key 설정 key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = getFindPreference(key);
        onSharedPreferenceChangeListener(pref, key);
    }

    /**
     * Callback Method isValidFragment
     *
     * 유효 Fragment 인지 확인하는 메소드
     * Fragment 가 유효하지 않거나 해당 Activity 와 무관하다면, 오류 발생
     *
     * @param fragmentName 템플릿 F Fragment
     * @return 대상 Activity 와 Fragment 가 불일치 한경우, false 일차하면 true 를 반환
     */
    @Override
    protected final boolean isValidFragment(String fragmentName) { return getFragment().getClass().getName().equals(fragmentName); }

    /**
     * Template 에 정의한 Fragment 를 반환하는 메소드
     * 반드시 BasePrefFragment 를 상속한 Fragment 만 가능함
     *
     * @return extends BasePrefFragment
     */
    protected abstract F getFragment();

    /**
     * Abstract Method onSharedPreferenceChangeListener
     *
     * onSharedPreferenceChanged Callback Method 이후에 정의할 메소드
     *
     * @param pref 변경 확인 대상 Preference
     * @param key 대상 Preference 에 해당하는 key
     */
    protected abstract void onSharedPreferenceChangeListener(Preference pref, String key);

    /**
     * Readonly Method getFindPreference
     *
     * key 값에 해당하는 Preference 를 반환하는 메소드
     *
     * @param key 설정 Preference key
     * @return Preference 반환
     */
    protected final Preference getFindPreference(String key) { return getFragment().findPreference(key); }

    /**
     * Readonly Method changePreferences
     *
     * 사용자가 Preference 버튼을 클릭하는 경우,
     * 그에 해당하는 메시지를 CommandManager 에게 보내어, 메시지 변환 후,
     * 서버로 메시지 전송
     *
     * @param sendMsg 보낼 메시지
     * @param pref 변경 대상 Preference
     * @param summary 바뀔 summary 내용
     * */
    @Override
    public final void changePreferences(String sendMsg, Preference pref, String summary) {
        mService.getPrefsendMessage(sendMsg);
        pref.setSummary(summary);
    }

    protected abstract void onCreate();

    protected final void setToastMessage(String message) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }
}
