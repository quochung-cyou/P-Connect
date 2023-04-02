package com.quochungcyou.proconnect.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.MessageAdapter;
import com.quochungcyou.proconnect.Model.MessageModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    List<MessageModel> messageList;
    MessageAdapter adapter;
    ImageView back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        initVar();
        initChatMessage();
    }

    private void initVar() {
        recyclerView = findViewById(R.id.recycleViewChatMessage);
        back_button = findViewById(R.id.back_button);

        back_button.setOnClickListener(v -> onBackPressed());
    }

    private void initChatMessage() {
        messageList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SendMessageActivity.this, LinearLayoutManager.VERTICAL, false);
        //add some test user
        for (int i = 0; i < 10; i++) {
            messageList.add(new MessageModel(System.currentTimeMillis(), "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum." ,  String.valueOf(i), String.valueOf(i)));
        }

        adapter = new MessageAdapter(SendMessageActivity.this , messageList);
        recyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}