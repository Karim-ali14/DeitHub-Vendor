package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.GetVehicles.Image;
import com.dopave.diethub_vendor.Models.SettingTitle;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

public class EAdapter extends BaseExpandableListAdapter {
    List<SettingTitle> list;
    Context context;

    public EAdapter(List<SettingTitle> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.parent_model,parent,false);
        ((TextView)convertView.findViewById(R.id.TitleEx)).setText(list.get(groupPosition).getName());
        ((ImageView)convertView.findViewById(R.id.ImageEx)).setImageResource(list.get(groupPosition).getImage());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {

        if (list.get(groupPosition).getType() == 0){
            view = LayoutInflater.from(context).inflate(R.layout.child_1,parent,false);
        }else if (list.get(groupPosition).getType() == 1){
            view = LayoutInflater.from(context).inflate(R.layout.child_2,parent,false);
            getInitchild1(view);
        }else if (list.get(groupPosition).getType() == 2){
            view = LayoutInflater.from(context).inflate(R.layout.child_3,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.parent_model,parent,false);
        }
        Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
        view.setAnimation(animation);
        return view;
    }

    private void getInitchild1(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.Recycler_Res_Icons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new AdapterForResImage(getData(), context,null,"update",getData().size(),recyclerView));
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private List<Image> getData() {
        List<Image> list = new ArrayList<>();
        list.add(new Image(BitmapFactory.decodeResource(context.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(context.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(context.getResources(),R.drawable.gg)));
        return list;
    }
}
