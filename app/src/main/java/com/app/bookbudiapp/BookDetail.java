package com.app.bookbudiapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setElevation(0);
        ab.setTitle("Bookbudi");
    }
}
