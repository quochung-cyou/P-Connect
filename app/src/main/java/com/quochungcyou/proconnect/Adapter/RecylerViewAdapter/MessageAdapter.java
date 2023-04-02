package com.quochungcyou.proconnect.Adapter.RecylerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.quochungcyou.proconnect.Model.MessageModel;
import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.DateFormat;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private final Context context;
    private final List<MessageModel> messageList;
    int lastPosition = -1;

    public MessageAdapter(Context context , List<MessageModel> post){
        this.context = context;
        messageList = post;
    }
    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chatitem , parent , false);
        return new MessageHolder(view);

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        MessageModel message = messageList.get(position);
        if (Integer.parseInt(message.getSender()) % 2 == 0) { //ís sender
            holder.receiveLayout.setVisibility(View.GONE);
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.messageSend.setText(message.getMessage());
            holder.timeSend.setText(DateFormat.militoHour(message.getTime()));
        } else { //is receiver
            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveLayout.setVisibility(View.VISIBLE);
            holder.messageReceive.setText(message.getMessage());
            holder.timeReceiver.setText(DateFormat.militoHour(message.getTime()));
        }
    }




    @Override
    public void onViewDetachedFromWindow(@NonNull final MessageHolder viewHolder)
    {
        super.onViewDetachedFromWindow(viewHolder);
        viewHolder.itemView.clearAnimation();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_enter);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }



    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder {

        final TextView messageSend;
        final TextView messageReceive;
        final TextView timeSend;
        final TextView timeReceiver;
        final LinearLayout sendLayout, receiveLayout;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messageReceive = itemView.findViewById(R.id.messageReceive);
            messageSend = itemView.findViewById(R.id.messageTextSend);
            timeSend = itemView.findViewById(R.id.timeTextSend);
            timeReceiver = itemView.findViewById(R.id.timeTextReceived);
            sendLayout = itemView.findViewById(R.id.sendLayout);
            receiveLayout = itemView.findViewById(R.id.receivedLayout);

        }
    }
}