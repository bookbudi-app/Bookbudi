package com.app.bookbudiapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomBar;
    FirebaseAuth fAuth;
    FirebaseUser user;
    EditText search;
    Spinner chooseLocation;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dRef;

    Bundle bundle;

    ArrayList<String> list;
   // private static final String URL = "https://bookbudiapp.herokuapp.com/loadCity";
      private static final String URL = "https://bookbudi-prod.herokuapp.com/loadCity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dRef = firebaseDatabase.getInstance().getReference();

        final ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setTitle("");

        fAuth = FirebaseAuth.getInstance();

        user = fAuth.getCurrentUser();

        search = findViewById(R.id.search);
        chooseLocation = findViewById(R.id.chooseLocation);

        if(user == null){

            Intent i = new Intent(MainActivity.this,Login.class);
            startActivity(i);
            finish();
        }

        bottomBar = findViewById(R.id.bottomBar);

        list = new ArrayList<>();

        Home fragment = new Home();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragment.setArguments(bundle);
        fragmentTransaction.commit();

        loadSpinner();

        chooseLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String item = parent.getItemAtPosition(position).toString();


                        bundle = new Bundle();
                        bundle.putString("data", item);

                        //Sending spinner data to fragment when changed

                        Home fragment = new Home();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragment.setArguments(bundle);
                        fragmentTransaction.commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
            
        });

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){

                    case R.id.navigation_home:

                        Home fragment = new Home();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragment.setArguments(bundle);
                        fragmentTransaction.commit();

                        assert ab != null;
                        ab.setTitle("");
                        search.setVisibility(View.VISIBLE);
                        chooseLocation.setVisibility(View.VISIBLE);

                        return true;

                    case R.id.library:

                        MyLibrary fragment1 = new MyLibrary();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame, fragment1);
                        fragmentTransaction1.commit();

                        assert ab != null;
                        ab.setTitle("My library");
                        search.setVisibility(View.GONE);
                        chooseLocation.setVisibility(View.GONE);

                        return true;

                    case R.id.navigation_account:

                        Account fragment2 = new Account();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, fragment2);
                        fragmentTransaction2.commit();

                        assert ab != null;
                        ab.setTitle("Account");
                        search.setVisibility(View.GONE);
                        chooseLocation.setVisibility(View.GONE);

                        return true;
                }

                return false;
            }
        });

    }

    private void loadSpinner(){


        dRef.child("Cities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot areaSnapshot:dataSnapshot.getChildren()){
                    String areaName = areaSnapshot.child("city").getValue(String.class);
                    list.add(areaName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_city,
                        R.id.locaions,list);

                chooseLocation.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                TastyToast.makeText(getApplicationContext(),"Unable to fetch locations",TastyToast.LENGTH_SHORT,TastyToast.ERROR).show();
            }
        });

     /*   OkHttpClient client  = new OkHttpClient.Builder()
                               .connectTimeout(22, TimeUnit.SECONDS)
                               .readTimeout(22, TimeUnit.SECONDS)
                               .writeTimeout(22,TimeUnit.SECONDS)
                               .build();

        Request request = new Request.Builder().url(URL).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            JSONArray jsonArray = new JSONArray(response.body().string());

                            for(int i = 0; i<jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                String str = object.getString("Place");

                                list.add(str);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.spinner_city,
                                                                               R.id.locaions,list);

                            chooseLocation.setAdapter(adapter);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();

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

                        TastyToast.makeText(getApplicationContext(),e.getMessage(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                    }
                });
            }

        });  */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            Intent i = new Intent(MainActivity.this,BookForm.class);
            startActivity(i);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
