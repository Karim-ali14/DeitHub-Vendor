package com.dopave.diethub_vendor.UI.Setting.TimeWork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetTimeWork.Data;
import com.dopave.diethub_vendor.Models.GetTimeWork.Friday;
import com.dopave.diethub_vendor.Models.GetTimeWork.Monday;
import com.dopave.diethub_vendor.Models.GetTimeWork.Saturday;
import com.dopave.diethub_vendor.Models.GetTimeWork.Sunday;
import com.dopave.diethub_vendor.Models.GetTimeWork.Thursday;
import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;
import com.dopave.diethub_vendor.Models.GetTimeWork.Tuesday;
import com.dopave.diethub_vendor.Models.GetTimeWork.Wednesday;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.HomeActivity;

import java.util.Calendar;

public class Modify_Work_TimeActivity extends AppCompatActivity {
    ImageView SaturdayCheckbox,SundayCheckbox,MondayCheckBox,TuesdayCheckbox,WednesdayCheckbox,ThursdayCheckbox,FridayCheckbox;
    TextView SaturdayFromTime,SaturdayToTime,SundayFromTime,SundayToTime,MondayFromTime,MondayToTime,TuesdayFromTime,TuesdayToTime
            ,WednesdayFromTime,WednesdayToTime,ThursdayFromTime,ThursdayToTime,FridayFromTime,FridayToTime;

    Button setTimeButton;

    TimeWorkViewModel viewModel;

    String SaturdayFrom,SaturdayTo,SundayFrom,SundayTo,MondayFrom,MondayTo,
            TuesdayFrom,TuesdayTo,WednesdayFrom,WednesdayTo,ThursdayFrom,ThursdayTo,FridayFrom,
            FridayTo;
    ProgressDialog dialog;

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
        dialog = new ProgressDialog(this);
        viewModel = ViewModelProviders.of(this).get(TimeWorkViewModel.class);
        setTimeButton = findViewById(R.id.setTimeButton);
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

        getDataOfTimeWork();

