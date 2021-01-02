package com.dopave.diethub_vendor.UI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.AboutUs.AboutUs_Activity;
import com.dopave.diethub_vendor.UI.Conditions.Conditions_Activity;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryFragment;
import com.dopave.diethub_vendor.UI.Fragments.Home_Fragment;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.Fragments.SettingsFragment;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.Notifications.Notification_Activity;
import com.dopave.diethub_vendor.UI.Subscriptions.SubscriptionsActivity;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private long backPressedTime = 0;
    DrawerLayout drawer;
    public static ImageView Logo,Notification_Icon;
    public static TextView Title;
    public static String Current_Page = "nav_Home";
    CircleImageView ProviderIconMain;
    TextView ProviderNameMain;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        pref = new SharedPref(this);
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

        startingFragmentAccordingOnActivityComing(); // to open specific Fragment when come form ang activity

        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Logo = findViewById(R.id.Logo);
        Notification_Icon = findViewById(R.id.Notification_Icon);
        Title = findViewById(R.id.Title);
        Notification_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, Notification_Activity.class));
            }
        });

        ProviderIconMain = findViewById(R.id.ProviderIconMain);
        ProviderNameMain = findViewById(R.id.ProviderNameMain);
        setProviderData();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment(0)).commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DeliveryFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.delegates));
            Current_Page = "nav_delegates";
        }else if (view.getId() == R.id.nav_Consulting ){
            startActivity(new Intent(this, Conditions_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_mySubscriptionOrders ){
            startActivity(new Intent(this, SubscriptionsActivity.class)
                    .putExtra("type","nav_mySubscriptionOrders"));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_Subscription ){
            startActivity(new Intent(this,SubscriptionsActivity.class)
                    .putExtra("type","nav_Subscription"));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_aboutApp ){
            startActivity(new Intent(this, AboutUs_Activity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.nav_langu ){
            startActivity(new Intent(this,LanguageActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        }else if (view.getId() == R.id.LogOut ){
            LogOut();
        }else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void LogOut(){
        Common.currentPosition = null;
        Common.getPreferences(this).edit().clear().apply();
        startActivity(new Intent(this, Login_inActivity.class));
        finish();
        drawer.closeDrawer(GravityCompat.START);
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

    private void startingFragmentAccordingOnActivityComing(){
        if (getIntent().getExtras().getString("type").equals("Login_inActivity"))
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new Home_Fragment()).commit();
        else if (getIntent().getExtras().getString("type").equals("CreateDeliveryActivity")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DeliveryFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.delegates));
            Current_Page = "nav_delegates";
        }else if (getIntent().getExtras().getString("type").equals("Details_OrderActivity")){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,
                    new OrderFragment(getIntent().getExtras().getInt("typeId"))).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.Orders));
            Current_Page = "nav_myOrders";
        }else if (getIntent().getExtras().getString("type").equals("Modify")){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new SettingsFragment()).commit();
            drawer.closeDrawer(GravityCompat.START);
            Logo.setVisibility(View.GONE);
            Notification_Icon.setVisibility(View.GONE);
            Title.setVisibility(View.VISIBLE);
            Title.setText(getResources().getString(R.string.sittings));
            Current_Page = "nav_setting";
        }
    }

    private void setProviderData() {
        if (Common.currentPosition.getData().getProvider().getMainImage() != null) {
            String path = Common.BaseUrlForImages +
                    Common.currentPosition.getData().getProvider().getMainImage().getFor()
                    + "/" + Uri.encode(Common.currentPosition.getData().getProvider().getMainImage().getName());
            Picasso.with(this).load(path).into(ProviderIconMain);
        }
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                if (Common.currentPosition.getData().getProvider().getName() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getName());
                else if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else
                    ProviderNameMain.setText("--------");
            }else if (pref.getLagu().equals("en")) {
                if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else if (Common.currentPosition.getData().getProvider().getName() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getName());
                else
                    ProviderNameMain.setText("--------");
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else if (Common.currentPosition.getData().getProvider().getName() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getName());
                else
                    ProviderNameMain.setText("--------");
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                if (Common.currentPosition.getData().getProvider().getName() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getName());
                else if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    ProviderNameMain.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else
                    ProviderNameMain.setText("--------");
            }
        }
    }
}
