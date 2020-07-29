package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    private SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pref = new SharedPref(this);
        new Thread(){
            @Override
            public void run() {
                try {
                    if (!pref.getLagu().equals("empty")) {
                        if (pref.getLagu().equals("ar")) {
                            String languageToLoad = "ar";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());
                            pref.setLangu("ar");
                        } else if (pref.getLagu().equals("en")) {
                            String languageToLoad = "en";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());
                            pref.setLangu("en");
                        }
                    }
                    Thread.sleep(2000);
                    startActivity(new Intent(getApplicationContext(), Login_inActivity.class));
                    finish(); // finish(); because This Activity close After move To Next Activity
                    // And Next Activity will Be Launcher
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
