package com.dopave.diethub_vendor.UI.Setting;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_personal_infoActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ImageView Select_Location,search_Icon;
    private boolean checkForPermissions = false;
    private final static String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private FusedLocationProviderClient providerClient;
    EditText searchInMap;
    Button SaveButton;
    LatLng SelectedLocation;
    ProviderInformation providerInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Select_Location = findViewById(R.id.Select_Location);
        searchInMap = findViewById(R.id.searchInMap);
        search_Icon = findViewById(R.id.search_Icon);
        SaveButton = findViewById(R.id.Savebutton);
        providerClient = LocationServices.getFusedLocationProviderClient(this);

        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestPermissions();

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedLocation != null) {
                    providerInfo.getData().getProvider().setLatitude(SelectedLocation.latitude);
                    providerInfo.getData().getProvider().setLongitude(SelectedLocation.longitude);
                    startActivity(new Intent(MapsActivity.this, Modify_personal_infoActivity.class)
                            .putExtra("object",providerInfo));
                    finish();
                }
                else
                    Toast.makeText(MapsActivity.this, R.string.no_changes, Toast.LENGTH_SHORT).show();
            }
        });
    }


    void requestPermissions() {
        String[] Permissions = {ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i("MapInfor", "SDK > 23");
            if (checkSelfPermission(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.i("MapInfor", "Permissions are Granted");
                InitMap();
                checkForPermissions = true;
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, Permissions, 25);
            }
        } else {
            InitMap();
            checkForPermissions = true;
        }
    }

    private void InitMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getIntent().getExtras() != null) {
            providerInfo = getIntent().getExtras().getParcelable("object");
            if (providerInfo.getData().getProvider().getLatitude() != null &&
                providerInfo.getData().getProvider().getLongitude() != null )
            addMarker(new LatLng(
                    providerInfo.getData().getProvider().getLatitude()
                    ,
                    providerInfo.getData().getProvider().getLongitude()
            ));
        }

        Select_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }

    private void getLastLocation() {
        try {
            providerClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() != null) {

                                    Location result = task.getResult();
                                    LatLng latLng = new LatLng(result.getLatitude(), result.getLongitude());
                                    addMarker(latLng);
                                    SelectedLocation = latLng;
                                }
                            }
                        }
                    });

        } catch (SecurityException e) {
            Log.d("MainD", "Massage is " + e.getMessage());
        }


    }

    public void moveCameraT(LatLng latLng, float Zoom) {
        Log.i("infor", "Move Camera to Your Location");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom));
    }

    private void addMarker(LatLng latLng){
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.mark_white);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);
        mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        moveCameraT(latLng,10);

        search_In_Map();
    }

    public void search_In_Map() {
        searchInMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    getLocationByName();
                }
                return false;
            }
        });
        search_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationByName();
            }
        });
    }

    public void getLocationByName() {
        Address address = null;
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(searchInMap.getText().toString(), 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                addMarker(latLng);
                SelectedLocation = latLng;
                Log.i("inforA", address.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("infor", "Get This Location By Name : " + address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 25: {
                for (int permissionsReqult : grantResults) {
                    if (permissionsReqult == PackageManager.PERMISSION_GRANTED) {
                        checkForPermissions = true;
//                        getLastLocation();
                        //_GetUpdateOfLocation();
                    } else {
                        checkForPermissions = false;
                    }
                }
                if (checkForPermissions) {
                    Log.i("infor", "Map is Creating ");
                    InitMap();
                }
            }
        }
    }

    public void BackButton(View view) {
        finish();
    }

}
