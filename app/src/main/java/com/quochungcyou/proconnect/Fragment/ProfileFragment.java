package com.quochungcyou.proconnect.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.quochungcyou.proconnect.Activity.AuthenActivity;
import com.quochungcyou.proconnect.R;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ProfileFragment extends Fragment {

    MaterialButton signoutFunction;
    FirebaseAuth mAuth;
    CardView aboutCard, gotoEditProfile;
    ViewPager2 viewPagerMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPagerMain.setVisibility(View.VISIBLE);
    }

    public void initVar(View view) {
        signoutFunction = view.findViewById(R.id.signoutFunction);
        aboutCard = view.findViewById(R.id.aboutCard);
        gotoEditProfile = view.findViewById(R.id.gotoEditProfile);
        mAuth = FirebaseAuth.getInstance();
        viewPagerMain = getActivity().findViewById(R.id.view_pager);



        signoutFunction.setOnClickListener(v -> {
            mAuth.signOut();
            MotionToast.Companion.createColorToast(getActivity(), "Sign out", "Sign out successfully", MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM, MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(getActivity(), www.sanju.motiontoast.R.font.helvetica_regular));
            Intent intent = new Intent(getActivity(), AuthenActivity.class);
            startActivity(intent);
        });


        aboutCard.setOnClickListener(v -> {
            String url = "https://quochung.cyou";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        gotoEditProfile.setOnClickListener(v -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            viewPagerMain.setVisibility(View.GONE);
            transaction.replace(R.id.mainactivityFrameLayout, new EditProfileFragment()).addToBackStack(null).commit();

        });


    }
}