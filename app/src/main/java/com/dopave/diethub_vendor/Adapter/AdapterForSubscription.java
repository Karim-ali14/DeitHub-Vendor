package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.Subscription_detialsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class AdapterForSubscription extends RecyclerView.Adapter<AdapterForSubscription.ViewHolderForSubscription> {
    List<String> list;
    Context context;
    int Type;

    public AdapterForSubscription(List<String> list, Context context, int type) {
        this.list = list;
        this.context = context;
        Type = type;
    }

    @NonNull
    @Override
    public ViewHolderForSubscription onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForSubscription(LayoutInflater.from(context).inflate(R.layout.model_subsciption,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForSubscription holder, int position) {
        if (Type == 0) {
            holder.ButtonsLayout.setVisibility(View.VISIBLE);
            holder.MenuLayout.setVisibility(View.VISIBLE);
            holder.MenuLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(holder);
                }
            });
        }
        else {
            holder.MenuLayout.setVisibility(View.GONE);
            holder.ButtonsLayout.setVisibility(View.GONE);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Subscription_detialsActivity.class));
            }
        });
    }

    private void showDialog(ViewHolderForSubscription holder) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        View bottomSheet = LayoutInflater.from(context).inflate(R.layout.dialog_call_client
                ,(RelativeLayout)holder.view.findViewById(R.id.Container));
        dialog.setContentView(bottomSheet);
        dialog.show();
        LinearLayout Call = bottomSheet.findViewById(R.id.CallLayout);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Calling Client", Toast.LENGTH_SHORT).show();
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
        public ViewHolderForSubscription(@NonNull View itemView) {
            super(itemView);
            ButtonsLayout = itemView.findViewById(R.id.ButtonsLayout);
            MenuLayout = itemView.findViewById(R.id.MenuLayout);
            container = itemView.findViewById(R.id.container);
            view = itemView;
        }
    }
}
