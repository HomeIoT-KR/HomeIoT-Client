package xyz.neonkid.homeiot.main.presenter.intro;

import android.support.annotation.Nullable;

import xyz.neonkid.homeiot.base.presenter.BaseIntroPresenter;
import xyz.neonkid.homeiot.main.presenter.view.intro.IntroPresenterView;
import xyz.neonkid.homeiot.main.view.intro.slide.InputSlide;
import xyz.neonkid.homeiot.main.view.intro.slide.LottieSlide;

/**
 * IntroActivity 의 Presenter
 *
 * @see xyz.neonkid.homeiot.base.presenter.BasePresenter
 *
 * Created by neonkid on 7/24/17.
 */

public class IntroPresenter extends BaseIntroPresenter<IntroPresenterView> {
    public IntroPresenter(IntroPresenterView view) { super(view); }

    /**
     * Method createLottieSlide
     *
     * 새로이 LottieSlide 를 생성한 후,
     * View 에 추가하는 메소드
     *
     * @param title 새로운 슬라이드에 들어갈 Title
     * @param lottiePath 새로운 슬라이드에 들어갈 LottieAnimation
     * @param desc 새로운 슬라이드에 들어갈 코멘트 메시지
     */
    public void createLottieSlide(@Nullable String title, String lottiePath, @Nullable String desc) {
        LottieSlide slide = new LottieSlide();
        slide.newInstance(title, lottiePath, desc);
        getView().addLottieSlide(slide);
    }

    /**
     * Method createInputSlide
     *
     * 서버 연결 확인을 위한 슬라이드 생성 후,
     * View 에 추가하는 메소드
     *
     * @param title 슬라이드에 들어갈 Title
     * @param lottiePath 슬라이드에 들어갈 LottieAnimation
     * @param desc 슬라이드에 들어갈 코멘트 메시지
     */
    public void createInputSlide(@Nullable String title, String lottiePath, @Nullable String desc) {
        InputSlide slide = new InputSlide();
        slide.newInstance(title, lottiePath, desc);
        getView().addLottieSlide(slide);
    }
}
