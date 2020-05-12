package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class Details_OrderActivity extends AppCompatActivity {
    TextView CountText;
    LinearLayout CountLayout,CC;
    ImageView VisaIcon;
    View line3,line8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__order);
        init();
    }

    public void BackButton(View view) {
        finish();
    }

    private void init(){
        getWindow().getDecorView(). setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        CountText = findViewById(R.id.countText);
        CountLayout = findViewById(R.id.CountLayout);
        CC = findViewById(R.id.CC);
        VisaIcon = findViewById(R.id.VisaIcon);
        line3 = findViewById(R.id.line3);
        line8 = findViewById(R.id.line8);
    }
}
