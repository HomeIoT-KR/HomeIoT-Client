package xyz.neonkid.homeiot.main.view.intro.slide;

import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.view.BaseSlide;

/**
 * 앱 소개 화면에 나타나는 각 슬라이드 클래스
 *
 * @see BaseSlide
 *
 * Created by neonkid on 7/24/17.
 */

public class LottieSlide extends BaseSlide {
    @BindView(R.id.intro_title)
    TextView introTitle;

    @BindView(R.id.intro_description)
    TextView introDescription;

    @BindView(R.id.intro_image)
    LottieAnimationView introImage;

    @Override
    protected int getContentLayoutResource() { return R.layout.intro_lottie_layout; }

    @Override
    protected LottieAnimationView getAnimationView() {
        return introImage;
    }

    @Override
    protected TextView getIntroTitle() {
        return introTitle;
    }

    @Override
    protected TextView getIntroDescription() {
        return introDescription;
    }
}
