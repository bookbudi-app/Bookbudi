package com.app.bookbudiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.FaqAdapter;
import Models.FaqModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Faq extends AppCompatActivity {

    private static final String URL = "https://bookbudi-prod.herokuapp.com/loadFaq";

    RecyclerView faqRecycle;
    List<FaqModel> faqList;
    ProgressBar faqProgress;
    FaqAdapter faqAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        ActionBar ab = getSupportActionBar();
        assert ab!= null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("FAQs");

        faqProgress = findViewById(R.id.faqProgress);
        faqRecycle = findViewById(R.id.faqRecycle);
        faqRecycle.setLayoutManager(new LinearLayoutManager(this));
        faqRecycle.setHasFixedSize(true);

        faqList = new ArrayList<>();

        loadFaq();

    }

    private void loadFaq(){

        OkHttpClient client = new OkHttpClient.Builder()
                              .readTimeout(20, TimeUnit.SECONDS)
                              .writeTimeout(20,TimeUnit.SECONDS)
                              .build();

        Request request = new Request.Builder().url(URL).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            faqProgress.setVisibility(View.INVISIBLE);
                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i = 0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String str1 = jsonObject.getString("Question");
                                String str2 = jsonObject.getString("Answer");

                                Log.d("Question",str1);
                                Log.d("Answer",str2);

                                FaqModel model = new FaqModel(str1,str2);

                                faqList.add(model);

                            }

                            faqAdapter = new FaqAdapter(faqList,getApplicationContext());

                            faqRecycle.setAdapter(faqAdapter);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TastyToast.makeText(getApplicationContext(),"Failed to connect with server",TastyToast.LENGTH_LONG,
                                         TastyToast.ERROR).show();
                    }
                });
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch(id){

            case android.R.id.home:

                Intent i = new Intent(Faq.this,MainActivity.class);
                startActivity(i);
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent back = new Intent(Faq.this,MainActivity.class);
        startActivity(back);
        finish();

    }
}
