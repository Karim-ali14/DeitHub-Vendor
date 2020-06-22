package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Subscription_detialsActivity;

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
    public void onBindViewHolder(@NonNull ViewHolderForSubscription holder, int position) {
        if (Type == 0)
            holder.ButtonsLayout.setVisibility(View.VISIBLE);
        else
            holder.ButtonsLayout.setVisibility(View.GONE);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Subscription_detialsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForSubscription extends RecyclerView.ViewHolder {
        LinearLayout ButtonsLayout;
        ConstraintLayout container;
        public ViewHolderForSubscription(@NonNull View itemView) {
            super(itemView);
            ButtonsLayout = itemView.findViewById(R.id.ButtonsLayout);
            container = itemView.findViewById(R.id.container);
        }
    }
}
