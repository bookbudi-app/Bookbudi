package com.app.bookbudiapp;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

import Adapters.MyPostedBookAdapter;
import Models.PostedModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Tab2Fragment extends Fragment {


    RecyclerView recyclerView;
    ProgressBar progressBar1;
    ImageView postImage;
    List<PostedModel> listItem;

    FirebaseAuth fAuth;
    FirebaseUser user;
    String id;

   // private static final String URL = "https://bookbudiapp.herokuapp.com/postedBook";

    private static final String URL = "https://bookbudi-prod.herokuapp.com/postedBook";

    public Tab2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        postImage = view.findViewById(R.id.postImage);
        progressBar1 = view.findViewById(R.id.progressBar1);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listItem = new ArrayList<>();

        loadPostedBook();

        return view;
    }

    private void loadPostedBook(){

        id = user.getUid();

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(22, TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        final RequestBody formBody = new FormBody.Builder()
                                  .add("userId",id)
                                  .build();

        Request request = new Request.Builder().url(URL).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if(getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                progressBar1.setVisibility(View.GONE);

                                JSONArray array = new JSONArray(response.body().string());

                                if (array.length() == 0) {

                                    postImage.setVisibility(View.VISIBLE);
                                }

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    String str1 = object.getString("Book_name");
                                    String str2 = object.getString("Book_image");
                                    String str3 = object.getString("Id");

                                    PostedModel model = new PostedModel(str2, str1, str3);

                                    listItem.add(model);
                                }

                                MyPostedBookAdapter adapter = new MyPostedBookAdapter(listItem, getActivity());
                                recyclerView.setAdapter(adapter);

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

                if(getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            progressBar1.setVisibility(View.GONE);

                            TastyToast.makeText(getActivity(), "Error:" + e.getMessage(), TastyToast.LENGTH_LONG
                                    , TastyToast.ERROR).show();
                        }
                    });

                }
            }
        });
    }

}
