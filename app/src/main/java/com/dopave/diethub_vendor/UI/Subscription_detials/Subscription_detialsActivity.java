package com.dopave.diethub_vendor.UI.Subscription_detials;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Paint;
import android.media.Rating;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import com.dopave.diethub_vendor.Adapter.AdapterForSilder;
import com.dopave.diethub_vendor.Adapter.AdapterForSubscription;
import com.dopave.diethub_vendor.Models.Subscriptions.Client;
import com.dopave.diethub_vendor.Models.Subscriptions.Image;
import com.dopave.diethub_vendor.Models.Subscriptions.Package;
import com.dopave.diethub_vendor.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Subscription_detialsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    LinearLayout points;
    List<Image> list;
    Button ClientInfo,Package_Content;
    ConstraintLayout LayoutOfSubInfo, LayoutOfClientDetails;
    TextView title,Package_Price,NameOfPackage,Ratting,TotalCalorie,DetailsPackage,Duration,
            NameOfClientDetails,PhoneOfClient;
    RatingBar RattingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detials);
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        LayoutOfSubInfo = findViewById(R.id.LayoutOfSubInfo);
        LayoutOfClientDetails = findViewById(R.id.LayoutOfClientDetails);
        Package_Content = findViewById(R.id.Package_Content);
        ClientInfo = findViewById(R.id.ClientInfo);
        viewPager = findViewById(R.id.ViewPager);
        points = findViewById(R.id.Points);
        title = findViewById(R.id.title);
        Package_Price = findViewById(R.id.Package_Price);
        NameOfPackage = findViewById(R.id.NameOfPackage);
        Ratting = findViewById(R.id.Ratting);
        TotalCalorie = findViewById(R.id.totalCalorie);
        DetailsPackage = findViewById(R.id.DetailsPackage);
        Duration = findViewById(R.id.duration);
        RattingBar = findViewById(R.id.RattingBar);
        NameOfClientDetails = findViewById(R.id.NameOfClientDetails);
        PhoneOfClient = findViewById(R.id.PhoneOfClient);
        if(AdapterForSubscription.listImage.size() != 0)
            list = AdapterForSubscription.listImage;
        else {
            list = new ArrayList<>();
            list.add(new Image(R.drawable.picture));
        }
        viewPager.setAdapter(new AdapterForSilder(list,this));
        setPoints(0);
        viewPager.setOnPageChangeListener(this);
        Package aPackage = (Package) Objects.requireNonNull(getIntent().getExtras().getParcelable("Package"));
        Client client = (Client) Objects.requireNonNull(getIntent().getExtras().getParcelable("Client"));
        setData(aPackage,client);
        TotalCalorie.setPaintFlags(TotalCalorie.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
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
            LayoutOfSubInfo.setVisibility(View.GONE);
        }else {
            ClientInfo.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ClientInfo.setTextColor(getResources().getColor(R.color.colorPrimary));
            Package_Content.setBackground(getResources().getDrawable(R.drawable.style_button));
            Package_Content.setTextColor(getResources().getColor(R.color.background));
            LayoutOfClientDetails.setVisibility(View.GONE);
            LayoutOfSubInfo.setVisibility(View.VISIBLE);
        }
    }

    public void BackButton(View view) {
        finish();
    }

    private void setData(Package aPackage,Client client){
        if (aPackage != null) {
            title.setText(aPackage.getName());
            NameOfPackage.setText(aPackage.getName());
            Package_Price.setText(aPackage.getPrice() + "");
            int totalCal =  aPackage.getFatCal() + aPackage.getProteinCal()
                            + aPackage.getCarbCal();
            TotalCalorie.setText(totalCal + "" +getResources().getString(R.string.Calorie));
            Ratting.setText(aPackage.getTotalRate() +"");
            RattingBar.setRating(aPackage.getTotalRate());
            DetailsPackage.setText(aPackage.getDescription());
            Duration.setText(aPackage.getDeliveryTime());
        }
        if (client != null){
            PhoneOfClient.setText(client.getMobilePhone()+"");
            NameOfClientDetails.setText(client.getName());
        }
    }
}