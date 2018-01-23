package xyz.neonkid.homeiot.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import xyz.neonkid.homeiot.base.adapter.BaseRecyclerAdapter;
import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.homeiot.main.adapter.model.chat.ChatMessage;
import xyz.neonkid.homeiot.main.adapter.model.chat.ChatModel;
import xyz.neonkid.homeiot.main.adapter.view.ChatRecyclerView;

/**
 * Created by neonkid on 5/18/17.
 */

public class ChatRecyclerAdapter extends BaseRecyclerAdapter<ChatMessage> implements ChatModel {

    public ChatRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseRecyclerView onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatRecyclerView(parent, this);
    }

    @Override
    protected void addItem(ChatMessage data) {
        addItem(data, false);
    }

    @Override
    public void addChat(@Nullable ChatMessage object) {
        addItem(object);
    }
}
