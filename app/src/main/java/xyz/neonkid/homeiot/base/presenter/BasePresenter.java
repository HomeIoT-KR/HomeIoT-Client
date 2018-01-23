package xyz.neonkid.homeiot.base.presenter;

import xyz.neonkid.homeiot.base.presenter.view.BasePresenterView;
import xyz.neonkid.homeiot.base.view.BaseActivity;

/**
 * BaseActivity 에 해당하는 Presenter
 * Presenter: View 이외의 작업을 분리하기 위한 클래스
 *
 * 템플릿에는 View 에서 처리할 함수를 정의할 Interface 와,
 * Activity 를 정의하면 됩니다.
 *
 * ex) MainActivity / MainPresenter 와 묶는다면,
 * MainPresenter<MainPresenterView, MainActivity>
 *
 * @see BaseActivity
 *
 * Created by neonkid on 5/15/17.
 */

public abstract class BasePresenter<V extends BasePresenterView, C extends BaseActivity> {
    private V view;
    private C context;

    /**
     * Constructor 1
     *
     * Context 를 불러올 필요가 없는 경우...
     * @param view Presenter 와 통신할 View
     */
    public BasePresenter(V view) { this.view = view; }

    /**
     * Constructor 2
     *
     * Context 가 필요한 경우...
     * @param view Presenter 와 통신할 View
     * @param context View 에 해당하는 Context
     */
    public BasePresenter(V view, C context) {
        this.view = view;
        this.context = context;
    }

    protected final V getView() { return view; }

    protected final C getContext() { return context; }
}
