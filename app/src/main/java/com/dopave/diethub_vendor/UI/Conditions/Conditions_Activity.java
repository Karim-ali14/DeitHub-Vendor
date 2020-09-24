package com.dopave.diethub_vendor.UI.Conditions;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Settings.Settings;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.AboutUs.AboutUsViewModels;
import com.dopave.diethub_vendor.UI.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Conditions_Activity extends AppCompatActivity {
    TextView PrivacyText;
    ProgressDialog dialog;
    AboutUsViewModels viewModels;
    SharedPref pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conditions_);
        pref = new SharedPref(this);
        dialog = new ProgressDialog(this);
        PrivacyText = findViewById(R.id.PrivacyText);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModels = ViewModelProviders.of(this).get(AboutUsViewModels.class);
        viewModels.getSetting(this, dialog, viewModels, "Conditions").observe(this, new Observer<Settings>() {
            @Override
            public void onChanged(Settings settings) {
                onGetDataSettings(settings);
            }
        });
    }

    public void onGetDataSettings(Settings settings) {
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                PrivacyText.setText(settings.getData().getPrivacy());
            }else if (pref.getLagu().equals("en")) {
                PrivacyText.setText(settings.getData().getPrivacyEn());
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                PrivacyText.setText(settings.getData().getPrivacyEn());
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                PrivacyText.setText(settings.getData().getPrivacy());
            }
        }
    }

    public void BackButton(View view) {
        finish();
    }

}