package com.quochungcyou.proconnect.Fragment.AuthenActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.quochungcyou.proconnect.R;

public class AuthenFragment extends Fragment {

    private VideoView videoView;
    private LinearLayout loginTrans, regisTrans;



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
        Button loginButton = view.findViewById(R.id.fragmentLogin_LoginButton);
        Button registerButton = view.findViewById(R.id.fragmentLogin_RegisterButton);
        loginTrans = view.findViewById(R.id.transformLogin);
        regisTrans = view.findViewById(R.id.transformRegister);
        runVideoStart();


        loginButton.setOnClickListener(view12 -> {
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            ViewCompat.setTransitionName(loginTrans, "loginTrans");
            Transition transition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.shared_image);
            setSharedElementReturnTransition(transition);

            transaction.addSharedElement(loginTrans, "loginTrans");
            transaction.setReorderingAllowed(true);

            transaction.replace(R.id.authenFrameLayout, loginFragment).addToBackStack(null).commit();

        });
        registerButton.setOnClickListener(view1 -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            RegisterFragment registerFragment = new RegisterFragment();
            ViewCompat.setTransitionName(regisTrans, "regisTrans");
            Transition transition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.shared_image);
            setSharedElementReturnTransition(transition);

            transaction.addSharedElement(regisTrans, "regisTrans");
            transaction.setReorderingAllowed(true);

            transaction.replace(R.id.authenFrameLayout, registerFragment).addToBackStack(null);
            transaction.commit();
        });
    }

    public void runVideoStart() {
        //videoView.setVisibility(View.GONE);
        //Handle the first animation
        Uri video = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.login_bg);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(mp -> {
            Log.d("VideoView", "VideoView prepared");

            mp.setOnInfoListener((mp1, what, extra) -> {

                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started; hide the placeholder.
                    Log.d("VideoView", "VideoView started");
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    //videoView.setVisibility(View.VISIBLE);

                    return true;
                }
                return false;
            });
            mp.start();

        });
    }

}