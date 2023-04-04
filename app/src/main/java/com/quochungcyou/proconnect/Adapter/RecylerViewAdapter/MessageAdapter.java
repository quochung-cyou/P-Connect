package com.quochungcyou.proconnect.Adapter.RecylerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.Model.MessageModel;
import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.DateFormat;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private final Context context;
    private final List<MessageModel> messageList;
    int lastPosition = -1;
    String selfuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
        Log.d("MessageAdapter", message.getSendername() + " " + message.getTargetname() + " " + message.getMessage());
        if (message.getReceiver().equals(selfuid)) { //ís sender
            holder.receiveLayout.setVisibility(View.GONE);
            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.messageSend.setText(message.getMessage().trim());
            holder.timeSend.setText(DateFormat.militoHour(message.getTime()).trim());
            //holder.sendername.setText(message.getSendername().trim());
            Glide.with(context).load(message.getSenderavatar()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder.avatarsend);
        } else { //is receiver
            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveLayout.setVisibility(View.VISIBLE);
            holder.messageReceive.setText(message.getMessage());
            holder.timeReceive.setText(DateFormat.militoHour(message.getTime()).trim());
            //holder.receivername.setText(message.getSendername());
            Glide.with(context).load(message.getSenderavatar()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder.avatarreceive);
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
        final TextView timeSend, timeReceive;
        final LinearLayout sendLayout, receiveLayout;
        CircleImageView avatarsend, avatarreceive;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messageReceive = itemView.findViewById(R.id.messageReceive);
            messageSend = itemView.findViewById(R.id.messagesend);
            //time = itemView.findViewById(R.id.time);
            sendLayout = itemView.findViewById(R.id.sendLayout);
            receiveLayout = itemView.findViewById(R.id.receivedLayout);
            avatarsend = itemView.findViewById(R.id.avatarsend);
            avatarreceive = itemView.findViewById(R.id.avatarreceive);
            timeSend = itemView.findViewById(R.id.timeinmessagesend);
            timeReceive = itemView.findViewById(R.id.timeinmessagereceive);
            //sendername = itemView.findViewById(R.id.sendername);
            //receivername = itemView.findViewById(R.id.receivename);


        }
    }
}