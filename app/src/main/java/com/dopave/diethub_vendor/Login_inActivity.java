package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_inActivity extends AppCompatActivity {
    EditText Phone,Password;
    ConstraintLayout Layout;

    Button EnterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        init();
    }

    private void init() {
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
                //startActivity(new Intent(Login_inActivity.this,HomeActivity.class));
                //finish();
                SignInStatus();
            }
        });
        Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(v instanceof EditText))
                    closeKeyBoard();
            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Phone.clearFocus();
        Password.clearFocus();
    }

    public void onClick(View view) {
        startActivity(new Intent(this,Password_RecoveryActivity.class));
    }

    private boolean SignInStatus(){
        Common.getAPIRequest().signIn("+966590123457","password-1")
                .enqueue(new Callback<SignIn>() {
                    @Override
                    public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                        if (response.code() == 200){
                            Log.i("TTTTTT",response.body().getData().getToken().getAccessToken() + response.body().getData().getProvider().getId());
                            Toast.makeText(Login_inActivity.this, "The account created Successfully", Toast.LENGTH_SHORT).show();
                            Common.currentPosition = response.body();
                            startActivity(new Intent(Login_inActivity.this,HomeActivity.class));
                        }else {

                        }
                    }

                    @Override
                    public void onFailure(Call<SignIn> call, Throwable t) {
                        Log.i("TTTTT",t.getMessage());
                    }
                });
        return true;
    }
}