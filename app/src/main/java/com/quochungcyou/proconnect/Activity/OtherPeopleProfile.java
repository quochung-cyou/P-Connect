package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.quochungcyou.proconnect.APIUtils.APIHelper;
import com.quochungcyou.proconnect.APIUtils.APIInterface;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.UserPostAdapter;
import com.quochungcyou.proconnect.Model.ArticleModel;
import com.quochungcyou.proconnect.Model.ResultModel;
import com.quochungcyou.proconnect.R;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class OtherPeopleProfile extends AppCompatActivity {
    TextView profileName;
    ImageView profileImage;
    private DatabaseReference databaseReference;
    RecyclerView recyclerView;
    List<ArticleModel> postlist;
    String fullname, username, avatarurl;
    MaterialButton addFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_people_profile);
        initVar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initVar();
    }

    private void initVar() {
        profileName = findViewById(R.id.profileName);
        profileImage = findViewById(R.id.profile_image);
        recyclerView = findViewById(R.id.recycleViewPost);
        addFriend = findViewById(R.id.addFriend);

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullScreenImage.class);
            intent.putExtra("useruid", getIntent().getExtras().getString("useruid"));
            this.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
            startActivity(intent);
        });

        updateData();
        initAddFriend();
    }


    private void initAddFriend() {
        addFriend.setOnClickListener(v -> {
            Bundle extras = getIntent().getExtras();
            String friendname = extras.getString("useruid");
            String myname = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/friend"); //tạo từ quan hệ bản thân
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("relation" + "/" + friendname + "/friend"); //tạo từ quan hệ của người kia
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.child(friendname).exists()) { //chưa tồn tại friend
                        databaseReference.child(friendname).setValue("ongoingsent");
                        databaseReference2.child(myname).setValue("ongoing");
                        MotionToast.Companion.createToast(OtherPeopleProfile.this,
                                "Sent friend request!",
                                "You have sent a friend request",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(OtherPeopleProfile.this,R.font.opensanlight));
                    } else { //đã tồn tại mối quan hệ
                        String relationship = snapshot.child(friendname).getValue(String.class); //lấy dữ liệu mỗi quan hệ
                        if (relationship.equals("ongoing")) { //đang chờ
                            MotionToast.Companion.createToast(OtherPeopleProfile.this,
                                    "You already sent friend request !",
                                    "You already sent a friend request",
                                    MotionToastStyle.INFO,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(OtherPeopleProfile.this,R.font.opensanlight));
                        } else if (relationship.equals("friend")) { //đã là bạn
                            MotionToast.Companion.createToast(OtherPeopleProfile.this,
                                    "Already friend!",
                                    "You are already friend with this person",
                                    MotionToastStyle.INFO,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(OtherPeopleProfile.this,R.font.opensanlight));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    MotionToast.Companion.createToast(OtherPeopleProfile.this,
                            "Error",
                            "Something went wrong",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(OtherPeopleProfile.this,R.font.opensanlight));
                }
            });

        });
    }





    private void updateData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            onBackPressed();
            return;
        }
        String useruid = extras.getString("useruid");
        databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + useruid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("name").setValue("Guess");
                }
                profileName.setText(name);
                fullname = name;
                name = snapshot.child("username").getValue(String.class);
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("username").setValue("Guess");
                }
                username = name;


                String avatar = snapshot.child("avatar").getValue(String.class);
                if (avatar != null && !avatar.isEmpty()) {
                    Glide.with(OtherPeopleProfile.this)
                            .load(avatar)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .placeholder(R.drawable.loadinganim).into(profileImage);
                }
                avatarurl = avatar;
                initRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void initRecyclerView() {
        Call<ResultModel> call;
        APIInterface apiInterface = APIHelper.getApiClient("https://newsdata.io/", this).create(APIInterface.class);
        call = apiInterface.getLastestHeadline("pub_1954270c22eae1320f8f448ffdf45c482fcf1", "us", "entertainment");
        call.enqueue(new Callback<ResultModel>() {

            @Override
            public void onResponse(Call<ResultModel> call, Response<ResultModel> response) {
                Log.d("HomeFragment", "null reponse " + response.isSuccessful() + " " + response + " " + response.body().getArticle().size());

                if (response.isSuccessful() && Objects.requireNonNull(response.body()).getArticle() != null) {
                    postlist = response.body().getArticle();
                    if (!postlist.isEmpty()) {
                        for (int i = 0; i < postlist.size(); i++) {
                            postlist.get(i).setDescription(fullname);
                            postlist.get(i).setSource_id(username);
                            postlist.get(i).setSummary(avatarurl);
                        }
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OtherPeopleProfile.this, LinearLayoutManager.VERTICAL, false);
                        UserPostAdapter adapter = new UserPostAdapter(OtherPeopleProfile.this , postlist);
                        recyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.d("OtherPeopleProfile", "Empty postlist");
                    }

                } else {
                    Log.d("OtherPeopleProfile", "null reponse " + response.isSuccessful() + " " + response);
                }
            }

            @Override
            public void onFailure(Call<ResultModel> call, Throwable t) {

                Log.d("OtherPeopleProfile", t.getMessage());
            }

        });
    }
}