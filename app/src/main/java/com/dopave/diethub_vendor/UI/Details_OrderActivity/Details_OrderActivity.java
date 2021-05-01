package com.dopave.diethub_vendor.UI.Details_OrderActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.dopave.diethub_vendor.Adapter.AdapterForMeals;
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
import com.dopave.diethub_vendor.UI.SharedPref;
import com.squareup.picasso.Picasso;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Details_OrderActivity extends AppCompatActivity {
    TextView paymentMethod,status,clientAddress,
            NumberOfOrder,TotalNumber,NameOfClient;
    LinearLayout CountLayout;
    ImageView VisaIcon;
    View line3,line8;
    Button ButtonUpDate;
    String SelectedStatus;
    boolean isSpinnerSelected = false;
    ImageView updateIcon;
    ConstraintLayout Update_Layout;
    ProgressDialog dialog;
    CircleImageView ClientIconDetails;
    RecyclerView recyclerForMeals;
    SharedPref pref;
    int firstOpenDialog = 0;
    OrderRaw raw;
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
        pref = new SharedPref(this);
        raw = getIntent().getExtras().getParcelable("orderRaw");
        getWindow().getDecorView(). setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        NumberOfOrder = findViewById(R.id.NumberOfOrder);
        recyclerForMeals = findViewById(R.id.recyclerForMeals);
        recyclerForMeals.setHasFixedSize(true);
        recyclerForMeals.setLayoutManager(new LinearLayoutManager(this));
        ClientIconDetails = findViewById(R.id.ClientIconDetails);
        NameOfClient = findViewById(R.id.NameOfClient);
        paymentMethod = findViewById(R.id.paymentMethod);
        status = findViewById(R.id.status);
        TotalNumber = findViewById(R.id.TotalNumber);
        clientAddress = findViewById(R.id.clientAddress);
        CountLayout = findViewById(R.id.CountLayout);
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
        hideUpdateButton();
    }

    private void hideUpdateButton() {
        if (
                raw.getStatus().equals("readyForDelivery") ||
                raw.getStatus().equals("acceptForDelivery") ||
                raw.getStatus().equals("delivering") ||
                raw.getStatus().equals("delivered") ||
                raw.getStatus().equals("canceled") ||
                raw.getStatus().equals("return") ){
            ButtonUpDate.setVisibility(View.GONE);
        }else
            ButtonUpDate.setVisibility(View.VISIBLE);
    }

    private void setOrderData(OrderRaw raw) {
        NumberOfOrder.setText(raw.getId().toString());

        if (AdapterForOrder.listDetail != null){
            recyclerForMeals.setAdapter(new AdapterForMeals(raw.getDetails(),this,pref));
        }

        TotalNumber.setText(raw.getTotalPricePiastre().getTotal()+" "+getResources().getString(R.string.SAR));
        if (raw.getPaymentMethod() != null)
            paymentMethod.setText(getPaymentMethods(raw.getPaymentMethod()));
        status.setText(getStatus(raw.getStatus()));
        if (raw.getAddress()!=null)
            clientAddress.setText(raw.getAddress().getAddress());
        if (raw.getClient().getName() != null)
            NameOfClient.setText(raw.getClient().getName());

        if (raw.getClient().getImage()!=null){
            String photoPath = Common.BaseUrlForImages + raw.getClient().getImage().getFor() + "/" +
                    Uri.encode(raw.getClient().getImage().getName());
            Picasso.with(this).load(photoPath).into(ClientIconDetails);
        }
    }

    private void showUpDateDialog(final OrderRaw raw) {
        firstOpenDialog = 0;
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dilaog_update_order, null);
        final Spinner spinnerUpdateOrder = view.findViewById(R.id.spinnerUpdateOrder);
        ConstraintLayout parentLayout = view.findViewById(R.id.parentLayout);
        Update_Layout = view.findViewById(R.id.Update_Layout);
        updateIcon = view.findViewById(R.id.updateIcon);
        Button UpDateButton = view.findViewById(R.id.UpDateButton);
        final TextView statusSelected = view.findViewById(R.id.statusSelected);
        statusSelected.setText(raw.getStatus());
        final List<String> statusList = new ArrayList<>();
//        statusList.add(getResources().getString(R.string.Pending));
        statusList.add(getResources().getString(R.string.Accepted));
        statusList.add(getResources().getString(R.string.Preparing));
        statusList.add(getResources().getString(R.string.finished));
//        statusList.add(getResources().getString(R.string.readyForDelivery));
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
                if (firstOpenDialog == 0 && !getSelectedStatus(getStatus(raw.getStatus())).isEmpty()){
                    spinnerUpdateOrder.setSelection(statusList.indexOf(getStatus(raw.getStatus())));
                    String oldStatus = getStatus(raw.getStatus());
                    statusSelected.setText(oldStatus);
                    SelectedStatus = getSelectedStatus(oldStatus);
                    firstOpenDialog++;
                }
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
                if (SelectedStatus.equals(raw.getStatus())) {
                    dialog.dismiss();
                    Toast.makeText(Details_OrderActivity.this, R.string.no_changes, Toast.LENGTH_SHORT).show();
                } else {
                    Common.getAPIRequest().updateOrder("Bearer " + Common.currentPosition.getData().getToken().getAccessToken(),
                            Common.currentPosition.getData().getProvider().getId() + "", raw.getId() + "",
                            new UpdateSubscriptionStatus(SelectedStatus)).enqueue(new Callback<Orders>() {
                        @Override
                        public void onResponse(Call<Orders> call, Response<Orders> response) {
                            dialog1.dismiss();
                            dialog.dismiss();
                            if (response.code() == 200) {
                                startActivity(new Intent(Details_OrderActivity.this,
                                        HomeActivity.class).putExtra("type",
                                        "Details_OrderActivity")
                                        .putExtra("typeId", getIntent().getExtras()
                                                .getInt("typeId")));
                            }
                            if (response.code() >= 500) {
                                Toast.makeText(Details_OrderActivity.this, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 401) {
                                Common.onCheckTokenAction(Details_OrderActivity.this);
                            }
                        }

                        @Override
                        public void onFailure(Call<Orders> call, Throwable t) {
                            dialog.dismiss();
                            dialog1.dismiss();
                            if (t instanceof SocketTimeoutException) {
                                Toast.makeText(Details_OrderActivity.this, R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
                            } else if (t instanceof UnknownHostException) {
                                Toast.makeText(Details_OrderActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Details_OrderActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
//        if (selectedStatus.equals(getResources().getString(R.string.Pending)))
//            return "pending";
        if (selectedStatus.equals(getResources().getString(R.string.Accepted)))
            return "accepted";
        else if (selectedStatus.equals(getResources().getString(R.string.Preparing)))
            return "preparing";
        else if (selectedStatus.equals(getResources().getString(R.string.finished)))
            return "prepared";
//        else if (selectedStatus.equals(getResources().getString(R.string.readyForDelivery)))
//            return "readyForDelivery";
        return "";
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
        }
        else return s;
    }

}
