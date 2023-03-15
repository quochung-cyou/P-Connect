package com.quochungcyou.proconnect.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.quochungcyou.proconnect.R;

public class HomeFragment extends Fragment {

    ViewPager2 viewPagerMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initVar(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPagerMain = getActivity().findViewById(R.id.view_pager);
        //viewPagerMain.setVisibility(View.VISIBLE);
    }

    public void initVar(View view) {
        viewPagerMain = getActivity().findViewById(R.id.view_pager);


    }
}