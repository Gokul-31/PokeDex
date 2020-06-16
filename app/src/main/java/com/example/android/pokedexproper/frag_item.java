package com.example.android.pokedexproper;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class frag_item extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rView;
    private RecyclerView.LayoutManager rLayoutMan;
    private RecycleAdapter rItemAdap;
    private ArrayList<PokeName> pItems=new ArrayList<>();
    private getApi gApi;

    public frag_item() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static frag_item newInstance(String param1, String param2) {
        frag_item fragment = new frag_item();
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
        return inflater.inflate(R.layout.fragment_frag_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String[] name = new String[1];
        rView = view.findViewById(R.id.irecycle);
        rView.setHasFixedSize(true);
        rLayoutMan = new LinearLayoutManager(getContext());
        rView.setLayoutManager(rLayoutMan);
        rItemAdap = null;

        final Loader l = new Loader(getActivity());
        l.startLoading();

        SyncThread st=new SyncThread();
        st.start();
        l.dismissLoading();
    }

    @SuppressLint("HandlerLeak")
    class SyncThread extends Thread{

        @Override
        public void run() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://pokeapi.co/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            gApi = retrofit.create(getApi.class);

            retrofit2.Call<PJ_Loc_Item_Type_Reg> call = gApi.getItems();

            try
            {
                Response<PJ_Loc_Item_Type_Reg> response = call.execute();
                if(response.isSuccessful()) {
                    pItems = new ArrayList<PokeName>(Arrays.asList(response.body().results));
                    Log.i("call:", pItems.get(0).name);

                    Handler threadHandler = new Handler(Looper.getMainLooper());
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            rItemAdap = new RecycleAdapter((pItems));
                            rView.setAdapter(rItemAdap);
                        }
                    });
                }
            }
            catch(Exception ex)
            {
                Log.i("call:", "Exception occured ");
                ex.printStackTrace();
            }
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
                rItemAdap.getFilter().filter(newText);
                return false;
            }
        });
    }
}
