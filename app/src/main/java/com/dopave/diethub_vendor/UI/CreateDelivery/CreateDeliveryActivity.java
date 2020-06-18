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
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.Image;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;
import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.UpdateDeliveryRequest;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDeliveryActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE = 1;
    EditText UserName, Phone, Email, Password, RePassword;
    ConstraintLayout Layout;
    ImageView chickBox;
    boolean isChecked = false;
    ProgressDialog dialog;
    Spinner spinnerCity;
    TextView CitySelected;
    CityRow cityRow;
    LinearLayout chickBoxLayout;
    ImageView mark,openGallery;
    CreateDeliveryViewModel viewModel;
    CircleImageView profile_image;
    String DeliveryImage = null;
    DeliveryRow delivery;
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
        if (getIntent().getExtras().getString("type").equals("update")) {
            Password.setVisibility(View.GONE);
            RePassword.setVisibility(View.GONE);
            delivery = getIntent().getExtras().getParcelable("delivery");
            if (delivery != null)
                setData();
        }
    }

    private void setData() {
        UserName.setText(delivery.getName());
        Phone.setText(delivery.getMobilePhone());
        Email.setText(delivery.getEmail());
        spinnerCity.setSelection(delivery.getCityId());
        if (delivery.getImage()!=null){
            String path = Common.BaseUrl + "images/" + delivery.getImage().getFor() + "/" + Uri.encode(delivery.getImage().getName());
            Log.i("TTTTTTT",path);
            Picasso.with(this).load(path).into(profile_image);
        }
    }

    private void update(){
        String ImageUrl = "PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI3NCIgaGVpZ2h0PSI0Ni4yNSIgdmlld0JveD0iMCAwIDc0IDQ2LjI1Ij48ZGVmcz48c3R5bGU+LmF7ZmlsbDojZmZmO308L3N0eWxlPjwvZGVmcz48cGF0aCBjbGFzcz0iYSIgZD0iTTY5LjY2MywxNS45SDY4LjIxOHYtNi41QTUuNzc5LDUuNzc5LDAsMCwwLDU5LjQsNC40NzksNS43ODksNS43ODksMCwwLDAsNTMuNzY1LDBoMGE1Ljc4OCw1Ljc4OCwwLDAsMC01Ljc4MSw1Ljc4MVYxNS45SDI2LjAxN1Y1Ljc4MUE1Ljc4Miw1Ljc4MiwwLDAsMCwxNC42LDQuNDc5LDUuNzc5LDUuNzc5LDAsMCwwLDUuNzgxLDkuMzk1djYuNUg0LjMzNUE0LjM0MSw0LjM0MSwwLDAsMCwwLDIwLjIzNHY1Ljc4MWE0LjM0MSw0LjM0MSwwLDAsMCw0LjMzNiw0LjMzNkg1Ljc4MXY2LjVBNS43NzksNS43NzksMCwwLDAsMTQuNiw0MS43NzFhNS43ODksNS43ODksMCwwLDAsNS42MzIsNC40NzloMGE1Ljc4OCw1Ljc4OCwwLDAsMCw1Ljc4MS01Ljc4MVYzMC4zNTJINDcuOTgyVjQwLjQ2OWE1Ljc4Miw1Ljc4MiwwLDAsMCwxMS40MTUsMS4zLDUuNzc5LDUuNzc5LDAsMCwwLDguODIxLTQuOTE2di02LjVoMS40NDVBNC4zNDEsNC4zNDEsMCwwLDAsNzQsMjYuMDE2VjIwLjIzNEE0LjM0MSw0LjM0MSwwLDAsMCw2OS42NjMsMTUuOVpNNS43ODEsMjcuNDYxSDQuMzM1QTEuNDQ3LDEuNDQ3LDAsMCwxLDIuODksMjYuMDE2VjIwLjIzNGExLjQ0NywxLjQ0NywwLDAsMSwxLjQ0NS0xLjQ0NUg1Ljc4MVptOC42NzIsOS4zOTVhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NGEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm04LjY3NCwzLjYxM2EyLjg5NCwyLjg5NCwwLDAsMS0yLjg5MSwyLjg5MWgwYTIuODk0LDIuODk0LDAsMCwxLTIuODkxLTIuODkxVjUuNzgxYTIuODkxLDIuODkxLDAsMCwxLDUuNzgzLDBaTTQ3Ljk3NCwyMS42OEgzNy44NjdhMS40NDUsMS40NDUsMCwwLDAsMCwyLjg5MUg0Ny45NzR2Mi44OTFIMjYuMDA5VjE4Ljc4OUg0Ny45NzRabTguNjgyLDE4Ljc4OWEyLjg5MSwyLjg5MSwwLDAsMS01Ljc4MywwVjIzLjJhLjg0OS44NDksMCwwLDEtLjAwOC4wOTR2LS4zMzVhLjg0OS44NDksMCwwLDEsLjAwOC4wOTRWNS43ODFhMi44OTQsMi44OTQsMCwwLDEsMi44OTEtMi44OTFoMGEyLjg5NCwyLjg5NCwwLDAsMSwyLjg5MSwyLjg5MVptOC42NzItMy42MTNhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NWEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm01Ljc4MS0xMC44NGExLjQ0NywxLjQ0NywwLDAsMS0xLjQ0NSwxLjQ0NUg2OC4yMThWMTguNzg5aDEuNDQ1YTEuNDQ3LDEuNDQ3LDAsMCwxLDEuNDQ1LDEuNDQ1Wm0wLDAiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAuMDAxIDApIi8+PHBhdGggY2xhc3M9ImEiIGQ9Ik0yMTMuNDYyLDE1Mi44OTFhMS40NDUsMS40NDUsMCwwLDEsMC0yLjg5MWgwYTEuNDQ1LDEuNDQ1LDAsMCwxLDAsMi44OTFabTAsMCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTE4MS4zNzMgLTEyOC4zMikiLz48L3N2Zz4=";

        Common.getAPIRequest().updateDeliveryByProvider("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken()
                ,new UpdateDeliveryRequest(
                        Email.getText().toString()+""
                        ,Phone.getText().toString()+""
                        ,UserName.getText().toString()+""
                        ,false,true,"active",
                        new com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.Image(ImageUrl),
                        cityRow.getId()
                ),
                Common.currentPosition.getData().getProvider().getId()+""
                ,delivery.getId()+"").enqueue(new Callback<GetDeliveriesData>() {
            @Override
            public void onResponse(Call<GetDeliveriesData> call, Response<GetDeliveriesData> response) {
                dialog.dismiss();
                if (response.code() == 200){
                    Toast.makeText(CreateDeliveryActivity.this, "success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateDeliveryActivity.this,
                            HomeActivity.class).putExtra("type",
                            "CreateDeliveryActivity").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }else {
                    try {
                        String message = new JSONObject(response.errorBody()
                                .string()).getString("message");
                        Log.i("TTTTTTT",message);
                        Toast.makeText(CreateDeliveryActivity.this.getApplicationContext(),message, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDeliveriesData> call, Throwable t) {

            }
        });
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
        if (getIntent().getExtras().getString("type").equals("update")){
            if (UserName.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            else if (Phone.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Phone", Toast.LENGTH_SHORT).show();
            else if (Email.getText().toString().isEmpty())
                Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            else if (cityRow == null)
                Toast.makeText(this, "اختر المدينه", Toast.LENGTH_SHORT).show();
            else if (!isChecked)
                Toast.makeText(this, "يجب الوافقه علي الشروط و الاحكام", Toast.LENGTH_SHORT).show();
            else {
                dialog.show();
                update();
            }
        }else {
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
    }

    private void createDelivery(){
        String ImageUrl = "PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI3NCIgaGVpZ2h0PSI0Ni4yNSIgdmlld0JveD0iMCAwIDc0IDQ2LjI1Ij48ZGVmcz48c3R5bGU+LmF7ZmlsbDojZmZmO308L3N0eWxlPjwvZGVmcz48cGF0aCBjbGFzcz0iYSIgZD0iTTY5LjY2MywxNS45SDY4LjIxOHYtNi41QTUuNzc5LDUuNzc5LDAsMCwwLDU5LjQsNC40NzksNS43ODksNS43ODksMCwwLDAsNTMuNzY1LDBoMGE1Ljc4OCw1Ljc4OCwwLDAsMC01Ljc4MSw1Ljc4MVYxNS45SDI2LjAxN1Y1Ljc4MUE1Ljc4Miw1Ljc4MiwwLDAsMCwxNC42LDQuNDc5LDUuNzc5LDUuNzc5LDAsMCwwLDUuNzgxLDkuMzk1djYuNUg0LjMzNUE0LjM0MSw0LjM0MSwwLDAsMCwwLDIwLjIzNHY1Ljc4MWE0LjM0MSw0LjM0MSwwLDAsMCw0LjMzNiw0LjMzNkg1Ljc4MXY2LjVBNS43NzksNS43NzksMCwwLDAsMTQuNiw0MS43NzFhNS43ODksNS43ODksMCwwLDAsNS42MzIsNC40NzloMGE1Ljc4OCw1Ljc4OCwwLDAsMCw1Ljc4MS01Ljc4MVYzMC4zNTJINDcuOTgyVjQwLjQ2OWE1Ljc4Miw1Ljc4MiwwLDAsMCwxMS40MTUsMS4zLDUuNzc5LDUuNzc5LDAsMCwwLDguODIxLTQuOTE2di02LjVoMS40NDVBNC4zNDEsNC4zNDEsMCwwLDAsNzQsMjYuMDE2VjIwLjIzNEE0LjM0MSw0LjM0MSwwLDAsMCw2OS42NjMsMTUuOVpNNS43ODEsMjcuNDYxSDQuMzM1QTEuNDQ3LDEuNDQ3LDAsMCwxLDIuODksMjYuMDE2VjIwLjIzNGExLjQ0NywxLjQ0NywwLDAsMSwxLjQ0NS0xLjQ0NUg1Ljc4MVptOC42NzIsOS4zOTVhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NGEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm04LjY3NCwzLjYxM2EyLjg5NCwyLjg5NCwwLDAsMS0yLjg5MSwyLjg5MWgwYTIuODk0LDIuODk0LDAsMCwxLTIuODkxLTIuODkxVjUuNzgxYTIuODkxLDIuODkxLDAsMCwxLDUuNzgzLDBaTTQ3Ljk3NCwyMS42OEgzNy44NjdhMS40NDUsMS40NDUsMCwwLDAsMCwyLjg5MUg0Ny45NzR2Mi44OTFIMjYuMDA5VjE4Ljc4OUg0Ny45NzRabTguNjgyLDE4Ljc4OWEyLjg5MSwyLjg5MSwwLDAsMS01Ljc4MywwVjIzLjJhLjg0OS44NDksMCwwLDEtLjAwOC4wOTR2LS4zMzVhLjg0OS44NDksMCwwLDEsLjAwOC4wOTRWNS43ODFhMi44OTQsMi44OTQsMCwwLDEsMi44OTEtMi44OTFoMGEyLjg5NCwyLjg5NCwwLDAsMSwyLjg5MSwyLjg5MVptOC42NzItMy42MTNhMi44OTEsMi44OTEsMCwwLDEtNS43ODEsMFY5LjM5NWEyLjg5MSwyLjg5MSwwLDAsMSw1Ljc4MSwwWm01Ljc4MS0xMC44NGExLjQ0NywxLjQ0NywwLDAsMS0xLjQ0NSwxLjQ0NUg2OC4yMThWMTguNzg5aDEuNDQ1YTEuNDQ3LDEuNDQ3LDAsMCwxLDEuNDQ1LDEuNDQ1Wm0wLDAiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDAuMDAxIDApIi8+PHBhdGggY2xhc3M9ImEiIGQ9Ik0yMTMuNDYyLDE1Mi44OTFhMS40NDUsMS40NDUsMCwwLDEsMC0yLjg5MWgwYTEuNDQ1LDEuNDQ1LDAsMCwxLDAsMi44OTFabTAsMCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTE4MS4zNzMgLTEyOC4zMikiLz48L3N2Zz4=";

        viewModel.createDelivery("Bearer " +
                        Common.currentPosition.getData().getToken().getAccessToken(),
                new CreateDeliveryRequest(Phone.getText().toString(), Password.getText().toString(),
                        UserName.getText().toString(), Email.getText().toString(),
                        new Image(ImageUrl), cityRow.getId()),
                Common.currentPosition.getData().getProvider().getId() + "",
                this, dialog).observe(this, new Observer<CreateDeliveryResponse>() {
            @Override
            public void onChanged(CreateDeliveryResponse deliveryByProviderResponse) {
                Log.i("FFFFFFF", deliveryByProviderResponse.getData().getId() + "");
                startActivity(new Intent(CreateDeliveryActivity.this, CreateVehicleActivity.class).
                        putExtra("deliveryId",deliveryByProviderResponse.getData().getId()+""));
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