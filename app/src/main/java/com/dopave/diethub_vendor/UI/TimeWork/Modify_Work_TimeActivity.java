package com.dopave.diethub_vendor.UI.TimeWork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;
import com.dopave.diethub_vendor.R;

import java.util.Calendar;

public class Modify_Work_TimeActivity extends AppCompatActivity {
    ImageView SaturdayCheckbox,SundayCheckbox,MondayCheckBox,TuesdayCheckbox,WednesdayCheckbox,ThursdayCheckbox,FridayCheckbox;
    TextView SaturdayFromTime,SaturdayToTime,SundayFromTime,SundayToTime,MondayFromTime,MondayToTime,TuesdayFromTime,TuesdayToTime
            ,WednesdayFromTime,WednesdayToTime,ThursdayFromTime,ThursdayToTime,FridayFromTime,FridayToTime;

    TimeWorkViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__work__time);
       init();
    }

    public void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModel = ViewModelProviders.of(this).get(TimeWorkViewModel.class);

        SaturdayCheckbox = findViewById(R.id.SaturdayCheckbox);
        SaturdayToTime = findViewById(R.id.SaturdayToTime);
        SundayFromTime = findViewById(R.id.SundayFromTime);
        SundayToTime = findViewById(R.id.SundayToTime);
        MondayFromTime = findViewById(R.id.MondayFromTime);
        MondayToTime = findViewById(R.id.MondayToTime);
        TuesdayFromTime = findViewById(R.id.TuesdayFromTime);
        TuesdayToTime = findViewById(R.id.TuesdayToTime);
        WednesdayFromTime = findViewById(R.id.WednesdayFromTime);
        WednesdayToTime = findViewById(R.id.WednesdayToTime);
        ThursdayFromTime = findViewById(R.id.ThursdayFromTime);
        ThursdayToTime = findViewById(R.id.ThursdayToTime);
        FridayFromTime = findViewById(R.id.FridayFromTime);
        FridayToTime = findViewById(R.id.FridayToTime);
        SaturdayFromTime = findViewById(R.id.SaturdayFromTime);
        SundayCheckbox = findViewById(R.id.SundayCheckbox);
        MondayCheckBox = findViewById(R.id.MondayCheckBox);
        TuesdayCheckbox = findViewById(R.id.TuesdayCheckbox);
        WednesdayCheckbox = findViewById(R.id.WednesdayCheckbox);
        ThursdayCheckbox = findViewById(R.id.ThursdayCheckbox);
        FridayCheckbox = findViewById(R.id.FridayCheckbox);
//        SaturdayCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar calendar = Calendar.getInstance();
//                TimePickerDialog dialog = new TimePickerDialog(Modify_Work_TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        SaturdayTime.setText(hourOfDay+":"+minute);
//                        SaturdayCheckbox.setImageResource(R.drawable.active);
//                    }
//                },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),
//                        android.text.format.DateFormat
//                                .is24HourFormat(Modify_Work_TimeActivity.this));
//                dialog.show();
//            }
//        });
        getDataOfTimeWork();
    }

    private void getDataOfTimeWork(){
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        viewModel.getTimeWork(this,dialog).observe(this, new Observer<TimeWorks>() {
            @Override
            public void onChanged(TimeWorks timeWorks) {
                onGetDataOfTimeWork(timeWorks);
            }
        });
    }

    private void onGetDataOfTimeWork(TimeWorks timeWorks) {
        if (timeWorks.getData() != null){
            if (timeWorks.getData().getSaturday().getFrom() != null
                    && timeWorks.getData().getSaturday().getTo() != null) {
                SaturdayFromTime.setText(timeWorks.getData().getSaturday().getFrom());
                SaturdayToTime.setText(timeWorks.getData().getSaturday().getTo());
            }
            else if (timeWorks.getData().getSunday().getFrom() != null
                    && timeWorks.getData().getSunday().getTo() != null) {
                SundayFromTime.setText(timeWorks.getData().getSunday().getFrom());
                SundayToTime.setText(timeWorks.getData().getSunday().getTo());
            }else if (timeWorks.getData().getMonday().getFrom() != null
                    && timeWorks.getData().getMonday().getTo() != null) {
                MondayFromTime.setText(timeWorks.getData().getMonday().getFrom());
                MondayToTime.setText(timeWorks.getData().getMonday().getTo());
            }else if (timeWorks.getData().getTuesday().getFrom() != null
                    && timeWorks.getData().getTuesday().getTo() != null) {
                TuesdayFromTime.setText(timeWorks.getData().getTuesday().getFrom());
                TuesdayToTime.setText(timeWorks.getData().getTuesday().getTo());
            }else if (timeWorks.getData().getWednesday().getFrom() != null
                    && timeWorks.getData().getWednesday().getTo() != null) {
                WednesdayFromTime.setText(timeWorks.getData().getWednesday().getFrom());
                WednesdayToTime.setText(timeWorks.getData().getWednesday().getTo());
            }else if (timeWorks.getData().getThursday().getFrom() != null
                    && timeWorks.getData().getThursday().getTo() != null) {
                ThursdayFromTime.setText(timeWorks.getData().getThursday().getFrom());
                ThursdayToTime.setText(timeWorks.getData().getThursday().getTo());
            }else if (timeWorks.getData().getFriday().getFrom() != null
                    && timeWorks.getData().getFriday().getTo() != null) {
                FridayFromTime.setText(timeWorks.getData().getFriday().getFrom());
                FridayToTime.setText(timeWorks.getData().getFriday().getTo());
            }
        }
    }

    public void BackButton(View view) {
        finish();
    }
}
