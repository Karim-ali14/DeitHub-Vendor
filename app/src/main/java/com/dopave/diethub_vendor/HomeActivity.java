package com.dopave.diethub_vendor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.DeliveryByProviderRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private long backPressedTime = 0;
    DrawerLayout drawer;
    public static ImageView Logo,Notification_Icon;
    public static TextView Title;
    public static String Current_Page = "nav_Home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = findViewById(R.id.toolbar);
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
        Notification_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,Notification_Activity.class));
            }
        });

//        Common.getAPIRequest().createDeliveryByProvider("Bearer "+
//                Common.currentPosition.getData().getToken().getAccessToken(),new DeliveryByProviderRequest("+2001226854332","gggggghgggg",
//                        "karhghghghim ali","karim.alffgfgi@gmail.com",1),
//                Common.currentPosition.getData().getProvider().getId()+"")
//                .enqueue(new Callback<DeliveryByProvider>() {
//            @Override
//            public void onResponse(Call<DeliveryByProvider> call, Response<DeliveryByProvider> response) {
//
//                if (response.code() == 201){
//                    Log.i("TTTTTT","done");
//                } else {
//                    try {
//                        Log.i("TTTTTTT",new JSONObject(response.errorBody()
//                                .string()).getString("message")+response.code());
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeliveryByProvider> call, Throwable t) {
//
//            }
//        });
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
            Current_Page = "nav_Home";
        }else if (view.getId() == R.id.nav_myOrders){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.Orders));
            Current_Page = "nav_myOrders";
        }else if (view.getId() == R.id.nav_setting){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new SettingsFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.sittings));
            Current_Page = "nav_setting";
        }else if (view.getId() == R.id.nav_delegates){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DelegateFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.delegates));
            Current_Page = "nav_delegates";
        }else if (view.getId() == R.id.nav_Consulting ){
            startActivity(new Intent(this,Conditions_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_aboutApp ){
            startActivity(new Intent(this,AboutUs_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_langu ){
            startActivity(new Intent(this,LanguageActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (!Current_Page.equals("nav_Home")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Home_Fragment()).commit();
            Logo.setVisibility(View.VISIBLE);
            Notification_Icon.setVisibility(View.VISIBLE);
            Title.setVisibility(View.GONE);
            Current_Page = "nav_Home";
        }else {
            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 secs
                backPressedTime = t;
                Toast.makeText(this, getResources().getString(R.string.Press_To_back), Toast.LENGTH_SHORT).show();
            } else {    // this guy is serious
                // clean up
                super.onBackPressed();       // bye
            }
        }
    }

}
