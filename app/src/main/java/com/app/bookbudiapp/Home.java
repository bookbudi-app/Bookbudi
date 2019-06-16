package com.app.bookbudiapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.HomeBookAdapter;
import Adapters.HomeOfferAdapter;
import Models.HomeOffersModel;
import Models.LoadHomeBooks;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Home extends Fragment {

    String myValue;
    RecyclerView recycle;
    ArrayList<LoadHomeBooks> list;
    ProgressBar prog;
    ImageView nobook;
    SwipeRefreshLayout refresh;
    HomeBookAdapter adapter;
    EditText search;

   // ViewPager homeOffers;
   // HomeOfferAdapter offerAdapter;
    List<HomeOffersModel> banners;

    private static final String URL = "https://bookbudiapp.herokuapp.com/loadBooks";
    private static final String HOME_OFFERS = "https://bookbudiapp.herokuapp.com/banners";

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

      //  homeOffers = view.findViewById(R.id.homeOffers);
        prog = view.findViewById(R.id.progress2);
        nobook = view.findViewById(R.id.nobook);
        recycle = view.findViewById(R.id.recycle);
        refresh = view.findViewById(R.id.refresh);
        search = getActivity().findViewById(R.id.search);

        refresh.setColorSchemeResources(android.R.color.holo_green_dark,
                                         android.R.color.holo_orange_dark,
                                         android.R.color.holo_blue_dark);

        list = new ArrayList<>();
     //   banners = new ArrayList<>();

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Getting data from MainActivity

        final Bundle bundle = this.getArguments();

        if (bundle != null) {

       //     loadHomeOffers();

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

                                 adapter = new HomeBookAdapter(list, getActivity());
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


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                if(refresh.isRefreshing()){

                    list.clear();
                    adapter.notifyDataSetChanged();

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

                                          //  prog.setVisibility(View.GONE);

                                            refresh.setRefreshing(false);

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

                                            adapter = new HomeBookAdapter(list, getActivity());

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

            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ArrayList<LoadHomeBooks> filterBooks = new ArrayList<>();

                for(LoadHomeBooks books: list){

                    String name = books.getbName().toLowerCase();
                    String sub = books.getbSub().toLowerCase();
                    String std = books.getbClass().toLowerCase();

                    if(name.contains(s.toString().toLowerCase()) || std.contains(s.toString().toLowerCase()) || sub.contains(s.toString().toLowerCase())){

                        filterBooks.add(books);
                    }

                    adapter.setFilter(filterBooks);
                }

            }
        });

    } //end if statement that checks bundle value.

        return view;

    }

  /*  private void loadHomeOffers(){

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(22,TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        Request request = new Request.Builder().url(HOME_OFFERS).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jsonArray = new JSONArray(response.body().string());

                                for (int i = jsonArray.length() - 1; i > -1; i--) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String str = jsonObject.getString("Image");

                                    Log.d("response",str);

                                    HomeOffersModel model = new HomeOffersModel(str);
                                    banners.add(model);

                                }

                                offerAdapter = new HomeOfferAdapter(banners, getActivity());
                                homeOffers.setAdapter(offerAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {

                                response.close();
                            }
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TastyToast.makeText(getActivity(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });

    } */


}
