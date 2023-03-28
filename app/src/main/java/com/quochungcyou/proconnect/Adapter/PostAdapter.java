package com.quochungcyou.proconnect.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
import com.makeramen.roundedimageview.RoundedImageView;
import com.quochungcyou.proconnect.Activity.ReadActivity;
import com.quochungcyou.proconnect.Model.ArticleModel;
import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.DateFormat;
import com.quochungcyou.proconnect.Utils.UriParse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {

    private final Context context;
    private final List<ArticleModel> postlist;
    int lastPosition = -1;

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


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        ArticleModel post = postlist.get(position);
        holder.title.setText(post.getTitle());
        if (post.getAuthor() != null && post.getAuthor().size() > 0) holder.author.setText(post.getAuthor().get(0));
        else holder.author.setText(post.getSource_id());
        holder.date.setText(DateFormat.DateFormat(post.getTime()));

        if (post.getUrlimage() == null) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    String imageUrl = null;
                    Document doc;
                    try {
                        doc = Jsoup
                                .connect(post.getUrl())
                                .ignoreContentType(true)
                                .timeout(5 * 1000)
                                .get();
                    } catch (IOException e) {
                        return;
                    }
                    Elements elements = doc.select("meta");
                    for (Element e : elements) {
                        imageUrl = e.attr("content");
                        //OR more specifically you can check meta property.
                        if (e.attr("property").equalsIgnoreCase("og:image")) {
                            imageUrl = e.attr("content");
                            break;
                        }
                    }
                    Log.d("PostAdapter", "Done loading " + imageUrl);
                    if (imageUrl != null) {
                        Activity tmp = (Activity) context;
                        String finalImageUrl = imageUrl;
                        tmp.runOnUiThread(() -> Glide.with(context).load(finalImageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).placeholder(R.drawable.thumbnailnewpost).into(holder.thumbnail));
                    } else {
                        Activity tmp = (Activity) context;
                        String finalImageUrl = imageUrl;
                        tmp.runOnUiThread(() -> Glide.with(context).load(context.getDrawable(R.drawable.thumbnailnewpost)).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).placeholder(R.drawable.thumbnailnewpost).into(holder.thumbnail));
                    }
                }
            }).start();


        } else {
            Glide.with(context).load("https:" + post.getUrlimage()).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).placeholder(R.drawable.thumbnailnewpost).into(holder.thumbnail);
        }

        //parse url clean
        String url = UriParse.getUrlDomainName(post.getUrl());

        Glide.with(context).load("https://www.google.com/s2/favicons?domain=" + url + "&sz=64")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .placeholder(R.drawable.thumbnailnewpost)
                .into(holder.avatar);
        setAnimation(holder.itemView, position);
    }




    @Override
    public void onViewDetachedFromWindow(final PostHolder viewHolder)
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
                //intent.putExtra("author", postlist.get(getAdapterPosition()).getAuthor().get(0));
                //intent.putExtra("image", postlist.get(getAdapterPosition()).getUrlimage());
                //intent.putExtra("date", postlist.get(getAdapterPosition()).getTime());
                //intent.putExtra("summary", postlist.get(getAdapterPosition()).getSummary());

                context.startActivity(intent);
            });
        }
    }
}