package com.quochungcyou.proconnect.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochungcyou.proconnect.Activity.AuthenActivity;
import com.quochungcyou.proconnect.Activity.EditProfileActivity;
import com.quochungcyou.proconnect.R;

import java.util.Objects;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ProfileFragment extends Fragment {

    MaterialButton signoutFunction;
    FirebaseAuth mAuth;
    CardView aboutCard, gotoEditProfile;
    TextView profileName;
    private  DatabaseReference databaseReference;

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
        updateData();
    }

    @Override
    public void onResume() {
        super.onResume();
        //viewPagerMain.setVisibility(View.VISIBLE);
    }

    public void initVar(View view) {
        signoutFunction = view.findViewById(R.id.signoutFunction);
        aboutCard = view.findViewById(R.id.aboutCard);
        gotoEditProfile = view.findViewById(R.id.gotoEditProfile);
        mAuth = FirebaseAuth.getInstance();
        profileName = view.findViewById(R.id.profileName);



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
            Intent intentMainActivity = new Intent(getActivity(), EditProfileActivity.class);
            getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(intentMainActivity);
        });


    }

    private void updateData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("name").setValue("Guess");
                }
                profileName.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }
}