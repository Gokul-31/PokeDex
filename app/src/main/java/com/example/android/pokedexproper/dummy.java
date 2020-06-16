package com.example.android.pokedexproper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class dummy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        Loader l = new Loader(dummy.this);

        l.startLoading();

    }
}
