package com.quochungcyou.proconnect.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.quochungcyou.proconnect.Fragment.ChatFragment;
import com.quochungcyou.proconnect.Fragment.HomeFragment;
import com.quochungcyou.proconnect.Fragment.LikeFragment;
import com.quochungcyou.proconnect.Fragment.ProfileFragment;

public class ScreenSlidePageAdapter extends FragmentStateAdapter {

    public ScreenSlidePageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ScreenSlidePageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ScreenSlidePageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new LikeFragment();
        } else if (position == 2) {
            return new ChatFragment();
        } else if (position == 3) {
            return new ProfileFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}