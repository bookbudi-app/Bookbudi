package com.app.bookbudiapp;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.MoreBooksAdapter;
import Models.MoreBooksModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoreBooksByUser extends AppCompatActivity {

    String str;
    TextView moreName;
    ImageView moreProfileImg;
    ProgressBar moreProg;
    RelativeLayout rel;
    RecyclerView moreRecycle;
    List<MoreBooksModel> list;

    //private static final String URL = "https://bookbudiapp.herokuapp.com/getMoreBooks";

    private static final String URL = "https://bookbudi-prod.herokuapp.com/getMoreBooks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_books_by_user);

        ActionBar ab = getSupportActionBar();
        if(ab != null){
            ab.hide();
        }

        moreName = findViewById(R.id.moreName);
        moreRecycle = findViewById(R.id.moreRecycle);
        moreProg = findViewById(R.id.moreProg);
        moreProfileImg = findViewById(R.id.moreProfileImg);
        rel = findViewById(R.id.rel);

        list = new ArrayList<>();

        moreRecycle.setHasFixedSize(true);
        moreRecycle.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        str = intent.getStringExtra("userid");

        getUserBooks(str);

    }

    private void getUserBooks(String str) {

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(20, TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        RequestBody body = new FormBody.Builder().add("userId",str).build();

        Request request = new Request.Builder().url(URL).post(body).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        moreProg.setVisibility(View.GONE);
                        rel.setVisibility(View.VISIBLE);

                        try {

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i =0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String bookImg = jsonObject.getString("bookImg");
                                String bookName = jsonObject.getString("bookName");
                                String bookSubject = jsonObject.getString("bookSubject");
                                String bookClass = jsonObject.getString("bookClass");
                                String bookCity = jsonObject.getString("bookCity");
                                String userName = jsonObject.getString("userName");
                                String profile = jsonObject.getString("profileImage");

                                moreName.setText(userName);

                                RequestOptions options = new RequestOptions();
                                options.placeholder(R.drawable.user);

                                Glide.with(getApplicationContext()).load(profile).apply(options).into(moreProfileImg);

                                MoreBooksModel model = new MoreBooksModel(bookImg,bookName,bookSubject,bookClass,bookCity);

                                list.add(model);
                            }

                            MoreBooksAdapter adapter = new MoreBooksAdapter(list,getApplicationContext());
                            moreRecycle.setAdapter(adapter);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call call, final IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        moreProg.setVisibility(View.GONE);

                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(MoreBooksByUser.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
