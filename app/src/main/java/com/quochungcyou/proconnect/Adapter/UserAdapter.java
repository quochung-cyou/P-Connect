package com.quochungcyou.proconnect.Adapter;

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

import com.makeramen.roundedimageview.RoundedImageView;
import com.quochungcyou.proconnect.Activity.SendMessageActivity;
import com.quochungcyou.proconnect.Model.UserModel;
import com.quochungcyou.proconnect.R;

import java.util.List;


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
        UserModel post = postlist.get(position);

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

        RoundedImageView avatar;
        TextView author, title, date;
        RoundedImageView thumbnail;


        public UserHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SendMessageActivity.class);
                    context.startActivity(intent);
                }
            });

        }
    }
}