package com.app.bookbudiapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.FavBookAdapter;
import Models.FavouriteBooksModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Tab1Fragment extends Fragment {

    RecyclerView favBook;
    FavBookAdapter adapter;
    List<FavouriteBooksModel> lists;
    FirebaseAuth fAuth;
    ImageView favImage;
    ProgressBar favProgress;
    FirebaseUser user;
    String userid;

    private static final String URL = "https://bookbudiapp.herokuapp.com/loadWishlist";

    public Tab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        favBook = view.findViewById(R.id.favBook);
        favImage = view.findViewById(R.id.favImage);
        favProgress = view.findViewById(R.id.favProgress);

        favBook.setHasFixedSize(true);
        favBook.setLayoutManager(new LinearLayoutManager(getActivity()));

        lists = new ArrayList<>();

        loadfavBooks();

        return view;
    }

    private void loadfavBooks(){


        OkHttpClient client = new OkHttpClient.Builder()
                                  .connectTimeout(22, TimeUnit.SECONDS)
                                  .readTimeout(22,TimeUnit.SECONDS)
                                  .writeTimeout(22,TimeUnit.SECONDS)
                                  .build();

        RequestBody formBody = new FormBody.Builder().add("uId",user.getUid()).build();

        final Request request = new Request.Builder().url(URL).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                favProgress.setVisibility(View.INVISIBLE);

                                JSONArray jsonArray = new JSONArray(response.body().string());

                                if (jsonArray.length() == 0) {

                                    favImage.setVisibility(View.VISIBLE);

                                }

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String str1 = jsonObject.getString("BookImage");
                                    String str2 = jsonObject.getString("BookName");
                                    String str3 = jsonObject.getString("UserId");
                                    String str4 = jsonObject.getString("BookId");

                                    FavouriteBooksModel model = new FavouriteBooksModel(str1, str2, str3, str4);

                                    lists.add(model);
                                }

                                adapter = new FavBookAdapter(lists, getActivity());
                                favBook.setAdapter(adapter);

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

                        favProgress.setVisibility(View.INVISIBLE);

                        TastyToast.makeText(getActivity(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });

    }

}
