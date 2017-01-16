package com.mapuw.lpop.ui.userhome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mapuw on 2016/12/20.
 */

public class UserHomeFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> mDatas;
    String [] titles = new String[]{"全部","原创","相册"};

    public UserHomeFragmentAdapter(FragmentManager fm, List<Fragment> mDatas)
    {
        super(fm);
        this.mDatas = mDatas;
    }

    @Override
    public Fragment getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return titles[position];
    }
}
