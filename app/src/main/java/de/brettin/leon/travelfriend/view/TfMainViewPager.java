package de.brettin.leon.travelfriend.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * Created by Leon on 08.04.18.
 */

public class TfMainViewPager extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragments;

    public TfMainViewPager(FragmentManager fm, OnMapReadyCallback onMapsReadyListener) {
        super(fm);

        TfMapsFragment mapsFragment = TfMapsFragment.newInstance();
        mapsFragment.setOnMapReadyListener(onMapsReadyListener);

        mFragments = new ArrayList<>();
        mFragments.add(mapsFragment);
        mFragments.add(TfAccountFragment.newInstance());

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
