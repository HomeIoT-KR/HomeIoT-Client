package xyz.neonkid.homeiot.base.presenter;

import xyz.neonkid.homeiot.base.presenter.view.BasePrefPresenterView;
import xyz.neonkid.homeiot.base.view.BasePrefActivity;

/**
 * 수동 제어 Activity 의 Presenter
 * 템플릿은 BasePresenter 와 동일하게 적용
 *
 * @see BasePresenter
 *
 * Created by neonkid on 7/5/17.
 */

public abstract class BasePrefPresenter<V extends BasePrefPresenterView, C extends BasePrefActivity> {
    private V view;
    private C context;

    /**
     * Constructor
     *
     * 여기서 Context 는 StringRes 를 가져오기 위함
     * @param view 템플릿에 정의된 View
     * @param context 템플릿에 정의된 Context
     */
    public BasePrefPresenter(V view, C context) {
        this.view = view;
        this.context = context;
    }

    protected final V getView() { return view; }

    protected final C getContext() { return context; }
}
