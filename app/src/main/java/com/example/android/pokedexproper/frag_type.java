package com.example.android.pokedexproper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_type extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rView;
    private RecyclerView.LayoutManager rLayoutMan;
    private RecycleAdapter rTypeAdap;
    private ArrayList<PokeName> pTypes;
    private ArrayList<PokeName> pTypesInner=new ArrayList<>();
    private getApi gApi;

    public frag_type() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static frag_type newInstance(String param1, String param2) {
        frag_type fragment = new frag_type();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] name = new String[1];
        rView = view.findViewById(R.id.trecycle);
        rView.setHasFixedSize(true);
        rLayoutMan = new LinearLayoutManager(getContext());
        rView.setLayoutManager(rLayoutMan);
        rTypeAdap=null;

        final Loader l= new Loader(getActivity());
        l.startLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gApi = retrofit.create(getApi.class);

        if(pTypes==null) {
            retrofit2.Call<PJ_Loc_Item_Type_Reg> call1 = gApi.getTypes();

            call1.enqueue(new Callback<PJ_Loc_Item_Type_Reg>() {
                @Override
                public void onResponse(retrofit2.Call<PJ_Loc_Item_Type_Reg> call, Response<PJ_Loc_Item_Type_Reg> response) {
                    if (!response.isSuccessful()) {
                        Log.i("Error after fetch: ", "" + response.code());
                        return;
                    }

                    if(pTypesInner!=null){
                        pTypesInner.clear();
                    }

                    l.dismissLoading();

                    pTypes = new ArrayList<PokeName>(Arrays.asList(response.body().results));
                    rTypeAdap = new RecycleAdapter((pTypes));
                    rView.setAdapter(rTypeAdap);

                    rTypeAdap.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            showInnerType(position+1);
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
            rView.setAdapter(rTypeAdap);
        }
    }

    private void showInnerType(int n) {
        Call<PJ_Type> call2= gApi.getTypePokes(n);
        final Loader l2=new Loader(getActivity());
        l2.startLoading();

        call2.enqueue(new Callback<PJ_Type>() {
            @Override
            public void onResponse(Call<PJ_Type> call, Response<PJ_Type> response) {
                if (!response.isSuccessful()) {
                    Log.i("Error after fetch: ", "" + response.code());
                    return;
                }
                l2.dismissLoading();
             for(PJ_TypeInner t: response.body().pokemon){
                 pTypesInner.add(t.pokemon);
             }
                rTypeAdap = new RecycleAdapter((pTypesInner));
                rView.setAdapter(rTypeAdap);
            }

            @Override
            public void onFailure(Call<PJ_Type> call, Throwable t) {
                Log.i("Error:", "Fetch: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem= menu.findItem(R.id.action_search);
        SearchView sView=(SearchView) searchItem.getActionView();

        sView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rTypeAdap.getFilter().filter(newText);
                return false;
            }
        });
    }
}
