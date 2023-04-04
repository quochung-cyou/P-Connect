package com.quochungcyou.proconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aghajari.emojiview.AXEmojiManager;
import com.aghajari.emojiview.microsoft3dprovider.AXMicrosoft3DEmojiProvider;
import com.aghajari.emojiview.view.AXEmojiEditText;
import com.aghajari.emojiview.view.AXEmojiPopup;
import com.aghajari.emojiview.view.AXEmojiView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.MessageAdapter;
import com.quochungcyou.proconnect.Model.MessageModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendMessageActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<MessageModel> messageList;
    MessageAdapter adapter;
    ImageView back_button, emojiicon, sendicon;
    AXEmojiEditText chatMessage;
    String selfuid, targetuid;
    String selfname, targetname;
    String selfavatar, targetavatar;
    ImageView avatartargetimage;
    TextView targetnametextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        AXEmojiManager.install(this, new AXMicrosoft3DEmojiProvider(this));
        passData();
        initVar();
        initChatMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initChatMessage();
    }

    private void passData() {
        Intent intent = getIntent();
        selfuid = intent.getStringExtra("selfuid");
        targetuid = intent.getStringExtra("targetuid");
        targetname = intent.getStringExtra("targetname");
        targetavatar = intent.getStringExtra("targetavatar");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users" + "/" + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                if (name == null || name.isEmpty()) {
                    name = "Guess";
                    databaseReference.child("name").setValue("Guess");
                }
                selfname = name;

                selfavatar = snapshot.child("avatar").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void initVar() {
        recyclerView = findViewById(R.id.recycleViewChatMessage);
        back_button = findViewById(R.id.back_button);
        chatMessage = findViewById(R.id.chatMessage);
        emojiicon = findViewById(R.id.emojiicon);
        sendicon = findViewById(R.id.sendicon);
        avatartargetimage = findViewById(R.id.avatartargetimage);
        targetnametextview = findViewById(R.id.targetnametextview);



        AXEmojiView emojiView = new AXEmojiView(this);
        emojiView.setEditText(chatMessage);
        AXEmojiPopup emojiPopup = new AXEmojiPopup(emojiView);

        sendicon.setOnClickListener(v -> {
            String message = chatMessage.getText().toString();
            if (!message.isEmpty()) {
                sendMessage(selfuid, targetuid, message);
                chatMessage.setText("");
            }
        });

        Glide.with(this).load(targetavatar).diskCacheStrategy(DiskCacheStrategy.ALL).priority(Priority.HIGH).into(avatartargetimage);
        targetnametextview.setText(targetname);

        emojiicon.setOnClickListener(v -> emojiPopup.toggle());
        back_button.setOnClickListener(v -> onBackPressed());


    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("relation" + "/" + selfuid +"/chat/" + targetuid);
        databaseReference.push().setValue(new MessageModel(System.currentTimeMillis(), message, sender, receiver, selfavatar, targetavatar, selfname, targetname));
        DatabaseReference databaseReferenceTarget = FirebaseDatabase.getInstance().getReference("relation" + "/" + targetuid + "/chat/" + selfuid);
        databaseReferenceTarget.push().setValue(new MessageModel(System.currentTimeMillis(), message, sender, receiver, selfavatar, targetavatar, selfname, targetname));
    }


    private void initChatMessage() {
        messageList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SendMessageActivity.this, LinearLayoutManager.VERTICAL, false);
        adapter = new MessageAdapter(SendMessageActivity.this , messageList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        DatabaseReference databaseReference =  FirebaseDatabase.getInstance().getReference("relation" + "/" + selfuid + "/chat/" + targetuid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    if (messageModel.getSender().equals(selfuid)) {
                        messageModel.setSendername(selfname);
                        messageModel.setSenderavatar(selfavatar);
                    } else {
                        messageModel.setSendername(targetname);
                        messageModel.setSenderavatar(targetavatar);
                    }

                    messageList.add(messageModel);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ProfileFragment", "onCancelled: " + error.getMessage());
            }
        });
    }



}