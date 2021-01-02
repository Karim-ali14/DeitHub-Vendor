package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Subscriptions.Image;
import com.dopave.diethub_vendor.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class AdapterForSilder extends PagerAdapter {
    List<Image> list;
    Context context;

    public AdapterForSilder(List<Image> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_image_slilder, container, false);
        ImageView image = view.findViewById(R.id.imageSlider);
        if (list.get(position).getName() != null && list.get(position).getFor() != null) {
            String photoPath = Common.BaseUrlForImages + list.get(position).getFor() + "/" +
                    Uri.encode(list.get(position).getName());
            Picasso.with(context).load(photoPath).into(image);
        }else if (list.get(position).getDefaultImage() != 0){
            Picasso.with(context).load(list.get(position).getDefaultImage()).into(image);
        }
        container.addView(view); // without this line nothing happen
        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
