package com.dopave.diethub_vendor.UI.AboutUs;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUs_Activity extends AppCompatActivity {
    TextView AboutUsText;
    ProgressDialog dialog;
    AboutUsViewModels viewModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_);
        dialog = new ProgressDialog(this);
        AboutUsText = findViewById(R.id.AboutUsText);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        viewModels = ViewModelProviders.of(this).get(AboutUsViewModels.class);
        viewModels.getSetting(this,dialog,viewModels,"AboutUs").observe(this, new Observer<Settings>() {
            @Override
            public void onChanged(Settings settings) {
                onGetDataSettings(settings);
            }
        });
    }
    public void onGetDataSettings(Settings settings){
        AboutUsText.setText(settings.getData().getAbout());
    }
    public void BackButton(View view) {
        finish();
    }
}
