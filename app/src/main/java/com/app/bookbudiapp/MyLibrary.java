package com.app.bookbudiapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyLibrary extends Fragment {

    private TabLayout my_tab;
    private ViewPager pager;
    private TabAdapter adapter;

    public MyLibrary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_library, container, false);

        my_tab = view.findViewById(R.id.my_tab);
        pager = view.findViewById(R.id.pager);

        adapter = new TabAdapter(getChildFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Purchased");
        adapter.addFragment(new Tab2Fragment(), "Posted");

        my_tab.setupWithViewPager(pager);

        pager.setAdapter(adapter);

        return view;
    }

}
