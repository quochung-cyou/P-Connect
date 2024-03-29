package com.quochungcyou.proconnect.Fragment.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.quochungcyou.proconnect.Activity.QRActivity;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.UserAdapter;
import com.quochungcyou.proconnect.Model.MessageModel;
import com.quochungcyou.proconnect.Model.UserModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    List<UserModel> userList = new ArrayList<>();
    UserAdapter adapter;
    FloatingActionButton addButton;
    CircleImageView selfavatar;
    TextView selfname;

    String selfFullname, selfFullAvatar;
    LottieAnimationView loadpost;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
        initSearchView();
        initRecyclerView();
    }

    private void initVar() {
        recyclerView = getView().findViewById(R.id.recycleViewUserchat);
        searchView = getView().findViewById(R.id.searchView);
        addButton = getView().findViewById(R.id.addFriend);
        selfavatar = getView().findViewById(R.id.selfavatar);
        selfname = getView().findViewById(R.id.selfname);
        loadpost = getView().findViewById(R.id.loadPost);


        addButton.setOnClickListener(v -> {
            Intent intentMainActivity = new Intent(getActivity(), QRActivity.class);
            getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            startActivity(intentMainActivity);


        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new UserAdapter(getActivity() , userList);
        recyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


    //update to top bar of user
    private void updateData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class).toString().trim();
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("name").setValue("Guess");
                }
                selfname.setText(name);
                selfFullname = name;

                String avatar = snapshot.child("avatar").getValue(String.class).toString().trim();
                if (avatar != null && !avatar.isEmpty()) {
                    Glide.with(getActivity())
                            .load(avatar)
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                            .placeholder(R.drawable.loadinganim).into(selfavatar);
                }
                selfFullAvatar = avatar;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }

    //update user list
    private void initRecyclerView() {


        String myname = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/friend"); //tạo từ quan hệ bản thân
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadpost.setVisibility(View.VISIBLE);
                userList.clear();
                recyclerView.setVisibility(View.GONE);
                adapter.setLastPosition(-1);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getValue().toString().equals("friend")) {
                        String uid = dataSnapshot.getKey();
                        DatabaseReference frienddatabaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + uid);
                        frienddatabaseReference.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = snapshot.child("name").getValue().toString();
                                String profileImageUrl = snapshot.child("avatar").getValue().toString();
                                UserModel userModel = new UserModel(name, "", profileImageUrl, uid, 0L);
                                DatabaseReference lastMsgRef = FirebaseDatabase.getInstance().getReference("relation" + "/" + myname + "/chat/" + uid);
                                //Lấy câu chat cuối
                                lastMsgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList<MessageModel> messageList = new ArrayList<>();
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                                            messageList.add(messageModel);
                                        }
                                        if (messageList.size() != 0) {
                                            MessageModel lastMessage = messageList.get(messageList.size() - 1);
                                            if (lastMessage.getSender().equals(myname)) {
                                                lastMessage.setSendername(selfFullname);
                                                lastMessage.setSenderavatar(selfFullAvatar);
                                            } else {
                                                lastMessage.setSendername(name);
                                                lastMessage.setSenderavatar(profileImageUrl);
                                            }
                                            //Log.d("ChatFragment", "Last message " + lastMessage.getSendername().toString().trim() + " " + lastMessage.getMessage().toString().trim());
                                            userModel.setLastMessage(lastMessage.getSendername().toString().trim() + ": " + lastMessage.getMessage().toString().trim());
                                            userModel.setTimelastMessage(lastMessage.getTime());
                                        } else {
                                            userModel.setTimelastMessage(System.currentTimeMillis());
                                        }

                                        userList.add(userModel);
                                        userList.sort((o1, o2) -> {
                                            if (o1.getTimelastMessage() > o2.getTimelastMessage()) {
                                                return -1;
                                            } else if (o1.getTimelastMessage() < o2.getTimelastMessage()) {
                                                return 1;
                                            } else {
                                                return 0;
                                            }
                                        });
                                        loadpost.setVisibility(View.GONE);
                                        recyclerView.setVisibility(View.VISIBLE);
                                        adapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

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

    private void initSearchView() {

        searchView.setOnClickListener(v -> searchView.setIconified(false));

        searchView.setOnQueryTextFocusChangeListener((view, b) -> {
            if (!b) {
                if (searchView.getQuery().toString().length() < 1) {
                    searchView.setIconified(true);
                }

                searchView.clearFocus();

            }
        });
        //endregion
    }


}