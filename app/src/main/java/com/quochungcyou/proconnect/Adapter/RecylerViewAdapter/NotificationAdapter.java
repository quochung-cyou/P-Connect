package com.quochungcyou.proconnect.Adapter.RecylerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quochungcyou.proconnect.Activity.NotificationActivity;
import com.quochungcyou.proconnect.Model.NotificationModel;
import com.quochungcyou.proconnect.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private final Context context;
    private final List<NotificationModel> messageList;
    int lastPosition = -1;

    public NotificationAdapter(Context context , List<NotificationModel> post){
        this.context = context;
        messageList = post;
    }
    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notificationitem , parent , false);
        return new NotificationHolder(view);

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        NotificationModel notificationModel = messageList.get(position);
        holder.fullname.setText(notificationModel.getFullname());
        Glide.with(context).load(notificationModel.getAvatarurl()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder.avatar);

        NotificationActivity notificationActivity = (NotificationActivity) context;

        holder.accept.setOnClickListener(v -> {
            String myname = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/friend");
            DatabaseReference friendReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + notificationModel.getUseruid() + "/friend");
            databaseReference.child(notificationModel.getUseruid()).setValue("friend");
            friendReference.child(myname).setValue("friend");
            notificationActivity.initRecyclerView();

        });

        holder.remove.setOnClickListener(v -> {
            String myname = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/friend");
            DatabaseReference friendReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + notificationModel.getUseruid() + "/friend");
            databaseReference.child(notificationModel.getUseruid()).removeValue();
            friendReference.child(myname).removeValue();
            notificationActivity.initRecyclerView();
        });


        setAnimation(holder.itemView, position);

    }




    @Override
    public void onViewDetachedFromWindow(@NonNull final NotificationHolder viewHolder)
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

    public class NotificationHolder extends RecyclerView.ViewHolder {

        CircleImageView avatar;
        TextView fullname;
        MaterialButton accept, remove;


        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            accept = itemView.findViewById(R.id.acceptaddFriend);
            remove = itemView.findViewById(R.id.removeaddFriend);
            fullname = itemView.findViewById(R.id.name);


        }
    }
}