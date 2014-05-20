package com.augmate;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MenuPagerAdapter extends FragmentPagerAdapter
{
    private List<MenuPagerMenuItem> mMenuItems;

    public MenuPagerAdapter(FragmentManager fm, List<MenuPagerMenuItem> fragments)
    {
        super(fm);
        this.mMenuItems = fragments;
    }

    @Override
    public Fragment getItem(int position) 
    {
        return this.mMenuItems.get(position);
    }

    @Override
    public int getCount() {
        return this.mMenuItems.size();
    }
}