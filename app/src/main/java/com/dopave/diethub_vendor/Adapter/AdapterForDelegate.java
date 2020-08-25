package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.GetDeliveries.DeliveryRow;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.VehicleTypes.RowVehicleTypes;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;
import com.dopave.diethub_vendor.UI.CreateVehicle.CreateVehicleActivity;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryViewModel;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterForDelegate extends RecyclerView.Adapter<AdapterForDelegate.ViewHolderForDelegate> {
    List<DeliveryRow> list;
    Context context;
    RecyclerView recyclerView;
    DeliveryViewModel viewModel;
    VehicleTypes vehicleTypes;

    public AdapterForDelegate(List<DeliveryRow> list, Context context, RecyclerView recyclerView,
                              DeliveryViewModel viewModel, VehicleTypes vehicleTypes) {
        this.list = list;
        this.context = context;
        this.recyclerView = recyclerView;
        this.viewModel = viewModel;
        this.vehicleTypes = vehicleTypes;
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
        final DeliveryRow row = list.get(position);
        holder.NameOfDelegate.setText(row.getName());
        holder.MenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_modify_delivery :
                                updateDelivery(row);
                                return true;
                                case R.id.action_modify_vehicle :{
                                    if (row.getVehicle() == null){
                                        showDialog(row);
                                        return true;
                                    }else {
                                        updateVehicle(row.getId());
                                        return true;
                                    }
                                }
                            case R.id.action_delete:
                                delete(row);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(R.menu.menu_delivery);
                popupMenu.show();
            }
        });
        if (row.getImage() != null){
            String path = Common.BaseUrl + "images/" + row.getImage().getFor() + "/" + Uri.encode(row.getImage().getName());
            Picasso.with(context).load(path).into(holder.IconOfDelegate);
        }

        if (row.getVehicle() != null){
            for (RowVehicleTypes rowVehicleTypes : vehicleTypes.getData().getRowVehicleTypes()) {
                if (row.getVehicle().getVehicleTypeId() == rowVehicleTypes.getId()) {

                    if (Common.knowLang(context).equals("ar"))
                        holder.NameOfVehicle.setText(rowVehicleTypes.getType());
                    else if (Common.knowLang(context).equals("en"))
                        holder.NameOfVehicle.setText(rowVehicleTypes.getTypeEn());

                }
            }
            holder.ModelOfVehicle.setText(", "+row.getVehicle().getModel()+" ,");
            holder.YearOfVehicle.setText(row.getVehicle().getYear()+" ,");
            holder.NumberOfVehicle.setText(row.getVehicle().getNumber());
            holder.NameOfVehicle.setVisibility(View.VISIBLE);
            holder.ModelOfVehicle.setVisibility(View.VISIBLE);
            holder.YearOfVehicle.setVisibility(View.VISIBLE);
        }else {
            holder.NameOfVehicle.setVisibility(View.GONE);
            holder.YearOfVehicle.setVisibility(View.GONE);
            holder.ModelOfVehicle.setVisibility(View.GONE);
        }

        if (row.getCity() != null){
            if (Common.knowLang(context).equals("ar"))
                holder.AddressOfDelegate.setText(row.getCity().getName());
            else if (Common.knowLang(context).equals("en"))
                holder.AddressOfDelegate.setText(row.getCity().getNameEn());
        }
    }

    private void showDialog(final DeliveryRow row) {
        final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_registraion_success, null);
        ImageView Icon = view.findViewById(R.id.Icon);
        Icon.setVisibility(View.GONE);
        TextView Massage = view.findViewById(R.id.Massage);
        Massage.setText(context.getResources()
                .getString(R.string.donthaveVehicle));
        Button createVehicleButton = view.findViewById(R.id.LoginButton);
        createVehicleButton.setText(context.getResources()
                .getString(R.string.createVehicle));
        Adialog.setView(view);
        final AlertDialog dialog1 = Adialog.create();
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setCancelable(false);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        createVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tttttt",row.getId()+"");
                context.startActivity(new Intent(context, CreateVehicleActivity.class)
                        .putExtra("type","create").putExtra("deliveryId",row.getId()+""));
                dialog1.dismiss();
            }
        });
    }

    private void updateDelivery(DeliveryRow row) {
        context.startActivity(new Intent(context, CreateDeliveryActivity.class).putExtra("type","update")
                .putExtra("delivery",row));
    }

    private void updateVehicle(Integer id) {
        context.startActivity(new Intent(context, CreateVehicleActivity.class).putExtra("type","update")
                .putExtra("deliveryId",id+""));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForDelegate extends RecyclerView.ViewHolder{
        CircleImageView IconOfDelegate;
        TextView NameOfDelegate,AddressOfDelegate,NameOfVehicle,ModelOfVehicle,YearOfVehicle,NumberOfVehicle;
        LinearLayout MenuLayout,Vehicle_DetailsLayout;
        public ViewHolderForDelegate(@NonNull View itemView) {
            super(itemView);
            IconOfDelegate = itemView.findViewById(R.id.IconOfDelegate);
            NameOfDelegate = itemView.findViewById(R.id.NameOfDelegate);
            AddressOfDelegate = itemView.findViewById(R.id.AddressOfDelegate);
            NameOfVehicle = itemView.findViewById(R.id.NameOfVehicle);
            ModelOfVehicle = itemView.findViewById(R.id.ModelOfVehicle);
            YearOfVehicle = itemView.findViewById(R.id.YearOfVehicle);
            NumberOfVehicle = itemView.findViewById(R.id.NumberOfVehicle);
            MenuLayout = itemView.findViewById(R.id.MenuLayout);
            Vehicle_DetailsLayout = itemView.findViewById(R.id.Vehicle_DetailsLayout);
        }
    }

    private void delete(DeliveryRow row){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        viewModel.deleteDelivery(row.getId()+"",dialog,context).
                observe((LifecycleOwner) context, new Observer<GetDeliveriesData>() {
            @Override
            public void onChanged(GetDeliveriesData getDeliveriesData) {
                viewModel.getAllDeliveries(dialog,context,viewModel,recyclerView,vehicleTypes).observe((LifecycleOwner) context,
                        new Observer<GetDeliveriesData>() {
                            @Override
                            public void onChanged(GetDeliveriesData getDeliveriesData) {
                                dialog.dismiss();
                                if (getDeliveriesData.getData().getDeliveryRows().size() != 0) {
                                    recyclerView.setAdapter(new AdapterForDelegate(getDeliveriesData.getData().getDeliveryRows(),
                                            context, recyclerView,viewModel,vehicleTypes));
                                }
                                else {
                                    recyclerView.setAdapter(new AdapterForDelegate(getDeliveriesData.getData().getDeliveryRows(),
                                            context, recyclerView,viewModel,vehicleTypes));
                                    Toast.makeText(context,
                                            context.getResources().getString(R.string.noDeliveries)
                                            , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void allList(List<DeliveryRow> list){
        for (DeliveryRow row : list)
            this.list.add(row);
    }
}
