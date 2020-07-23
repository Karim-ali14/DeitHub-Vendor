package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.R;
import java.util.List;

public class AdapterForAssign extends RecyclerView.Adapter<AdapterForAssign.ViewHolderForAssign> {

    List<DeliveryRow> list;
    Context context;
    int typeStatus;
    ViewHolderForAssign oldCard = null;
    int oldPosition = -1;
    public AdapterForAssign(List<DeliveryRow> list, Context context, int typeStatus) {
        this.list = list;
        this.context = context;
        this.typeStatus = typeStatus;
    }

    @NonNull
    @Override
    public ViewHolderForAssign onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForAssign(LayoutInflater.from(context)
                .inflate(R.layout.model_assign_delivery,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderForAssign holder, final int position) {
        holder.DeliveryName.setText(list.get(position).getName());
        holder.DeliveryAddress.setText(list.get(position).getCity().getName());
        onSelectDelivery();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForAssign extends RecyclerView.ViewHolder {
        ConstraintLayout ChangeStatusLayout;
        TextView DeliveryName,DeliveryAddress;
        ImageView AssignCheck;
        public ViewHolderForAssign(@NonNull View itemView) {
            super(itemView);
            ChangeStatusLayout = itemView.findViewById(R.id.ChangeStatusLayout);
            DeliveryName = itemView.findViewById(R.id.nameOfDelivery);
            DeliveryAddress = itemView.findViewById(R.id.DeliveryAddress);
            AssignCheck = itemView.findViewById(R.id.AssignCheck);
        }
    }

    private void onSelectDelivery(final ViewHolderForAssign holder, final int position){
        if (typeStatus == position){
            oldPosition = position;
            holder.ChangeStatusLayout.setBackground(context.getResources().getDrawable(R.drawable.style_active_assign_delivery));
            holder.DeliveryName.setTextColor(context.getResources().getColor(R.color.white));
            holder.AssignCheck.setVisibility(View.VISIBLE);
            oldCard = holder;
        }
        holder.ChangeStatusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPosition != position){
                    holder.ChangeStatusLayout.setBackground(context.getResources().getDrawable(R.drawable.style_active_assign_delivery));
                    holder.DeliveryName.setTextColor(context.getResources().getColor(R.color.white));
                    holder.AssignCheck.setVisibility(View.VISIBLE);

                    oldCard.ChangeStatusLayout.setBackground(context.getResources().getDrawable(R.drawable.style_nomal_assign_delivery));
                    oldCard.DeliveryName.setTextColor(context.getResources().getColor(R.color.black));
                    oldCard.AssignCheck.setVisibility(View.GONE);

                    oldPosition = position;
                    oldCard = holder;
                }
            }
        });
    }
}
