package xyz.neonkid.homeiot.main.presenter.view.intro;

import xyz.neonkid.homeiot.base.presenter.view.BaseIntroPresenterView;
import xyz.neonkid.homeiot.base.view.BaseSlide;

/**
 * Created by neonkid on 7/24/17.
 */

public interface IntroPresenterView extends BaseIntroPresenterView {
    void addLottieSlide(BaseSlide slide);
}
