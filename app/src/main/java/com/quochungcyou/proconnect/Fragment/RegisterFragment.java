package com.quochungcyou.proconnect.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.R;

import me.ibrahimsn.particle.ParticleView;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class RegisterFragment extends Fragment {


    TextView gotoSignin, gobackAuthen;
    TextInputEditText email, password;
    MaterialButton registerFunction;
    FirebaseAuth mAuth;
    ParticleView particleView;
    ConstraintLayout registerLayout;

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
        registerFunction = view.findViewById(R.id.registerFunction);
        mAuth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.emailLogin);
        password = view.findViewById(R.id.passwordLogin);
        particleView = view.findViewById(R.id.particleView);
        registerLayout = view.findViewById(R.id.regisView);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
        ViewCompat.setTransitionName(registerLayout, "regisTrans");


        //transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        gotoSignin.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenFrameLayout, new LoginFragment()).addToBackStack(null).commit();
        });
        gobackAuthen.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenFrameLayout, new AuthenFragment()).addToBackStack(null).commit();
        });

        registerFunction.setOnClickListener(
                v -> register()
        );
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        particleView.setVisibility(View.VISIBLE);
                    }
                },
                1500);
    }

    public void register() {

        if (TextUtils.isEmpty(email.getText().toString())) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "You need to enter email address",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
            return;
        }

        //regex check email address
        if (!email.getText().toString().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "You need to enter a valid email address",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "You need to enter password",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
            return;
        }

        //regex check password 8 - 24 character
        if (password.getText().toString().length() < 8 || password.getText().toString().length() > 24) {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "Password must be 8 - 24 characters",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
            return;
        }

        //create User
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
              .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        MotionToast.Companion.createToast(getActivity(),
                                "Success ✅",
                                "You have successfully registered",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                        email.setText("");
                        password.setText("");
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setReorderingAllowed(true);
                        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                        transaction.replace(R.id.authenFrameLayout, new LoginFragment()).addToBackStack(null).commit();

                    } else {
                        MotionToast.Companion.createToast(getActivity(),
                                "Failed ☹️",
                                "Failed to register",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                    }
                });





    }



}