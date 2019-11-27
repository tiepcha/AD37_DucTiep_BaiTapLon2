package com.example.myapplication;

import com.example.myapplication.Interface.IonClickCategory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {


    private List<Fragment> fragmentList = new ArrayList<>();
    private  List<String> titleList = new ArrayList<>();

    IonClickCategory ionClickCategory;

    public PagerAdapter(@NonNull FragmentManager fm,  IonClickCategory ionClickCategory) {
        super(fm);
        this.ionClickCategory = ionClickCategory;
    }

    public void setIonClickCategory(IonClickCategory ionClickCategory) {
        this.ionClickCategory = ionClickCategory;
    }

    public  void add(Fragment fragment, String title){


        fragmentList.add(fragment);

        titleList.add(title);

    }
    public  void open(Fragment fragment,String title){


        fragmentList.set(2,fragment);

        titleList.add(title);

    }




    public PagerAdapter( FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }



}
