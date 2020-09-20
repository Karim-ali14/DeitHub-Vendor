package com.dopave.diethub_vendor.UI.Splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.ProviderInfo.Token;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.SharedPref;

import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    private SharedPref pref;
    private SharedPreferences preferences;
    SplashViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        viewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        pref = new SharedPref(this);
        preferences = Common.getPreferences(this);
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
                    autoLogin();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void autoLogin() {
        if (preferences.contains(Common.Token)&&preferences.contains(Common.ProviderId)){
            if (checkToken(preferences.getString(Common.Token,""))){
                Log.i("TTTTTTT", "Token is not EX");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("TTTTTT",preferences.getString(Common.Token,"")
                                + " "+preferences.getString(Common.ProviderId,""));
                        viewModel.getProviderInfo(SplashActivity.this,preferences.getString(Common.Token,"")
                                ,preferences.getString(Common.ProviderId,""))
                                .observe(SplashActivity.this, new Observer<ProviderInformation>() {
                                    @Override
                                    public void onChanged(ProviderInformation providerInformation) {
                                        Common.currentPosition = providerInformation;
                                        Token token = new Token(preferences.getString(Common.Token,""),"Bearer ");
                                        Common.currentPosition.getData().setToken(token);
                                        startActivity(new Intent(SplashActivity.this,
                                                HomeActivity.class).putExtra("type",
                                                "Login_inActivity"));
                                        finish();
                                    }
                                });
                    }
                });
            }else
                normalLogin();
        }else
            normalLogin();
    }

    private boolean checkToken(String Token){
        JWT jwt = new JWT(Token);
        Date expiresAt = jwt.getExpiresAt();
        return new Date().before(expiresAt) || new Date().equals(expiresAt);
    }

    private void normalLogin(){
        startActivity(new Intent(getApplicationContext(), Login_inActivity.class));
        finish(); // finish(); because This Activity close After move To Next Activity
        // And Next Activity will Be Launcher
    }

}
