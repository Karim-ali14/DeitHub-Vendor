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

import com.dopave.diethub_vendor.R;

public class Enter_CodeActivity extends AppCompatActivity {
    Button button;
    EditText Code;
    ConstraintLayout Layout;
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
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Code.getText().toString().isEmpty()){
                    startActivity(new Intent(Enter_CodeActivity.this,NewPassword_Activity.class)
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Toast.makeText(this, getIntent().getExtras().getString("email"), Toast.LENGTH_SHORT).show();
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        Code.clearFocus();
    }
}
