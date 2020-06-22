package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dopave.diethub_vendor.Adapter.AdapterForSilder;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

public class Subscription_detialsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    LinearLayout points;
    List<Integer> list;
    Button ClientInfo,Package_Content;
    ConstraintLayout LayoutOfSubDetails, LayoutOfSubInfo, LayoutOfClientDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detials);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        LayoutOfSubInfo = findViewById(R.id.LayoutOfSubInfo);
        LayoutOfClientDetails = findViewById(R.id.LayoutOfClientDetails);
        LayoutOfSubDetails = findViewById(R.id.LayoutOfSubDetails);
        Package_Content = findViewById(R.id.Package_Content);
        ClientInfo = findViewById(R.id.ClientInfo);
        viewPager = findViewById(R.id.ViewPager);
        points = findViewById(R.id.Points);
        list = new ArrayList<>();
        list.add(R.drawable.ch2);
        list.add(R.drawable.ch2);
        list.add(R.drawable.ch2);
        viewPager.setAdapter(new AdapterForSilder(list,this));
        setPoints(0);
        viewPager.setOnPageChangeListener(this);
    }
    private void setPoints(int position){
        if (points.getChildCount() > 0){
            points.removeAllViews();
        }
        ImageView imageView[] = new ImageView[list.size()];
        for (int i = 0;i < list.size();i++){
            imageView[i] = new ImageView(this);
            if (i == position)
                imageView[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.point_selected));
            else
                imageView[i].setImageDrawable(ContextCompat.getDrawable(this,R.drawable.point_nono_selected2));
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,0,5,0);
            points.addView(imageView[i],params);
        }

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setPoints(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onClick(View view) {
        if (view.getId() == R.id.ClientInfo){
            ClientInfo.setBackground(getResources().getDrawable(R.drawable.style_button));
            ClientInfo.setTextColor(getResources().getColor(R.color.background));
            Package_Content.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            Package_Content.setTextColor(getResources().getColor(R.color.colorPrimary));
            LayoutOfClientDetails.setVisibility(View.VISIBLE);
            LayoutOfSubDetails.setVisibility(View.GONE);
            LayoutOfSubInfo.setVisibility(View.GONE);
        }else {
            ClientInfo.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ClientInfo.setTextColor(getResources().getColor(R.color.colorPrimary));
            Package_Content.setBackground(getResources().getDrawable(R.drawable.style_button));
            Package_Content.setTextColor(getResources().getColor(R.color.background));
            LayoutOfClientDetails.setVisibility(View.GONE);
            LayoutOfSubDetails.setVisibility(View.GONE);
            LayoutOfSubInfo.setVisibility(View.VISIBLE);
        }
    }

    public void BackButton(View view) {
        finish();
    }
}
