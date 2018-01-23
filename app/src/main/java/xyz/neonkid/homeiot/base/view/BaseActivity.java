package xyz.neonkid.homeiot.base.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.neonkid.homeiot.R;

/**
 * HomeIoT App 에서 사용할 기본 공통 Activity 클래스
 *
 * Created by neonkid on 5/15/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public View view;

    /**
     * Layout 형태는 추상 메소드를 이용해서, 각 Activity 별로 반환한다.
     * 상단바 부분까지 배경으로 사용하기 위해, FLAG_LAYOUT_NO_LIMITS 를 사용하였음
     *
     * 각 Activity 별로, onCreate() 메소드를 재정의하여 사용할 수 있도록 설계
     * @param savedInstanceState 자원 저장 값
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutResource());
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        view = getWindow().getDecorView();
        setToolbar();
        onCreate();
    }

    /**
     * Method setToolbar
     *
     * 기본적으로 Activity 에 Toolbar 를 사용하지만,
     * ToolbarTitle 값을 0 으로 지정할 경우, App 이름이 나타난다.
     *
     * Activity 에 Toolbar 가 존재하지 않는 경우, 표시하지 않으며
     * 존재 여부는 LayoutResource 에 id가 toolbar 라는 이름으로 정의되어 있는지로 구분한다.
     */
    private void setToolbar() {
        if(toolbar != null) {
            if(getToolbarTitle() > 0)
                toolbar.setTitle(getToolbarTitle());
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    /**
     * Abstract Method getContentLayoutResource
     *
     * 각 Activity 별 Layout Resource 정의
     *
     * @return Layout 소스
     */
    @LayoutRes
    protected abstract int getContentLayoutResource();

    protected abstract void onCreate();

    /**
     * Abstract Method getToolbarTitle
     *
     * 사용할 툴바 제목 정의
     *
     * @return Toolbar 제목 (String 형x, StringRes 형태 사용)
     */
    @StringRes
    protected abstract int getToolbarTitle();

    protected final void setToastMessage(String msg) { Toast.makeText(this, msg, Toast.LENGTH_LONG).show(); }
}
