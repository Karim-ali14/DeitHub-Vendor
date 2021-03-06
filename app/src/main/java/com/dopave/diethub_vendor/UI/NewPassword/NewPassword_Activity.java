package com.dopave.diethub_vendor.UI.NewPassword;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassword_Activity extends AppCompatActivity {
    EditText Password,RePassword;
    ConstraintLayout Layout;
    ProgressDialog dialog;
    NewPassword_ViewModel viewModel;
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
        dialog = new ProgressDialog(this);
        viewModel = ViewModelProviders.of(this).get(NewPassword_ViewModel.class);
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int dfValue = Layout.getDescendantFocusability();
        Layout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        Password.clearFocus();
        RePassword.clearFocus();
        Layout.setDescendantFocusability(dfValue);
    }

    public void onClick(View view1) {
        dialog.dismiss();
        if (!validationPass()){
            dialog.dismiss();
        }
        else {
            viewModel.reset_password(this,dialog,getIntent().getExtras().getString("email"),
                    getIntent().getExtras().getString("Code"),Password.getText().toString())
                    .observe(this, new Observer<ResetPassword>() {
                @Override
                public void onChanged(ResetPassword resetPassword) {
                    showDialog();
                }
            });
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
        else if  (Password.getText().toString().length() < 8 ||
                Password.getText().toString().length() > 25) {
            Toast.makeText(this, R.string.Password_length, Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;
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