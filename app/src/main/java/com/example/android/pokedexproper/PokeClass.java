package com.example.android.pokedexproper;

import java.io.Serializable;

public class PokeClass implements Serializable {

    Sprite sprites;
    Ability[] abilities;
    String name;
    int id;
    int height;
    int weight;
    int base_experience;
    PokeName[] forms;
    TypeClass[] types;
}
