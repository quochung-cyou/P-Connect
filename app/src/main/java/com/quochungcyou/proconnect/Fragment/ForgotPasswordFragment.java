package com.quochungcyou.proconnect.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.R;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ForgotPasswordFragment extends Fragment {

    TextInputEditText email;

    MaterialButton resetpassFunction;
    final FirebaseAuth fAuth = FirebaseAuth.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);

    }

    public void initVar(View view) {
        email = view.findViewById(R.id.emailLogin);
        resetpassFunction = view.findViewById(R.id.resetPassFunction);


        //transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        resetpassFunction.setOnClickListener(
                v -> resetPass()
        );

    }

    private void resetPass() {
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

        fAuth.sendPasswordResetEmail(email.getText().toString().trim()).addOnSuccessListener(task -> MotionToast.Companion.createToast(getActivity(),
                "Success 😊",
                "Sent reset email to your email address",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getActivity(),R.font.opensanlight))).addOnFailureListener(task -> {
            MotionToast.Companion.createToast(getActivity(),
                    "Failed ☹️",
                    "Some error happened",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getActivity(),R.font.opensanlight));
            Log.e("Error", task.getMessage());

        });




    }


}