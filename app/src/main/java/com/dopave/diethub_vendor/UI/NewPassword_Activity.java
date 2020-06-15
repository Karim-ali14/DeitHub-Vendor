package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword_Activity extends AppCompatActivity {
    EditText Password,RePassword;
    ConstraintLayout Layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password_);
        init();
    }
    private void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Password = findViewById(R.id.Password);
        RePassword = findViewById(R.id.RePassword);
        Layout = findViewById(R.id.Layout);
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
        Password.clearFocus();
        RePassword.clearFocus();
    }

    public void onClick(View view1) {
        if (Password.getText().toString().isEmpty() && Password.getText().toString().isEmpty()){

        }else if (!Password.getText().toString().equals(Password.getText().toString())){

        }else {
            Common.getAPIRequest().reset_password(getIntent().getExtras().getString("email"),
                    getIntent().getExtras().getString("Code"),Password.getText().toString())
                    .enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    Toast.makeText(NewPassword_Activity.this, response.code()+""+response.message(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 200)
                        showDialog();
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {

                }
            });
        }

    }

    private void showDialog(){
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_registraion_success, null);
        TextView Massage = view.findViewById(R.id.Massage);
        Button LoginButton = view.findViewById(R.id.LoginButton);
        Adialog.setView(view);
        Massage.setText(getResources().getString(R.string.Password_Retrieved_Successfully));
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewPassword_Activity.this, Login_inActivity.class));
                finish();
            }
        });
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
    }
}