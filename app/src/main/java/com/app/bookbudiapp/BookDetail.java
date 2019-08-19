package com.app.bookbudiapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookDetail extends AppCompatActivity {


  /*  private static final String URL = "https://bookbudiapp.herokuapp.com/bookDetail";
    private static final String WISHLIST_URI = "https://bookbudiapp.herokuapp.com/checkWishlist";
    private static final String ADD_TO_WISHLIST_URL = "https://bookbudiapp.herokuapp.com/addtoWishlist";
    private static final String REMOVE_FROM_WISHLIST = "https://bookbudiapp.herokuapp.com/removeFromWishlist";
    private static final String PHONE_CALL = "https://bookbudiapp.herokuapp.com/getPhone";  */

    private static final String URL = "https://bookbudi-prod.herokuapp.com/bookDetail";
    private static final String WISHLIST_URI = "https://bookbudi-prod.herokuapp.com/checkWishlist";
    private static final String ADD_TO_WISHLIST_URL = "https://bookbudi-prod.herokuapp.com/addtoWishlist";
    private static final String REMOVE_FROM_WISHLIST = "https://bookbudi-prod.herokuapp.com/removeFromWishlist";
    private static final String PHONE_CALL = "https://bookbudi-prod.herokupp.com/getPhone";

    TextView detailBookName,detailBookSubject,detailClass,detailPrice,moreByUser,name,detailBookCity;
    ImageView detailBookImage;
    CardView card_class_prc,card_nam_sub,userDetail;
    ProgressBar progBar;
    View view1,view2,view3;
    TextView disclaimer,fav;
    Button callButton;
   // FloatingActionButton chat;
    String str6,str7;
    FirebaseAuth fAuth;
    FirebaseUser user;

    FirebaseDatabase firData;
    DatabaseReference dRef;

    String str,strbImage,strbName,strbSub,strbClass,userid,uid;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.hide();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        if(user != null){

            userid = user.getUid();
        }

        detailBookName = findViewById(R.id.detailBookName);
        detailBookSubject = findViewById(R.id.detailBookSubject);
        detailBookCity = findViewById(R.id.detailBookCity);
        detailClass = findViewById(R.id.detailClass);
        detailPrice = findViewById(R.id.detailPrice);
        detailBookImage = findViewById(R.id.detailBookImage);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        //heartButton = findViewById(R.id.heartButton);
        fav = findViewById(R.id.fav);
        progBar = findViewById(R.id.progBar);
        card_nam_sub = findViewById(R.id.card_nam_sub);
        card_class_prc = findViewById(R.id.card_class_prc);
        userDetail  = findViewById(R.id.userDetail);
        disclaimer = findViewById(R.id.disclaimer);
        callButton = findViewById(R.id.callButton);
        name = findViewById(R.id.name);
        moreByUser = findViewById(R.id.moreByUser);
      //  chat = findViewById(R.id.chat);

        detailBookName.setVisibility(View.GONE);
        detailBookSubject.setVisibility(View.GONE);
        detailBookCity.setVisibility(View.GONE);
        detailClass.setVisibility(View.GONE);
        detailPrice.setVisibility(View.GONE);
        detailBookImage.setVisibility(View.GONE);
        view1.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
        fav.setVisibility(View.INVISIBLE);
        card_nam_sub.setVisibility(View.INVISIBLE);
        card_class_prc.setVisibility(View.INVISIBLE);
        userDetail.setVisibility(View.INVISIBLE);
        disclaimer.setVisibility(View.INVISIBLE);
        callButton.setVisibility(View.INVISIBLE);

       // chat.setVisibility(View.GONE);

         Intent intent = getIntent();
         str = intent.getStringExtra("bId");
         strbImage = intent.getStringExtra("bookImage");
         strbName = intent.getStringExtra("bookName");
         strbSub = intent.getStringExtra("bookSub");
         strbClass = intent.getStringExtra("bookClass");
         uid = intent.getStringExtra("uid");

         callButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 dRef = firData.getInstance().getReference();

                 HashMap<String,String> map = new HashMap<String,String>();

                 map.put("Caller",user.getUid());
                 map.put("Call_to",uid);
                 map.put("Book_image",strbImage);
                 map.put("Book_name",strbName);
                 map.put("Subject",strbSub);
                 map.put("Class",strbClass);

                 dRef.child("Callings").push().setValue(map);

                 if(!user.getUid().equals(uid)){

                     calling(uid);
                     callButton.setVisibility(View.VISIBLE);

                 }else{

                     callButton.setVisibility(View.INVISIBLE);
                 }

             }
         });

         fav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(fav.getText().toString().equals("ADD TO FAVOURITES")){

                   addtoWishlist(userid,str,strbImage,strbName,strbSub,strbClass);

                }else{

                   removefromWishlist(str,userid);
               }

            }
        });

        checkWishlist(str,userid);

        moreByUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(BookDetail.this,MoreBooksByUser.class);
                in.putExtra("userid",uid);
                startActivity(in);
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
            public void onResponse(final Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            progBar.setVisibility(View.GONE);

                            detailBookName.setVisibility(View.VISIBLE);
                            detailBookSubject.setVisibility(View.VISIBLE);
                            detailBookCity.setVisibility(View.VISIBLE);
                            detailClass.setVisibility(View.VISIBLE);
                            detailPrice.setVisibility(View.VISIBLE);
                            detailBookImage.setVisibility(View.VISIBLE);
                            fav.setVisibility(View.VISIBLE);
                            view1.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                            card_class_prc.setVisibility(View.VISIBLE);
                            card_nam_sub.setVisibility(View.VISIBLE);
                            userDetail.setVisibility(View.VISIBLE);
                            disclaimer.setVisibility(View.VISIBLE);
                            callButton.setVisibility(View.VISIBLE);
                            // chat.setVisibility(View.VISIBLE);

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i = 0;i<jsonArray.length();i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String str1 = object.getString("Book_name");
                                String str2 = object.getString("Book_image");
                                String str3 = object.getString("Subject");
                                String str4 = object.getString("Cost");
                                String str5 = object.getString("Class");
                                String str8 = object.getString("City");
                                 str6 = object.getString("Name");
                                 str7 = object.getString("User_id");

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.placeholder(R.drawable.openbook);

                                Glide.with(BookDetail.this).load(str2).apply(requestOptions).into(detailBookImage);

                                detailBookName.setText(String.format("Book name: %s", str1));
                                detailBookSubject.setText(String.format("Subject: %s", str3));
                                detailBookCity.setText(String.format("City: %s", str8));
                                detailClass.setText(String.format("Standard: %s", str5));
                                detailPrice.setText(String.format("Price: %s", str4));

                                name.setText(String.format("Posted by: %s", str6));

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

    private void checkWishlist(String str,String userid){

           OkHttpClient client = new OkHttpClient.Builder()
                                 .connectTimeout(30,TimeUnit.SECONDS)
                                 .readTimeout(30,TimeUnit.SECONDS)
                                 .writeTimeout(30,TimeUnit.SECONDS)
                                 .build();

           RequestBody body = new FormBody.Builder()
                                       .add("bId",str)
                                       .add("uId",userid)
                                       .build();

           Request request = new Request.Builder().url(WISHLIST_URI).post(body).build();

           client.newCall(request).enqueue(new Callback() {

               @Override
               public void onResponse(Call call, final Response response) throws IOException {

                   runOnUiThread(new Runnable() {
                       @SuppressLint("SetTextI18n")
                       @Override
                       public void run() {

                           try {

                               String resp = response.body().string();

                               if(resp.equals("Available")){

                                   fav.setVisibility(View.VISIBLE);
                                   fav.setText("UNFAVOURITE");

                               }if(resp.equals("not available")){

                                   fav.setVisibility(View.VISIBLE);
                                   fav.setText("ADD TO FAVOURITES");
                               }

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

                           TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                       }
                   });
               }

           });
    }

    private void addtoWishlist(String userid,String str,String strbImage,String strbName,String strbSub,String strbClass){

        final ProgressDialog prg = new ProgressDialog(BookDetail.this);
        prg.setMessage("Adding to favourites...");
        prg.setCancelable(false);
        prg.show();

        OkHttpClient client = new OkHttpClient.Builder()
                                  .connectTimeout(30,TimeUnit.SECONDS)
                                  .readTimeout(30,TimeUnit.SECONDS)
                                  .writeTimeout(30,TimeUnit.SECONDS)
                                  .build();

        RequestBody body = new FormBody.Builder()
                            .add("UserId",userid)
                            .add("BookId",str)
                            .add("Book_Image",strbImage)
                            .add("Book_name",strbName)
                            .add("Book_subject",strbSub)
                            .add("Book_class",strbClass)
                            .build();

        Request request = new Request.Builder().post(body).url(ADD_TO_WISHLIST_URL).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        try {

                            String resp = response.body().string();

                            if(resp.equals("Data added")){

                                prg.dismiss();
                                fav.setText("UNFAVOURITE");

                            }else{

                                prg.dismiss();
                                fav.setText("ADD TO FAVOURITES");
                            }

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

                        prg.dismiss();
                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });
    }

    private void removefromWishlist(String str,String userid){

        final ProgressDialog prg = new ProgressDialog(BookDetail.this);
        prg.setMessage("Removing from favourites...");
        prg.show();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(22,TimeUnit.SECONDS)
                .readTimeout(22,TimeUnit.SECONDS)
                .writeTimeout(22,TimeUnit.SECONDS)
                .build();

        FormBody body = new FormBody.Builder()
                      .add("bId",str)
                      .add("uId",userid) 
                      .build();

        Request request = new Request.Builder().post(body).url(REMOVE_FROM_WISHLIST).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                           runOnUiThread(new Runnable() {
                               @SuppressLint("SetTextI18n")
                               @Override
                               public void run() {

                                   try {

                                       String resp = response.body().string();

                                       if(resp.equals("Deleted")){

                                           prg.dismiss();
                                           fav.setText("ADD TO FAVOURITES");
                                       }else{

                                           prg.dismiss();
                                           fav.setText("UNFAVOURITE");
                                       }

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

                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });

    }

    private void calling(String uid){

        final ProgressDialog prg= new ProgressDialog(BookDetail.this);
        prg.setMessage("Getting number...");
        prg.show();

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(22,TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        FormBody body = new FormBody.Builder().add("userid",uid).build();

        Request request = new Request.Builder().post(body).url(PHONE_CALL).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            prg.dismiss();

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String calling = jsonObject.getString("phone");

                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" +calling));
                                startActivity(callIntent);
                            }


                        }catch (JSONException | IOException e) {
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

                        prg.dismiss();
                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                    }
                });
            }

        });
    }

    @Override
    public void onBackPressed() {

          Intent i = new Intent(BookDetail.this,MainActivity.class);
          startActivity(i);
          finish();
    }
}
