package xyz.neonkid.homeiot.main.presenter;

import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.base.presenter.BasePresenter;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;
import xyz.neonkid.homeiot.main.presenter.view.SplashPresenterView;
import xyz.neonkid.homeiot.main.view.SplashActivity;

/**
 * SplashActivity 의 Presenter
 *
 * Created by neonkid on 5/17/17.
 */

public class SplashPresenter extends BasePresenter<SplashPresenterView, SplashActivity> {
    public SplashPresenter(SplashPresenterView view) { super(view); }

    public void checkIP(SplashActivity sa) {
        PrefManager prefManager = new PrefManager(sa);
        if(!prefManager.getPrefBoolean(sa.getString(R.string.hIoT_init)))
            getView().loadIntro();
        else
            getView().loadApp();
    }
}
