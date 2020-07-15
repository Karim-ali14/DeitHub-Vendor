package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Subscriptions.Image;
import com.dopave.diethub_vendor.Models.Subscriptions.Row;
import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;
import com.dopave.diethub_vendor.Models.Subscriptions.UpdateStatus.UpdateSubscriptionStatus;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Subscription_detials.Subscription_detialsActivity;
import com.dopave.diethub_vendor.UI.Subscriptions.SubscriptionsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterForSubscription extends RecyclerView.Adapter<AdapterForSubscription.ViewHolderForSubscription> {
    List<Row> list;
    Context context;
    int Type;
    SubscriptionsViewModel viewModel;
    ProgressDialog dialog;
    String Status;
    RecyclerView recyclerView;
    public static List<Image> listImage;
    public AdapterForSubscription(List<Row> list, Context context,
                                  int type, SubscriptionsViewModel viewModel,
                                  ProgressDialog dialog, String status, RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        Type = type;
        this.viewModel = viewModel;
        this.dialog = dialog;
        Status = status;
        this.recyclerView = recyclerView;
        listImage = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolderForSubscription onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForSubscription(LayoutInflater.from(context)
                .inflate(R.layout.model_subsciption,parent,false));
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
                    showDialogToReason(SubRow);
                }
            });
            holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // acceptSubscription(SubRow);
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
                if (SubRow.get_package().getImages() != null && SubRow.get_package().getImages().size() != 0) {
                    AdapterForSubscription.listImage.clear();
                    AdapterForSubscription.listImage.addAll(SubRow.get_package().getImages());
                }
                else {
                    AdapterForSubscription.listImage.clear();
                }
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
                        SubRow.get_package().getProteinCal())+""+context.getResources().getString(R.string.Calorie));
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

    private void showDialogToReason(final Row row){
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_reason, null);
        final EditText reasonToCancel = view.findViewById(R.id.reasonToCancel);
        Button ConfirmButton = view.findViewById(R.id.ConfirmButton);
        Button CancelButton = view.findViewById(R.id.CancelButton);
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                if (reasonToCancel.getText().toString().isEmpty()) {
                    dialog.dismiss();
                    Toast.makeText(context, R.string.enter_cancel_reason, Toast.LENGTH_LONG).show();
                }
                else {
                    dialog1.dismiss();
                    //cancelSubscription(row);
                }
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

//    private void cancelSubscription(Row row){
//        viewModel.UpdateSubscriptionStatus(context,dialog,row.getId()+"",
//                new UpdateSubscriptionStatus("cancelled"," "))
//                .observe((LifecycleOwner) context,
//                        new Observer<Subscriptions>() {
//                            @Override
//                            public void onChanged(Subscriptions subscriptions) {
//                                viewModel.getAllSubscriptions(context,dialog,viewModel,Type,Status)
//                                        .observe((LifecycleOwner) context, new Observer<Subscriptions>() {
//                                            @Override
//                                            public void onChanged(Subscriptions subscriptions) {
//                                                list.clear();
//                                                Toast.makeText(context, context.getResources()
//                                                                .getString(R.string.order_was_canceled)
//                                                        , Toast.LENGTH_SHORT).show();
//                                                recyclerView.setAdapter(
//                                                        new AdapterForSubscription(
//                                                                subscriptions.getData().getRows(),
//                                                                context, Type, viewModel,dialog,Status
//                                                                ,recyclerView));
//                                            }
//                                        });
//                            }
//                        });
//    }
//
//    private void acceptSubscription(Row row){
//        dialog.show();
//        viewModel.UpdateSubscriptionStatus(context,dialog,row.getId()+"",
//                new UpdateSubscriptionStatus("approved"))
//                .observe((LifecycleOwner) context,
//                        new Observer<Subscriptions>() {
//                            @Override
//                            public void onChanged(Subscriptions subscriptions) {
//                                viewModel.getAllSubscriptions(context,dialog,viewModel,Type,Status)
//                                        .observe((LifecycleOwner) context, new Observer<Subscriptions>() {
//                                            @Override
//                                            public void onChanged(Subscriptions subscriptions) {
//                                                list.clear();
//                                                Toast.makeText(context, context.getResources()
//                                                                .getString(R.string.order_was_accepted)
//                                                        , Toast.LENGTH_SHORT).show();
//                                                recyclerView.setAdapter(
//                                                        new AdapterForSubscription(
//                                                                subscriptions.getData().getRows(),
//                                                                context, Type, viewModel,dialog,Status
//                                                                ,recyclerView));
//                                            }
//                                        });
//                            }
//                        });
//    }

    public void allList(List<Row> list){
        for (Row raw : list)
            this.list.add(raw);
    }
}