        onClickEvents();
    }

    private void onClickEvents() {
        SaturdayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SaturdayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(SaturdayFromTime,SaturdayToTime,SaturdayCheckbox,"From","Sat");
                else
                    showTimeWorkDialog(SaturdayFromTime,SaturdayToTime,SaturdayCheckbox,"To","Sat");
            }
        });

        SundayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SundayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(SundayFromTime,SundayToTime,SundayCheckbox,"From","Sun");
                else
                    showTimeWorkDialog(SundayFromTime,SundayToTime,SundayCheckbox,"To","Sun");
            }
        });

        MondayCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MondayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(MondayFromTime,MondayToTime,MondayCheckBox,"From","Mon");
                else
                    showTimeWorkDialog(MondayFromTime,MondayToTime,MondayCheckBox,"To","Mon");
            }
        });

        TuesdayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TuesdayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(TuesdayFromTime,TuesdayToTime,TuesdayCheckbox,"From","Tues");
                else
                    showTimeWorkDialog(TuesdayFromTime,TuesdayToTime,TuesdayCheckbox,"To","Tues");
            }
        });

        WednesdayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WednesdayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(WednesdayFromTime,WednesdayToTime,WednesdayCheckbox,"From","Wed");
                else
                    showTimeWorkDialog(WednesdayFromTime,WednesdayToTime,WednesdayCheckbox,"To","Wed");
            }
        });

        ThursdayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ThursdayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(ThursdayFromTime,ThursdayToTime,ThursdayCheckbox,"From","Thur");
                else
                    showTimeWorkDialog(ThursdayFromTime,ThursdayToTime,ThursdayCheckbox,"To","Thur");
            }
        });

        FridayCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FridayFromTime.getText().equals("--:--"))
                    showTimeWorkDialog(FridayFromTime,FridayToTime,FridayCheckbox,"From","Fri");
                else
                    showTimeWorkDialog(FridayFromTime,FridayToTime,FridayCheckbox,"To","Fri");
            }
        });

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                viewModel.updateWorkTime(new Data(
                        new Saturday(SundayFrom,SundayTo),
                        new Sunday(SundayFrom,SundayTo),
                        new Monday(MondayFrom,MondayTo),
                        new Tuesday(ThursdayFrom,ThursdayTo),
                        new Wednesday(WednesdayFrom,WednesdayTo),
                        new Thursday(ThursdayFrom,ThursdayTo),
                        new Friday(FridayFrom,FridayTo)),Modify_Work_TimeActivity.this,
                        dialog).observe(Modify_Work_TimeActivity.this, new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt defualt) {
                        startActivity(new Intent(Modify_Work_TimeActivity.this,
                                HomeActivity.class).putExtra("type","Modify"));
                    }
                });
            }
        });
    }

    private void showTimeWorkDialog(final TextView timeFrom, final TextView timeTo,
                                    final ImageView image, final String type, final String day){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(Modify_Work_TimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (type.equals("From")) {
                    timeFrom.setText(hourOfDay + ":" + minute);
                    setNewTime(day,type,hourOfDay + ":" + minute);
                    showTimeWorkDialog(timeFrom,timeTo,image,"To",day);
                }else {
                    timeTo.setText(hourOfDay + ":" + minute);
                    setNewTime(day,type,hourOfDay + ":" + minute);
                    image.setImageResource(R.drawable.active);
                }
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),
                android.text.format.DateFormat
                        .is24HourFormat(Modify_Work_TimeActivity.this));
        dialog.show();
    }

    private void setNewTime(String day, String type,String time) {
        if (day.equals("Sat")){
            if (type.equals("From"))
                SaturdayFrom = time;
            else if (type.equals("To"))
                SaturdayTo = time;
        }else if (day.equals("Sun")){
            if (type.equals("From"))
                SundayFrom = time;
            else if (type.equals("To"))
                SundayTo = time;
        }else if (day.equals("Mon")){
            if (type.equals("From"))
                MondayFrom = time;
            else if (type.equals("To"))
                MondayTo = time;
        }else if (day.equals("Tues")){
            if (type.equals("From"))
                TuesdayFrom = time;
            else if (type.equals("To"))
                TuesdayTo = time;
        }else if (day.equals("Wed")){
            if (type.equals("From"))
                WednesdayFrom = time;
            else if (type.equals("To"))
                WednesdayTo = time;
        }else if (day.equals("Thur")){
            if (type.equals("From"))
                ThursdayFrom = time;
            else if (type.equals("To"))
                ThursdayTo = time;
        }else if (day.equals("Fri")){
            if (type.equals("From"))
                FridayFrom = time;
            else if (type.equals("To"))
                FridayTo = time;
        }
    }

    private void getDataOfTimeWork(){
        dialog.show();
        viewModel.getTimeWork(this,dialog,viewModel).observe(this, new Observer<TimeWorks>() {
            @Override
            public void onChanged(TimeWorks timeWorks) {
                onGetDataOfTimeWork(timeWorks);
            }
        });
    }

    public void onGetDataOfTimeWork(TimeWorks timeWorks) {
        if (timeWorks.getData() != null){
            if (timeWorks.getData().getSaturday().getFrom() != null
                    && timeWorks.getData().getSaturday().getTo() != null) {
                SaturdayFromTime.setText(timeWorks.getData().getSaturday().getFrom());
                SaturdayToTime.setText(timeWorks.getData().getSaturday().getTo());
                SaturdayCheckbox.setImageResource(R.drawable.active);
                SundayFrom = timeWorks.getData().getSaturday().getFrom();
                SundayTo = timeWorks.getData().getSaturday().getTo();
            }
            if (timeWorks.getData().getSunday().getFrom() != null
                    && timeWorks.getData().getSunday().getTo() != null) {
                SundayFromTime.setText(timeWorks.getData().getSunday().getFrom());
                SundayToTime.setText(timeWorks.getData().getSunday().getTo());
                SundayCheckbox.setImageResource(R.drawable.active);
                SundayFrom = timeWorks.getData().getSunday().getFrom();
                SundayTo = timeWorks.getData().getSunday().getTo();
            }if (timeWorks.getData().getMonday().getFrom() != null
                    && timeWorks.getData().getMonday().getTo() != null) {
                MondayFromTime.setText(timeWorks.getData().getMonday().getFrom());
                MondayToTime.setText(timeWorks.getData().getMonday().getTo());
                MondayCheckBox.setImageResource(R.drawable.active);
                MondayFrom = timeWorks.getData().getMonday().getFrom();
                MondayTo = timeWorks.getData().getMonday().getTo();
            }if (timeWorks.getData().getTuesday().getFrom() != null
                    && timeWorks.getData().getTuesday().getTo() != null) {
                TuesdayFromTime.setText(timeWorks.getData().getTuesday().getFrom());
                TuesdayToTime.setText(timeWorks.getData().getTuesday().getTo());
                TuesdayCheckbox.setImageResource(R.drawable.active);
                TuesdayFrom = timeWorks.getData().getTuesday().getFrom();
                TuesdayTo = timeWorks.getData().getTuesday().getTo();
            }if (timeWorks.getData().getWednesday().getFrom() != null
                    && timeWorks.getData().getWednesday().getTo() != null) {
                WednesdayFromTime.setText(timeWorks.getData().getWednesday().getFrom());
                WednesdayToTime.setText(timeWorks.getData().getWednesday().getTo());
                WednesdayCheckbox.setImageResource(R.drawable.active);
                WednesdayFrom = timeWorks.getData().getWednesday().getFrom();
                WednesdayTo = timeWorks.getData().getWednesday().getTo();
            }if (timeWorks.getData().getThursday().getFrom() != null
                    && timeWorks.getData().getThursday().getTo() != null) {
                ThursdayFromTime.setText(timeWorks.getData().getThursday().getFrom());
                ThursdayToTime.setText(timeWorks.getData().getThursday().getTo());
                ThursdayCheckbox.setImageResource(R.drawable.active);
                ThursdayFrom = timeWorks.getData().getThursday().getFrom();
                ThursdayTo = timeWorks.getData().getThursday().getTo();
            }if (timeWorks.getData().getFriday().getFrom() != null
                    && timeWorks.getData().getFriday().getTo() != null) {
                FridayFromTime.setText(timeWorks.getData().getFriday().getFrom());
                FridayToTime.setText(timeWorks.getData().getFriday().getTo());
                FridayCheckbox.setImageResource(R.drawable.active);
                FridayFrom = timeWorks.getData().getFriday().getFrom();
                FridayTo = timeWorks.getData().getFriday().getTo();
            }
        }
    }

    public void BackButton(View view) {
        finish();
    }
}
