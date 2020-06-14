package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery.DeliveryRow;
import com.dopave.diethub_vendor.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForDelegate extends RecyclerView.Adapter<AdapterForDelegate.ViewHolderForDelegate> {
    List<DeliveryRow> list;
    Context context;
    public AdapterForDelegate(List<DeliveryRow> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolderForDelegate onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForDelegate(
                LayoutInflater.from(context)
                        .inflate(R.layout.model_delegate,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForDelegate holder, int position) {
        DeliveryRow row = list.get(position);
        holder.NameOfDelegate.setText(row.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForDelegate extends RecyclerView.ViewHolder{
        CircleImageView IconOfDelegate;
        TextView NameOfDelegate,AddressOfDelegate,DesOfDelegate;
        public ViewHolderForDelegate(@NonNull View itemView) {
            super(itemView);
            IconOfDelegate = itemView.findViewById(R.id.IconOfDelegate);
            NameOfDelegate = itemView.findViewById(R.id.NameOfDelegate);
            AddressOfDelegate = itemView.findViewById(R.id.AddressOfDelegate);
            DesOfDelegate = itemView.findViewById(R.id.DesOfDelegate);
        }
    }
}
