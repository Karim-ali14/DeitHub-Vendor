package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.UI.Details_OrderActivity.Details_OrderActivity;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryViewModel;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.PrograssBarAnimation;
import com.dopave.diethub_vendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdapterForOrder extends RecyclerView.Adapter<AdapterForOrder.ViewHolderForOrders> {
    List<OrderRaw> list;
    Context context;
    int i ;
    public static int countItemsVisible = 0;
    DeliveryViewModel DViewModel;

    public AdapterForOrder(List<OrderRaw> list, Context context, int i, DeliveryViewModel DViewModel) {
        this.list = list;
        this.context = context;
        this.i = i;
        this.DViewModel = DViewModel;
    }

    @NonNull
    @Override
    public ViewHolderForOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForOrders(LayoutInflater.from(context).inflate(R.layout.model_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForOrders holder, int position) {
        final OrderRaw orderRaw = list.get(position);
        AnimationProcess(holder,position,orderRaw.getStatus());
        holder.AllDetailsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Details_OrderActivity.class)
                        .putExtra("orderRaw",orderRaw));

            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAssignDelivery(orderRaw);
            }
        });

    }

    private void showAssignDelivery(final OrderRaw orderRaw) {
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();

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
                assignDeliveryForOrder(orderRaw);
            }
        });
        cancel_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    private void assignDeliveryForOrder(final OrderRaw orderRaw) {
        HashMap<String,String> body = new HashMap<>();
        body.put("deliveryrep_id",AdapterForAssign.DeliverySelected.getId()+"");
        Common.getAPIRequest().assignOrder("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                orderRaw.getId()+"",body).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if(response.code() == 200){
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        Log.i("GGGGGGGGG",new JSONObject(response.errorBody().string())
                                .getJSONArray("errors")
                                .getJSONObject(0).getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {

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

    private void AnimationProcess(ViewHolderForOrders holder,int position,String status){
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
            holder.RatingButton.setText(getStatus(status));
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
            holder.RatingButton.setText(getStatus(status));
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
            holder.RatingButton.setText(getStatus(status));
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

    private String getStatus(String status) {
        if (status.equals("accepted"))
            return context.getResources().getString(R.string.Accepted);
        else if (status.equals("prepared"))
            return context.getResources().getString(R.string.Preparing);
        else if (status.equals("delivering"))
            return context.getResources().getString(R.string.Delivering);
        else if (status.equals("delivered"))
            return context.getResources().getString(R.string.finished);
        else
            return status;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForOrders extends RecyclerView.ViewHolder {
        ProgressBar progressBar,progressBar2;
        ImageView iconP,iconPreparing,finishIcon,menu;
        TextView RatingButton,AllDetailsText;
        ConstraintLayout delegateLayout;
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
            menu = itemView.findViewById(R.id.menu);
        }
    }

    public void allList(List<OrderRaw> list,int type){
        for (OrderRaw raw : list)
            this.list.add(raw);
        this.i = type;
    }

    private void getDeliveries(ProgressDialog dialog,final RecyclerView recyclerForAssign
            ,final AlertDialog dialog1){
        DViewModel.getAllDeliveries(dialog,context,DViewModel,recyclerForAssign,null)
                .observe((LifecycleOwner) context, new Observer<GetDeliveriesData>() {
                    @Override
                    public void onChanged(GetDeliveriesData getDeliveriesData) {

                        if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                        {
                            Toast.makeText(context, getDeliveriesData.getData().getDeliveryRows().size()+"", Toast.LENGTH_SHORT).show();
                            List<DeliveryRow> deliveryRows = getDeliveriesData.getData().getDeliveryRows();
                            recyclerForAssign.setAdapter(new AdapterForAssign(deliveryRows,context,0));
                            dialog1.show();
                        }
                        else
                            Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
