package xyz.neonkid.homeiot.base.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;

/**
 * 수동 제어 화면 Fragment 기본 클래스
 *
 * BasePrefActivity 와 같이 사용되는 형태의 Fragment 클래스
 *
 * Created by neonkid on 7/27/17.
 */

public abstract class BasePrefFragment extends PreferenceFragment {

    /**
     * XmlResources 를 가져와서, 뷰에 띄워주는 역할 메소드
     * @param savedInstanceState 자원 저장 값
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(getXmlResources());
    }

    /**
     * Abstract Method getXmlResources
     *
     * 각 Fragment 별, XmlResource 를 지정하는 메소드
     * @return XmlResource
     */
    @XmlRes
    protected abstract int getXmlResources();
}
