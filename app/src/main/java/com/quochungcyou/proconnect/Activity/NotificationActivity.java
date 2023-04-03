package com.quochungcyou.proconnect.Activity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.NotificationAdapter;
import com.quochungcyou.proconnect.Model.NotificationModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    TextView notificationText;
    RecyclerView recyclerviewnotif;
    ImageView backButton;
    private DatabaseReference databaseReference;
    List<NotificationModel> notificationModels;
    NotificationAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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
    }



    private void initVar() {
        notificationText = findViewById(R.id.notificationtext);
        recyclerviewnotif = findViewById(R.id.recyclerviewnotif);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(v -> onBackPressed());

        initRecyclerView();

    }

    public void initRecyclerView() {
        notificationModels = new ArrayList<>();
        notificationModels.clear();
        recyclerviewnotif.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new NotificationAdapter(this, notificationModels);
        recyclerviewnotif.setAdapter(adapter);



        String myname = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/friend"); //tạo từ quan hệ bản thân

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String type = dataSnapshot.getValue(String.class);
                    if (type.equals("ongoing")) {
                        Log.d("NotificationActivity-Debug", "Read friend " + dataSnapshot.getKey().toString());
                        String friendname = dataSnapshot.getKey().toString();
                        DatabaseReference databaseReferenceFriend = FirebaseDatabase.getInstance().getReference("users" + "/" + friendname);
                        databaseReferenceFriend.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String fullname = snapshot.child("name").getValue(String.class);
                                String avatarurl = snapshot.child("avatar").getValue(String.class);
                                Log.d("NotificationActivity-Debug", "Read friend " + fullname + " " + avatarurl);
                                NotificationModel notificationModel = new NotificationModel(friendname, fullname, avatarurl);
                                notificationModels.add(notificationModel);
                                if (notificationModels.size() != 0) {
                                    notificationText.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}