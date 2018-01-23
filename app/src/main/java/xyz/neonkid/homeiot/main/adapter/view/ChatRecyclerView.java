package xyz.neonkid.homeiot.main.adapter.view;

import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;

import xyz.neonkid.homeiot.base.adapter.view.BaseRecyclerView;
import xyz.neonkid.homeiot.R;
import xyz.neonkid.homeiot.main.adapter.ChatRecyclerAdapter;
import xyz.neonkid.homeiot.main.adapter.model.chat.ChatMessage;
import xyz.neonkid.homeiot.main.components.preference.PrefManager;

/**
 * 음성 인식 메시지 View
 *
 * Created by neonkid on 5/18/17.
 */

public class ChatRecyclerView extends BaseRecyclerView<ChatRecyclerAdapter, ChatMessage> {
    @BindView(R.id.chatMessage)
    TextView chatText;

    @BindView(R.id.singleMessageContainer)
    LinearLayout singleMessageContainer;

    public ChatRecyclerView(ViewGroup parent, ChatRecyclerAdapter adapter) {
        super(R.layout.content_chatmessage, parent, adapter);
    }

    /**
     * Callback Method onViewHolder
     *
     * 음성 메시지를 View 에 표시합니다.
     *
     * @param item 메시지 Model
     * @param position 메시지 표시 방향
     */
    @Override
    public void onViewHolder(@Nullable ChatMessage item, final int position) {
        PrefManager pref = new PrefManager(getContext());
        if(item != null) {
            Animation animation = AnimationUtils.loadAnimation(getContext(), item.left ? R.anim.slide_in_right : R.anim.slide_in_left);
            animation.setDuration(500);
            chatText.setText(item.message);
            chatText.setTextSize(pref.getPrefInt(getContext().getString(R.string.speech_TextSize)));
            chatText.setBackgroundResource(item.left ? R.drawable.bubble_out : R.drawable.bubble_in);
            singleMessageContainer.setGravity(item.left ? Gravity.START : Gravity.END);
            chatText.startAnimation(animation);
        }
    }
}
