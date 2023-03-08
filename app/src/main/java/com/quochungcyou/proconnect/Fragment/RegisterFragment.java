package com.quochungcyou.proconnect.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quochungcyou.proconnect.R;

public class RegisterFragment extends Fragment {


    TextView gotoSignin, gobackAuthen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
    }

    public void initVar(View view) {
        gotoSignin = view.findViewById(R.id.gotosignin);
        gobackAuthen = view.findViewById(R.id.goBackauthen);

        gotoSignin.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.authenFrameLayout, new LoginFragment()).setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            ).commit();
        });
        gobackAuthen.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.authenFrameLayout, new AuthenFragment()).setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            ).commit();
        });
    }



}