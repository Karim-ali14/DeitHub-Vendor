package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.dopave.diethub_vendor.Models.GetDeliveries.Image;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Images.Modify_ImagesActivity;
import com.dopave.diethub_vendor.UI.Setting.Modify_Images.Modify_Images_ViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterForEditImages extends RecyclerView.Adapter<AdapterForEditImages.ViewHolderForEditImages> {
    List<Image> list;
    Context context;
    int numberOfIndexes;
    List<File> imageListRequest;
    RecyclerView recyclerView;
    Modify_Images_ViewModel viewModel;
    ProgressDialog dialog;

    public AdapterForEditImages(List<Image> list, Context context, int numberOfIndexes,
                                List<File> imageListRequest, RecyclerView recyclerView,
                                Modify_Images_ViewModel viewModel,ProgressDialog dialog) {
        this.list = list;
        this.context = context;
        this.numberOfIndexes = numberOfIndexes;
        this.imageListRequest = imageListRequest;
        this.recyclerView = recyclerView;
        this.viewModel = viewModel;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolderForEditImages onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForEditImages(
                LayoutInflater.from(context)
                        .inflate(R.layout.model_image_res,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForEditImages holder, final int position) {
       Image image = list.get(position);
        Log.i("ImageProPP",image.getId()+"");
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
                        Modify_ImagesActivity modify_imagesActivity = (Modify_ImagesActivity) context;
                        modify_imagesActivity.requestStoragePermission(modify_imagesActivity.SELECT_IMAGE_FOR_PROVIDER);
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
        if (Modify_ImagesActivity.numberOfIndexes != 0) {
            if (position <= Modify_ImagesActivity.numberOfIndexes) {
                deleterequest(position);
            } else {
                Log.i("ListsSizes : ",list.size() + " "
                        + imageListRequest.size()+" "+Modify_ImagesActivity.numberOfIndexes+" "+position);

                int n = position - Modify_ImagesActivity.numberOfIndexes;

                list.remove(position);
                imageListRequest.remove(--n);

            }
        }else {

            Log.i("ListsSizes : ",list.size() + " "
                    + imageListRequest.size()+" "+Modify_ImagesActivity.numberOfIndexes+" "+position);

            list.remove(position);
            imageListRequest.remove(--position);

        }

        recyclerView.setAdapter(new AdapterForEditImages(list,context,numberOfIndexes,
                imageListRequest,recyclerView,viewModel,dialog));


    }

    private void deleterequest(final int position) {
        Image image = list.get(position);
        ArrayList<Integer> images = new ArrayList<>();
        images.add(image.getId());
        viewModel.deleteImage(context,dialog,images).observe((LifecycleOwner) context,
                new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt defualt) {
                        Modify_ImagesActivity.numberOfIndexes--;
                        list.remove(position);
                        recyclerView.setAdapter(new AdapterForEditImages(list,context,numberOfIndexes,
                                imageListRequest,recyclerView,viewModel,dialog));

                        Log.i("ListsSizes : ",list.size() + " "
                                + imageListRequest.size()+" "+Modify_ImagesActivity.numberOfIndexes);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForEditImages extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout Add_Photo_Layout,menuIconForImage;
        public ViewHolderForEditImages(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.Res_icon);
            menuIconForImage = itemView.findViewById(R.id.menuIconForImage);
            Add_Photo_Layout = itemView.findViewById(R.id.Add_Photo_Layout);
        }
    }

}