package com.quochungcyou.proconnect.Fragment;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.transition.MaterialElevationScale;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.quochungcyou.proconnect.R;
import com.skydoves.transformationlayout.TransformationCompat;

public class AuthenFragment extends Fragment {

    private VideoView videoView;
    private TextView title;
    private TextView subtitle;
    private Button loginButton;
    private Button registerButton;
    private LinearLayout loginTrans;



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //morph into login/register

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_authen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);

    }

    public void initVar(View view) {
        videoView = view.findViewById(R.id.videoViewLogin);
        title = view.findViewById(R.id.fragmentLoginTitle);
        subtitle = view.findViewById(R.id.fragmentLoginSubTitle);
        loginButton = view.findViewById(R.id.fragmentLogin_LoginButton);
        registerButton = view.findViewById(R.id.fragmentLogin_RegisterButton);
        loginTrans = view.findViewById(R.id.transformLogin);
        runVideoStart();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = new LoginFragment();
                
                Transition explodeTransform = TransitionInflater.from(this).
                        inflateTransition(android.R.transition.explode);



                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.authenFrameLayout, loginFragment).addToBackStack(null).commit();

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();;
                RegisterFragment registerFragment = new RegisterFragment();
                transaction.replace(R.id.authenFrameLayout, registerFragment).addToBackStack(null);
                transaction.commit();
            }
        });
    }

    public void runVideoStart() {
        //Handle the first animation
        Uri video = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.login_bg);
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

}