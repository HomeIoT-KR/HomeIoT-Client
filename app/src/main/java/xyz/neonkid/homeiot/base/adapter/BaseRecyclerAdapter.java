package xyz.neonkid.homeiot.base.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;

/**
 * Created by neonkid on 5/18/17.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerView> {
    private Context context;

    // 각 모델별 데이터 리스트
    private List<T> itemList;

    public BaseRecyclerAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
    }

    // ListView의 getView !!
    @Override
    public void onBindViewHolder(BaseRecyclerView holder, int position) {
        if(holder != null)
            holder.onViewHolder(getItem(position), position);
    }

    protected abstract void addItem(T data);

    protected void addItem(@Nullable T data, boolean isNotify) {
        itemList.add(data);
        if(isNotify)
            notifyItemChanged(data);
    }

    protected void notifyItemChanged(T item) {
        if(getItemList() != null) {
            int index = getItemList().indexOf(item);
            notifyItemChanged(index);
        }
    }

    @Nullable
    protected T getItem(int position) {
        return itemList != null ? (T)itemList.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public Context getContext() {
        return context;
    }

    protected final List<T> getItemList() { return itemList; }
}
