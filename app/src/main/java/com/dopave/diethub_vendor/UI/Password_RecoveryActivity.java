package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Password_RecoveryActivity extends AppCompatActivity {
    Button SendCodeButton;
    EditText Phone;
    ConstraintLayout Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__recovery);
        init();
    }
    private void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        SendCodeButton = findViewById(R.id.Send_Code_Button);
        SendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Password_RecoveryActivity.this,Enter_CodeActivity.class));
                sendCode();
            }
        });
        Phone = findViewById(R.id.PhoneNumber);
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
        Common.getAPIRequest().sendCode("karim.ali14199314@gmail.com").enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                switch (response.code()){
                    case 200 : {
                        Toast.makeText(Password_RecoveryActivity.this, "200", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Password_RecoveryActivity.this,Enter_CodeActivity.class)
                                .putExtra("email","karim.ali14199314@gmail.com"));
                    }
                        break;
                    case 401 :
                        Toast.makeText(Password_RecoveryActivity.this, "You are not authorized", Toast.LENGTH_SHORT).show();
                        break;
                    case 404 :
                        Toast.makeText(Password_RecoveryActivity.this, "Resource not found", Toast.LENGTH_SHORT).show();
                        break;
                    case 422 :
                        Toast.makeText(Password_RecoveryActivity.this, "validation Error", Toast.LENGTH_SHORT).show();
                        break;
                    case 500 :
                        Toast.makeText(Password_RecoveryActivity.this, "Error happend while handling the response", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<ResetPassword> call, Throwable t) {

            }
        });
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Phone.clearFocus();
    }
}
