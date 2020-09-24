package com.dopave.diethub_vendor.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Models.Orders.Detail;
import com.dopave.diethub_vendor.Models.Orders.Option;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.SharedPref;

import java.util.List;
import java.util.Locale;

public class AdapterForMeals extends RecyclerView.Adapter<AdapterForMeals.ViewHolderForMeals> {
    List<Detail> list;
    Context context;
    SharedPref pref;
    public AdapterForMeals(List<Detail> list, Context context,SharedPref pref) {
        this.list = list;
        this.context = context;
        this.pref = pref;
    }

    @NonNull
    @Override
    public ViewHolderForMeals onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForMeals(LayoutInflater.from(context)
                .inflate(R.layout.model_of_meal,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForMeals holder, int position) {
        Detail detail = list.get(position);
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                setDataInArabic(detail,holder);
            }else if (pref.getLagu().equals("en")) {
                setDataInEnglish(detail,holder);
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English")) {
                setDataInEnglish(detail,holder);
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                setDataInArabic(detail,holder);
            }
        }

        if (detail.getUnitPricePiastre() != null)
           holder.Price.setText(detail.getUnitPricePiastre()+" "+context.getResources().getString(R.string.SAR));

        if (detail.getQuantity() != null)
            holder.Number.setText(detail.getQuantity()+"");

        if ((list.size() - 1) == position)
            holder.underLine.setVisibility(View.GONE);
        else
            holder.underLine.setVisibility(View.VISIBLE);
    }

    private void setDataInArabic(Detail detail,ViewHolderForMeals holder){
        Log.i("TTTTTTTTTTTT","dkjashcajk");
        if (detail.getItem().getName() != null) {
            Log.i("TTTTTTTTTTTT",detail.getItem().getName());
            holder.NameOfMeal.setText(detail.getItem().getName());
        }
        else if (detail.getItem().getNameEn() != null) {
            Log.i("TTTTTTTTTTTT",detail.getItem().getNameEn());
            holder.NameOfMeal.setText(detail.getItem().getNameEn());
        }
        else {
            Log.i("TTTTTTTTTTTT","null");
        }

        if (detail.getOptions().size()!=0){
            holder.DecOfMeal.setVisibility(View.VISIBLE);
            String adds = "";
            for (Option option : detail.getOptions()) {
                if (option.getOptions().getName() != null) {
                    adds += option.getOptions().getName() + ",";
                } else if (option.getOptions().getNameEn() != null) {
                    adds += option.getOptions().getNameEn() + ",";
                }

            }
            holder.DecOfMeal.setText(adds);
        }else {
            holder.DecOfMeal.setText(R.string.no_adds);
        }

    }
    private void setDataInEnglish(Detail detail,ViewHolderForMeals holder){

        if (detail.getItem().getNameEn() != null)
            holder.NameOfMeal.setText(detail.getItem().getNameEn());
        else if (detail.getItem().getName() != null)
            holder.NameOfMeal.setText(detail.getItem().getName());

        if (detail.getOptions().size() != 0){
            holder.DecOfMeal.setVisibility(View.VISIBLE);
            String adds = "";
            for (Option option : detail.getOptions()) {
                if (option.getOptions().getNameEn() != null)
                    adds += option.getOptions().getNameEn() + " , ";
                else if (option.getOptions().getName() != null)
                    adds += option.getOptions().getName() + " , ";
            }
            holder.DecOfMeal.setText(adds);
        }else {
            holder.DecOfMeal.setText(R.string.no_adds);
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolderForMeals extends RecyclerView.ViewHolder{
        TextView NameOfMeal,DecOfMeal,Number,Price;
        View underLine;
        public ViewHolderForMeals(@NonNull View itemView) {
            super(itemView);
            NameOfMeal = itemView.findViewById(R.id.NameOfMeal);
            DecOfMeal = itemView.findViewById(R.id.DecOfMeal);
            Number = itemView.findViewById(R.id.Number);
            Price = itemView.findViewById(R.id.Price);
            underLine = itemView.findViewById(R.id.underLine);
        }
    }

}
