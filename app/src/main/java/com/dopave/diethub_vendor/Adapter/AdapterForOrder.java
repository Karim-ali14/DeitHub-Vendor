package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.Orders.Detail;
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.UI.Details_OrderActivity.Details_OrderActivity;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryViewModel;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrdersViewModel;
import com.dopave.diethub_vendor.UI.PrograssBarAnimation;
import com.dopave.diethub_vendor.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterForOrder extends RecyclerView.Adapter<AdapterForOrder.ViewHolderForOrders> {
    List<OrderRaw> list;
    Context context;
    int i ;
    public static int countItemsVisible = 0;
    DeliveryViewModel DViewModel;
    OrdersViewModel OViewModel;
    RecyclerView recyclerView;
    public static List<Detail> listDetail;

    public AdapterForOrder(List<OrderRaw> list, Context context, int i,
                           DeliveryViewModel DViewModel, OrdersViewModel OViewModel,
                           RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.i = i;
        this.DViewModel = DViewModel;
        this.OViewModel = OViewModel;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderForOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForOrders(LayoutInflater.from(context)
                .inflate(R.layout.model_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForOrders holder, int position) {
        final OrderRaw orderRaw = list.get(position);
        AnimationProcess(holder,position,orderRaw.getStatus(),orderRaw);
        holder.AllDetailsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdapterForOrder.listDetail = orderRaw.getDetails();
                context.startActivity(new Intent(context, Details_OrderActivity.class)
                        .putExtra("orderRaw",orderRaw).putExtra("typeId",i));

            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAssignDelivery(orderRaw);
            }
        });
        holder.OrderId.setText(orderRaw.getId()+"");
        holder.ClientName.setText(orderRaw.getClient().getName());

        if (orderRaw.getClient().getImage() != null) {
            String path = Common.BaseUrl + "images/" + orderRaw.getClient().getImage().getFor() + "/" +
                    Uri.encode(orderRaw.getClient().getImage().getName());
            Picasso.with(context).load(path).into(holder.ClientIcon);
        }else {
            holder.ClientIcon.setImageResource(R.drawable.personalinfo);
        }

        try {
            holder.createAt.setText(new SimpleDateFormat("dd/MM/yyyy").format(
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            .parse(orderRaw.getCreatedAt())));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showAssignDelivery(final OrderRaw orderRaw) {
        final ProgressDialog dialog = new ProgressDialog(context);

        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_assign_delivery, null);
        final RecyclerView recyclerForAssign = view.findViewById(R.id.RecyclerForAssign);
        ImageView cancel_image = view.findViewById(R.id.cancel_image);
        recyclerForAssign.setLayoutManager(new LinearLayoutManager(context));
        recyclerForAssign.setHasFixedSize(true);
        Button ConfirmButton = view.findViewById(R.id.ConfirmCancelButton);
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDeliveries(dialog,recyclerForAssign,dialog1);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignDeliveryForOrder(orderRaw,dialog,dialog1);
            }
        });
        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void assignDeliveryForOrder(final OrderRaw orderRaw,
                                        final ProgressDialog dialog, final AlertDialog dialog1) {
        dialog.show();
        HashMap<String,String> body = new HashMap<>();
        body.put("deliveryrep_id",AdapterForAssign.DeliverySelected.getId()+"");
        Common.getAPIRequest().assignOrder("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                orderRaw.getId()+"",body).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {

                if(response.code() == 200){
                    dialog1.dismiss();
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    String status[] = new String[2];
                    status[0] = "prepared";
                    status[1] = "readyForDelivery";
                    OViewModel.getAllOrders(status,OViewModel,dialog,context,OrderFragment.limit,
                            0,0).observe((LifecycleOwner) context, new Observer<Orders>() {
                        @Override
                        public void onChanged(Orders orders) {
                            dialog.dismiss();
                            list.clear();
                            recyclerView.setAdapter(
                                    new AdapterForOrder(orders.getData().getOrderRaw()
                                            ,context,i,DViewModel,OViewModel,recyclerView));
                        }
                    });
                }else {
                    dialog.dismiss();
                    if (response.code() == 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 401){
                        Common.onCheckTokenAction(context);
                    }else {
                        try {
                            Log.i("GGGGGGGGG", new JSONObject(response.errorBody().string())
                                    .getJSONArray("errors")
                                    .getJSONObject(0).getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                dialog.dismiss();

                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(context,R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
                }

                else if (t instanceof UnknownHostException) {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceesIN(final ProgressBar progressBar) {
        PrograssBarAnimation barAnimation = new PrograssBarAnimation(context,progressBar,null,0,100);
        barAnimation.setDuration(700);
        progressBar.setAnimation(barAnimation);
    }

    private void proceesDE(final ProgressBar progressBar) {
        PrograssBarAnimation barAnimation = new PrograssBarAnimation(context,progressBar,null,100,0);
        barAnimation.setDuration(700);
        progressBar.setAnimation(barAnimation);
    }

    private void AnimationProcess(ViewHolderForOrders holder,int position,String status,OrderRaw orderRaw) {
        holder.progressBar.setProgress(100);
        holder.iconP.setImageResource(R.drawable.ic_check_black_check);
        holder.iconP.setBackground(context.getResources().getDrawable(R.drawable.style_check));

        if (i == 0 && OrderFragment.PREPARING){
            holder.menu.setVisibility(View.GONE);
            holder.RatingButton.setVisibility(View.GONE);
            holder.delegateLayout.setVisibility(View.GONE);
            holder.progressBar2.setProgress(0);
            proceesDE(holder.progressBar2);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_normal);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            if (list.size() == 0){
                OrderFragment.PREPARING = false;
            }else if (list.size() -1 == position){
                OrderFragment.PREPARING = false;
            }
            Log.i("JJJJJJJ",OrderFragment.PREPARING+"");
        }else if (i == 1) {
            holder.menu.setVisibility(View.GONE);
            holder.RatingButton.setVisibility(View.VISIBLE);
            holder.delegateLayout.setVisibility(View.GONE);
            getStatus(status,holder,orderRaw);
            holder.progressBar.setProgress(100);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
            proceesIN(holder.progressBar2);
            holder.progressBar.setProgress(100);
            holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
            holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            if (list.size() -1 == position) {
                Log.i("JJJ",OrderFragment.PREPARING+" ");
                OrderFragment.PREPARING = true;
            }
            if (OrderFragment.FINISHED) {
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
                if (list.size() -1 == position)
                    OrderFragment.FINISHED = false;
            }
        }else if (i == 2){
            holder.menu.setVisibility(View.VISIBLE);
            holder.RatingButton.setVisibility(View.VISIBLE);
            getStatus(status,holder,orderRaw);
            holder.delegateLayout.setVisibility(View.VISIBLE);
            holder.RatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialogForRating();
                }
            });
            if (!OrderFragment.PREPARING && !OrderFragment.FINISHED){
                holder.progressBar.setProgress(100);
                holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
                holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                proceesIN(holder.progressBar2);
                holder.progressBar.setProgress(100);
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_check);
                if (list.size() == 0){
                    OrderFragment.FINISHED = true;
                    OrderFragment.PREPARING = true;
                }else if (list.size() -1 == position) {
                    OrderFragment.FINISHED = true;
                    OrderFragment.PREPARING = true;
                }
                Log.i("JJJJJJ",OrderFragment.PREPARING + " "+OrderFragment.FINISHED + list.size());
            }else if (!OrderFragment.FINISHED){
                holder.progressBar2.setProgress(100);
                holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
                holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_check);
                if (list.size() == 0){
                    OrderFragment.FINISHED = true;
                }else if (list.size() -1 == position)
                    OrderFragment.FINISHED = true;
            }
        }else if (i == 3){
            holder.menu.setVisibility(View.GONE);
            holder.RatingButton.setVisibility(View.VISIBLE);
            holder.delegateLayout.setVisibility(View.GONE);
            getStatus(status,holder,orderRaw);
            holder.progressBar2.setProgress(100);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
            holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
            if (list.size() == 0){
                OrderFragment.PREPARING = true;
                OrderFragment.FINISHED = false;
            }else if (list.size() -1 == position) {
                OrderFragment.PREPARING = true;
                OrderFragment.FINISHED = false;
            }
        }
    }

    private void getStatus(String status,ViewHolderForOrders holder,OrderRaw orderRaw) {
        if (status.equals("accepted")) {
            holder.OrderStats2.setText(context.getResources().getString(R.string.Accepted));
            holder.RatingButton.setText(context.getResources().getString(R.string.Accepted));
        }
        else if (status.equals("preparing")){
            holder.OrderStats2.setText(context.getResources().getString(R.string.Preparing));
            holder.RatingButton.setText(context.getResources().getString(R.string.Preparing));
        }
        else if (status.equals("prepared")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.Prepared));
            holder.OrderStats3.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.RatingButton.setText(context.getResources().getString(R.string.Prepared));
        }
        else if (status.equals("delivering")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.Delivering));
            setDeliveryDate(holder,orderRaw);
        }
        else if (status.equals("delivered")){
            holder.RatingButton.setText(context.getResources().getString(R.string.delivered));
            holder.OrderStats3.setText(context.getResources().getString(R.string.delivered));
            setDeliveryDate(holder,orderRaw);
        }
        else if (status.equals("readyForDelivery")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.readyForDelivery));
            holder.OrderStats3.setTextColor(context.getResources().getColor(R.color.orange));
            holder.RatingButton.setText(context.getResources().getString(R.string.readyForDelivery));
        }
        else if (status.equals("acceptForDelivery")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.acceptForDelivery));
            holder.OrderStats3.setTextColor(context.getResources().getColor(R.color.orange));
            holder.RatingButton.setText(context.getResources().getString(R.string.acceptForDelivery));
            setDeliveryDate(holder,orderRaw);
        }
        else if (status.equals("canceled")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.cancel));
            holder.OrderStats3.setTextColor(context.getResources().getColor(R.color.orange));
            holder.RatingButton.setText(context.getResources().getString(R.string.cancel));
            setDeliveryDate(holder,orderRaw);
        }else if (status.equals("return")){
            holder.OrderStats3.setText(context.getResources().getString(R.string.Return));
            holder.OrderStats3.setTextColor(context.getResources().getColor(R.color.orange));
            holder.RatingButton.setText(context.getResources().getString(R.string.Return));
            setDeliveryDate(holder,orderRaw);
        }
        else {
            holder.RatingButton.setText(status);
        }
    }

    private void setDeliveryDate(ViewHolderForOrders holder, OrderRaw orderRaw) {
        if (orderRaw.getDeliveryrep() != null){
            holder.delegateLayout.setVisibility(View.VISIBLE);
            holder.delegate.setText(context.getResources().getString(R.string.Delivery)+" : "+orderRaw.getDeliveryrep().getName());
            if (orderRaw.getDeliveryrep().getImage() != null){
                if (orderRaw.getDeliveryrep().getImage() != null) {
                    String path = Common.BaseUrl + "images/" + orderRaw.getDeliveryrep().getImage().getFor() + "/" +
                            Uri.encode(orderRaw.getDeliveryrep().getImage().getName());
                    Picasso.with(context).load(path).into(holder.delegateIcon);
                }else {
                    holder.delegateIcon.setImageResource(R.drawable.personalinfo);
                }
            }else {
                holder.delegateLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForOrders extends RecyclerView.ViewHolder {
        ProgressBar progressBar,progressBar2;
        ImageView iconP,iconPreparing,finishIcon,menu;
        TextView RatingButton,AllDetailsText,OrderId,ClientName,createAt,OrderStats1,OrderStats2,OrderStats3,delegate;
        ConstraintLayout delegateLayout;
        CircleImageView ClientIcon,delegateIcon;
        public ViewHolderForOrders(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar2 = itemView.findViewById(R.id.progressBar2);
            iconP = itemView.findViewById(R.id.iconP);
            iconPreparing = itemView.findViewById(R.id.iconPreparing);
            finishIcon = itemView.findViewById(R.id.finishIcon);
            RatingButton = itemView.findViewById(R.id.RatingButton);
            AllDetailsText = itemView.findViewById(R.id.AllDetailsText);
            delegateLayout = itemView.findViewById(R.id.delegateLayout);
            delegate = itemView.findViewById(R.id.delegate);
            menu = itemView.findViewById(R.id.menu);
            OrderId = itemView.findViewById(R.id.OrderId);
            ClientIcon = itemView.findViewById(R.id.ClientIcon);
            delegateIcon = itemView.findViewById(R.id.delegateIcon);
            ClientName = itemView.findViewById(R.id.ClientName);
            createAt = itemView.findViewById(R.id.createAt);
            OrderStats1 = itemView.findViewById(R.id.OrderStats1);
            OrderStats2 = itemView.findViewById(R.id.OrderStats2);
            OrderStats3 = itemView.findViewById(R.id.OrderStats3);
        }
    }

    public void allList(List<OrderRaw> list,int type){
        for (OrderRaw raw : list)
            this.list.add(raw);
        this.i = type;
    }

    private void getDeliveries(ProgressDialog dialog,final RecyclerView recyclerForAssign
            ,final AlertDialog dialog1) {
        DViewModel.getAllDeliveries(dialog,context,DViewModel,recyclerForAssign,null)
                .observe((LifecycleOwner) context, new Observer<GetDeliveriesData>() {
                    @Override
                    public void onChanged(GetDeliveriesData getDeliveriesData) {
                        List<DeliveryRow> deliveryRows = getDeliveriesData.getData().getDeliveryRows();
                        recyclerForAssign.setAdapter(new AdapterForAssign(deliveryRows,context,0));
                        dialog1.show();
                        if (getDeliveriesData.getData().getDeliveryRows().size() == 0)
                            Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();

                    }
                });
    }

}
