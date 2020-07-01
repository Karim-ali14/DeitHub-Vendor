package com.dopave.diethub_vendor.UI.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Password_Recovery.Password_RecoveryActivity;

public class Login_inActivity extends AppCompatActivity {
    EditText Phone,Password;
    ConstraintLayout Layout;
    Button EnterButton;
    Login_ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        init();
    }

    private void init() {
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
                if (Phone.getText().toString().isEmpty())
                    Toast.makeText(Login_inActivity.this, "Enter Your phone", Toast.LENGTH_SHORT).show();
                else if (Password.getText().toString().isEmpty())
                    Toast.makeText(Login_inActivity.this, "Enter Your password", Toast.LENGTH_SHORT).show();
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

    private void SignIn(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();

        viewModel.onSignIn(Phone.getText().toString(),
                Password.getText().toString(),
                this,dialog,viewModel).observe(this, new Observer<SignIn>() {
            @Override
            public void onChanged(SignIn signIn) {
                Log.i("TTTTTT",signIn.getData().getToken().getAccessToken() +"   "+ signIn.getData().getProvider().getId());
                Toast.makeText(Login_inActivity.this, signIn.getMessage(), Toast.LENGTH_SHORT).show();
                Common.currentPosition = signIn;
                startActivity(new Intent(Login_inActivity.this,
                        HomeActivity.class).putExtra("type",
                        "Login_inActivity"));
            }
        });
    }
}