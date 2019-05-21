package com.app.bookbudiapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Adapters.HomeBookAdapter;
import Models.LoadHomeBooks;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Fragment {

    private static final String APP_ID = "ca-app-pub-1218708258118994~6078465910";
    AdView adView;
    String myValue;
    RecyclerView recycle;
    ArrayList<LoadHomeBooks> list;
    ProgressBar prog;
    ImageView nobook;


    private static final String URL = "https://bookbudiapp.herokuapp.com/loadBooks";

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MobileAds.initialize(getContext(), APP_ID);

        adView = view.findViewById(R.id.adView);
        prog = view.findViewById(R.id.progress2);
        nobook = view.findViewById(R.id.nobook);
        recycle = view.findViewById(R.id.recycle);


        list = new ArrayList<>();

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {

            @Override
            public void onAdClosed() {
                super.onAdClosed();

                adView.setVisibility(View.GONE);
            }
        });

        // Getting data from MainActivity

        final Bundle bundle = this.getArguments();

        if (bundle != null) {

            myValue = bundle.getString("data");

            OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(22, TimeUnit.SECONDS)
                .readTimeout(22, TimeUnit.SECONDS)
                .writeTimeout(22, TimeUnit.SECONDS)
                .build();

        RequestBody formBody = new FormBody.Builder().add("city", myValue).build();

        Request request = new Request.Builder().url(URL).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                prog.setVisibility(View.GONE);

                                JSONArray jsonArray = new JSONArray(response.body().string());

                                if (jsonArray.length() == 0) {

                                    nobook.setVisibility(View.VISIBLE);

                                }

                                for (int i = jsonArray.length() - 1; i > -1; i--) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String str1 = object.getString("Book_name");
                                    String str2 = object.getString("Book_image");
                                    String str3 = object.getString("Subject");
                                    String str4 = object.getString("Class");
                                    String str5 = object.getString("Id");
                                    String str6 = object.getString("User_id");

                                    LoadHomeBooks model = new LoadHomeBooks(str1, str2, str5, str6, str3, str4);

                                    list.add(model);
                                }

                                HomeBookAdapter adapter = new HomeBookAdapter(list, getActivity());

                                recycle.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call call, final IOException e) {

                if (getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            prog.setVisibility(View.GONE);

                            TastyToast.makeText(getActivity(), e.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                        }
                    });

                }

            }

        });


    }

        return view;
    }


}
