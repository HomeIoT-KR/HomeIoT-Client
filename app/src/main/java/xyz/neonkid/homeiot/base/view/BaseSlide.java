package xyz.neonkid.homeiot.base.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 앱 소개 화면 슬라이드 기본 클래스
 *
 * HomeIoT App 처음 앱 실행시, 나타나는 앱 소개 화면 클래스입니다.
 *
 * Created by neonkid on 7/24/17.
 */

public abstract class BaseSlide extends Fragment {

    // 앱 소개 화면 제목
    protected final String ARG_TITLE = "introTitle";

    // 앱 소개 화면 LottieSlide 경로 (File format: Json)
    protected final String ARG_LOTTIE = "lottiePath";

    // 앱 소개 화면 코멘트
    protected final String ARG_DESC = "introDesc";

    private Unbinder unbinder;

    /**
     * LayoutResource 를 뷰로 렌더링 해주는 메소드
     *
     * @param inflater 현재 슬라이드의 ParentView
     * @param container 현재 슬라이드의 ChildView
     * @param savedInstanceState 자원 저장 값
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentLayoutResource(), container, false);
    }

    /**
     * 앱 소개 화면, 제목, 코멘트를 설정하는 메소드
     * ButterKnife 를 이용해, ViewBinding
     *
     * @param view 현재 슬라이드의 View
     * @param savedInstanceState 자원 저장 값
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        if(getArguments() != null) {
            setIntroTitle(getArguments().getString(ARG_TITLE));
            setIntroImage(getArguments().getString(ARG_LOTTIE));
            setIntroDescription(getArguments().getString(ARG_DESC));
        }
    }

    /**
     * Readonly Method newInstance
     *
     * 새로운 슬라이드를 만들어주는 메소드
     *
     * @param title 슬라이드의 제목
     * @param lottiePath LottieSLide 경로 (File Format: Json)
     * @param desc 슬라이드의 코멘트
     */
    public final void newInstance(@Nullable String title, String lottiePath, @Nullable String desc) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_LOTTIE, lottiePath);
        args.putString(ARG_DESC, desc);
        this.setArguments(args);
    }

    /**
     * Readonly Method setIntroImage
     *
     * LayoutResource 에서 AnimationView 를 가져와,
     * LottieSlide 를 설정하는 메소드
     *
     * 옵션: setAnimation - AnimationView 에 LottieSlide 를 띄움
     *       loop - AnimationView 를 계속 반복 재생
     *       playAnimation - AnimationView 재생
     * @param path LottieSlide 경로
     */
    protected final void setIntroImage(String path) {
        getAnimationView().setAnimation(path);
        getAnimationView().loop(true);
        getAnimationView().playAnimation();
    }

    /**
     * Readonly Method setIntroTitle
     *
     * LayoutResource 에서 introTitle ID 를 가진 컴포넌트의
     * text 를 설정하는 메소드
     *
     * @param title 설정할 text
     */
    protected final void setIntroTitle(String title) { getIntroTitle().setText(title); }

    /**
     * Readonly Method setIntroDescription
     *
     * LayoutResource 에서 introDescription ID 를 가진 컴포넌트의
     * text 를 설정하는 메소드
     *
     * @param desc 설정할 text
     */
    protected final void setIntroDescription(String desc) { getIntroDescription().setText(desc); }

    /**
     * Slide 를 빠져나갈 경우,
     * ButterKnife 의 사용 해제
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * Abstract Method getContentLayoutResource
     *
     * 각 Slide 별 LayoutResource 지정 메소드
     *
     * @return LayoutResource
     */
    @LayoutRes
    protected abstract int getContentLayoutResource();

    protected abstract LottieAnimationView getAnimationView();

    protected abstract TextView getIntroTitle();

    protected abstract TextView getIntroDescription();
}
