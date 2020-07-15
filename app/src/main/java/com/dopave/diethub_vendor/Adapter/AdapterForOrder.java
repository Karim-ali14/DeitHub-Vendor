package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.dopave.diethub_vendor.Models.Orders.OrderRaw;
import com.dopave.diethub_vendor.UI.Details_OrderActivity.Details_OrderActivity;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.PrograssBarAnimation;
import com.dopave.diethub_vendor.R;
import java.util.List;


public class AdapterForOrder extends RecyclerView.Adapter<AdapterForOrder.ViewHolderForOrders> {
    List<OrderRaw> list;
    Context context;
    int i ;
    public static int countItemsVisible = 0;


    public AdapterForOrder(List<OrderRaw> list, Context context, int i) {
        this.list = list;
        this.context = context;
        this.i = i;
    }

    @NonNull
    @Override
    public ViewHolderForOrders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForOrders(LayoutInflater.from(context).inflate(R.layout.model_order,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForOrders holder, int position) {
        final OrderRaw orderRaw = list.get(position);
        AnimationProcess(holder,position,orderRaw.getStatus());
        holder.AllDetailsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Details_OrderActivity.class)
                        .putExtra("orderRaw",orderRaw));
            }
        });
    }

    private void proceesIN(final ProgressBar progressBar) {
        PrograssBarAnimation barAnimation = new PrograssBarAnimation(context,progressBar,null,0,100);
        barAnimation.setDuration(700);
        progressBar.setAnimation(barAnimation);
    }

    private void proceesDE(final ProgressBar progressBar) {
        PrograssBarAnimation barAnimation = new PrograssBarAnimation(context,progressBar,null,100,0);
        barAnimation.setDuration(700);
        progressBar.setAnimation(barAnimation);
    }

    private void AnimationProcess(ViewHolderForOrders holder,int position,String status){
        holder.progressBar.setProgress(100);
        holder.iconP.setImageResource(R.drawable.ic_check_black_check);
        holder.iconP.setBackground(context.getResources().getDrawable(R.drawable.style_check));
        if (i == 0 && OrderFragment.PREPARING){
            holder.RatingButton.setVisibility(View.GONE);
            holder.delegateLayout.setVisibility(View.GONE);
            holder.progressBar2.setProgress(0);
            proceesDE(holder.progressBar2);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_normal);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            if (list.size() == 0){
                OrderFragment.PREPARING = false;
            }else if (list.size() -1 == position){
                OrderFragment.PREPARING = false;
            }
            Log.i("JJJJJJJ",OrderFragment.PREPARING+"");
        }else if (i == 1) {
            holder.RatingButton.setVisibility(View.VISIBLE);
            holder.delegateLayout.setVisibility(View.GONE);
            holder.RatingButton.setText(getStatus(status));
            holder.progressBar.setProgress(100);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
            proceesIN(holder.progressBar2);
            holder.progressBar.setProgress(100);
            holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
            holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            if (list.size() -1 == position) {
                Log.i("JJJ",OrderFragment.PREPARING+" ");
                OrderFragment.PREPARING = true;
            }
            if (OrderFragment.FINISHED) {
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
                if (list.size() -1 == position)
                    OrderFragment.FINISHED = false;
            }
        }else if (i == 2){
            holder.RatingButton.setVisibility(View.VISIBLE);
            holder.RatingButton.setText(getStatus(status));
            holder.delegateLayout.setVisibility(View.VISIBLE);
            holder.RatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialogForRating();
                }
            });
            if (!OrderFragment.PREPARING && !OrderFragment.FINISHED){
                holder.progressBar.setProgress(100);
                holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
                holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                proceesIN(holder.progressBar2);
                holder.progressBar.setProgress(100);
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_check);
                if (list.size() == 0){
                    OrderFragment.FINISHED = true;
                    OrderFragment.PREPARING = true;
                }else if (list.size() -1 == position) {
                    OrderFragment.FINISHED = true;
                    OrderFragment.PREPARING = true;
                }
                Log.i("JJJJJJ",OrderFragment.PREPARING + " "+OrderFragment.FINISHED + list.size());
            }else if (!OrderFragment.FINISHED){
                holder.progressBar2.setProgress(100);
                holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
                holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_check));
                holder.finishIcon.setImageResource(R.drawable.ic_check_black_check);
                if (list.size() == 0){
                    OrderFragment.FINISHED = true;
                }else if (list.size() -1 == position)
                    OrderFragment.FINISHED = true;
            }
        }else if (i == 3){
            holder.RatingButton.setVisibility(View.VISIBLE);
            holder.delegateLayout.setVisibility(View.GONE);
            holder.RatingButton.setText(getStatus(status));
            holder.progressBar2.setProgress(100);
            holder.iconPreparing.setImageResource(R.drawable.ic_check_black_check);
            holder.iconPreparing.setBackground(context.getResources().getDrawable(R.drawable.style_check));
            holder.finishIcon.setBackground(context.getResources().getDrawable(R.drawable.style_normal));
            holder.finishIcon.setImageResource(R.drawable.ic_check_black_normal);
            if (list.size() == 0){
                OrderFragment.PREPARING = true;
                OrderFragment.FINISHED = false;
            }else if (list.size() -1 == position) {
                OrderFragment.PREPARING = true;
                OrderFragment.FINISHED = false;
            }
        }
    }

    private String getStatus(String status) {
        if (status.equals("accepted"))
            return context.getResources().getString(R.string.Accepted);
        else if (status.equals("prepared"))
            return context.getResources().getString(R.string.Preparing);
        else if (status.equals("delivering"))
            return context.getResources().getString(R.string.Delivering);
        else if (status.equals("delivered"))
            return context.getResources().getString(R.string.finished);
        else
            return status;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForOrders extends RecyclerView.ViewHolder {
        ProgressBar progressBar,progressBar2;
        ImageView iconP,iconPreparing,finishIcon;
        TextView RatingButton,AllDetailsText;
        ConstraintLayout delegateLayout;
        public ViewHolderForOrders(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            progressBar2 = itemView.findViewById(R.id.progressBar2);
            iconP = itemView.findViewById(R.id.iconP);
            iconPreparing = itemView.findViewById(R.id.iconPreparing);
            finishIcon = itemView.findViewById(R.id.finishIcon);
            RatingButton = itemView.findViewById(R.id.RatingButton);
            AllDetailsText = itemView.findViewById(R.id.AllDetailsText);
            delegateLayout = itemView.findViewById(R.id.delegateLayout);
        }
    }

    public void allList(List<OrderRaw> list,int type){
        for (OrderRaw raw : list)
            this.list.add(raw);
        this.i = type;
    }
}
