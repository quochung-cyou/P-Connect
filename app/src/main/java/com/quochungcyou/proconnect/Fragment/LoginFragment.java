package com.quochungcyou.proconnect.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.Activity.MainActivity;
import com.quochungcyou.proconnect.R;

import me.ibrahimsn.particle.ParticleView;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class LoginFragment extends Fragment {

    TextView gotoSignup, gobackAuthen, gotoForgotPassword;
    TextInputEditText email, password;

    MaterialButton loginFunction;
    FirebaseAuth mAuth;
    ConstraintLayout loginLayout;
    ParticleView particleView;

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
        gotoSignup = view.findViewById(R.id.gotosignup);
        gobackAuthen = view.findViewById(R.id.goBackauthen);
        email = view.findViewById(R.id.emailLogin);
        password = view.findViewById(R.id.passwordLogin);
        loginFunction = view.findViewById(R.id.loginFunction);
        mAuth = FirebaseAuth.getInstance();
        loginLayout = view.findViewById(R.id.loginView);
        particleView = view.findViewById(R.id.particleView);
        gotoForgotPassword = view.findViewById(R.id.forgotPassword);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
        ViewCompat.setTransitionName(loginLayout, "loginTrans");

        String text = "<font color=#7A7A7A>Don’t have account? </font> <font color=#B12341><u>Sign up here!</u></font>";
        gotoSignup.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));



        //transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        gotoForgotPassword.setOnClickListener(v -> {
            Log.d("Debug", "Go to Forgot Password");
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            particleView.setVisibility(View.GONE);
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenFrameLayout, new ForgotPasswordFragment()).addToBackStack(null).commit();
        });
        gotoSignup.setOnClickListener(v -> {
            Log.d("Debug", "Go to Signup");
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            particleView.setVisibility(View.GONE);
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenFrameLayout, new RegisterFragment()).addToBackStack(null).commit();
        });

        gobackAuthen.setOnClickListener(v -> {
            Log.d("Debug", "Go to authen");
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setReorderingAllowed(true);
            particleView.setVisibility(View.GONE);
            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            transaction.replace(R.id.authenFrameLayout, new AuthenFragment()).addToBackStack(null).commit();
        });


        loginFunction.setOnClickListener(
                v -> login()
        );

        //Handler delay 2s
        new android.os.Handler().postDelayed(
                () -> particleView.setVisibility(View.VISIBLE),
                1500);
    }

    public void login() {
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
        if (!email.getText().toString().matches("^[A-Za-z\\d+_.-]+@(.+)$")) {
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

        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        MotionToast.Companion.createToast(getActivity(),
                                "Success 😊",
                                "Login successfully",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));

                                //Enter main activity
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                    } else {
                        MotionToast.Companion.createToast(getActivity(),
                                "Failed ☹️",
                                "Login failed",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
                    }
                }
        );

    }




}