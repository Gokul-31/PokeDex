package com.example.android.pokedexproper;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class Loader {
    Activity activity;
    AlertDialog dialog;

    Loader(Activity a){
        this.activity=a;
    }

    void startLoading(){
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);

        LayoutInflater inflater= activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(true);

        dialog=builder.create();
        dialog.show();
    }

    void dismissLoading(){
        dialog.dismiss();
    }
}
