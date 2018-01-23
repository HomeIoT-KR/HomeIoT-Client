package xyz.neonkid.homeiot.base.adapter.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import xyz.neonkid.homeiot.base.adapter.BaseRecyclerAdapter;

/**
 * Created by neonkid on 5/18/17.
 */

public abstract class BaseRecyclerView<AD extends BaseRecyclerAdapter, T> extends RecyclerView.ViewHolder {
    private AD adapter;

    public BaseRecyclerView(AD adapter,  View itemView) {
        super(itemView);
        this.adapter = adapter;
        ButterKnife.bind(this, itemView);
    }

    public BaseRecyclerView(@LayoutRes int layoutRes, ViewGroup parent, AD adapter) {
        this(adapter, LayoutInflater.from(adapter.getContext()).inflate(layoutRes, parent, false));
    }

    public abstract void onViewHolder(final @Nullable T item, int position);

    protected AD getAdapter() {
        return adapter;
    }

    protected Context getContext() {
        return adapter.getContext();
    }
}
