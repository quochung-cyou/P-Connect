package com.quochungcyou.proconnect.Adapter.RecylerViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quochungcyou.proconnect.Model.ArticleModel;
import com.quochungcyou.proconnect.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.UserPostHolder> {

    private final Context context;
    private final List<ArticleModel> postlist;
    int lastPosition = -1;

    public UserPostAdapter(Context context , List<ArticleModel> post){
        this.context = context;
        postlist = post;
    }
    @NonNull
    @Override
    public UserPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userpostitem , parent , false);
        return new UserPostHolder(view);

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull UserPostHolder holder, int position) {
        ArticleModel post = postlist.get(position);
        holder.fullname.setText(post.getDescription());
        holder.fullname1.setText(post.getDescription());
        holder.username.setText("@" + post.getSource_id());
        holder.username1.setText("@" + post.getSource_id());
        holder.message.setText(post.getTitle());
        Glide.with(context).load(post.getUrlimage()).placeholder(R.drawable.headerread).into(holder.postimage);
        Glide.with(context).load(post.getSummary()).placeholder(R.drawable.headerread).into(holder.avatar);
        setAnimation(holder.itemView, position);

    }




    @Override
    public void onViewDetachedFromWindow(@NonNull final UserPostHolder viewHolder)
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

    public class UserPostHolder extends RecyclerView.ViewHolder {
        TextView fullname, fullname1, username, username1;
        CircleImageView avatar;
        TextView message;
        ImageView postimage;



        public UserPostHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.fullname);
            fullname1 = itemView.findViewById(R.id.fullname1);
            username = itemView.findViewById(R.id.username);
            username1 = itemView.findViewById(R.id.username1);
            avatar = itemView.findViewById(R.id.avatar);
            message = itemView.findViewById(R.id.message);
            postimage = itemView.findViewById(R.id.postimage);

        }
    }
}