package com.quochungcyou.proconnect.Adapter.ViewPagerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.quochungcyou.proconnect.Fragment.QrActivity.MyQrFragment;
import com.quochungcyou.proconnect.Fragment.QrActivity.ScanQrFragment;

public class QrActivityBar extends FragmentStateAdapter {

    public QrActivityBar(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public QrActivityBar(@NonNull Fragment fragment) {
        super(fragment);
    }

    public QrActivityBar(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new MyQrFragment();
        } else if (position == 1) {
            return new ScanQrFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}