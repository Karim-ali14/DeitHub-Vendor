package com.dopave.diethub_vendor.UI.Details_OrderActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForOrder;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Orders.Detail;
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.Models.Subscriptions.UpdateStatus.UpdateSubscriptionStatus;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details_OrderActivity extends AppCompatActivity {
    TextView CountText,detailsOfOrders,countOfOrder,paymentMethod,status,clientAddress,
            NumberOfOrder,TotalNumber,NameOfClient;
    LinearLayout CountLayout,CC;
    ImageView VisaIcon;
    View line3,line8;
    Button ButtonUpDate;
    String SelectedStatus;
    boolean isSpinnerSelected = false;
    ImageView updateIcon;
    ConstraintLayout Update_Layout;
    ProgressDialog dialog;
    CircleImageView ClientIconDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__order);
        init();
    }

    public void BackButton(View view) {
        finish();
    }

    private void init() {
        final OrderRaw raw = getIntent().getExtras().getParcelable("orderRaw");
        getWindow().getDecorView(). setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        NumberOfOrder = findViewById(R.id.NumberOfOrder);
        detailsOfOrders = findViewById(R.id.detailsOfOrders);
        countOfOrder = findViewById(R.id.countOfOrder);
        ClientIconDetails = findViewById(R.id.ClientIconDetails);
        NameOfClient = findViewById(R.id.NameOfClient);
        paymentMethod = findViewById(R.id.paymentMethod);
        status = findViewById(R.id.status);
        TotalNumber = findViewById(R.id.TotalNumber);
        clientAddress = findViewById(R.id.clientAddress);
        CountText = findViewById(R.id.countText);
        CountLayout = findViewById(R.id.CountLayout);
        CC = findViewById(R.id.CC);
        VisaIcon = findViewById(R.id.VisaIcon);
        line3 = findViewById(R.id.line3);
        line8 = findViewById(R.id.line8);
        ButtonUpDate = findViewById(R.id.ButtonUpDate);
        ButtonUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpDateDialog(raw);
            }
        });
        dialog = new ProgressDialog(this);
        setOrderData(raw);
    }

    private void setOrderData(OrderRaw raw) {
        NumberOfOrder.setText(raw.getId().toString());

        if (AdapterForOrder.listDetail != null){
            String meals = "";
            for (Detail detail : AdapterForOrder.listDetail){
                meals += detail.getItem().getName() + " ,";
            }
            detailsOfOrders.setText(meals);
            countOfOrder.setText(AdapterForOrder.listDetail.size()+"");
        }

        TotalNumber.setText(raw.getTotalPricePiastre().getTotal()+" "+getResources().getString(R.string.SAR));
        paymentMethod.setText(getPaymentMethods(raw.getPaymentMethod()));
        status.setText(getStatus(raw.getStatus()));
        clientAddress.setText(raw.getAddress().getAddress());
        NameOfClient.setText(raw.getClient().getName());

        if (raw.getClient().getImage()!=null){
            String photoPath = Common.BaseUrl + "images/" + raw.getClient().getImage().getFor() + "/" +
                    Uri.encode(raw.getClient().getImage().getName());
            Picasso.with(this).load(photoPath).into(ClientIconDetails);
        }
    }

    private void showUpDateDialog(final OrderRaw raw) {
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dilaog_update_order, null);
        Spinner spinnerUpdateOrder = view.findViewById(R.id.spinnerUpdateOrder);
        ConstraintLayout parentLayout = view.findViewById(R.id.parentLayout);
        Update_Layout = view.findViewById(R.id.Update_Layout);
        updateIcon = view.findViewById(R.id.updateIcon);
        Button UpDateButton = view.findViewById(R.id.UpDateButton);
        final TextView statusSelected = view.findViewById(R.id.statusSelected);
        statusSelected.setText(raw.getStatus());
        List<String> statusList = new ArrayList<>();
        statusList.add(getResources().getString(R.string.Pending));
        statusList.add(getResources().getString(R.string.Accepted));
        statusList.add(getResources().getString(R.string.Preparing));
        statusList.add(getResources().getString(R.string.finished));
        statusList.add(getResources().getString(R.string.readyForDelivery));

        AdapterOfSpinnerStatus adapterOfSpinnerStatus =
                new AdapterOfSpinnerStatus(this,R.layout.city_item,statusList);

        spinnerUpdateOrder.setAdapter(adapterOfSpinnerStatus);
        spinnerUpdateOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedStatus = (String) parent.getItemAtPosition(position);
                statusSelected.setText(SelectedStatus);
                SelectedStatus = getSelectedStatus(SelectedStatus);
                updateIcon.setImageResource(R.drawable.pencil_);
                Update_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput_spinner));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();

        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        UpDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                Common.getAPIRequest().updateOrder("Bearer "+Common.currentPosition.getData().getToken().getAccessToken(),
                        Common.currentPosition.getData().getProvider().getId()+"",raw.getId()+"",
                        new UpdateSubscriptionStatus(SelectedStatus)).enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        dialog1.dismiss();
                        dialog.dismiss();
                        startActivity(new Intent(Details_OrderActivity.this,
                                HomeActivity.class).putExtra("type",
                                "Details_OrderActivity")
                                .putExtra("typeId",getIntent().getExtras()
                                        .getInt("typeId")));
                    }

                    @Override
                    public void onFailure(Call<Orders> call, Throwable t) {

                    }
                });
            }
        });
        spinnerUpdateOrder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    updateIcon.setImageResource(R.drawable.pencil_active);
                    Update_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput_active));
                    isSpinnerSelected = true;
                }
                return false;
            }
        });
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIcon.setImageResource(R.drawable.pencil_);
                Update_Layout.setBackground(getResources().getDrawable(R.drawable.style_textinput_spinner));
            }
        });
    }

    public class AdapterOfSpinnerStatus extends ArrayAdapter<String> {
        List<String> list;
        LayoutInflater inflater;
        public AdapterOfSpinnerStatus(Activity context, int id ,List<String> list)
        {
            super(context,id,list);
            this.list=list;
            inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);

            return vv;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View vv = inflater.inflate(R.layout.city_item,parent,false);
            TextView Tname =(TextView)vv.findViewById(R.id.item);
            Tname.setText(list.get(position));
            return vv;
        }

    }

    private String getSelectedStatus(String selectedStatus){
        if (selectedStatus.equals(getResources().getString(R.string.Pending)))
            return "pending";
        else if (selectedStatus.equals(getResources().getString(R.string.Accepted)))
            return "accepted";
        else if (selectedStatus.equals(getResources().getString(R.string.Preparing)))
            return "preparing";
        else if (selectedStatus.equals(getResources().getString(R.string.finished)))
            return "prepared";
        else if (selectedStatus.equals(getResources().getString(R.string.readyForDelivery)))
            return "readyForDelivery";
        return selectedStatus;
    }

    private String getPaymentMethods(String s){
        if (s.equals("cashOnDelivery")) {
            VisaIcon.setVisibility(View.GONE);
            return getResources().getString( R.string.cashOnDelivery);
        }else if (s.equals("online")){
            VisaIcon.setVisibility(View.VISIBLE);
            return getResources().getString( R.string.cashOnDelivery);
        }else return s;
    }

    private String getStatus(String s) {
        if (s.equals("pending")) {
            return getResources().getString( R.string.Pending);
        }else if (s.equals("accepted")){
            return getResources().getString( R.string.Accepted);
        }else if (s.equals("preparing")){
            return getResources().getString( R.string.Preparing);
        }else if (s.equals("prepared")){
            return getResources().getString( R.string.Prepared);
        }else if (s.equals("readyForDelivery")){
            return getResources().getString( R.string.readyForDelivery);
        }else return s;
    }
}