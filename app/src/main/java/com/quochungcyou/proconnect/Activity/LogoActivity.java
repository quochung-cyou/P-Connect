package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.quochungcyou.proconnect.APIUtils.APIHelper;
import com.quochungcyou.proconnect.APIUtils.APIInterface;
import com.quochungcyou.proconnect.Model.ResultModel;
import com.quochungcyou.proconnect.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogoActivity extends AppCompatActivity {

    VideoView videoView;
    APIInterface apiInterface;
    Call<ResultModel> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);

            if (getWindow().getInsetsController() != null) {
                getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        initVar();

    }

    public void initVar() {
        videoView = findViewById(R.id.gifLogo);

        apiInterface  = APIHelper.getApiClient("https://newsdata.io/", LogoActivity.this).create(APIInterface.class);


        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.logo);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {

                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            // video started; hide the placeholder.
                            videoView.setBackgroundColor(Color.TRANSPARENT);
                            return true;
                        }
                        return false;
                    }
                });
                mp.start();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(() -> {
            getPostList();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // User is signed in
                Intent intentMainActivity = new Intent(LogoActivity.this, MainActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intentMainActivity);
                finish();
            } else {
                // User is signed out
                Intent intentMainActivity = new Intent(LogoActivity.this, AuthenActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivity(intentMainActivity);
                finish();
            }

        }, 2000);

    }

    private void getPostList() {

        call = apiInterface.getLastestHeadlineNoTopic("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi");
        preloadData();
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi", "technology");
        preloadData();
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi", "world");
        preloadData();
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi", "sports");
        preloadData();
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi", "business");
        preloadData();
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "vi", "entertainment");


    }

    private void preloadData() {
        call.enqueue(new Callback<ResultModel>() {


            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {

            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

            }
        });
    }


}