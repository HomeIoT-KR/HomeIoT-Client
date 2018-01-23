package xyz.neonkid.homeiot.main.adapter.model.member;

import android.support.annotation.ColorRes;

/**
 * HomeIoT 개발자 모델
 *
 * Created by neonkid on 11/21/17.
 */

public class HomeIoTMember {
    // 이니셜 문자 색깔
    @ColorRes public final int color;

    // 개발 타이틀
    public final String developerTitle;

    // 팀원 이니셜
    public final String memberLetter;

    // 팀원 이름
    public final String memberName;

    // 팀원 담당 내용
    public final String memberComment;

    public HomeIoTMember(@ColorRes int color, String developerTitle, String memberLetter, String memberName, String memberComment) {
        this.color = color;
        this.developerTitle = developerTitle;
        this.memberLetter = memberLetter;
        this.memberName = memberName;
        this.memberComment = memberComment;
    }
}
