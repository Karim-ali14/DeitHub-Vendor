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
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.Cities.CityRow;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Request.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Request.Image;
import com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Response.DeliveryByProviderResponse;
import com.dopave.diethub_vendor.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateDeliveryActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    EditText UserName, Phone, Email, Password, RePassword;
    ConstraintLayout Layout;
    ImageView chickBox;
    boolean isChecked = false;
    ProgressDialog dialog;
    String mVerificationId,CodeOfVerify;
    Spinner spinnerCity;
    TextView CitySelected;
    CityRow cityRow;
    LinearLayout chickBoxLayout;
    ImageView mark,openGallery;
    boolean firstSelect = false;
    CreateDeliveryViewModel viewModel;
    CircleImageView profile_image;
    String DeliveryImage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_delivery);
        init();
    }


    private void init() {
        viewModel = ViewModelProviders.of(this).get(CreateDeliveryViewModel.class);
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
        chickBoxLayout = findViewById(R.id.chickBoxLayout);
        UserName = findViewById(R.id.UserName);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.PhoneNumber);
        Password = findViewById(R.id.Password);
        RePassword = findViewById(R.id.RePassword);
        Layout = findViewById(R.id.Layout_Registration);
        chickBox = findViewById(R.id.chickBox);
        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });
        chickBoxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isChecked) {
                    isChecked = true;
                    chickBox.setImageResource(R.drawable.active);
                }else {
                    isChecked = false;
                    chickBox.setImageResource(R.drawable.tockbg);
                }
            }
        });

        editTextChangeStatus();

        getCities();
    }

    private void getCities(){
        viewModel.getCities(this).observe(this, new Observer<Cities>() {
            @Override
            public void onChanged(Cities cities) {
                AdapterOfSpinner arrayAdapter = new AdapterOfSpinner(CreateDeliveryActivity.this,
                        R.layout.city_item,cities.getData().getCityRows());

                spinnerCity.setAdapter(arrayAdapter);
                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mark.setImageResource(R.drawable.pin_active);
                        cityRow = ((CityRow) parent.getItemAtPosition(position));
                        CitySelected.setText(((CityRow) parent.getItemAtPosition(position)).getName());
                        CitySelected.setTextColor(getResources().getColor(R.color.black));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
        Password.clearFocus();
        RePassword.clearFocus();
        Layout.setDescendantFocusability(dfValue);
    }

    public void onClick(View view) {
        if (UserName.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        else if (Phone.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
        else if (Email.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
        else if (Password.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Your Password", Toast.LENGTH_SHORT).show();
        else if (RePassword.getText().toString().isEmpty())
            Toast.makeText(this, "Enter Your RePassword", Toast.LENGTH_SHORT).show();
        else if (!Password.getText().toString().equals(RePassword.getText().toString()))
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        else if (!isChecked)
            Toast.makeText(this, "يجب الوافقه علي الشروط و الاحكام", Toast.LENGTH_SHORT).show();
        else if (cityRow == null)
            Toast.makeText(this, "اختر المدينه", Toast.LENGTH_SHORT).show();
        else if (DeliveryImage == null)
            Toast.makeText(this, "اختر الصوره الشخصيه", Toast.LENGTH_SHORT).show();
        else {
            dialog.show();
           createDelivery();
        }
    }

    private void createDelivery(){
        viewModel.createDelivery("Bearer " +
                        Common.currentPosition.getData().getToken().getAccessToken(),
                new DeliveryByProvider(Phone.getText().toString(), Password.getText().toString(),
                        UserName.getText().toString(), Email.getText().toString(),
                        new Image(DeliveryImage), cityRow.getId()),
                Common.currentPosition.getData().getProvider().getId() + "",
                this, dialog).observe(this, new Observer<DeliveryByProviderResponse>() {
            @Override
            public void onChanged(DeliveryByProviderResponse deliveryByProviderResponse) {
                Log.i("FFFFFFF", deliveryByProviderResponse.getData().getDelivery().getId() + "");
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        Log.i("TTTTTT",imageString);
                        DeliveryImage = imageString;
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
    }

    public class AdapterOfSpinner extends ArrayAdapter<CityRow> {
        List<CityRow> list;
        LayoutInflater inflater;
        public AdapterOfSpinner(Activity context, int id , List<CityRow> list)
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
            Tname.setText(list.get(position).getName());
            return vv;
        }

    }
}