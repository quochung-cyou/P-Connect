package com.quochungcyou.proconnect.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.quochungcyou.proconnect.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class ReadActivity extends AppCompatActivity {
    private String mSource;
    private String mTitle;
    private String mUrl;
    ProgressBar progressBar;
    LottieAnimationView loadPost;

    private boolean isHideToolbarView = false;
    private boolean doneLoading = false;

    public ReadActivity() {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_read);
        Log.d("ReadActivity", "init read");
        initVar();
        getDataIntent();
        initWebView(mUrl);
    }

    @SuppressLint("CheckResult")
    private void initVar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressbar);
        loadPost = findViewById(R.id.loadPost);



        //appBarLayout.addOnOffsetChangedListener(this);


        //init toolbar
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //collapsingToolbarLayout.setTitle("");

        //request option
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.color.lightred);
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("url");
        mTitle = intent.getStringExtra("title");
        mSource = intent.getStringExtra("source");

        //time.setText(this.mSource + str + " • " + DateFormat.DateToTimeFormat(this.mDate));
    }

    private void initWebView(String str) {
        WebView webView = findViewById(R.id.webView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, true);
        }
        webView.getSettings().setSupportMultipleWindows(false);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress)
            {
                Log.d("progressgogo", "onProgressChanged: " + progress +"%");
                progressBar.setVisibility(ProgressBar.VISIBLE);
                loadPost.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                    loadPost.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    if (!doneLoading) {
                        doneLoading = true;
                        Animation animation = AnimationUtils.loadAnimation(ReadActivity.this, R.anim.pop_enter);
                        webView.startAnimation(animation);
                    }


                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                loadPost.setVisibility(View.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);

            }


            //progress

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressBar.setVisibility(ProgressBar.GONE);
                loadPost.setVisibility(View.GONE);
            }

            //load image by glide for faster loading
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.toLowerCase(Locale.ROOT).contains(".jpg") || url.toLowerCase(Locale.ROOT).contains(".jpeg")) {
                    try {
                        Bitmap bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get();
                        return new WebResourceResponse("image/jpg", "UTF-8",getBitmapInputStream(bitmap, Bitmap.CompressFormat.JPEG));

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(url.toLowerCase(Locale.ROOT).contains(".png")){
                    Bitmap bitmap;
                    try {
                        bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get();
                        return new WebResourceResponse("image/png", "UTF-8",getBitmapInputStream(bitmap, Bitmap.CompressFormat.PNG));
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                } else if(url.toLowerCase(Locale.ROOT).contains(".webp")){
                    Bitmap bitmap;
                    try {
                        bitmap = Glide.with(webView).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(url).submit().get();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            return new WebResourceResponse("image/webp", "UTF-8",getBitmapInputStream(bitmap, Bitmap.CompressFormat.WEBP_LOSSY));
                        }
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                return super.shouldInterceptRequest(view, request);
            }

        });
        webView.loadUrl(str);


    }

    private InputStream getBitmapInputStream(Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, 80, byteArrayOutputStream);
        byte[] bitmapData = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(bitmapData);
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
            isHideToolbarView = false;
        } else if (abs < 1.0f && !isHideToolbarView) {
            isHideToolbarView = true;
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