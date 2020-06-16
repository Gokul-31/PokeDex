package com.example.android.pokedexproper;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface getApi {
    @GET("pokemon?offset=20&limit=25")
    Call<PokeList> getPokeNames();

    @GET("location?offset=20&limit=20")
    Call<PJ_Loc_Item_Type_Reg> getLocations();

    @GET("item?offset=20&limit=20")
    Call<PJ_Loc_Item_Type_Reg> getItems();

    @GET("type")
    Call<PJ_Loc_Item_Type_Reg> getTypes();

    @GET("type/{id}/")
    Call<PJ_Type> getTypePokes(@Path("id") int id);

    @GET("pokemon/{id}/")
    Call<PokeClass> getPokeDetails(@Path("id") int id);

    @GET("region")
    Call<PJ_Loc_Item_Type_Reg> getRegionNames();

    @GET("region/{id}")
    Call<RegToLoc> getRegLocs(@Path("id") int id);

    @GET("pokemon/{id}/encounters")
    Call<List<LocEncounter>> getLocationEncounters(@Path("id") int id);
}
