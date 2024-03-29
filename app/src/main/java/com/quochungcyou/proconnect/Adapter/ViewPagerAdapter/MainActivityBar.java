package com.quochungcyou.proconnect.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.quochungcyou.proconnect.Fragment.MainActivity.ChatFragment;
import com.quochungcyou.proconnect.Fragment.MainActivity.HomeFragment;
import com.quochungcyou.proconnect.Fragment.MainActivity.LikeFragment;
import com.quochungcyou.proconnect.Fragment.MainActivity.ProfileFragment;

public class MainActivityBar extends FragmentStateAdapter {

    public MainActivityBar(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public MainActivityBar(@NonNull Fragment fragment) {
        super(fragment);
    }

    public MainActivityBar(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new ChatFragment();
        } else if (position == 2) {
            return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}