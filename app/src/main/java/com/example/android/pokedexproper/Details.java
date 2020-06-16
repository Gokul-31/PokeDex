package com.example.android.pokedexproper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Details extends AppCompatActivity {

    private Intent receiveIntent;
    private int num;
    private getApi gApi;
    private Retrofit retrofit;
    private PokeClass pc;

    private String typeS;
    private String formS;
    private String abilityS;
    private ConstraintLayout all;
    private TextView heightView;
    private ImageView pokeImage;
    private TextView weightView;
    private TextView idView;
    private TextView beView;
    private TextView formsView;
    private TextView typesView;
    private TextView abilityView;
    private TextView nameView;
    private String name;
    private Loader l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);receiveIntent=getIntent();
        num=receiveIntent.getIntExtra("pos",1);
        name=receiveIntent.getStringExtra("name");

        heightView=findViewById(R.id.height);
        pokeImage=findViewById(R.id.pokeImage);
        all=findViewById(R.id.root);
        idView=findViewById(R.id.id);
        weightView=findViewById(R.id.weight);
        beView=findViewById(R.id.be);
        formsView=findViewById(R.id.forms);
        typesView=findViewById(R.id.types);
        abilityView=findViewById(R.id.ability);
        nameView=findViewById(R.id.name);

        l=new Loader(this);
        l.startLoading();

        retrofit= new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gApi= retrofit.create(getApi.class);

        Call<PokeClass> call1 =gApi.getPokeDetails(num);

        call1.enqueue(new Callback<PokeClass>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PokeClass> call, Response<PokeClass> response) {
                if(!response.isSuccessful()){
                    Log.i("2", "responded but no data");
                    return;
                }
                pc=response.body();

                l.dismissLoading();

                idView.setText(Integer.toString(pc.id));
                heightView.setText(Integer.toString(pc.height));
                weightView.setText(Integer.toString(pc.weight));
                beView.setText(Integer.toString(pc.base_experience));
                nameView.setText(name);

                formS="";
                for(PokeName f : pc.forms){
                    formS+=f.name;
                    formS+="\n";
                }

                typeS="";
                for(TypeClass t: pc.types){
                    typeS+=t.type.name;
                    typeS+="\n";
                }

                abilityS="";
                for(Ability a : pc.abilities){
                    abilityS+=a.ability.name;
                    abilityS+="\n";
                }

                abilityView.setText(abilityS);
                typesView.setText(typeS);
                formsView.setText(formS);
                Picasso.with(getApplicationContext()).load(pc.sprites.front_default).resize(all.getWidth()/2,all.getWidth()/2).into(pokeImage);
            }

            @Override
            public void onFailure(Call<PokeClass> call, Throwable t) {
                Log.i("2", "Fail to receive ");
            }
        });
    }
}