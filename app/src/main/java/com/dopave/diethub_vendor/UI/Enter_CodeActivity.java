package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.NewPassword.NewPassword_Activity;

public class Enter_CodeActivity extends AppCompatActivity {
    Button button;
    EditText Code;
    ConstraintLayout Layout;
    TextView MessageOfVerification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter__code);
        init();
    }
    private void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Code = findViewById(R.id.Code);
        MessageOfVerification = findViewById(R.id.MessageOfVerification);
        MessageOfVerification.setText(getResources().getString(R.string.enter_code_to_password_forEmail));
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Code.getText().toString().isEmpty()){
                    startActivity(new Intent(Enter_CodeActivity.this, NewPassword_Activity.class)
                            .putExtra("email",getIntent().getExtras().getString("email"))
                            .putExtra("Code",Code.getText().toString()));
                }else {
                    Toast.makeText(Enter_CodeActivity.this, "enter Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        Code.clearFocus();
        Layout.setDescendantFocusability(dfValue);

    }
}
