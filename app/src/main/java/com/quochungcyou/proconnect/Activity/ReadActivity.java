package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.quochungcyou.proconnect.R;
import com.quochungcyou.proconnect.Utils.DateFormat;

public class ReadActivity extends AppCompatActivity {
    private String mAuthor, mDate, mImg, mSource, mTitle, mUrl;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private FrameLayout date_behavior;
    private LinearLayout titleAppbar;
    private ImageView imageView;
    private TextView appbar_subtitle, date, time, title;

    private boolean isHideToolbarView = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_read);
        Log.d("ReadActivity", "init read");
        initVar();
        getDataIntent();
        initWebView(mUrl);
    }

    private void initVar() {
        toolbar = findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.appbar);

        date_behavior = findViewById(R.id.date_behavior);
        titleAppbar = findViewById(R.id.title_appbar);
        imageView = findViewById(R.id.backdrop);
        appbar_subtitle = findViewById(R.id.subtitle_on_appbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        date =  findViewById(R.id.date);
        time = findViewById(R.id.timedocbai);
        title = findViewById(R.id.title);


        //appBarLayout.addOnOffsetChangedListener(this);


        //init toolbar
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //collapsingToolbarLayout.setTitle("");

        //request option
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.color.lightred);
        Glide.with(this).load(this.mImg).apply(requestOptions).transition(DrawableTransitionOptions.withCrossFade()).into(this.imageView);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mImg = intent.getStringExtra("img");
        mTitle = intent.getStringExtra("title");
        mDate = intent.getStringExtra("date");
        mSource = intent.getStringExtra("source");
        mAuthor = intent.getStringExtra("author");
        appbar_subtitle.setText(mUrl);
        date.setText(DateFormat.DateFormat(mDate));
        title.setText(mTitle);


        String str = "";
        if (mAuthor != null) {
            str = " • " + mAuthor;
        } else {
            str = "";
        }
        time.setText(this.mSource + str + " • " + DateFormat.DateToTimeFormat(this.mDate));
    }

    private void initWebView(String str) {
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(str);
    }

    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onOffsetChanged(AppBarLayout appBarLayout2, int i) {
        float abs = ((float) Math.abs(i)) / ((float) appBarLayout2.getTotalScrollRange());
        if (abs == 1.0f && isHideToolbarView) {
            date_behavior.setVisibility(View.GONE);
            titleAppbar.setVisibility(View.VISIBLE);
            isHideToolbarView = !isHideToolbarView;
        } else if (abs < 1.0f && !isHideToolbarView) {
            date_behavior.setVisibility(View.VISIBLE);
            titleAppbar.setVisibility(View.GONE);
            isHideToolbarView = !isHideToolbarView;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.view_web) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(this.mUrl));
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.share) {
            try {
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("text/plan");
                intent2.putExtra("android.intent.extra.SUBJECT", this.mSource);
                intent2.putExtra("android.intent.extra.TEXT", this.mTitle + "\n" + this.mUrl + "\nShare from the News App\n");
                startActivity(Intent.createChooser(intent2, "Share with :"));
            } catch (Exception unused) {
                Toast.makeText(this, "Hmm.. Sorry, \nCannot be share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}