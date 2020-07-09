package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Subscriptions.Row;
import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.Subscription_detialsActivity;
import com.dopave.diethub_vendor.UI.Subscriptions.SubscriptionsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterForSubscription extends RecyclerView.Adapter<AdapterForSubscription.ViewHolderForSubscription> {
    List<Row> list;
    Context context;
    int Type;
    SubscriptionsViewModel viewModel;

    public AdapterForSubscription(List<Row> list, Context context, int type, SubscriptionsViewModel viewModel) {
        this.list = list;
        this.context = context;
        Type = type;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolderForSubscription onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForSubscription(LayoutInflater.from(context).inflate(R.layout.model_subsciption,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForSubscription holder, int position) {
        final Row SubRow = list.get(position);
        if (Type == 0) {
            holder.ButtonsLayout.setVisibility(View.VISIBLE);
            holder.MenuLayout.setVisibility(View.VISIBLE);
            holder.MenuIcon.setVisibility(View.VISIBLE);
            holder.MenuIcon.setImageResource(R.drawable.menu_points);
            holder.MenuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(holder,SubRow);
                }
            });
            holder.CancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        else {
            holder.MenuLayout.setVisibility(View.VISIBLE);
            holder.MenuIcon.setImageResource(R.color.white);
            holder.ButtonsLayout.setVisibility(View.GONE);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Subscription_detialsActivity.class)
                        .putExtra("Package",SubRow.get_package())
                        .putExtra("Client",SubRow.getClient()));
            }
        });
        holder.NameOfSubscription.setText(SubRow.get_package().getName());
        holder.DecOfSubscription.setText(SubRow.get_package().getDescription());
        holder.Price.setText(SubRow.get_package().getPrice()+" "+context.getResources().getString(R.string.SAR));
        holder.Calories.setText(
                (SubRow.get_package().getFatCal()+
                        SubRow.get_package().getCarbCal()+
                        SubRow.get_package().getProteinCal())+" "+context.getResources().getString(R.string.Calorie));
        holder.SubscriptionRating.setText(SubRow.get_package().getTotalRate()+"");
        holder.NameOfClient.setText(SubRow.getClient().getName());
        holder.Calories.setPaintFlags(holder.Calories.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        String photoPath = Common.BaseUrl + "images/" + SubRow.get_package().getMainImage().getFor() + "/" +
                Uri.encode(SubRow.get_package().getMainImage().getName());
        Picasso.with(context).load(photoPath).into(holder.imageSubscription);
    }

    private void showDialog(ViewHolderForSubscription holder, final Row subRow) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        View bottomSheet = LayoutInflater.from(context).inflate(R.layout.dialog_call_client
                ,(RelativeLayout)holder.view.findViewById(R.id.Container));
        dialog.setContentView(bottomSheet);
        dialog.show();
        LinearLayout Call = bottomSheet.findViewById(R.id.CallLayout);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallingClient(subRow.getClient().getMobilePhone()+"");
                dialog.dismiss();
            }
        });
        bottomSheet.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForSubscription extends RecyclerView.ViewHolder {
        LinearLayout ButtonsLayout,MenuLayout;
        ConstraintLayout container;
        View view;
        ImageView imageSubscription,MenuIcon;
        TextView NameOfSubscription,DecOfSubscription,Price,SubscriptionRating,Calories,NameOfClient;
        Button AcceptButton,CancelButton;
        public ViewHolderForSubscription(@NonNull View itemView) {
            super(itemView);
            ButtonsLayout = itemView.findViewById(R.id.ButtonsLayout);
            MenuLayout = itemView.findViewById(R.id.MenuLayout);
            MenuIcon = itemView.findViewById(R.id.MenuIcon);
            container = itemView.findViewById(R.id.container);
            imageSubscription = itemView.findViewById(R.id.imageSubscription);
            NameOfSubscription = itemView.findViewById(R.id.NameOfSubscription);
            DecOfSubscription = itemView.findViewById(R.id.DecOfSubscription);
            Price = itemView.findViewById(R.id.Price);
            SubscriptionRating = itemView.findViewById(R.id.SubscriptionRating);
            Calories = itemView.findViewById(R.id.Calories);
            NameOfClient = itemView.findViewById(R.id.NameOfClient);
            AcceptButton = itemView.findViewById(R.id.AcceptButton);
            CancelButton = itemView.findViewById(R.id.CancelButton);
            view = itemView;
        }
    }

    private void CallingClient(String PhoneNumber){
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:"+Uri.encode(PhoneNumber.trim())));
        context.startActivity(dialIntent);
    }
}
