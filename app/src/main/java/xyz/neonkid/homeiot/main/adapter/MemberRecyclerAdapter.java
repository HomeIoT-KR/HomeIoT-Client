package xyz.neonkid.homeiot.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import xyz.neonkid.homeiot.main.adapter.model.member.HomeIoTMember;
import xyz.neonkid.homeiot.main.adapter.model.member.MemberModel;
import xyz.neonkid.homeiot.main.adapter.view.MemberRecyclerView;
import xyz.neonkid.homeiot.base.adapter.BaseRecyclerAdapter;
import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;

/**
 * Created by neonkid on 11/21/17.
 */

public class MemberRecyclerAdapter extends BaseRecyclerAdapter<HomeIoTMember> implements MemberModel {
    public MemberRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected void addItem(HomeIoTMember data) {
        addItem(data, true);
    }

    @Override
    public void addMember(@NonNull HomeIoTMember member) {
        addItem(member);
    }

    @Override
    public BaseRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MemberRecyclerView(parent, this);
    }
}
