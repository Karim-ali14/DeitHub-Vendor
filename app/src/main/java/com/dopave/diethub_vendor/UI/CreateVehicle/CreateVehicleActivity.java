package com.dopave.diethub_vendor.UI.CreateVehicle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.net.Uri;
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
import android.widget.LinearLayout;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class CreateVehicleActivity extends AppCompatActivity {
    public static final int SELECT_IMAGE_FOR_VEHICLE = 1;
    public static final int SELECT_IMAGE_FOR_DRIVING_LICENSE = 2;
    public static final int SELECT_IMAGE_FOR_VEHICLE_LICENSE = 3;
    RecyclerView Recycler;
    ConstraintLayout LayoutCreateVehicles,YearManufactureLayout,VehicleTypeLayout;
    CardView VehicleLicenceLayout,DrivingLicenseLayout;
    LinearLayout driving_licence_Add,vehicle_licence_Add;
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
    ImageView Type_VehicleImage,YearImage,driving_licence_Image,vehicle_licence_Image;
    int updateYear = 0;
    int updateVehicle = 0;
    int SpinnerYearClick = 0;
    int SpinnerVehicleClick = 0;
    boolean firstOpen,isSpinnerYear,isSpinnerVehicle;
    List<com.dopave.diethub_vendor.Models.GetVehicles.Image> list;
    List<Image> listImageRequest;
    int numberOfIndexes = 0;
    String driving_licence_ImageBase46,vehicle_licence_ImageBase46;
    Button Confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_vehicle);
        Confirm = findViewById(R.id.Confirm);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickConfirmButton();
            }
        });
        DrivingLicenseLayout = findViewById(R.id.DrivingLicenseLayout);
        DrivingLicenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(SELECT_IMAGE_FOR_DRIVING_LICENSE);
            }
        });
        VehicleLicenceLayout = findViewById(R.id.vehicleLicenceLayout);
        VehicleLicenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(SELECT_IMAGE_FOR_VEHICLE_LICENSE);
            }
        });
        driving_licence_Add = findViewById(R.id.driving_licence_Add);
        vehicle_licence_Add = findViewById(R.id.vehicle_licence_Add);

        driving_licence_Image = findViewById(R.id.driving_licence_Image);
        vehicle_licence_Image = findViewById(R.id.vehicle_licence_Image);

        dialog = new ProgressDialog(this);
        dialog.show();
        listImageRequest = new ArrayList<>();
        list = new ArrayList<>();
        list.add(new com.dopave.diethub_vendor.Models.GetVehicles.Image());
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
            Confirm.setText(getResources().getString(R.string.update_vehicle));
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
                    closeKeyBoard();
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
                    closeKeyBoard();
                }
                return false;
            }
        });
    }

    private void getVehicleData() {
        viewModel.getVehicleData(deliveryId,this,dialog,viewModel).observe(this, new Observer<GetVehicleData>() {
            @Override
            public void onChanged(GetVehicleData getVehicleData) {
                VehicleData = getVehicleData;
                setData();
            }
        });
    }

    public void setData() {
        VehicleID.setText(VehicleData.getData().getNumber());
        VehicleModel.setText(VehicleData.getData().getModel());
        spinnerYears.setSelection(VehicleData.getData().getYear());
        if (VehicleData.getData().getImages() != null) {
            if (VehicleData.getData().getImages().size() != 0) {
                list.addAll(VehicleData.getData().getImages());
                numberOfIndexes = list.size();
            }
        }
        if (VehicleData.getData().getDrivingLicence() != null){
            String path = Common.BaseUrl + "images/" +
                    VehicleData.getData().getDrivingLicence().getFor() + "/" +
                    Uri.encode(VehicleData.getData().getDrivingLicence().getName());
            Picasso.with(this).load(path).into(driving_licence_Image);
        }
        if (VehicleData.getData().getVehicleLicence() != null){
            String path = Common.BaseUrl + "images/" +
                    VehicleData.getData().getVehicleLicence().getFor() + "/" +
                    Uri.encode(VehicleData.getData().getVehicleLicence().getName());
            Picasso.with(this).load(path).into(vehicle_licence_Image);
        }
        Recycler.setAdapter(new AdapterForResImage(list,this,listImageRequest,"update",numberOfIndexes,Recycler));
        getVehicleTypes();
    }

    public void onClickConfirmButton() {
        if (getIntent().getExtras().getString("type").equals("update")) {
            updateVehicle();
        }else {
            if (VehicleID.getText().toString().isEmpty())
                Toast.makeText(this, R.string.Enter_vehicle_id, Toast.LENGTH_LONG).show();
            else if (VehicleModel.getText().toString().isEmpty())
                Toast.makeText(this, R.string.Enter_Vehicle_model, Toast.LENGTH_LONG).show();
            else if (selectedYear == 0)
                Toast.makeText(this, R.string.Choose_year_vehicle, Toast.LENGTH_LONG).show();
            else if (rowVehicleTypes == null)
                Toast.makeText(this, R.string.Select_vehicle_model, Toast.LENGTH_SHORT).show();
            else
                createVehicle();
        }
    }

    private void updateVehicle() {
        dialog.show();
        Log.i("Informations","VId = "+VehicleData.getData().getId()+" ");
        viewModel.updateVehicle(VehicleData.getData().getId()+""
                ,VehicleID.getText().toString(),VehicleModel.getText().toString(),selectedYear
                ,rowVehicleTypes.getId(),driving_licence_ImageBase46,vehicle_licence_ImageBase46,listImageRequest,this,dialog)
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
        Massage.setText(R.string.successfully_registered);
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

        Log.i("TTTTT",getIntent().getExtras().getString("deliveryId"));
        viewModel.createVehicle("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                    getIntent().getExtras().getString("deliveryId"),
                new CreateVehicleRequest(VehicleID.getText().toString(),
                        VehicleModel.getText().toString(),
                        selectedYear,rowVehicleTypes.getId(),
                        new DrivingLicence(driving_licence_ImageBase46),
                        new VehicleLicence(vehicle_licence_ImageBase46),listImageRequest),this,dialog)
                .observe(this, new Observer<CreateVehicleRespons>() {
            @Override
            public void onChanged(CreateVehicleRespons createVehicleRespons) {
//                showSuccessDialog();
                Toast.makeText(CreateVehicleActivity.this, R.string.successfully_registered, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CreateVehicleActivity.this, HomeActivity.class)
                        .putExtra("type","CreateDeliveryActivity")
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    private void getYears(){
        viewModel.getAllYears(this,dialog,viewModel).observe(this, new Observer<Years>() {
            @Override
            public void onChanged(Years years) {
                onGetYears(years);
            }
        });
    }

    public void onGetYears(Years years) {
        if (getIntent().getExtras().getString("type").equals("create")) {
            Recycler.setAdapter(new AdapterForResImage(list,
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
                if (getIntent().getExtras().getString("type").equals("update") && updateYear == 0) {
                    updateYear++;
                    selectedYear = VehicleData.getData().getYear();
                    YearSelected.setText(selectedYear+"");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getVehicleTypes(){
        viewModel.getAllVehicleTypes(this,dialog,viewModel,"CreateVehicleActivity",null)
                .observe(this, new Observer<VehicleTypes>() {
            @Override
            public void onChanged(final VehicleTypes vehicleTypes) {
                onGetVehicleType(vehicleTypes);
            }
        });
    }

    public void onGetVehicleType(final VehicleTypes vehicleTypes){
        getYears();
        CreateVehicleActivity.this.vehicleTypes = vehicleTypes;
        AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(CreateVehicleActivity.this,
                R.layout.city_item,vehicleTypes.getData().getRowVehicleTypes());

        spinnerVehicleType.setAdapter(arrayAdapter);

        spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rowVehicleTypes = ((RowVehicleTypes) parent.getItemAtPosition(position));
                if (Common.knowLang(CreateVehicleActivity.this).equals("ar"))
                    VehicleTypeSelected.setText(((RowVehicleTypes) parent.getItemAtPosition(position)).getType());
                else if (Common.knowLang(CreateVehicleActivity.this).equals("en"))
                    VehicleTypeSelected.setText(((RowVehicleTypes) parent.getItemAtPosition(position)).getTypeEn());
                VehicleTypeSelected.setTextColor(getResources().getColor(R.color.black));
                if (getIntent().getExtras().getString("type").equals("update") && updateVehicle == 0) {
                    updateVehicle++;
                    for (RowVehicleTypes row : vehicleTypes.getData().getRowVehicleTypes()) {
                        if (row.getId() == VehicleData.getData().getVehicleTypeId()) {
                            if (Common.knowLang(CreateVehicleActivity.this).equals("ar"))
                                VehicleTypeSelected.setText(row.getType());
                            else if (Common.knowLang(CreateVehicleActivity.this).equals("en"))
                                VehicleTypeSelected.setText(row.getTypeEn());
                            rowVehicleTypes = row;
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

            if (Common.knowLang(CreateVehicleActivity.this).equals("ar"))
                Tname.setText(list.get(position).getType());
            else if (Common.knowLang(CreateVehicleActivity.this).equals("en"))
                Tname.setText(list.get(position).getTypeEn());
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

    public void openGallery(int type){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),type);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_FOR_VEHICLE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Uri targetUri = data.getData();
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));

                        list.add(new com.dopave.diethub_vendor.Models.GetVehicles.Image(bitmap));
                        //
                        listImageRequest.add(new Image(compressToBase46(bitmap)));
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
        else if (requestCode == SELECT_IMAGE_FOR_DRIVING_LICENSE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri targetUri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                        driving_licence_Image.setImageBitmap(bitmap);
                        driving_licence_ImageBase46 = compressToBase46(bitmap);
                        driving_licence_Add.setVisibility(View.GONE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if (requestCode == SELECT_IMAGE_FOR_VEHICLE_LICENSE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri targetUri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                        vehicle_licence_Image.setImageBitmap(bitmap);
                        vehicle_licence_ImageBase46 = compressToBase46(bitmap);
                        vehicle_licence_Add.setVisibility(View.GONE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String compressToBase46(Bitmap btmap){
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        btmap.compress(Bitmap.CompressFormat.JPEG, 30, boas);
        byte[] b = boas.toByteArray();
        String encodeImage = Base64.encodeToString(b, Base64.DEFAULT).replaceAll("\n| ","").trim();

        return encodeImage;
    }

}
