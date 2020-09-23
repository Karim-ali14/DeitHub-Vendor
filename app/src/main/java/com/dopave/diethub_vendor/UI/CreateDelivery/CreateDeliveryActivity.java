package com.dopave.diethub_vendor.UI.CreateDelivery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateDeliveryActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    EditText UserName, Phone, Email, Password, RePassword;
    ConstraintLayout Layout,City_Layout;
    ProgressDialog dialog;
    Spinner spinnerCity;
    TextView CitySelected;
    CityRow cityRow;
    ImageView mark,openGallery;
    CreateDeliveryViewModel viewModel;
    CircleImageView profile_image;
    String DeliveryImage = null;
    DeliveryRow delivery;
    boolean isValid,firstOpen,isSpinnerCities;
    int SpinnerCitiesClick = 0;
    Button EnterButton;
    int i = 0;
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
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
        UserName.setText(delivery.getName());
        Phone.setText(delivery.getMobilePhone());
        Email.setText(delivery.getEmail());
        if (delivery.getImage() != null){
            String path = Common.BaseUrl + "images/" +
                    delivery.getImage().getFor() + "/" +
                    Uri.encode(delivery.getImage().getName());
            Picasso.with(this).load(path).into(profile_image);
            Log.i("TTTTTT",path);
        }
        getCities();
    }

    private void update(){
        if (DeliveryImage != null) {
            viewModel.updateDelivery(new UpdateDeliveryRequest(
                    Email.getText().toString() + ""
                    , Phone.getText().toString() + ""
                    , UserName.getText().toString() + ""
                    , false, true, "active",
                    new com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.Image(DeliveryImage),
                    cityRow.getId()
            ), delivery
                    .getId().toString(), dialog, this).observe(this, new Observer<GetDeliveriesData>() {
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
            viewModel.updateDelivery(new UpdateDeliveryRequest(
                    Email.getText().toString() + ""
                    , Phone.getText().toString() + ""
                    , UserName.getText().toString() + ""
                    , false, true, "active",
                    null,
                    cityRow.getId()
            ), delivery
                    .getId().toString(), dialog, this).observe(this, new Observer<GetDeliveriesData>() {
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
                    if (getIntent().getExtras().getString("type").equals("update") && i == 0) {
                        i++;
                        Log.i("TTTTTTTT",delivery.getCity().getId()+"");
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
            if (delivery.getImage() == null) {
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
            if (DeliveryImage == null) {
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
                        Common.currentPosition.getData().getToken().getAccessToken(),
                new CreateDeliveryRequest(Phone.getText().toString(), Password.getText().toString(),
                        UserName.getText().toString(), Email.getText().toString(),
                        new Image(DeliveryImage), cityRow.getId()),
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
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        DeliveryImage = imageString.replaceAll("\n| ","").trim();
                        byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profile_image.setImageBitmap(decodedByte);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
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