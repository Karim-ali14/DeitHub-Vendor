package com.dopave.diethub_vendor;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
    public static ImageView Logo,Notification_Icon;
    public static TextView Title;
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
        Logo = findViewById(R.id.Logo);
        Notification_Icon = findViewById(R.id.Notification_Icon);
        Title = findViewById(R.id.Title);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.nav_Home){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.VISIBLE);
            Notification_Icon.setVisibility(View.VISIBLE);
            Title.setVisibility(View.GONE);
        }else if (view.getId() == R.id.nav_myOrders){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.Orders));
        }else if (view.getId() == R.id.nav_setting ){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new SettingsFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.sittings));
        }else if (view.getId() == R.id.nav_delegates ){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DelegateFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.delegates));
        }else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
        Logo.setVisibility(View.VISIBLE);
        Notification_Icon.setVisibility(View.VISIBLE);
        Title.setVisibility(View.GONE);

        //HomeActivity.super.onBackPressed();
    }

}
