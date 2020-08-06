package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Modify_Work_TimeActivity extends AppCompatActivity {
    ImageView SaturdayCheckbox;
    TextView SaturdayTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__work__time);
        SaturdayCheckbox = findViewById(R.id.SaturdayCheckbox);
        SaturdayTime = findViewById(R.id.SaturdayTime);
        SaturdayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog dialog = new TimePickerDialog(Modify_Work_TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        SaturdayTime.setText(hourOfDay+":"+minute);
                        SaturdayCheckbox.setImageResource(R.drawable.active);
                    }
                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),
                        android.text.format.DateFormat
                                .is24HourFormat(Modify_Work_TimeActivity.this));
                dialog.show();
            }
        });

        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void BackButton(View view) {
        finish();
    }
}
