package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.GetVehicles.Image;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleViewModel;
import com.dopave.diethub_vendor.UI.Setting.Modify_Images.Modify_ImagesActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterForResImage extends RecyclerView.Adapter<AdapterForResImage.ViewHolderForResImage> {
    List<Image> list;
    Context context;
    List<File> imageListRequest;
    String type;
    int numberOfIndexes;
    RecyclerView recyclerView;
    CreateVehicleViewModel viewModel;
    GetVehicleData VehicleData;
    ProgressDialog dialog;
    public AdapterForResImage(List<Image> list, Context context,
                              List<File> imageListRequest,
                              String type, int numberOfIndexes, RecyclerView recyclerView,
                              CreateVehicleViewModel viewModel, GetVehicleData vehicleData,
                              ProgressDialog dialog) {
        this.list = list;
        this.context = context;
        this.imageListRequest = imageListRequest;
        this.type = type;
        this.numberOfIndexes = numberOfIndexes;
        this.recyclerView = recyclerView;
        this.viewModel = viewModel;
        VehicleData = vehicleData;
        this.dialog = dialog;
    }

    public AdapterForResImage(List<Image> list, Context context, List<File> imageListRequest, String type, int numberOfIndexes, RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.imageListRequest = imageListRequest;
        this.type = type;
        this.numberOfIndexes = numberOfIndexes;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolderForResImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForResImage(
                LayoutInflater.from(context)
                        .inflate(R.layout.model_image_res,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForResImage holder, final int position) {
        Image image = list.get(position);
        if (position == 0) {
            holder.menuIconForImage.setVisibility(View.GONE);
            holder.Add_Photo_Layout.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.Add_Photo_Layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() <= 5) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        CreateVehicleActivity createVehicleActivity = (CreateVehicleActivity) context;
                        createVehicleActivity.requestStoragePermission(CreateVehicleActivity.SELECT_IMAGE_FOR_VEHICLE);
                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.maximum_of_five_pictures), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            holder.menuIconForImage.setVisibility(View.VISIBLE);
            holder.Add_Photo_Layout.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setImageResource(R.drawable.gg);
            if (image.getImageAdd() != null)
                holder.imageView.setImageBitmap(image.getImageAdd());
            else if (image.getFor() != null && image.getName() != null){
                String path = Common.BaseUrlForImages + image.getFor() + "/" +
                        Uri.encode(image.getName());
                Picasso.with(context).load(path).into(holder.imageView);
            }
        }
        holder.menuIconForImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_delete_image:
                                delete(position);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.menu_for_image);
                popupMenu.show();
            }
        });
    }

    private void delete(int position) {
        if (type.equals("update") && CreateVehicleActivity.numberOfIndexes != 0){
            if (position <= CreateVehicleActivity.numberOfIndexes){
                deleteImage(position);
            }else {
                Log.i("ListsSizes : ",list.size() + " "
                        + imageListRequest.size()+" "+CreateVehicleActivity.numberOfIndexes+" "+position);

                int n = position - CreateVehicleActivity.numberOfIndexes;

                Log.i("ListsSizes : ",n+"");

                list.remove(position);
                imageListRequest.remove(--n);

                recyclerView.setAdapter(new AdapterForResImage(list,context,imageListRequest,
                        "update",CreateVehicleActivity.numberOfIndexes,recyclerView,viewModel,VehicleData,dialog));
            }
        }else {
            list.remove(position);
            imageListRequest.remove(--position);
            recyclerView.setAdapter(new AdapterForResImage(list,context,imageListRequest,
                    "create",CreateVehicleActivity.numberOfIndexes,recyclerView,viewModel,VehicleData,dialog));
        }

    }

    private void deleteImage(final int position) {
        Image image = list.get(position);
        List<Integer> listdalete = new ArrayList<>();
        listdalete.add(image.getId());
        viewModel.deleteImage(VehicleData.getData().getId()+"",listdalete,context,dialog)
                .observe((LifecycleOwner) context, new Observer<Data>() {
            @Override
            public void onChanged(Data data) {
                CreateVehicleActivity.numberOfIndexes--;
                list.remove(position);
                recyclerView.setAdapter(new AdapterForResImage(list,context,imageListRequest,
                        "update",CreateVehicleActivity.numberOfIndexes,recyclerView,viewModel,VehicleData,dialog));

                Log.i("ListsSizes : ",list.size() + " "
                        + imageListRequest.size()+" "+ CreateVehicleActivity.numberOfIndexes);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForResImage extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout Add_Photo_Layout,menuIconForImage;
        public ViewHolderForResImage(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Res_icon);
            menuIconForImage = itemView.findViewById(R.id.menuIconForImage);
            Add_Photo_Layout = itemView.findViewById(R.id.Add_Photo_Layout);
        }
    }
}
