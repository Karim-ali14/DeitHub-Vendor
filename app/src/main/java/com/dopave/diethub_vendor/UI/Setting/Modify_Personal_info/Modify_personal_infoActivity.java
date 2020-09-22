package com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.ProviderInfo.Request.ProviderInfoRequest;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryViewModel;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Setting.MapsActivity;
import com.dopave.diethub_vendor.UI.SharedPref;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Modify_personal_infoActivity extends AppCompatActivity {
    Spinner spinnerCity;
    TextView CitySelected,EnglishText,ArabicText,Address;
    EditText RestaurantsName,About_Res,RestaurantsNameEn,About_ResEn;
    CityRow cityRow;
    boolean isSpinnerCities,firstOpen;
    int SpinnerCitiesClick = 0;
    ImageView mark,edit_Location;
    CreateDeliveryViewModel viewModelForCities;
    ProgressDialog dialog;
    ConstraintLayout City_Layout,Layout_ProInfo,Layout_TabArabic,Layout_TabEnglish;
    private MapView mapView;
    private GoogleMap gmap;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    LinearLayout Layout_Ar,Layout_En;
    ProviderInformation providerInfo;
    Button UpDateButton;
    Modify_Person_info_viewModel viewModel;
    SharedPref pref;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_personal_info);
        pref = new SharedPref(this);
        dialog = new ProgressDialog(this);
        viewModel = ViewModelProviders.of(this).get(Modify_Person_info_viewModel.class);
        viewModelForCities = ViewModelProviders.of(this).get(CreateDeliveryViewModel.class);
        UpDateButton = findViewById(R.id.UpDateButton);
        Address = findViewById(R.id.Address);
        edit_Location = findViewById(R.id.edit_Location);
        spinnerCity = findViewById(R.id.spinnerCityProviderInfo);
        RestaurantsName = findViewById(R.id.RestaurantsName);
        About_Res = findViewById(R.id.About_Res);
        RestaurantsNameEn = findViewById(R.id.RestaurantsNameEn);
        About_ResEn = findViewById(R.id.About_ResEn);
        CitySelected = findViewById(R.id.CitySelectedProInfo);
        mark = findViewById(R.id.markProInfo);
        City_Layout = findViewById(R.id.City_Layout_ProInfo);
        Layout_ProInfo = findViewById(R.id.Layout_ProInfo);
        Layout_TabArabic = findViewById(R.id.Layout_TabArabic);
        Layout_TabEnglish = findViewById(R.id.Layout_TabEnglish);
        Layout_Ar = findViewById(R.id.Layout_Ar);
        ArabicText = findViewById(R.id.ArabicText);
        Layout_En = findViewById(R.id.Layout_En);
        EnglishText = findViewById(R.id.EnglishText);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        initMap(savedInstanceState);

        if (getIntent().getExtras() == null) {
            viewModel.getProviderInfo(this, dialog, viewModel, "forPersonalInfo").observe(this,
                    new Observer<ProviderInformation>() {
                        @Override
                        public void onChanged(ProviderInformation providerInfo) {
                            onGetProviderInfo(providerInfo);
                        }
                    });
        }else {
            setLocationUpdate();
            getCities();
            setData("Ar");
        }

        chickEvents();

    }

    public void onGetProviderInfo(ProviderInformation providerInfo) {
        this.providerInfo = providerInfo;
        getCities();
        setData("Ar");
        if (providerInfo.getData().getProvider().getLatitude() != null && providerInfo.getData().getProvider().getLongitude() != null)
            addMarker(new LatLng(providerInfo.getData().getProvider().getLatitude(),providerInfo.getData().getProvider().getLongitude()));
    }

    private void chickEvents() {
        edit_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Modify_personal_infoActivity.this,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modify_Address :
                                updateAddress();
                                return true;
                            case R.id.modify_Location :{
//                                startActivity(new Intent(Modify_personal_infoActivity.this,
//                                        MapsActivity.class)
//                                        .putExtra("Lat",providerInfo.getData().getProvider().getLatitude())
//                                        .putExtra("Long",providerInfo.getData().getProvider().getLongitude()));
                                startActivity(new Intent(Modify_personal_infoActivity.this,
                                        MapsActivity.class)
                                        .putExtra("object",providerInfo));
                            }
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.menu_modify_provider_data);
                popupMenu.show();
            }
        });
        Layout_ProInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });
        Layout_Ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Layout_Ar.setBackground(getResources().getDrawable(R.drawable.style_tab_active));
