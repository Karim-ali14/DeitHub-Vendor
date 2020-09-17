package com.dopave.diethub_vendor.UI.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Password_Recovery.Password_RecoveryActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;

public class Login_inActivity extends AppCompatActivity {
    EditText Phone,Password;
    ConstraintLayout Layout;
    Button EnterButton;
    Login_ViewModel viewModel;
    boolean isValid,firstOpen;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        init();
    }

    private void init() {
        dialog = new ProgressDialog(this);
        viewModel = ViewModelProviders.of(this).get(Login_ViewModel.class);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Phone = findViewById(R.id.PhoneNumber);
        Password = findViewById(R.id.Password);
        Layout = findViewById(R.id.Layout);
        EnterButton = findViewById(R.id.EnterButton);
        EnterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (!validationPhone()){dialog.dismiss();}
                else if (!validationPass()){dialog.dismiss();}
                else
                    SignIn();
            }
        });
        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });
        getChangeEditTextStatus();
    }

    private void getChangeEditTextStatus(){
        Phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Phone.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Phone.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Login_inActivity.this,R.drawable.call_active),null,null,null);
                }
                else {
                    Phone.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Phone.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Login_inActivity.this,R.drawable.call),null,null,null);
                    if (!Phone.getText().toString().isEmpty()){
                        Phone.setText(dellWithPhone(Phone.getText().toString()));
                    }
                }
            }
        });
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Password.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Password.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Login_inActivity.this,R.drawable.padlock_active),null,null,null);
                }
                else {
                    Password.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Password.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Login_inActivity.this,R.drawable.padlock),null,null,null);

                }
            }
        });
    }

    private boolean validationPhone() {
        if (Phone.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Phone, Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (!isValid){
//            Toast.makeText(this, R.string.phone_number_incorrect, Toast.LENGTH_LONG).show();
//            return false;
//        }
        else {
            return true;
        }
    }

    private boolean validationPass() {
        if (Password.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.Enter_Password, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (Password.getText().toString().length() < 8 ||
                Password.getText().toString().length() > 25) {
            Toast.makeText(this, R.string.Password_length, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;
    }


    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        int dfValue = Layout.getDescendantFocusability();
        Layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        Phone.clearFocus();
        Password.clearFocus();
        Layout.setDescendantFocusability(dfValue);
    }

    public void onClick(View view) {
        startActivity(new Intent(this, Password_RecoveryActivity.class));
    }

    private void SignIn() {
        viewModel.onSignIn(Phone.getText().toString(),
                Password.getText().toString(),
                this,dialog,viewModel).observe(this, new Observer<ProviderInformation>() {
            @Override
            public void onChanged(ProviderInformation signIn) {
                Log.i("TTTTTT",signIn.getData().getToken().getAccessToken() +"   "+ signIn.getData().getProvider().getId());
                Toast.makeText(Login_inActivity.this, signIn.getMessage(), Toast.LENGTH_SHORT).show();
                Common.currentPosition = signIn;
                SharedPreferences preferences = Common.getPreferences(Login_inActivity.this);

                preferences.edit() // save token and id to auto Login
                        .putString(Common.Token,signIn.getData().getToken().getAccessToken())
                        .putString(Common.ProviderId,signIn.getData().getProvider().getId()+"")
                        .apply();

                startActivity(new Intent(Login_inActivity.this,
                        HomeActivity.class).putExtra("type",
                        "Login_inActivity"));
                refreshTokenDevice();
                finish();
            }
        });
    }

    private void refreshTokenDevice() {
        Toast.makeText(this, FirebaseInstanceId.getInstance().getToken()+
                "", Toast.LENGTH_SHORT).show();
        HashMap<String, String> deviceId = new HashMap<>();
        deviceId.put("firebase_device_id", FirebaseInstanceId.getInstance().getToken());
        Common.getAPIRequest().setFirebaseDeviceId("Bearer " +
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",deviceId).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, retrofit2.Response<Defualt> response) {
                Toast.makeText(Login_inActivity.this, response.code()+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Defualt> call, Throwable t) {

            }
        });
    }

    public String dellWithPhone(String phone) {
        Phonenumber.PhoneNumber phoneNumber = null;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        String finalNumber = null;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        try {
            phoneNumber = phoneNumberUtil.parse(phone, "EG");
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
    }

}