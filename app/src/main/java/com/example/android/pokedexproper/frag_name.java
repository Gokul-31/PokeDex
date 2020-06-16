package com.example.android.pokedexproper;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class frag_name extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rView;
    private RecyclerView.LayoutManager rLayoutMan;
    private RecycleAdapter rNameAdap;
    private ArrayList<PokeName> pNames=new ArrayList<>();
    private getApi gApi;

    public frag_name() {
        // Required empty public constructor
    }

    public static frag_name newInstance(String param1, String param2) {
        frag_name fragment = new frag_name();
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
        return inflater.inflate(R.layout.frag_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] name = new String[1];
        rView = view.findViewById(R.id.nrecycle);
        rView.setHasFixedSize(true);
        rLayoutMan = new LinearLayoutManager(getContext());
        rView.setLayoutManager(rLayoutMan);
        rNameAdap=null;

        final Loader l = new Loader(getActivity());
        l.startLoading();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        gApi = retrofit.create(getApi.class);

        if(pNames.size()==0) {
            retrofit2.Call<PokeList> call1 = gApi.getPokeNames();

            call1.enqueue(new Callback<PokeList>() {
                @Override
                public void onResponse(retrofit2.Call<PokeList> call, Response<PokeList> response) {
                    if (!response.isSuccessful()) {
                        Log.i("Error after fetch: ", "" + response.code());
                    }

                    pNames = new ArrayList<PokeName>(Arrays.asList(response.body().results));
                    rNameAdap = new RecycleAdapter((pNames));

                    l.dismissLoading();

                    rView.setAdapter(rNameAdap);
                    //set on Click
                    rNameAdap.setOnItemClickListener(new RecycleAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent detailPage = new Intent(getContext(), Details.class);
                            name[0] = pNames.get(position).name;
                            detailPage.putExtra("name", name[0]);
                            detailPage.putExtra("pos", position + 1);
                            startActivity(detailPage);
                        }
                    });
                }
                @Override
                public void onFailure(Call<PokeList> call, Throwable t) {
                    Log.i("Error:", "Fetch: " + t.getMessage());
                }
            });
        }
        else{
            rView.setAdapter(rNameAdap);
        }
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
                rNameAdap.getFilter().filter(newText);
                return false;
            }
        });
    }
}
