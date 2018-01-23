package xyz.neonkid.homeiot.main.adapter.view;

import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pavlospt.roundedletterview.RoundedLetterView;

import butterknife.BindView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.MemberRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.member.HomeIoTMember;
import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;

/**
 * Created by neonkid on 11/21/17.
 */

public class MemberRecyclerView extends BaseRecyclerView<MemberRecyclerAdapter, HomeIoTMember> {
    @BindView(R.id.letterCard)
    RoundedLetterView letterCard;

    @BindView(R.id.developerTitle)
    TextView developerTitle;

    @BindView(R.id.memberName)
    TextView memberName;

    @BindView(R.id.memberComment)
    TextView memberComment;

    public MemberRecyclerView(ViewGroup parent, MemberRecyclerAdapter adapter) {
        super(R.layout.content_creditcard, parent, adapter);
    }

    @Override
    public void onViewHolder(@Nullable HomeIoTMember item, int position) {
        if(item != null) {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                letterCard.setBackgroundColor(getContext().getResources().getColor(item.color));
            else
                letterCard.setBackgroundColor(getContext().getColor(item.color));
            developerTitle.setText(item.developerTitle);
            letterCard.setTitleText(item.memberLetter);
            memberName.setText(item.memberName);
            memberComment.setText(item.memberComment);
        }
    }
}
