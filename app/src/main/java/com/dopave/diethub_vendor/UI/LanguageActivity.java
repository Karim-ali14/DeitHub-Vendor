package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.dopave.diethub_vendor.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private SharedPref pref;
    ConstraintLayout ArabicLayout,EnglishLayout;
    ImageView selectedArab,selectedEng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new SharedPref(this);
        setContentView(R.layout.activity_language);

        selectedArab = findViewById(R.id.selectedArab);
        selectedEng = findViewById(R.id.selectedEng);
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                selectedEng.setVisibility(View.GONE);
                selectedArab.setVisibility(View.VISIBLE);
            } else if (pref.getLagu().equals("en")) {
                selectedEng.setVisibility(View.VISIBLE);
                selectedArab.setVisibility(View.GONE);
            }
        }else {
            selectedEng.setVisibility(View.GONE);
            selectedArab.setVisibility(View.GONE);
        }
        ArabicLayout = findViewById(R.id.ArabicLayout);
        EnglishLayout = findViewById(R.id.EnglishLayout);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ArabicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageToLoad = "ar";
//                Locale locale = new Locale(languageToLoad);
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                setAppLocale(languageToLoad);
                pref.setLangu("ar");
                Intent intent = new Intent(LanguageActivity.this, HomeActivity.class)
                        .putExtra("type","Login_inActivity");
                startActivity(intent);
                finish();
            }
        });

        EnglishLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String languageToLoad = "en";
//                Locale locale = new Locale(languageToLoad);
//                Locale.setDefault(locale);
//                Configuration config = new Configuration();
//                config.locale = locale;
//                getBaseContext().getResources().updateConfiguration(config,
//                        getBaseContext().getResources().getDisplayMetrics());
                setAppLocale(languageToLoad);
                pref.setLangu("en");
                Intent intent = new Intent(LanguageActivity.this, HomeActivity.class)
                        .putExtra("type","Login_inActivity");
                startActivity(intent);
                finish();
            }
        });
    }

    public void BackButton(View view) {
        finish();
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}
