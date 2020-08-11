package com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInfo;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Modify_personal_infoActivity extends AppCompatActivity {
    Spinner spinnerCity;
    TextView CitySelected;
    CityRow cityRow;
    boolean isSpinnerCities;
    int SpinnerCitiesClick = 0;
    ImageView mark;
    CreateDeliveryViewModel viewModelForCities;
    ProgressDialog dialog;
    ConstraintLayout City_Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        dialog = new ProgressDialog(this);
        viewModelForCities = ViewModelProviders.of(this).get(CreateDeliveryViewModel.class);
        spinnerCity = findViewById(R.id.spinnerCityProviderInfo);
        CitySelected = findViewById(R.id.CitySelectedProInfo);
        mark = findViewById(R.id.markProInfo);
        City_Layout = findViewById(R.id.City_Layout_ProInfo);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Common.getAPIRequest().getProviderInfo("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<ProviderInfo>() {
            @Override
            public void onResponse(Call<ProviderInfo> call, Response<ProviderInfo> response) {
                if (response.code() == 200){
                    Toast.makeText(Modify_personal_infoActivity.this, response.body().getData().getName(), Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(Modify_personal_infoActivity.this,message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProviderInfo> call, Throwable t) {

            }
        });
        getCities();
        spinnerCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mark.setImageResource(R.drawable.pin_active);
                    City_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    // Load your spinner here
                    isSpinnerCities = true;
                }
                return false;
            }
        });
    }

    public void BackButton(View view) {
        finish();
    }

    private void getCities(){
        viewModelForCities = ViewModelProviders.of(this).get(CreateDeliveryViewModel.class);
        viewModelForCities.getCities(this,dialog,viewModelForCities).observe(this, new Observer<Cities>() {
            @Override
            public void onChanged(Cities cities) {
                onGetCity(cities);
            }
        });
    }

    public void onGetCity(Cities cities){
        CreateDeliveryActivity.AdapterOfSpinner arrayAdapter = new CreateDeliveryActivity.AdapterOfSpinner(Modify_personal_infoActivity.this,
                R.layout.city_item,cities.getData().getCityRows());

        spinnerCity.setAdapter(arrayAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityRow = ((CityRow) parent.getItemAtPosition(position));
                CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getName());
                CitySelected.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
            spinnerCity.setSelection(0);
    }
    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (isSpinnerCities){
            if (SpinnerCitiesClick == 0)
                SpinnerCitiesClick++;
            else {
                SpinnerCitiesClick = 0;
                mark.setImageResource(R.drawable.pin);
                City_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput));
            }
        }
    }

}
