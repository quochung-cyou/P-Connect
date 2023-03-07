package com.quochungcyou.proconnect.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quochungcyou.proconnect.R;

public class LoginFragment extends Fragment {

    private VideoView videoView;
    private TextView title;
    private TextView subtitle;
    private Button loginButton;
    private Button registerButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
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

        Uri video = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.login_bg);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(mp -> videoView.start());

        Animation slideupAnimation = new AnimationUtils().loadAnimation(getActivity(), R.anim.bounce);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title.startAnimation(slideupAnimation);
                subtitle.startAnimation(slideupAnimation);
            }
        });




    }

}