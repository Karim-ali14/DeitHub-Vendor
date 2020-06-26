package com.dopave.diethub_vendor.UI.CreateVehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForResImage;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.DrivingLicence;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.VehicleLicence;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.VehicleTypes.RowVehicleTypes;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateVehicleActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    RecyclerView Recycler;
    ConstraintLayout LayoutCreateVehicles,YearManufactureLayout,VehicleTypeLayout;
    EditText VehicleID,VehicleModel;
    Spinner spinnerYears,spinnerVehicleType;
    RowVehicleTypes rowVehicleTypes;
    TextView VehicleTypeSelected,YearSelected;
    boolean IsSelectedVehicleType,IsSelectedVehicleYears ;
    int selectedYear = 0;
    CreateVehicleViewModel viewModel;
    String deliveryId;
    ProgressDialog dialog;
    GetVehicleData VehicleData;
    VehicleTypes vehicleTypes;
    ImageView Type_VehicleImage,YearImage;
    int i = 0;
    int SpinnerYearClick = 0;
    int SpinnerVehicleClick = 0;
    boolean firstOpen,isSpinnerYear,isSpinnerVehicle;
    List<com.dopave.diethub_vendor.Models.GetVehicles.Image> list;
    List<Image> listImageRequest;
    int numberOfIndexes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        dialog = new ProgressDialog(this);
        dialog.show();
        listImageRequest = new ArrayList<>();
        viewModel = ViewModelProviders.of(this).get(CreateVehicleViewModel.class);
        YearSelected = findViewById(R.id.YearSelected);
        VehicleTypeSelected = findViewById(R.id.VehicleTypeSelected);
        spinnerVehicleType = findViewById(R.id.spinnerVehicleType);
        spinnerYears = findViewById(R.id.spinnerYears);
        Recycler = findViewById(R.id.Recycler_Res_Icons);
        Recycler.setHasFixedSize(true);
        Recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        LayoutCreateVehicles = findViewById(R.id.LayoutCreateVehicles);
        YearManufactureLayout = findViewById(R.id.YearManufactureLayout);
        VehicleTypeLayout = findViewById(R.id.VehicleTypeLayout);
        VehicleID = findViewById(R.id.VehicleID);
        VehicleModel = findViewById(R.id.VehicleModel);
        YearImage = findViewById(R.id.YearImage);
        Type_VehicleImage = findViewById(R.id.Type_VehicleImage);
        LayoutCreateVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });

        getChangeEditText();

        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (getIntent().getExtras().getString("type").equals("update")) {
            deliveryId = getIntent().getExtras().getString("deliveryId");
            getVehicleData();
        }else{
            getVehicleTypes();
        }

    }

    private void getChangeEditText() {
        VehicleID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    VehicleID.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    VehicleID.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.police_car),null,null,null);
                }
                else {
                    VehicleID.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    VehicleID.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.car),null,null,null);

                }
            }
        });
        VehicleModel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    VehicleModel.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    VehicleModel.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.steering_wheel_active),null,null,null);
                }
                else {
                    VehicleModel.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    VehicleModel.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.steering_wheel),null,null,null);

                }
            }
        });
        spinnerYears.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    YearImage.setImageResource(R.drawable.steering_wheel_active);
                    YearManufactureLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    // Load your spinner here
                    isSpinnerYear = true;
                }
                return false;
            }
        });
        spinnerVehicleType.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Type_VehicleImage.setImageResource(R.drawable.pencil_active);
                    VehicleTypeLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    isSpinnerVehicle = true;
                }
                return false;
            }
        });
    }


    private void getVehicleData() {
        viewModel.getVehicleData(deliveryId,this,dialog).observe(this, new Observer<GetVehicleData>() {
            @Override
            public void onChanged(GetVehicleData getVehicleData) {
                VehicleData = getVehicleData;
                setData();
            }
        });
    }

    private void setData() {
        VehicleID.setText(VehicleData.getData().getNumber());
        VehicleModel.setText(VehicleData.getData().getModel());
        spinnerYears.setSelection(VehicleData.getData().getYear());
        list.addAll(VehicleData.getData().getImages());
        numberOfIndexes = list.size();
        Recycler.setAdapter(new AdapterForResImage(list,this,listImageRequest,"update",numberOfIndexes,Recycler));
        getVehicleTypes();
    }

    private List<com.dopave.diethub_vendor.Models.GetVehicles.Image> getData(){
       list = new ArrayList<>();
       list.add(new com.dopave.diethub_vendor.Models.GetVehicles.Image());
        return list;
    }

    public void onClick(View view) {
        if (getIntent().getExtras().getString("type").equals("update")) {
            updateVehicle();
        }else {
            if (VehicleID.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            else if (VehicleModel.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
            else
                createVehicle();
        }
    }

    private void updateVehicle() {
        dialog.show();
        String ImageUrl = "PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI3NCIgaGVpZ2h0PSI0Ni4yNSIgdmlld0JveD0iMCAwIDc0IDQ2LjI1Ij48ZGVmcz48c3R5bGU+LmF7ZmlsbDojZmZmO308L3N0eWxlPjwvZGVmcz48cGF0aCBjbGFzcz0iYSIgZD0iTTY5LjY2MywxNS45SDY4LjIxOHYtNi41QTUuNzc5LDUuNzc5LDAsMCwwLDU5LjQsNC40NzksNS43ODksNS43ODksMCwwLDAsNTMuNzY1LDBoMGE1Ljc4OCw1Ljc4OCwwLDAsMC01Ljc4MSw1Ljc4MVYxNS45SDI2LjAxN1Y1Ljc4MUE1Ljc4Miw1Ljc4MiwwLDAsMCwxNC42LDQuNDc5LDUuNzc5LDUuNzc5LDAsMCwwLDUuNzgxLDkuMzk1djYuNUg0LjMzNUE0LjM0MSw0LjM0MSwwLDAsMCwwLDIwLjIzNHY1Ljc4MWE0LjM0MSw0LjM0MSwwLDAsMCw0LjMzNiw0LjMzNkg1Ljc4MXY2LjVBNS43NzksNS43NzksMCwwLDAsMTQuNiw0MS43NzFhNS43ODksNS43ODksMCwwLDAsNS42MzIsNC40NzloMGE1Ljc4OCw1Ljc4OCwwLDAsMCw1Ljc4MS01Ljc4MVYzMC4zNTJINDcuOTgyVjQwLjQ2OWE1Ljc4Miw1Ljc4MiwwLDAsMCwxMS40MTUsMS4zLDUuNzc5LDUuNzc5LDAsMCwwLDguODIxLTQuOTE2di02LjVoMS40NDVBNC4zNDEsNC4zNDEsMCwwLDAsNzQsMjYuMDE2VjIwLjIzNEE0LjM0MSw0LjM0MSwwLDAsMCw2OS42NjMsMTUuOVpNNS43ODEsMjcuNDYxSDQuMzM1QTEuNDQ3LDEuNDQ3LDAsMCwxLDIuODksMjYuMDE2VjIwLjIzNGExLjQ0NywxLjQ0NywwLDAsMSwxLjQ0NS0xLjQ0NUg1Ljc4MVptOC42NzIsOS4zOTVhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NGEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm04LjY3NCwzLjYxM2EyLjg5NCwyLjg5NCwwLDAsMS0yLjg5MSwyLjg5MWgwYTIuODk0LDIuODk0LDAsMCwxLTIuODkxLTIuODkxVjUuNzgxYTIuODkxLDIuODkxLDAsMCwxLDUuNzgzLDBaTTQ3Ljk3NCwyMS42OEgzNy44NjdhMS40NDUsMS40NDUsMCwwLDAsMCwyLjg5MUg0Ny45NzR2Mi44OTFIMjYuMDA5VjE4Ljc4OUg0Ny45NzRabTguNjgyLDE4Ljc4OWEyLjg5MSwyLjg5MSwwLDAsMS01Ljc4MywwVjIzLjJhLjg0OS44NDksMCwwLDEtLjAwOC4wOTR2LS4zMzVhLjg0OS44NDksMCwwLDEsLjAwOC4wOTRWNS43ODFhMi44OTQsMi44OTQsMCwwLDEsMi44OTEtMi44OTFoMGEyLjg5NCwyLjg5NCwwLDAsMSwyLjg5MSwyLjg5MVptOC42NzItMy42MTNhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NWEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm01Ljc4MS0xMC44NGExLjQ0NywxLjQ0NywwLDAsMS0xLjQ0NSwxLjQ0NUg2OC4yMThWMTguNzg5aDEuNDQ1YTEuNDQ3LDEuNDQ3LDAsMCwxLDEuNDQ1LDEuNDQ1Wm0wLDAiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAuMDAxIDApIi8+PHBhdGggY2xhc3M9ImEiIGQ9Ik0yMTMuNDYyLDE1Mi44OTFhMS40NDUsMS40NDUsMCwwLDEsMC0yLjg5MWgwYTEuNDQ1LDEuNDQ1LDAsMCwxLDAsMi44OTFabTAsMCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTE4MS4zNzMgLTEyOC4zMikiLz48L3N2Zz4=";
        List<Image> list = new ArrayList<>();
        list.add(new Image(ImageUrl));
        list.add(new Image(ImageUrl));
        list.add(new Image(ImageUrl));
        list.add(new Image(ImageUrl));
        list.add(new Image(ImageUrl));

        viewModel.updateVehicle(VehicleData.getData().getId()+""
                ,VehicleID.getText().toString(),VehicleModel.getText().toString(),selectedYear
                ,rowVehicleTypes.getId(),ImageUrl,ImageUrl,list,this,dialog)
                .observe(this, new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                Toast.makeText(CreateVehicleActivity.this,
                        getResources().getString(R.string.Updated), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateVehicleActivity.this, HomeActivity.class)
                .putExtra("type","CreateDeliveryActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    private void showSuccessDialog() {
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_registraion_success, null);
        TextView Massage = view.findViewById(R.id.Massage);
        Button LoginButton = view.findViewById(R.id.LoginButton);
        Adialog.setView(view);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateVehicleActivity.this, Login_inActivity.class));
                finish();
            }
        });
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        int dfValue = LayoutCreateVehicles.getDescendantFocusability();
        LayoutCreateVehicles.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        VehicleID.clearFocus();
        VehicleModel.clearFocus();
        LayoutCreateVehicles.setDescendantFocusability(dfValue);
    }

    private void createVehicle(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        String ImageUrl = "PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI3NCIgaGVpZ2h0PSI0Ni4yNSIgdmlld0JveD0iMCAwIDc0IDQ2LjI1Ij48ZGVmcz48c3R5bGU+LmF7ZmlsbDojZmZmO308L3N0eWxlPjwvZGVmcz48cGF0aCBjbGFzcz0iYSIgZD0iTTY5LjY2MywxNS45SDY4LjIxOHYtNi41QTUuNzc5LDUuNzc5LDAsMCwwLDU5LjQsNC40NzksNS43ODksNS43ODksMCwwLDAsNTMuNzY1LDBoMGE1Ljc4OCw1Ljc4OCwwLDAsMC01Ljc4MSw1Ljc4MVYxNS45SDI2LjAxN1Y1Ljc4MUE1Ljc4Miw1Ljc4MiwwLDAsMCwxNC42LDQuNDc5LDUuNzc5LDUuNzc5LDAsMCwwLDUuNzgxLDkuMzk1djYuNUg0LjMzNUE0LjM0MSw0LjM0MSwwLDAsMCwwLDIwLjIzNHY1Ljc4MWE0LjM0MSw0LjM0MSwwLDAsMCw0LjMzNiw0LjMzNkg1Ljc4MXY2LjVBNS43NzksNS43NzksMCwwLDAsMTQuNiw0MS43NzFhNS43ODksNS43ODksMCwwLDAsNS42MzIsNC40NzloMGE1Ljc4OCw1Ljc4OCwwLDAsMCw1Ljc4MS01Ljc4MVYzMC4zNTJINDcuOTgyVjQwLjQ2OWE1Ljc4Miw1Ljc4MiwwLDAsMCwxMS40MTUsMS4zLDUuNzc5LDUuNzc5LDAsMCwwLDguODIxLTQuOTE2di02LjVoMS40NDVBNC4zNDEsNC4zNDEsMCwwLDAsNzQsMjYuMDE2VjIwLjIzNEE0LjM0MSw0LjM0MSwwLDAsMCw2OS42NjMsMTUuOVpNNS43ODEsMjcuNDYxSDQuMzM1QTEuNDQ3LDEuNDQ3LDAsMCwxLDIuODksMjYuMDE2VjIwLjIzNGExLjQ0NywxLjQ0NywwLDAsMSwxLjQ0NS0xLjQ0NUg1Ljc4MVptOC42NzIsOS4zOTVhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NGEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm04LjY3NCwzLjYxM2EyLjg5NCwyLjg5NCwwLDAsMS0yLjg5MSwyLjg5MWgwYTIuODk0LDIuODk0LDAsMCwxLTIuODkxLTIuODkxVjUuNzgxYTIuODkxLDIuODkxLDAsMCwxLDUuNzgzLDBaTTQ3Ljk3NCwyMS42OEgzNy44NjdhMS40NDUsMS40NDUsMCwwLDAsMCwyLjg5MUg0Ny45NzR2Mi44OTFIMjYuMDA5VjE4Ljc4OUg0Ny45NzRabTguNjgyLDE4Ljc4OWEyLjg5MSwyLjg5MSwwLDAsMS01Ljc4MywwVjIzLjJhLjg0OS44NDksMCwwLDEtLjAwOC4wOTR2LS4zMzVhLjg0OS44NDksMCwwLDEsLjAwOC4wOTRWNS43ODFhMi44OTQsMi44OTQsMCwwLDEsMi44OTEtMi44OTFoMGEyLjg5NCwyLjg5NCwwLDAsMSwyLjg5MSwyLjg5MVptOC42NzItMy42MTNhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NWEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm01Ljc4MS0xMC44NGExLjQ0NywxLjQ0NywwLDAsMS0xLjQ0NSwxLjQ0NUg2OC4yMThWMTguNzg5aDEuNDQ1YTEuNDQ3LDEuNDQ3LDAsMCwxLDEuNDQ1LDEuNDQ1Wm0wLDAiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAuMDAxIDApIi8+PHBhdGggY2xhc3M9ImEiIGQ9Ik0yMTMuNDYyLDE1Mi44OTFhMS40NDUsMS40NDUsMCwwLDEsMC0yLjg5MWgwYTEuNDQ1LDEuNDQ1LDAsMCwxLDAsMi44OTFabTAsMCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTE4MS4zNzMgLTEyOC4zMikiLz48L3N2Zz4=";
//        Common.getAPIRequest().signUpVehicle(new CreateVehicleRequest(
//                getIntent().getExtras().getString("checkCode"),
//                VehicleID.getText().toString(),VehicleModel.getText().toString(),selectedYear,rowVehicleTypes.getId(),
//                new MainImage(ImageUrl),
//                new DrivingLicence(ImageUrl),
//                new VehicleLicence(ImageUrl)),getIntent().getExtras().getInt("id")).enqueue(new Callback<SignUp>() {
//            @Override
//            public void onResponse(Call<SignUp> call, Response<SignUp> response) {
//                if (response.code() == 201){
//
//                }else {
//                    try {
//                        Log.i("TTTTTT",new JSONObject(response.errorBody().string()).toString());
//                        Toast.makeText(Complete_RegistrationActivity.this,
//                                new JSONObject(response.errorBody().string()).getString("message")
//                                , Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                dialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<SignUp> call, Throwable t) {
//                dialog.dismiss();
//            }
//        });
        List<com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image> list = new ArrayList<>();
        list.add(new com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image(ImageUrl));
        viewModel.createVehicle("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                getIntent().getExtras().getString("deliveryId"),
                new CreateVehicleRequest(VehicleID.getText().toString(),
                        VehicleModel.getText().toString(),
                        selectedYear,rowVehicleTypes.getId(),
                        new DrivingLicence(ImageUrl),
                        new VehicleLicence(ImageUrl),list),this)
                .observe(this, new Observer<CreateVehicleRespons>() {
            @Override
            public void onChanged(CreateVehicleRespons createVehicleRespons) {
                showSuccessDialog();
            }
        });
    }

    private void getYears(){
        viewModel.getAllYears(this).observe(this, new Observer<Years>() {
            @Override
            public void onChanged(Years years) {
                dialog.dismiss();
                if (getIntent().getExtras().getString("type").equals("create")) {
                    Recycler.setAdapter(new AdapterForResImage(getData(),
                            CreateVehicleActivity.this, listImageRequest, "create",
                            0, Recycler));
                }
                AdapterOfSpinnerYear arrayAdapter = new AdapterOfSpinnerYear(CreateVehicleActivity.this,
                        R.layout.city_item,years.getData());
                spinnerYears.setAdapter(arrayAdapter);
                spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedYear = (Integer) parent.getItemAtPosition(position);
                        YearSelected.setText(selectedYear+"");
                        YearSelected.setTextColor(getResources().getColor(R.color.black));
                        if (getIntent().getExtras().getString("type").equals("update") && i ==1) {
                            i++;
                            selectedYear = VehicleData.getData().getYear();
                            YearSelected.setText(selectedYear+"");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });
    }

    private void getVehicleTypes(){
        viewModel.getAllVehicleTypes(this).observe(this, new Observer<VehicleTypes>() {
            @Override
            public void onChanged(final VehicleTypes vehicleTypes) {
                CreateVehicleActivity.this.vehicleTypes = vehicleTypes;
                getYears();
                AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(CreateVehicleActivity.this,
                        R.layout.city_item,vehicleTypes.getData().getRowVehicleTypes());

                spinnerVehicleType.setAdapter(arrayAdapter);

                spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(CreateVehicleActivity.this, "",
                                Toast.LENGTH_SHORT).show();
                        rowVehicleTypes = ((RowVehicleTypes) parent.getItemAtPosition(position));
                        VehicleTypeSelected.setText(((RowVehicleTypes) parent.getItemAtPosition(position)).getType());
                        VehicleTypeSelected.setTextColor(getResources().getColor(R.color.black));
                        if (getIntent().getExtras().getString("type").equals("update") && i == 0) {
                            i++;
                            for (RowVehicleTypes row : vehicleTypes.getData().getRowVehicleTypes()) {
                                if (row.getId() == VehicleData.getData().getVehicleTypeId()) {
                                    VehicleTypeSelected.setText(row.getType());
                                    rowVehicleTypes = row;
                                    Log.i("JJJJJJ", "1");
                                    Log.i("GGGGGGG", row.getType());
                                }
                            }
                        }
                        Type_VehicleImage.setImageResource(R.drawable.pencil_);
                        VehicleTypeLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Type_VehicleImage.setImageResource(R.drawable.pencil_);
                        VehicleTypeLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                        Toast.makeText(CreateVehicleActivity.this, "",
                                Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (!firstOpen){
            closeKeyBoard();
            firstOpen = true;
        }
        if (isSpinnerYear){
            if (SpinnerYearClick == 0)
                SpinnerYearClick++;
            else {
                SpinnerYearClick = 0;
                YearImage.setImageResource(R.drawable.steering_wheel);
                YearManufactureLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput));
            }
        }
        if (isSpinnerVehicle){
            if (SpinnerVehicleClick == 0)
                SpinnerVehicleClick++;
            else {
                SpinnerVehicleClick = 0;
                Type_VehicleImage.setImageResource(R.drawable.pencil_);
                VehicleTypeLayout.setBackground(getResources().getDrawable(R.drawable.style_textinput));
            }
        }
    }

    public class AdapterOfSpinner extends ArrayAdapter<RowVehicleTypes> {
        List<RowVehicleTypes> list;
        LayoutInflater inflater;
        public AdapterOfSpinner(Activity context, int id , List<RowVehicleTypes> list)
        {
            super(context,id,list);
            this.list=list;
            inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);

            return vv;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);
            Tname.setText(list.get(position).getType());
            return vv;
        }

    }

    public class AdapterOfSpinnerYear extends ArrayAdapter<Integer> {
        List<Integer> list;
        LayoutInflater inflater;
        public AdapterOfSpinnerYear(Activity context, int id , List<Integer> list)
        {
            super(context,id,list);
            this.list=list;
            inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);

            return vv;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);
            Tname.setText(list.get(position) + "");
            return vv;
        }

    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        list.add(new com.dopave.diethub_vendor.Models.GetVehicles.Image(bitmap));
                        listImageRequest.add(new Image(imageString));
                        if (getIntent().getExtras().getString("type").equals("update")) {
                            Recycler.setAdapter(new AdapterForResImage(list,this,
                                    listImageRequest,"update",numberOfIndexes,Recycler));
                        }else {
                            Recycler.setAdapter(new AdapterForResImage(list,this,
                                    listImageRequest,"create",numberOfIndexes,Recycler));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
