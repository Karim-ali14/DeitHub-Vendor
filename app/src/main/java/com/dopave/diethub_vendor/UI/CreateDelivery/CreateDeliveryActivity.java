package com.dopave.diethub_vendor.UI.CreateDelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
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

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.Image;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;
import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.UpdateDeliveryRequest;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateDeliveryActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    EditText UserName, Phone, Email, Password, RePassword;
    ConstraintLayout Layout,City_Layout;
    ProgressDialog dialog;
    Spinner spinnerCity;
    TextView CitySelected;
    CityRow cityRow;
    ImageView mark,openGallery;
    CreateDeliveryViewModel viewModel;
    CircleImageView profile_image;
    DeliveryRow delivery;
    boolean isValid,firstOpen,isSpinnerCities;
    int SpinnerCitiesClick = 0;
    Button EnterButton;
    int update = 0;
    String DeliveryImagePath;
    File DeliveryImageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_delivery);
        init();
    }

    private void init() {
        viewModel = ViewModelProviders.of(this).get(CreateDeliveryViewModel.class);
        EnterButton = findViewById(R.id.EnterButton);
        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton();
            }
        });
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog = new ProgressDialog(this);
        openGallery = findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }
        });
        mark = findViewById(R.id.mark);
        profile_image = findViewById(R.id.profile_image);
        spinnerCity = findViewById(R.id.spinnerCity);
        CitySelected = findViewById(R.id.CitySelected);
        UserName = findViewById(R.id.UserName);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.PhoneNumber);
        Password = findViewById(R.id.Password);
        RePassword = findViewById(R.id.RePassword);
        Layout = findViewById(R.id.Layout_Registration);
        City_Layout = findViewById(R.id.City_Layout);
        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });

        editTextChangeStatus();


        if (getIntent().getExtras().getString("type").equals("update")) {
            Password.setVisibility(View.GONE);
            RePassword.setVisibility(View.GONE);
            EnterButton.setText(getResources().getString(R.string.update));
            delivery = getIntent().getExtras().getParcelable("delivery");
            if (delivery != null)
                setData();
        }else
            getCities();
    }

    private void setData() {
        Log.i("TTTTTT",delivery.getId()+"");
        UserName.setText(delivery.getName());
        Phone.setText(delivery.getMobilePhone());
        Email.setText(delivery.getEmail());
        if (delivery.getImage() != null){
            String path = Common.BaseUrl + "images/" +
                    delivery.getImage().getFor() + "/" +
                    Uri.encode(delivery.getImage().getName());
            Picasso.with(this).load(path).skipMemoryCache().into(profile_image);
            Log.i("TTTTTT",path);
        }
        getCities();
    }

    private void update(){
        if (DeliveryImageFile != null) {
            viewModel.updateDelivery(
                    Email.getText().toString() + ""
                    , Phone.getText().toString() + ""
                    , UserName.getText().toString() + ""
                    , false, true, "active",DeliveryImageFile
                    , cityRow.getId()+"", delivery.getId().toString(), dialog, this)
                    .observe(this, new Observer<GetDeliveriesData>() {
                @Override
                public void onChanged(GetDeliveriesData getDeliveriesData) {
                    Toast.makeText(CreateDeliveryActivity.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateDeliveryActivity.this,
                            HomeActivity.class).putExtra("type",
                            "CreateDeliveryActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
        }
        else {
            viewModel.updateDelivery(
                    Email.getText().toString() + ""
                    , Phone.getText().toString() + ""
                    , UserName.getText().toString() + ""
                    , false, true, "active",
                    null,cityRow.getId()+""
                    , delivery.getId().toString(), dialog, this)
                    .observe(this, new Observer<GetDeliveriesData>() {
                @Override
                public void onChanged(GetDeliveriesData getDeliveriesData) {
                    Toast.makeText(CreateDeliveryActivity.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateDeliveryActivity.this,
                            HomeActivity.class).putExtra("type",
                            "CreateDeliveryActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
        }
    }

    private void getCities(){
        viewModel.getCities(this,dialog,viewModel).observe(this, new Observer<Cities>() {
            @Override
            public void onChanged(Cities cities) {
                onGetCity(cities);
            }
        });
    }

    public void onGetCity(final Cities cities){
        AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(CreateDeliveryActivity.this,
                R.layout.city_item,cities.getData().getCityRows());

        spinnerCity.setAdapter(arrayAdapter);
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityRow = ((CityRow) parent.getItemAtPosition(position));
                if (Common.knowLang(CreateDeliveryActivity.this).equals("ar"))
                    CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getName());
                else if (Common.knowLang(CreateDeliveryActivity.this).equals("en"))
                    CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getNameEn());
                CitySelected.setTextColor(getResources().getColor(R.color.black));
                if (getIntent().getExtras().getString("type").equals("update")) {
                    if (getIntent().getExtras().getString("type").equals("update") && update == 0) {
                        update++;
                        for (CityRow row : cities.getData().getCityRows()) {
                            if (row.getId() == delivery.getCityId()) {
                                cityRow = row;
                                if (Common.knowLang(CreateDeliveryActivity.this).equals("ar"))
                                    CitySelected.setText(row.getName());
                                else if (Common.knowLang(CreateDeliveryActivity.this).equals("en"))
                                    CitySelected.setText(row.getNameEn());
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        int dfValue = Layout.getDescendantFocusability();
        Layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        UserName.clearFocus();
        Phone.clearFocus();
        Email.clearFocus();
        Password.clearFocus();
        RePassword.clearFocus();
        Layout.setDescendantFocusability(dfValue);
    }

    public void onClickButton() {
        dialog.show();
        if (getIntent().getExtras().getString("type").equals("update")){
            if (delivery.getImage() == null && DeliveryImageFile == null) {
                Toast.makeText(this, R.string.Choose_personal_picture, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else if (!validationName()){dialog.dismiss();}
            else if (!validationEmail()){dialog.dismiss();}
            else if (cityRow == null) {
                dialog.dismiss();
                Toast.makeText(this, R.string.Choose_city, Toast.LENGTH_SHORT).show();
            }
            else {
                update();
            }
        }else {
            if (DeliveryImageFile == null) {
                Toast.makeText(this, R.string.Choose_personal_picture, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else if (!validationName()){dialog.dismiss();}
            else if (!validationPhone()){dialog.dismiss();}
            else if (!validationEmail()){dialog.dismiss();}
            else if (cityRow == null) {
                Toast.makeText(this, R.string.Choose_city, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else if (!validationPass()){dialog.dismiss();}
            else {
                createDelivery();
            }
        }
    }

    private void createDelivery(){
        viewModel.createDelivery("Bearer " +
                        Common.currentPosition.getData().getToken().getAccessToken(),Phone.getText().toString(), Password.getText().toString(),
                        UserName.getText().toString(), Email.getText().toString(),cityRow.getId()+"",DeliveryImageFile,
                Common.currentPosition.getData().getProvider().getId() + "",
                this, dialog).observe(this, new Observer<CreateDeliveryResponse>() {
            @Override
            public void onChanged(CreateDeliveryResponse deliveryByProviderResponse) {
                Log.i("FFFFFFF", deliveryByProviderResponse.getData().getId() + "");
                startActivity(new Intent(CreateDeliveryActivity.this, CreateVehicleActivity.class).
                        putExtra("deliveryId",deliveryByProviderResponse.getData().getId()+"")
                        .putExtra("type","create"));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
//                        byte[] imageBytes = baos.toByteArray();
//                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//                        DeliveryImage = imageString.replaceAll("\n| ","").trim();
//                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        Uri data1 = data.getData();
                        getPhotoPathFromInternalStorage(data1);
                        if (DeliveryImagePath == null){
                            getPhotoPathFromExternalStorage(data1);
                        }
                        if (DeliveryImagePath != null) {
                            DeliveryImageFile = new File(DeliveryImagePath);
                            if (!checkImageSize(DeliveryImageFile)){
                                DeliveryImageFile = null;
                                DeliveryImagePath = null;
                                Toast.makeText(this, R.string.The_size_of_the_image, Toast.LENGTH_SHORT).show();
                                if (getIntent().getExtras().getString("type").equals("update")) {
                                    if (delivery.getImage() != null) {
                                        String path = Common.BaseUrl + "images/" +
                                                delivery.getImage().getFor() + "/" +
                                                Uri.encode(delivery.getImage().getName());
                                        Picasso.with(this).load(path).into(profile_image);
                                    } else
                                        profile_image.setImageResource(R.drawable.personalinfo);
                                }else
                                    profile_image.setImageResource(R.drawable.personalinfo);
                            }else {
                                profile_image.setImageBitmap(bitmap);
                            }
                        }
                        else {
                            Toast.makeText(this, R.string.This_type_is_not_supported, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, R.string.Canceled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPhotoPathFromInternalStorage(Uri data1) {
        String[] filePath = { MediaStore.Images.Media.DATA };
        Cursor c = getContentResolver().query(data1,filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        DeliveryImagePath = c.getString(columnIndex);
    }

    public void getPhotoPathFromExternalStorage(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        DeliveryImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGalleryAction();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void openGalleryAction() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
    }

    private boolean checkImageSize(File file){
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
        if (file_size <= 2048){
            return true;
        }else {
            return false;
        }
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
//                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
                openGalleryAction();
            } else {
                //Displaying another toast if permission is not granted
//                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                showDialogToRequestPermissions();
            }
        }
    }

    private void showDialogToRequestPermissions(){
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.error_dialog, null);
        TextView Title = view.findViewById(R.id.Title);
        Title.setText(R.string.Permission_request_error);
        TextView Message = view.findViewById(R.id.Message);
        Message.setText(R.string.Permission_to_access_images);
        Button button = view.findViewById(R.id.Again);
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                requestStoragePermission();
            }
        });
        dialog1.show();
    }

    private void editTextChangeStatus(){

        UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    UserName.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    UserName.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.user_active),null,null,null);
                }
                else {
                    UserName.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    UserName.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.user),null,null,null);

                }
            }
        });
        Phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Phone.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Phone.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.call_active),null,null,null);
                }
                else {
                    Phone.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Phone.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.call),null,null,null);
                    if (!Phone.getText().toString().isEmpty()){
                        Phone.setText(dellWithPhone(Phone.getText().toString()));
                    }
                }
            }
        });
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Email.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.envelope_active),null,null,null);
                }
                else {
                    Email.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.envelope),null,null,null);
                }
            }
        });
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Password.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Password.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.padlock_active),null,null,null);
                }
                else {
                    Password.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Password.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.padlock),null,null,null);

                }
            }
        });
        RePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    RePassword.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    RePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.padlock_active),null,null,null);
                }
                else {
                    RePassword.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    RePassword.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(CreateDeliveryActivity.this,R.drawable.padlock),null,null,null);

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
    }

    public void BackButton(View view) {
        finish();
    }

    public static class AdapterOfSpinner extends ArrayAdapter<CityRow> {
        List<CityRow> list;
        LayoutInflater inflater;
        Activity context;
        public AdapterOfSpinner(Activity context, int id , List<CityRow> list)
        {
            super(context,id,list);
            this.list=list;
            this.context = context;
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
            if (Common.knowLang(context).equals("ar"))
                Tname.setText(list.get(position).getName());
            else if (Common.knowLang(context).equals("en"))
                Tname.setText(list.get(position).getNameEn());
            return vv;
        }

    }

    private boolean validationPass() {
        if (Password.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Password, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (RePassword.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_RePassword, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Password.getText().toString().equals(RePassword.getText().toString())) {
            Toast.makeText(this, R.string.Passwords_are_not_match, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (Password.getText().toString().length() < 8 ||
                Password.getText().toString().length() > 25) {
            Toast.makeText(this, R.string.Password_length, Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
    }
    private boolean validationName() {
        if (UserName.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Name, Toast.LENGTH_LONG).show();
            return false;
        }else if (UserName.getText().toString().length() < 2
                ||
                UserName.getText().toString().length() > 70){
            Toast.makeText(this, R.string.name_length, Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
    private boolean validationPhone() {
        if (Phone.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Phone, Toast.LENGTH_LONG).show();
            return false;
        }else if (!isValid){
            Toast.makeText(this, R.string.phone_number_incorrect, Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }
    private boolean validationEmail() {
        if (Email.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Email, Toast.LENGTH_LONG).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
            Toast.makeText(this, R.string.Email_VALID, Toast.LENGTH_LONG).show();
            return false;
        }else {
            return true;
        }
    }

    public String dellWithPhone(String phone) {
        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String finalNumber = null;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phone, "SA");
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        if (isValid && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile
                || PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
            finalNumber = phoneNumberUtil.format(phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
        }else {
            Toast.makeText(this, R.string.phone_number_incorrect, Toast.LENGTH_SHORT).show();
            finalNumber = phone;
        }
        return finalNumber;
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
}