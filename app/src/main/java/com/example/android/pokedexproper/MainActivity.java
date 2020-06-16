package com.example.android.pokedexproper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navView=findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, tb, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_name()).commit();
        navView.setCheckedItem(R.id.nameTouch);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nameTouch:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_name()).commit();
                break;
            case R.id.typeTouch:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_type()).commit();
                break;
            case R.id.itemTouch:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_item()).commit();
                break;
            case R.id.locTouch:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_loc()).commit();
                break;
            case R.id.regionTouch:
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,new frag_region()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
