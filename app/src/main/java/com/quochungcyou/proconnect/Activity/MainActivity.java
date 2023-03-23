package com.quochungcyou.proconnect.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochungcyou.proconnect.Adapter.ScreenSlidePageAdapter;
import com.quochungcyou.proconnect.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BubbleNavigationLinearView bubbleNavigationLinearView;
    private ScreenSlidePageAdapter pagerAdapter;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);

            if (getWindow().getInsetsController() != null) {
                getWindow().getInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                getWindow().getInsetsController().setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        initVar();
        initViewPager();
        nullData();


    }

    private void initVar() {
        viewPager = findViewById(R.id.view_pager);
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        pagerAdapter = new ScreenSlidePageAdapter(this);
        //viewPager.setOffscreenPageLimit(2);

    }

    private void nullData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String studentID = snapshot.child("studentID").getValue(String.class);
                String classname = snapshot.child("class").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                String dateofbirth = snapshot.child("dateofbirth").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                //check name is null or empty then set as guess
                if (name == null || name.isEmpty()) {
                    databaseReference.child("name").setValue("Guess");
                }
                //check studentID is null or empty then set as no data
                if (studentID == null || studentID.isEmpty()) {
                    databaseReference.child("studentID").setValue("No data");
                }
                //check class is null or empty then set as no data
                if (classname == null || classname.isEmpty()) {
                    databaseReference.child("class").setValue("No data");
                }
                //check username is null or empty then set as guess
                if (username == null || username.isEmpty()) {
                    databaseReference.child("username").setValue("Guess");
                }
                //check dateofbirth is null or empty then set as no data
                if (dateofbirth == null || dateofbirth.isEmpty()) {
                    databaseReference.child("dateofbirth").setValue("No data");
                }
                //check phone is null or empty then set as no data
                if (phone == null || phone.isEmpty()) {
                    databaseReference.child("phone").setValue("No data");
                }
                //check location is null or empty then set as no data
                if (location == null || location.isEmpty()) {
                    databaseReference.child("location").setValue("No data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("MainActivity", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void initViewPager() {
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);

        viewPager.setAdapter(pagerAdapter);
        AccordionTransformer transformation = new AccordionTransformer();
        viewPager.setPageTransformer(transformation);

        if (getIntent().getExtras() != null) {
            String fragment = getIntent().getExtras().getString("fragment");
            if (fragment != null) {
                if (fragment.equals("profile")) {
                    viewPager.setCurrentItem(2, true);
                }
            }
        }
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bubbleNavigationLinearView.setCurrentActiveItem(position);
            }
        });
        bubbleNavigationLinearView.setNavigationChangeListener((view, position) -> viewPager.setCurrentItem(position, true));

    }
}

 class AccordionTransformer implements ViewPager2.PageTransformer {

     @Override
     public void transformPage(@NonNull View view, float position) {
         view.setPivotX(position < 0 ? 0 : view.getWidth());
         view.setScaleX(position < 0 ? 1f + position : 1f - position);
     }
 }