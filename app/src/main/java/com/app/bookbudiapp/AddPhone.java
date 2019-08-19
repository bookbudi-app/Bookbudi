package com.app.bookbudiapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPhone extends AppCompatActivity {

    EditText phone;
    ProgressBar phoneProg;
    Button addPhone;
    FirebaseAuth fAuth;
    FirebaseUser user;

   // private static final String  URL = "https://bookbudiapp.herokuapp.com/addPhoneNo";
    private static final String  URL = "https://bookbudi-prod.herokuapp.com/addPhoneNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setElevation(0);
        ab.setTitle("Add phone");
        ab.setDisplayHomeAsUpEnabled(true);

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        phone = findViewById(R.id.phone);
        phoneProg = findViewById(R.id.phoneProg);
        addPhone = findViewById(R.id.addPhone);

        addPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                phoneProg.setVisibility(View.VISIBLE);
                phone.setEnabled(false);

                String str = phone.getText().toString();

                if(str.equals("")){

                    phoneProg.setVisibility(View.INVISIBLE);
                    phone.setEnabled(true);
                    TastyToast.makeText(getApplicationContext(),"Enter phone No.",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(!(str.length() == 10)){

                    phoneProg.setVisibility(View.INVISIBLE);
                    phone.setEnabled(true);
                    TastyToast.makeText(getApplicationContext(),"Invalid number",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else{

                    addPhone.setEnabled(false);
                    savePhone(str);
                }
            }
        });
    }

    private void savePhone(String phoneNo){

        OkHttpClient client = new OkHttpClient.Builder()
                                  .connectTimeout(22, TimeUnit.SECONDS)
                                  .writeTimeout(22,TimeUnit.SECONDS)
                                  .readTimeout(22,TimeUnit.SECONDS)
                                  .build();

        RequestBody formBody = new FormBody.Builder()
                                   .add("id",user.getUid())
                                   .add("phone",phoneNo)
                                   .build();

        Request request = new Request.Builder().post(formBody).url(URL).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                           String resp = response.body().string();

                            if(resp.equals("Updated successfully")){

                                phoneProg.setVisibility(View.INVISIBLE);

                                Intent intent = new Intent(AddPhone.this,BookForm.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (IOException e) {
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

                        addPhone.setEnabled(true);

                        phoneProg.setVisibility(View.INVISIBLE);
                        phone.setEnabled(false);
                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
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

                Intent in = new Intent(AddPhone.this,MainActivity.class);
                startActivity(in);
                finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent i  = new Intent(AddPhone.this,MainActivity.class);
        startActivity(i);
        finish();

    /*    AlertDialog.Builder builder = new AlertDialog.Builder(AddPhone.this);
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
        alertDialog.show();  */

    }
}

