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

public class Chat extends AppCompatActivity {

    ImageButton send;
    EditText msg;
    TextView username;
    RecyclerView messages;

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

        Intent i = getIntent();
        String str = i.getStringExtra("name");


        send = findViewById(R.id.send);
        msg = findViewById(R.id.msg);
        username = findViewById(R.id.username);
        messages = findViewById(R.id.messages);

        username.setText(str);

        messages.setHasFixedSize(true);
        messages.setLayoutManager(new LinearLayoutManager(this));


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
