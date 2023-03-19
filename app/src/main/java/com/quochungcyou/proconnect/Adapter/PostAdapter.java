package com.quochungcyou.proconnect.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.quochungcyou.proconnect.Activity.ReadActivity;
import com.quochungcyou.proconnect.Model.ArticleModel;
import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.DateFormat;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final Context context;
    private final List<ArticleModel> postlist;

    public PostAdapter(Context context , List<ArticleModel> post){
        this.context = context;
        postlist = post;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newsitem , parent , false);
        return new PostHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        ArticleModel post = postlist.get(position);
        holder.title.setText(post.getTitle());
        holder.author.setText(post.getAuthor());
        holder.date.setText(DateFormat.DateFormat(post.getTime()));
        if (post.getUrlimage() == null) {
            holder.thumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.thumbnailnewpost));
        } else {
            Glide.with(context).load(post.getUrlimage()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).placeholder(R.drawable.thumbnailnewpost).into(holder.thumbnail);
        }

        holder.avatar.setImageDrawable(context.getResources().getDrawable(R.drawable.headerprofile));




    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        RoundedImageView avatar;
        TextView author, title, date;
        RoundedImageView thumbnail;


        public PostHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.thumbnailavatar);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            thumbnail = itemView.findViewById(R.id.thumbnailnewpost);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("url", postlist.get(getAdapterPosition()).getUrl());
                intent.putExtra("title", postlist.get(getAdapterPosition()).getTitle());
                intent.putExtra("author", postlist.get(getAdapterPosition()).getAuthor());
                intent.putExtra("image", postlist.get(getAdapterPosition()).getUrlimage());
                intent.putExtra("date", postlist.get(getAdapterPosition()).getTime());
                intent.putExtra("Source", postlist.get(getAdapterPosition()).getSource().getName());

                context.startActivity(intent);
            });
        }
    }
}