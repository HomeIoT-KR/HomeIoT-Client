package xyz.neonkid.homeiot.main.view.intro.slide;

import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import butterknife.OnClick;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BaseSlide;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;
import xyz.neonkid.homeiot.main.components.networktest.TestConnection;

/**
 * 앱 소개 화면에서, HomeIoT 서버 연결을 도와줄 슬라이드 클래스
 *
 * @see BaseSlide
 *
 * Created by neonkid on 7/24/17.
 */

public class InputSlide extends BaseSlide {
    @BindView(R.id.intro_input_title)
    TextView introinputTitle;

    @BindView(R.id.intro_input_description)
    TextView introinputDescription;

    @BindView(R.id.intro_input_image)
    LottieAnimationView introinputImage;

    @BindView(R.id.intro_input_IP)
    EditText introinputIP;

    @BindView(R.id.connect_Btn)
    Button connectBtn;

    @Override
    protected int getContentLayoutResource() {
        return R.layout.intro_input_layout;
    }

    @Override
    protected LottieAnimationView getAnimationView() {
        return introinputImage;
    }

    @Override
    protected TextView getIntroTitle() {
        return introinputTitle;
    }

    @Override
    protected TextView getIntroDescription() {
        return introinputDescription;
    }

    /**
     * Click Event Method connectBtnClick
     *
     * 서버와 연결을 시도합니다.
     * 연결에 성공할 경우, PrefManager 를 통해, Preference 에 IP 주소가 저장됩니다.
     */
    @OnClick(R.id.connect_Btn)
    public void connectBtnClick() {
        final PrefManager manager = new PrefManager(getContext());
        connectBtn.setEnabled(false);
        connectBtn.setText(getString(R.string.connecting_Btn));

        TestConnection test = new TestConnection(introinputIP.getText().toString());
        new Thread(() -> {
            final String result = test.connect();
            if(result.equals("OK")) {
                manager.putPrefString(getString(R.string.ip_address), introinputIP.getText().toString());
                manager.putPrefBoolean("hIoT_init", true);
                manager.putPrefInt("speech_TextSize", 15);
                manager.putPrefInt("keepAlive_value", 0);

                getActivity().runOnUiThread(() -> {
                    connectBtn.setText(getString(R.string.successConnect_Btn));
                    introinputDescription.setText(getString(R.string.inputSlide_description));
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    connectBtn.setText(result);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        connectBtn.setEnabled(true);
                        connectBtn.setText(getString(R.string.connect_Btn));
                    }, 3000);
                });
            }
        }).start();
    }
}
