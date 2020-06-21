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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.UpdateVehicle.UpdateVehicle;
import com.dopave.diethub_vendor.Models.VehicleTypes.RowVehicleTypes;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateVehicleActivity extends AppCompatActivity {
    RecyclerView Recycler;
    ConstraintLayout LayoutCreateVehicles;
    EditText VehicleID,VehicleModel;
    Spinner spinnerYears,spinnerVehicleType;
    RowVehicleTypes rowVehicleTypes;
    TextView VehicleTypeSelected,YearSelected;
    boolean firstSelect = false;
    int selectedYear = 0;
    CreateVehicleViewModel viewModel;
    String deliveryId;
    ProgressDialog dialog;
    GetVehicleData VehicleData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        dialog = new ProgressDialog(this);
        dialog.show();
        viewModel = ViewModelProviders.of(this).get(CreateVehicleViewModel.class);
        YearSelected = findViewById(R.id.YearSelected);
        VehicleTypeSelected = findViewById(R.id.VehicleTypeSelected);
        spinnerVehicleType = findViewById(R.id.spinnerVehicleType);
        spinnerYears = findViewById(R.id.spinnerYears);
        Recycler = findViewById(R.id.Recycler_Res_Icons);
        Recycler.setHasFixedSize(true);
        Recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        Recycler.setAdapter(new AdapterForResImage(getData(),this));
        LayoutCreateVehicles = findViewById(R.id.LayoutCreateVehicles);
        VehicleID = findViewById(R.id.VehicleID);
        VehicleModel = findViewById(R.id.VehicleModel);
        LayoutCreateVehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });

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
                    VehicleModel.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.pencil_active),null,null,null);
                }
                else {
                    VehicleModel.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    VehicleModel.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateVehicleActivity.this,R.drawable.pencil_),null,null,null);

                }
            }
        });

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
        getVehicleTypes();
    }

    private List<String> getData(){
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }

    public void onClick(View view) {
        if (getIntent().getExtras().getString("type").equals("update")) {
            updateVehcle();
        }else {
            if (VehicleID.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            else if (VehicleModel.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
            else
                createVehicle();
        }
    }

    private void updateVehcle() {
//        Common.getAPIRequest().updateVehicle("Bearer "+
//                Common.currentPosition.getData().getToken().getAccessToken(),
//                deliveryId,VehicleData.getData().getId(),new UpdateVehicle(
//                        VehicleID.getText().toString(),VehicleModel.getText().toString(),VehicleTypeSelected,))
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

        List<Image> list = new ArrayList<>();
        list.add(new Image(ImageUrl));
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
                Log.i("TTTTTT",years.getData().toString());
                AdapterOfSpinnerYear arrayAdapter = new AdapterOfSpinnerYear(CreateVehicleActivity.this,
                        R.layout.city_item,years.getData());

                spinnerYears.setAdapter(arrayAdapter);
                spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedYear = (Integer) parent.getItemAtPosition(position);
                        YearSelected.setText(selectedYear+"");
                        YearSelected.setTextColor(getResources().getColor(R.color.black));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                selectedYear = VehicleData.getData().getYear();
                YearSelected.setText(selectedYear+"");
            }
        });
    }

    private void getVehicleTypes(){
        viewModel.getAllVehicleTypes(this).observe(this, new Observer<VehicleTypes>() {
            @Override
            public void onChanged(VehicleTypes vehicleTypes) {
                getYears();
                AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(CreateVehicleActivity.this,
                        R.layout.city_item,vehicleTypes.getData().getRowVehicleTypes());

                spinnerVehicleType.setAdapter(arrayAdapter);
                spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        rowVehicleTypes = ((RowVehicleTypes) parent.getItemAtPosition(position));
                        VehicleTypeSelected.setText(((RowVehicleTypes) parent.getItemAtPosition(position)).getType());
                        VehicleTypeSelected.setTextColor(getResources().getColor(R.color.black));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                for (RowVehicleTypes row : vehicleTypes.getData().getRowVehicleTypes()){
                    Log.i("TTTTTTT",(row.getId() == VehicleData.getData().getVehicleTypeId()) +"");
                    if (row.getId() == VehicleData.getData().getVehicleTypeId()){
                           VehicleTypeSelected.setText(row.getType());
                           rowVehicleTypes = row;
                    }
                }
            }
        });
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

}
