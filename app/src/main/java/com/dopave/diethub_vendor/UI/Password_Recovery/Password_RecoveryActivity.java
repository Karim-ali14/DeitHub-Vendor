package com.dopave.diethub_vendor.UI.Password_Recovery;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Enter_CodeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Password_RecoveryActivity extends AppCompatActivity {
    Button SendCodeButton;
    EditText Email;
    ConstraintLayout Layout;
    ProgressDialog dialog;
    Password_RecoveryViewModel viewModel;
    boolean firstOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__recovery);
        init();
        getChangeEditText();
    }

    private void getChangeEditText() {
        Email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Email.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    Email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Password_RecoveryActivity.this,R.drawable.envelope_active),null,null,null);
                }
                else {
                    Email.setBackground(getResources().getDrawable(R.drawable.style_textinput));
                    Email.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(Password_RecoveryActivity.this,R.drawable.envelope),null,null,null);
                }
            }
        });
    }

    private void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        viewModel = ViewModelProviders.of(this).get(Password_RecoveryViewModel.class);
        dialog = new ProgressDialog(this);
        SendCodeButton = findViewById(R.id.Send_Code_Button);
        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (!validationEmail())
                    sendCode();
                else {
                    dialog.dismiss();
                }
            }
        });
        Email = findViewById(R.id.EmailResetPass);
        Layout = findViewById(R.id.Layout);
        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void sendCode() {
        viewModel.sendCodeToEmail(Email.getText().toString(),dialog,this,viewModel)
                .observe(this, new Observer<ResetPassword>() {
            @Override
            public void onChanged(ResetPassword resetPassword) {
                startActivity(new Intent(Password_RecoveryActivity.this, Enter_CodeActivity.class)
                        .putExtra("email",Email.getText().toString()));
            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Email.clearFocus();
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

    @Override
    public void onWindowFocusChanged (boolean hasFocus) {
        if (!firstOpen){
            closeKeyBoard();
            firstOpen = true;
        }
    }
}
