package com.app.bookbudiapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sdsmdg.tastytoast.TastyToast;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;

public class Login extends AppCompatActivity {

    ViewPager onboardOffer;
    TabLayout tabLayout;
    OnboardAdapter adapter;
    GoogleSignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    FirebaseAuth fAuth;
    FirebaseUser user;
    ProgressDialog prg;
    String uid,email,name;
    ProgressBar progressBar;
    List<LoginBannerModel> banners;

    private static final String URL = "https://bookbudiapp.herokuapp.com/users";
    private static final String URI = "https://bookbudiapp.herokuapp.com/loginBanners";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.hide();

        prg = new ProgressDialog(Login.this);
        prg.setMessage("Signing in...");
        prg.setCancelable(false);

        onboardOffer = findViewById(R.id.onboardOffer);
        tabLayout = findViewById(R.id.tabLayout);
        progressBar = findViewById(R.id.progressBar);
        signIn = findViewById(R.id.signIn);

        progressBar.setVisibility(View.VISIBLE);
        signIn.setEnabled(false);

        banners = new ArrayList<>();

        tabLayout.setupWithViewPager(onboardOffer,true);

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(22, TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        okhttp3.Request request = new okhttp3.Request.Builder().url(URI).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        progressBar.setVisibility(View.GONE);
                        signIn.setEnabled(true);

                        try {

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i = 0;i < jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String str1 = jsonObject.getString("Image");
                                String str2 = jsonObject.getString("Description");

                                LoginBannerModel model = new LoginBannerModel(str2,str1);
                                banners.add(model);
                            }

                            adapter = new OnboardAdapter(banners,getApplicationContext());

                            onboardOffer.setAdapter(adapter);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finally {

                            response.close();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call call, final IOException e) {

                runOnUiThread(new Runnable() {

                     @Override
                     public void run() {

                         progressBar.setVisibility(View.GONE);

                         TastyToast.makeText(getApplicationContext(),"Error:"+e.getMessage(),TastyToast.LENGTH_SHORT
                                        ,TastyToast.ERROR).show();
                     }
                 });
            }

        });

      //  adapter = new OnboardAdapter(this);

        //  onboardOffer.setAdapter(adapter);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{

                prg.show();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            }catch(ApiException e){

                prg.hide();
                TastyToast.makeText(getApplicationContext(),"Login failed",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);

        fAuth.signInWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    saveData();

                }
                else{

                    prg.hide();
                    TastyToast.makeText(getApplicationContext(),"Unable to login with google.",TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR).show();

                }
            }
        });

    }

    public void saveData(){

        user = fAuth.getCurrentUser();

        uid = fAuth.getCurrentUser().getUid();
        name = user.getDisplayName();
        email = user.getEmail();

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response.equals("User exists")){

                    prg.dismiss();
                    Intent i = new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(response.equals("User created")){

                    prg.dismiss();
                    Intent i = new Intent(Login.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else{

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"" +response.toString(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                TastyToast.makeText(getApplicationContext(),""+error.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> map = new HashMap<>();

                map.put("id",uid);
                map.put("username",name);
                map.put("email",email);

                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Are you sure you want to exit.");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
