package com.app.bookbudiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookDetail extends AppCompatActivity {


    private static final String URL = "https://bookbudiapp.herokuapp.com/bookDetail";

    TextView detailBookName,detailBookSubject,detailClass,detailPrice;
    ImageView detailBookImage;
    ProgressBar progBar;
    FloatingActionButton chat;
    String str6;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.hide();

        detailBookName = findViewById(R.id.detailBookName);
        detailBookSubject = findViewById(R.id.detailBookSubject);
        detailClass = findViewById(R.id.detailClass);
        detailPrice = findViewById(R.id.detailPrice);
        detailBookImage = findViewById(R.id.detailBookImage);
        progBar = findViewById(R.id.progBar);
        chat = findViewById(R.id.chat);

        detailBookName.setVisibility(View.GONE);
        detailBookSubject.setVisibility(View.GONE);
        detailClass.setVisibility(View.GONE);
        detailPrice.setVisibility(View.GONE);
        detailBookImage.setVisibility(View.GONE);
        chat.setVisibility(View.GONE);

        Intent intent = getIntent();
        String str = intent.getStringExtra("bId");

        chat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(BookDetail.this,Chat.class);
                i.putExtra("name",str6);
                startActivity(i);
                finish();
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .build();

        RequestBody formBody = new FormBody.Builder().add("bId",str).build();

        Request request = new Request.Builder().url(URL).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            progBar.setVisibility(View.GONE);

                            detailBookName.setVisibility(View.VISIBLE);
                            detailBookSubject.setVisibility(View.VISIBLE);
                            detailClass.setVisibility(View.VISIBLE);
                            detailPrice.setVisibility(View.VISIBLE);
                            detailBookImage.setVisibility(View.VISIBLE);
                            chat.setVisibility(View.VISIBLE);

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i = 0;i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String str1 = object.getString("Book_name");
                                String str2 = object.getString("Book_image");
                                String str3 = object.getString("Subject");
                                String str4 = object.getString("Cost");
                                String str5 = object.getString("Class");
                                 str6 = object.getString("Name");

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.openbook);

                                Glide.with(BookDetail.this).load(str2).apply(requestOptions).into(detailBookImage);

                                detailBookName.setText(String.format("Book name: %s", str1));
                                detailBookSubject.setText(String.format("Subject: %s", str3));
                                detailClass.setText(String.format("Standard: %s", str5));
                                detailPrice.setText(String.format("Price: %s", str4));

                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (IOException e) {
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

                        progBar.setVisibility(View.GONE);

                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show();

                    }
                });
            }

        });

    }
}
