package com.quochungcyou.proconnect.Adapter.RecylerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.Activity.SendMessageActivity;
import com.quochungcyou.proconnect.Model.UserModel;
import com.quochungcyou.proconnect.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {


    private final Context context;
    private final List<UserModel> postlist;
    int lastPosition = -1;

    public UserAdapter(Context context , List<UserModel> post){
        this.context = context;
        postlist = post;
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.usersitem , parent , false);
        return new UserHolder(view);

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        UserModel userModel = postlist.get(position);
        holder.name.setText(userModel.getName().trim());
        holder.lastmessage.setText(userModel.getLastMessage().trim());
        Glide.with(context).load(userModel.getProfileImageUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(holder.avatar);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, SendMessageActivity.class);
            intent.putExtra("selfuid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            intent.putExtra("targetuid", userModel.getUseruid().trim());
            intent.putExtra("targetname", userModel.getName().trim());
            intent.putExtra("targetavatar", userModel.getProfileImageUrl().trim());

            context.startActivity(intent);
        });
        setAnimation(holder.itemView, position);


    }


    @Override
    public void onViewDetachedFromWindow(final UserHolder viewHolder)
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
        return postlist.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {

        CircleImageView avatar;
        TextView name, lastmessage, time;



        public UserHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            lastmessage = itemView.findViewById(R.id.lastmessage);
            time = itemView.findViewById(R.id.time);

        }
    }
}