package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Add_DelegateActivity extends AppCompatActivity {
    ConstraintLayout Layout_AddDelegate;
    EditText NameDelegate_Add,EmailDelegate_Add,PhoneNumber_Add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__delegate);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Layout_AddDelegate = findViewById(R.id.Layout_AddDelegate);
        NameDelegate_Add = findViewById(R.id.NameDelegate_Add);
        EmailDelegate_Add = findViewById(R.id.EmailDelegate_Add);
        PhoneNumber_Add = findViewById(R.id.PhoneNumber_Add);
        Layout_AddDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
            }
        });
    }
    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        NameDelegate_Add.clearFocus();
        EmailDelegate_Add.clearFocus();
        PhoneNumber_Add.clearFocus();
    }
    public void BackButton(View view) {
        finish();
    }
}
