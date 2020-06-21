package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dopave.diethub_vendor.R;
import java.util.List;

public class AdapterForSubscription extends RecyclerView.Adapter<AdapterForSubscription.ViewHolderForSubscription> {
    List<String> list;
    Context context;

    public AdapterForSubscription(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderForSubscription onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForSubscription(LayoutInflater.from(context).inflate(R.layout.model_subsciption,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForSubscription holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForSubscription extends RecyclerView.ViewHolder {
        public ViewHolderForSubscription(@NonNull View itemView) {
            super(itemView);
        }
    }
}
