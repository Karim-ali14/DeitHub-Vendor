package com.dopave.diethub_vendor.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.Notifications.Datum;
import com.dopave.diethub_vendor.Models.Notifications.NotificationData;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Notifications.Notification_Activity;
import com.dopave.diethub_vendor.UI.Notifications.NotificationsViewModel;

import java.util.List;

public class AdapterForNotification extends RecyclerSwipeAdapter<AdapterForNotification.ViewHolderForNotification> {
    List<Datum> list;
    Context context;
    NotificationsViewModel viewModel;
    ProgressDialog dialog;

    public AdapterForNotification(List<Datum> list, Context context, NotificationsViewModel viewModel, ProgressDialog dialog) {
        this.list = list;
        this.context = context;
        this.viewModel = viewModel;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public ViewHolderForNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderForNotification(LayoutInflater.from(context)
                .inflate(R.layout.model_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForNotification holder, final int position) {
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                viewModel.deleteSpecificNotify(context,dialog,
                        list.get(position).getId().toString())
                        .observe((LifecycleOwner) context, new Observer<Defualt>() {
                    @Override
                    public void onChanged(Defualt data) {
                        viewModel.getAllNotifies(context,dialog,viewModel,1)
                                .observe((LifecycleOwner) context, new Observer<NotificationData>() {
                            @Override
                            public void onChanged(NotificationData data) {
                                ((Notification_Activity)context).onGetNotifiesData(data,"delete");
                            }
                        });
                    }
                });
            }
        });
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ViewHolderForNotification extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        ImageButton deleteButton;

        public ViewHolderForNotification(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
