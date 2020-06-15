package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dopave.diethub_vendor.UI.Add_DelegateActivity;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery.DeliveryRow;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery.GetDeliveryByProviderId;
import com.dopave.diethub_vendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterForDelegate extends RecyclerView.Adapter<AdapterForDelegate.ViewHolderForDelegate> {
    List<DeliveryRow> list;
    Context context;
    RecyclerView recyclerView;
    public AdapterForDelegate(List<DeliveryRow> list, Context context,RecyclerView recyclerView) {
        this.list = list;
        this.context = context;
        this.recyclerView = recyclerView;
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
                            case R.id.action_modify :
                                update(row);
                                return true;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderForDelegate extends RecyclerView.ViewHolder{
        CircleImageView IconOfDelegate;
        TextView NameOfDelegate,AddressOfDelegate,DesOfDelegate;
        LinearLayout MenuLayout;
        public ViewHolderForDelegate(@NonNull View itemView) {
            super(itemView);
            IconOfDelegate = itemView.findViewById(R.id.IconOfDelegate);
            NameOfDelegate = itemView.findViewById(R.id.NameOfDelegate);
            AddressOfDelegate = itemView.findViewById(R.id.AddressOfDelegate);
            DesOfDelegate = itemView.findViewById(R.id.DesOfDelegate);
            MenuLayout = itemView.findViewById(R.id.MenuLayout);
        }
    }

    private void delete(DeliveryRow row){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.show();
        Common.getAPIRequest().deleteDeliveryByProvider("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+""
                ,row.getId()+"").enqueue(new Callback<DeliveryByProvider>() {
            @Override
            public void onResponse(Call<DeliveryByProvider> call, Response<DeliveryByProvider> response) {
                if (response.code() == 200){
                    Toast.makeText(context, "تم المسح المندوب بنجاح", Toast.LENGTH_SHORT).show();
                    Common.getAPIRequest().getDeliveryByProvider("Bearer "+
                                    Common.currentPosition.getData().getToken().getAccessToken(),
                            Common.currentPosition.getData().getProvider().getId()+"")
                            .enqueue(new Callback<GetDeliveryByProviderId>() {
                                @Override
                                public void onResponse(Call<GetDeliveryByProviderId> call, Response<GetDeliveryByProviderId> response) {
                                    if (response.code() == 200){
                                        dialog.dismiss();
                                        if (response.body().getData().getDeliveryRows().size() != 0) {
                                            recyclerView.setAdapter(new AdapterForDelegate(response.body().getData().getDeliveryRows(), context, recyclerView));
                                        }
                                        else {
                                            recyclerView.setAdapter(new AdapterForDelegate(response.body().getData().getDeliveryRows(), context, recyclerView));
                                            Toast.makeText(context, "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        dialog.dismiss();
                                        try {
                                            Log.i("TTTTTTT",new JSONObject(response.errorBody()
                                                    .string()).getString("message")+response.code());
                                        } catch (IOException | JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetDeliveryByProviderId> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });
                }else {
                    dialog.dismiss();
                    try {
                        Log.i("TTTTTTT",new JSONObject(response.errorBody()
                                .string()).getString("message")+response.code()+"dfsfsdfs");
                        Toast.makeText(context,new JSONObject(response.errorBody()
                                .string()).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryByProvider> call, Throwable t) {
                dialog.dismiss();
            }
        });
    }

    private void update(DeliveryRow row){
        context.startActivity(new Intent(context, Add_DelegateActivity.class).putExtra("type","update")
        .putExtra("data",row));

    }
}
