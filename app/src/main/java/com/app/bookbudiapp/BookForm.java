package com.app.bookbudiapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import Models.FirebaseCityModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class BookForm extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText bookName,subject,grade,city,price;
    ImageView bookPhoto,tick,gallery;
    ScrollView scrollView;
    Button submit;
    Spinner category;
    FirebaseAuth fAuth;
    FirebaseUser user;
    StorageReference sRef;
    Uri downloadUrl,imageUri;
    ProgressDialog prg;
    String imageUrl;

    FirebaseDatabase fireData;
    DatabaseReference dbRef;

    private static final int PICK_IMAGE_REQUEST = 1;

  //  private static final String URL = "https://bookbudiapp.herokuapp.com/addbooks";
  //  private static final String URI = "https://bookbudiapp.herokuapp.com/checkPhoneNo";

    private static final String URL = "https://bookbudi-prod.herokuapp.com/addbooks";
    private static final String URI = "https://bookbudi-prod.herokuapp.com/checkPhoneNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setTitle("Add item");
        ab.setElevation(0);

        sRef = FirebaseStorage.getInstance().getReference();
        dbRef = fireData.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        bookName = findViewById(R.id.bookName);
        subject = findViewById(R.id.subject);
        grade = findViewById(R.id.grade);
        city = findViewById(R.id.city);
        price = findViewById(R.id.price);
        submit = findViewById(R.id.submit);
        category = findViewById(R.id.category);
        tick = findViewById(R.id.tick);
        gallery = findViewById(R.id.gallery);
        bookPhoto = findViewById(R.id.bookPhoto);
        scrollView = findViewById(R.id.scrollView);

        bookPhoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });


        List<String> categories = new ArrayList<String>();
        categories.add("School");
        categories.add("College");
        categories.add("Competition");
        categories.add("Novel");
        categories.add("Others");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BookForm.this,android.R.layout.simple_spinner_item,categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        category.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                prg = new ProgressDialog(BookForm.this);
                prg.setMessage("Posting book...");
                prg.setCancelable(false);
                prg.show();

                if(imageUri == null){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Select image",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(bookName.getText().toString().equals("")){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Enter name",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(subject.getText().toString().equals("")){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Enter subject",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(grade.getText().toString().equals("")){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Enter grade",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(city.getText().toString().equals("")){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Enter city",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else if(price.getText().toString().equals("")){

                    prg.dismiss();
                    TastyToast.makeText(getApplicationContext(),"Enter price",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
                }
                else{

                    StorageReference riversRef = sRef.child("Books image").child(user.getEmail()).child(UUID.randomUUID().toString());

                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,40,baos);
                        byte[] byt = baos.toByteArray();

                        UploadTask uploadTask = riversRef.putBytes(byt);

                        uploadTask.addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                TastyToast.makeText(getApplicationContext(),"Error:"+e.getMessage(),TastyToast.LENGTH_LONG,
                                         TastyToast.ERROR).show();

                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                if(taskSnapshot.getMetadata() != null){

                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                        @Override
                                        public void onSuccess(Uri uri) {

                                             downloadUrl = Uri.parse(uri.toString());

                                           //  saveData();

                                            checkPhoneNo();
                                        }
                                    });
                                }

                            }
                        });

                    }catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK ){

            imageUri = data.getData();

            bookPhoto.setImageURI(imageUri);
        }
        else{

            TastyToast.makeText(getApplicationContext(),"No image selected",TastyToast.LENGTH_SHORT,TastyToast.INFO).show();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String item = parent.getItemAtPosition(position).toString();

        TastyToast.makeText(getApplicationContext(),""+item,TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveData(){

        final String uid = user.getUid();
        final String email = user.getEmail();
        final Uri profileImage  = user.getPhotoUrl();
        final String name = user.getDisplayName();
        final String bookname = bookName.getText().toString();
        final String sub = subject.getText().toString();
        final String categ = category.getSelectedItem().toString();
        final String grad = grade.getText().toString();
        final String cityname = city.getText().toString();
        final String cost = price.getText().toString();
        final String image  = downloadUrl.toString();

        RequestQueue requestQueue = Volley.newRequestQueue(BookForm.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                if(response.equals("Successfully inserted")){

                    prg.dismiss();

                    scrollView.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    tick.setVisibility(View.VISIBLE);

                    if(getSupportActionBar()!=null){
                        getSupportActionBar().hide();
                    }

                    final FirebaseCityModel model = new FirebaseCityModel();

                    model.setCity(cityname);
                   // dbRef.child("Cities").push().setValue(model);

                    dbRef.child("Cities").orderByChild("city").equalTo(cityname)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){

                                        Handler handler = new Handler();

                                        Runnable runnable  = new Runnable() {

                                            @Override
                                            public void run() {

                                                Intent i = new Intent(BookForm.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        };

                                        handler.postDelayed(runnable,2000);

                                    }else{

                                        dbRef.child("Cities").push().setValue(model);
                                        Handler handler = new Handler();

                                        Runnable runnable  = new Runnable() {

                                            @Override
                                            public void run() {

                                                Intent i = new Intent(BookForm.this,MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        };

                                        handler.postDelayed(runnable,2000);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                /*    Handler handler = new Handler();

                    Runnable runnable  = new Runnable() {

                        @Override
                        public void run() {

                            Intent i = new Intent(BookForm.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    };

                    handler.postDelayed(runnable,2000);  */

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                prg.dismiss();

                NetworkResponse status = error.networkResponse;
                TastyToast.makeText(getApplicationContext(),"Error:"+status,TastyToast.LENGTH_SHORT,TastyToast.INFO).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> map = new HashMap<>();

                map.put("userId",uid);
                map.put("email",email);
                map.put("name",name);
                map.put("bookName",bookname);
                map.put("subject",sub);
                map.put("category",categ);
                map.put("grade",grad);
                map.put("city",cityname);
                map.put("cost",cost);
                map.put("image",image);
                map.put("profileImage", String.valueOf(profileImage));

                return map;
            }
        };

        requestQueue.add(stringRequest);

    }

    private void checkPhoneNo(){

        OkHttpClient client = new OkHttpClient.Builder()
                              .connectTimeout(22, TimeUnit.SECONDS)
                              .readTimeout(22,TimeUnit.SECONDS)
                              .writeTimeout(22,TimeUnit.SECONDS)
                              .build();

        RequestBody formBody = new FormBody.Builder().add("uId",user.getUid()).build();

        okhttp3.Request request = new okhttp3.Request.Builder().post(formBody).url(URI).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            String resp = response.body().string();

                            if(resp.equals("Exists")){

                                saveData();
                            }
                            else{

                              Intent i = new Intent(BookForm.this,AddPhone.class);
                              startActivity(i);
                              finish();
                          }
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

                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                    }
                });
            }

        });
    }



  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){

            case android.R.id.home:

                Intent go = new Intent(BookForm.this,MainActivity.class);
                startActivity(go);
                finish();
        }

        return super.onOptionsItemSelected(item);
    } */

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BookForm.this);
        builder.setMessage("Do you want to leave.");
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

                Intent intent = new Intent(BookForm.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
