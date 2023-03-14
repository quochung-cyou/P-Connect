package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        videoView = findViewById(R.id.gifLogo);

        Uri video = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.logo);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(mp -> videoView.start());
    }

    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(() -> {
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

        }, 2500);

    }


}