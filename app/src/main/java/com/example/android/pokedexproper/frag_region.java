package com.example.android.pokedexproper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_region extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rView;
    private RecyclerView.LayoutManager rLayoutMan;
    private RecycleAdapter rRegionAdap;
    private RecycleAdapter rPokesAdap;
    private ArrayList<PokeName> pRegions=new ArrayList<>();
    private ArrayList<PokeName> pRegLocs=new ArrayList<>();
    private getApi gApi;
    private ArrayList<PokeName> pPokes=new ArrayList<>();
    private boolean flagDone=false;
    private ArrayList<PokeName> pNames=new ArrayList<>();

    public frag_region() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static frag_region newInstance(String param1, String param2) {
        frag_region fragment = new frag_region();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_region, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] name = new String[1];
        rView = view.findViewById(R.id.rrecycle);
        rView.setHasFixedSize(true);
        rLayoutMan = new LinearLayoutManager(getContext());
        rView.setLayoutManager(rLayoutMan);
        rRegionAdap=null;

        final Loader l = new Loader(getActivity());
        l.startLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gApi = retrofit.create(getApi.class);

        pNames.clear();
        setpNames();

        if(pRegions.size()==0) {
            retrofit2.Call<PJ_Loc_Item_Type_Reg> call1 = gApi.getRegionNames();

            call1.enqueue(new Callback<PJ_Loc_Item_Type_Reg>() {
                @Override
                public void onResponse(retrofit2.Call<PJ_Loc_Item_Type_Reg> call, Response<PJ_Loc_Item_Type_Reg> response) {
                    if (!response.isSuccessful()) {
                        Log.i("Error after fetch: ", "" + response.code());
                        return;
                    }

                    pRegions = new ArrayList<PokeName>(Arrays.asList(response.body().results));
                    rRegionAdap = new RecycleAdapter((pRegions));

                    l.dismissLoading();

                    rView.setAdapter(rRegionAdap);
                    //set on Click
                    rRegionAdap.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            locDisp(position+1);
                        }
                    });
                }
                @Override
                public void onFailure(Call<PJ_Loc_Item_Type_Reg> call, Throwable t) {
                    Log.i("Error:", "Fetch: " + t.getMessage());
                }
            });
        }
        else{
            rView.setAdapter(rRegionAdap);
        }
    }

    private void setpNames() {
        retrofit2.Call<PokeList> call4 = gApi.getPokeNames();

        call4.enqueue(new Callback<PokeList>() {
            @Override
            public void onResponse(retrofit2.Call<PokeList> call, Response<PokeList> response) {
                if (!response.isSuccessful()) {
                    Log.i("Error after fetch: ", "" + response.code());
                }

                pNames = new ArrayList<PokeName>(Arrays.asList(response.body().results));
            }
            @Override
            public void onFailure(Call<PokeList> call, Throwable t) {
                Log.i("Error:", "Fetch: " + t.getMessage());
            }
        });
    }

    private void locDisp(int i) {
        Call<RegToLoc> call2=gApi.getRegLocs(i);

        final Loader l2=new Loader(getActivity());
        l2.startLoading();

        call2.enqueue(new Callback<RegToLoc>() {
            @Override
            public void onResponse(Call<RegToLoc> call, Response<RegToLoc> response) {
                if (!response.isSuccessful()) {
                    Log.i("Error after fetch: ", "" + response.code());
                    return;
                }
                pPokes.clear();
                pRegLocs= new ArrayList<>(Arrays.asList(response.body().locations));
                for(PokeName p : pRegLocs){
                    addList(p.name);
                    //set the pPokes we got
                    rPokesAdap = new RecycleAdapter(pPokes);
                    rView.setAdapter(rPokesAdap);
                    l2.dismissLoading();
                }
            }

            @Override
            public void onFailure(Call<RegToLoc> call, Throwable t) {
                Log.i("Error:", "Fetch: " + t.getMessage());
            }
        });

    }

    private void addList(String name1) {
        name1+="-area";

        for(int i=1;i<3;i++){
            frag_region.SyncThread syncThread = new frag_region.SyncThread(name1,i);
            syncThread.start();
        }
    }

    class SyncThread extends Thread{

        String finalName;
        int m;
        boolean flagDo=true;

        SyncThread(String n,int num){
            finalName=n;
            m=num;
        }
        @Override
        public void run() {
            for (PokeName p1 : pPokes) {
                if (p1.name.equals(pNames.get(m - 1).name)) {
                    flagDo = false;
                    break;
                }
            }
            if (flagDo) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://pokeapi.co/api/v2/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                gApi = retrofit.create(getApi.class);
                Call<List<LocEncounter>> call3 = gApi.getLocationEncounters(m);

                try {
                    Response<List<LocEncounter>> response = call3.execute();
                    if (response.isSuccessful()) {
                        for (LocEncounter p : response.body()) {
                            if (p.location_area.name.equals(finalName)) {
                                Log.i("search :", p.location_area.name);
                                    pPokes.add(pNames.get(m - 1));
                                    return;
                            }
                        }
                    }
                } catch (Exception ex) {
                    Log.i("call:", "Exception occured ");
                    ex.printStackTrace();
                }
            }
        }
    }
}
