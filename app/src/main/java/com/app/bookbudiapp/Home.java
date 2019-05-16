package com.app.bookbudiapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {


    ViewPager bannerViewPager;
    BannerAdapter adapter;
    List<BannerModel> banners;
    TabLayout tabLayout;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    private static final String URI = "https://bookbudiapp.herokuapp.com/banners";

    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        bannerViewPager = view.findViewById(R.id.bannerViewpager);

        tabLayout.setupWithViewPager(bannerViewPager, true);

        banners = new ArrayList<>();

        loadViewPager();

        return view;
    }

    private void loadViewPager(){

        requestQueue = Volley.newRequestQueue(getActivity());

        stringRequest = new StringRequest(Request.Method.GET,URI, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String loadImages = jsonObject.getString("Image");

                        BannerModel model = new BannerModel(loadImages);
                        banners.add(model);

                    }

                    adapter = new BannerAdapter(banners,getActivity());

                    bannerViewPager.setAdapter(adapter);

                }catch(JSONException e){

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                TastyToast.makeText(getActivity(),"Error:"+error.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        });

        requestQueue.add(stringRequest);
    }

}
