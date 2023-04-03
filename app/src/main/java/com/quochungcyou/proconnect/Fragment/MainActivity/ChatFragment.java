package com.quochungcyou.proconnect.Fragment.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.itemanimators.SlideLeftAlphaAnimator;
import com.quochungcyou.proconnect.Activity.QRActivity;
import com.quochungcyou.proconnect.Adapter.RecylerViewAdapter.UserAdapter;
import com.quochungcyou.proconnect.Model.UserModel;
import com.quochungcyou.proconnect.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    List<UserModel> userList;
    UserAdapter adapter;
    FloatingActionButton addButton;


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
        initVar();
    }

    private void initVar() {
        recyclerView = getView().findViewById(R.id.recycleViewUserchat);
        searchView = getView().findViewById(R.id.searchView);
        addButton = getView().findViewById(R.id.addFriend);
        initSearchView();
        initRecyclerView();
        addButton.setOnClickListener(v -> {
            Intent intentMainActivity = new Intent(getActivity(), QRActivity.class);
            getActivity().overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            startActivity(intentMainActivity);


        });

    }


    private void initRecyclerView() {
        userList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        //add some test user
        for (int i = 0; i < 10; i++) {
            userList.add(new UserModel("1", "Quoc", "https://i.pinimg.com/originals/0c/0d/0d/0c0d0d0d0d0d0d0d0d0d0d0d0d0d0d0d.jpg"));
        }

        adapter = new UserAdapter(getActivity() , userList);
        recyclerView.setItemAnimator(new SlideLeftAlphaAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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