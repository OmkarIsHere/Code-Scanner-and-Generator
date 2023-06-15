package com.example.codescannergenerator;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final String TAG = "ViewPagerAdapter";
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new ScanFragment();
            case 1:
                return new GenerateFragment();
            default:
                return new ScanFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
