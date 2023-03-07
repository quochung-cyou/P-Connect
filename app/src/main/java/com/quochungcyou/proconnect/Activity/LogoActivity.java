package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.quochungcyou.proconnect.R;

public class LogoActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        initVar();

    }

    public void initVar() {
        videoView = findViewById(R.id.gifLogo);
        videoView = (VideoView) findViewById(R.id.gifLogo);

        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.logo);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(() -> {
            Intent intentMainActivity = new Intent(LogoActivity.this, AuthenActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intentMainActivity);
            finish();
        }, 2500);

    }


}