//                ArabicText.setTextColor(getResources().getColor(R.color.white));
//                Layout_En.setBackground(getResources().getDrawable(R.drawable.style_tab_normal));
//                EnglishText.setTextColor(getResources().getColor(R.color.colorPrimary));
                ArabicTab("show");
            }
        });
        Layout_En.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Layout_En.setBackground(getResources().getDrawable(R.drawable.style_tab_active));
//                EnglishText.setTextColor(getResources().getColor(R.color.white));
//                Layout_Ar.setBackground(getResources().getDrawable(R.drawable.style_tab_normal));
//                ArabicText.setTextColor(getResources().getColor(R.color.colorPrimary));
                EnglishTab("show");
            }
        });
        RestaurantsName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    RestaurantsName.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                }
                else {
                    RestaurantsName.setBackground(getResources().getDrawable(R.drawable.style_textinput));

                }
            }
        });
        About_Res.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    About_Res.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                }
                else {
                    About_Res.setBackground(getResources().getDrawable(R.drawable.style_textinput));

                }
            }
        });
        RestaurantsNameEn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    RestaurantsNameEn.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                }
                else {
                    RestaurantsNameEn.setBackground(getResources().getDrawable(R.drawable.style_textinput));

                }
            }
        });
        About_ResEn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    About_ResEn.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                }
                else {
                    About_ResEn.setBackground(getResources().getDrawable(R.drawable.style_textinput));

                }
            }
        });
        spinnerCity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    mark.setImageResource(R.drawable.pin_active);
                    City_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    // Load your spinner here
                    isSpinnerCities = true;
                    closeKeyBoard();
                }
                return false;
            }
        });
        UpDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDateProvider();
            }
        });
    }

    private void updateAddress() {
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_modify_address, null);
        final EditText AddressAr = view.findViewById(R.id.AddressAr);
        final EditText AddressEn = view.findViewById(R.id.AddressEn);
        AddressAr.setText(providerInfo.getData().getProvider().getAddress());
        AddressEn.setText(providerInfo.getData().getProvider().getAddressEn());
        Button AddressSaveButton = view.findViewById(R.id.AddressSaveButton);
        Button CancelButton = view.findViewById(R.id.CancelButton);
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        AddressSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AddressAr.getText().toString().isEmpty())
                    Toast.makeText(Modify_personal_infoActivity.this, R.string.enter_AddressAr, Toast.LENGTH_SHORT).show();
                else if (AddressEn.getText().toString().isEmpty())
                    Toast.makeText(Modify_personal_infoActivity.this, R.string.enter_AddressEn, Toast.LENGTH_SHORT).show();
                else {
                    providerInfo.getData().getProvider().setAddress(AddressAr.getText().toString());
                    providerInfo.getData().getProvider().setAddressEn(AddressEn.getText().toString());
                    Address.setText(AddressAr.getText().toString());
                    dialog1.dismiss();
                }
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void setData(String type) {
        //set data arabic
        if (providerInfo.getData().getProvider().getDescription() != null)
            About_Res.setText(providerInfo.getData().getProvider().getDescription());
        if (providerInfo.getData().getProvider().getName() != null)
            RestaurantsName.setText(providerInfo.getData().getProvider().getName());
        //set data english
        if (providerInfo.getData().getProvider().getDescriptionEn() != null)
            About_ResEn.setText(providerInfo.getData().getProvider().getDescriptionEn());
        if (providerInfo.getData().getProvider().getNameEn() != null)
            RestaurantsNameEn.setText(providerInfo.getData().getProvider().getNameEn());
        if (providerInfo.getData().getProvider().getAddress() != null || providerInfo.getData().getProvider().getAddressEn() != null)
            checkLang();
        if (type.equals("Ar"))
            ArabicTab("show");
        else
            EnglishTab("show");
        closeKeyBoard();
    }

    private void checkLang() {
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                Address.setText(providerInfo.getData().getProvider().getAddress());
            }else if (pref.getLagu().equals("en")) {
                Address.setText(providerInfo.getData().getProvider().getAddressEn());
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                Address.setText(providerInfo.getData().getProvider().getAddressEn());
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                Address.setText(providerInfo.getData().getProvider().getAddress());
            }
        }
    }

    private void updateDateProvider(){
        if (!ArabicTab("V")){
            ArabicTab("show");
            Toast.makeText(this, R.string.Complete_data, Toast.LENGTH_LONG).show();
        }else if (!EnglishTab("V")){
            EnglishTab("show");
            Toast.makeText(this, R.string.Complete_data, Toast.LENGTH_LONG).show();
        }else {
            if (makeRequestObjectUpdate() != null) {
                viewModel.updateProvideInfo(Modify_personal_infoActivity.this, dialog, new ProviderInfoRequest(
                        providerInfo.getData().getProvider().getName(), providerInfo.getData().getProvider().getNameEn(), providerInfo.getData().getProvider().getDescription(),
                        providerInfo.getData().getProvider().getAddress(), providerInfo.getData().getProvider().getDescriptionEn(), providerInfo.getData().getProvider().getAddressEn(), cityRow.getId(),
                        providerInfo.getData().getProvider().getLatitude(), providerInfo.getData().getProvider().getLongitude())).observe(Modify_personal_infoActivity.this, new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt defualt) {
                        Toast.makeText(Modify_personal_infoActivity.this, R.string.Successfully_updated, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Modify_personal_infoActivity.this, HomeActivity.class)
                        .putExtra("type","Modify")
                                .setFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TASK));
                    }
                });
            }
        }
    }

    private ProviderInformation makeRequestObjectUpdate(){
        providerInfo.getData().getProvider().setName(RestaurantsName.getText().toString());
        providerInfo.getData().getProvider().setNameEn(RestaurantsNameEn.getText().toString());
        providerInfo.getData().getProvider().setDescription(About_Res.getText().toString());
        providerInfo.getData().getProvider().setDescriptionEn(About_ResEn.getText().toString());
        if (providerInfo.getData().getProvider().getAddressEn() == null){
            Toast.makeText(Modify_personal_infoActivity.this, R.string.way_enter_addressEn, Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (providerInfo.getData().getProvider().getAddress() == null){
            Toast.makeText(Modify_personal_infoActivity.this, R.string.way_enter_address, Toast.LENGTH_SHORT).show();
            return null;
        }
        else if (providerInfo.getData().getProvider().getLatitude() == null ||
                providerInfo.getData().getProvider().getLongitude() == null){
            Toast.makeText(Modify_personal_infoActivity.this, R.string.way_set_location, Toast.LENGTH_SHORT).show();
            return null;
        }{
            Log.i("informationnnnnn",providerInfo.getData().getProvider().getAddress()+"" +
                    " "+providerInfo.getData().getProvider().getAddressEn()+" "+
                    providerInfo.getData().getProvider().getLongitude()+" "+
                    providerInfo.getData().getProvider().getLongitude());
            return providerInfo;
        }
    }

    private boolean ArabicTab(String type){
        Layout_Ar.setBackground(getResources().getDrawable(R.drawable.style_tab_active));
        ArabicText.setTextColor(getResources().getColor(R.color.white));
        Layout_En.setBackground(getResources().getDrawable(R.drawable.style_tab_normal));
        EnglishText.setTextColor(getResources().getColor(R.color.colorPrimary));
        if (type.equals("show")) {
            Layout_TabEnglish.setVisibility(View.GONE);
            Layout_TabArabic.setVisibility(View.VISIBLE);
            if (providerInfo.getData().getProvider().getAddress() != null)
                Address.setText(providerInfo.getData().getProvider().getAddress());
            closeKeyBoard();
            return true;
        }
        else if (RestaurantsName.getText().toString().isEmpty()
                || About_Res.getText().toString().isEmpty())
            return false;
        else
            return true;

    }

    private boolean EnglishTab(String type){
        Layout_En.setBackground(getResources().getDrawable(R.drawable.style_tab_active));
        EnglishText.setTextColor(getResources().getColor(R.color.white));
        Layout_Ar.setBackground(getResources().getDrawable(R.drawable.style_tab_normal));
        ArabicText.setTextColor(getResources().getColor(R.color.colorPrimary));
        if (type.equals("show")) {
            Layout_TabEnglish.setVisibility(View.VISIBLE);
            Layout_TabArabic.setVisibility(View.GONE);
            if (providerInfo.getData().getProvider().getAddressEn() != null)
                Address.setText(providerInfo.getData().getProvider().getAddressEn());
            closeKeyBoard();
            return true;
        }

        else if (RestaurantsNameEn.getText().toString().isEmpty()
                || About_ResEn.getText().toString().isEmpty())
            return false;
        else
            return true;

    }

    private void initMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                if (getIntent().getExtras()!=null){
                    if (providerInfo.getData().getProvider().getLatitude() != null && providerInfo.getData().getProvider().getLongitude() != null)
                        addMarker(new LatLng(providerInfo.getData().getProvider().getLatitude(),providerInfo.getData().getProvider().getLongitude()));
                }
            }
        });

    }

    private void addMarker(LatLng latLng){
        gmap.addMarker(new MarkerOptions().position(latLng).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.location_logo_green)));
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
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
                if (Common.knowLang(Modify_personal_infoActivity.this).equals("ar"))
                    CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getName());
                else if (Common.knowLang(Modify_personal_infoActivity.this).equals("en"))
                    CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getNameEn());
                CitySelected.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerCity.setSelection(providerInfo.getData().getProvider().getCityId());
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (!firstOpen){
            closeKeyBoard();
            firstOpen = true;
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    private void setLocationUpdate() {
        if (getIntent().getExtras()!=null){

//            providerInfo.getData().getProvider().setLatitude(getIntent().getExtras().getDouble("Lat"));
//            providerInfo.getData().getProvider().setLongitude(getIntent().getExtras().getDouble("Long"));
//
////            Data data = new Data(providerInfo.getData().getId(),
////                    providerInfo.getData().getLoginPhone(), providerInfo.getData().getEmail(),
////                    providerInfo.getData().getName(), providerInfo.getData().getNameEn(), providerInfo.getData().getCityId(),
////                    getIntent().getExtras().getString("Address"),getIntent().getExtras().getString("Address"),
////                    getIntent().getExtras().getDouble("Lat"),getIntent().getExtras().getDouble("Long"),
////                    providerInfo.getData().getDescription(),providerInfo.getData().getDescriptionEn(),
////                    providerInfo.getData().getDeliveryPrice(),providerInfo.getData().getMainImageId(), providerInfo.getData().getMainImage());
////            ProviderInfo providerInfo2 = new ProviderInfo(this.providerInfo.getMessage(), data, this.providerInfo.getSuccess(), this.providerInfo.getCode(), this.providerInfo.getErrors());
//
//            Log.i("ttttttt", providerInfo.getData().getProvider().getLatitude()+"");
            providerInfo = getIntent().getExtras().getParcelable("object");
        }
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        int dfValue = Layout_ProInfo.getDescendantFocusability();
        Layout_ProInfo.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        RestaurantsName.clearFocus();
        About_Res.clearFocus();
        RestaurantsNameEn.clearFocus();
        About_ResEn.clearFocus();
        Layout_ProInfo.setDescendantFocusability(dfValue);
    }
}
