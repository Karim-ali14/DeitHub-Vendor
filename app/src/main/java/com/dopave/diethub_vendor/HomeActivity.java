package com.dopave.diethub_vendor;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawer;
    String CurrentPage = "nav_Home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new
                ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.nav_Home && !CurrentPage.equals("nav_Home")){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
            CurrentPage = "nav_Home";
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_myOrders && !CurrentPage.equals("nav_myOrders")){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment()).commit();
            CurrentPage = "nav_myOrders";
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_myMeals && !CurrentPage.equals("nav_myMeals")){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DelegateFragment()).commit();
            CurrentPage = "nav_myMeals";
            drawer.closeDrawer(GravityCompat.START);
        }else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
        //HomeActivity.super.onBackPressed();
    }

}
