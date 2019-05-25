package com.app.bookbudiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;

public class Chat extends AppCompatActivity {

    ImageButton send;
    EditText msg;
    TextView username;
    RecyclerView messages;
    FirebaseAuth fAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        Intent i = getIntent();
        final String str = i.getStringExtra("name");


        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        username = findViewById(R.id.username);
        messages = findViewById(R.id.messages);

        username.setText(str);

        messages.setHasFixedSize(true);
        messages.setLayoutManager(new LinearLayoutManager(this));

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String text = msg.getText().toString();

                if(!text.equals("")){

                    sendMessage(user.getUid(),str,text);
                    Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
                }

                msg.setText("");
            }
        });
    }

    private void sendMessage(String sender,String receiver,String message){

        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> map = new HashMap<>();

        map.put("Sender",sender);
        map.put("Receiver",receiver);
        map.put("Message",message);

        dRef.child("Chats").push().setValue(map);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
