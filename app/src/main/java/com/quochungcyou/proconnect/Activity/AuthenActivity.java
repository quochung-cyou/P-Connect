package com.quochungcyou.proconnect.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.quochungcyou.proconnect.Fragment.AuthenFragment;
import com.quochungcyou.proconnect.R;

public class AuthenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //hide status bar (called before content)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authen);

        initFragment();


    }

    public void initFragment() {
        Fragment mFragment;
        mFragment = new AuthenFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.authenFrameLayout, mFragment).commit();
    }


}