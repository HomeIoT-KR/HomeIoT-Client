package xyz.neonkid.homeiot.main.presenter.view;

import xyz.neonkid.homeiot.base.presenter.view.BasePresenterView;

/**
 * Created by neonkid on 5/15/17.
 */

public interface MainPresenterView extends BasePresenterView {

    // 인식 내용 View 바인딩
    void sendView(String message);
    void recvView(String message);
}
