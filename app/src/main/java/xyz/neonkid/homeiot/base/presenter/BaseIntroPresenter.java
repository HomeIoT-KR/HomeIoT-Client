package xyz.neonkid.homeiot.base.presenter;

import xyz.neonkid.homeiot.base.presenter.view.BaseIntroPresenterView;

/**
 * 앱 소개 Activity 의 Presenter
 *
 * Created by neonkid on 7/24/17.
 */

public abstract class BaseIntroPresenter<V extends BaseIntroPresenterView> {
    private V view;

    public BaseIntroPresenter(V view) { this.view = view; }

    protected final V getView() { return view; }
}
