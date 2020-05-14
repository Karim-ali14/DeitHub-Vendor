package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.R;

import java.util.List;

public class AdapterForResImage extends RecyclerView.Adapter<AdapterForResImage.ViewHolderForResImage> {
    List<String> list;
    Context context;

    public AdapterForResImage(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderForResImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForResImage(
                LayoutInflater.from(context)
                        .inflate(R.layout.model_image_res,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForResImage holder, int position) {
        if (position == 0) {
            holder.Add_Photo_Layout.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }else {
            holder.Add_Photo_Layout.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(R.drawable.gg);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForResImage extends RecyclerView.ViewHolder{
        ImageView imageView;
        LinearLayout Add_Photo_Layout;
        public ViewHolderForResImage(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Res_icon);
            Add_Photo_Layout = itemView.findViewById(R.id.Add_Photo_Layout);
        }
    }
}
