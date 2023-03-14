package com.quochungcyou.proconnect.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.quochungcyou.proconnect.Adapter.ScreenSlidePageAdapter;
import com.quochungcyou.proconnect.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BubbleNavigationLinearView bubbleNavigationLinearView;
    private ScreenSlidePageAdapter pagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVar();
        initViewPager();


    }

    private void initVar() {
        viewPager = findViewById(R.id.view_pager);
        bubbleNavigationLinearView = findViewById(R.id.bottom_navigation_view_linear);
        pagerAdapter = new ScreenSlidePageAdapter(this);
    }

    private void initViewPager() {
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);
        viewPager.setAdapter(pagerAdapter);
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