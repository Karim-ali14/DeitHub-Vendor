package com.dopave.diethub_vendor.UI.Notifications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForNotification;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.Notifications.NotificationData;
import com.dopave.diethub_vendor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notification_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView DeleteAllNotifies;
    NotificationsViewModel viewModel;
    ProgressDialog dialog;
    NotificationData notificationData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_);
        init();
    }

    private void init(){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        viewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        DeleteAllNotifies = findViewById(R.id.DeleteAllNotifies);
        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getNotifiesData("get");
        deleteAllNotifies();
    }

    private void deleteAllNotifies()
    {
            DeleteAllNotifies.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (notificationData != null) {
                        if (notificationData.getData().size() != 0) {
                            viewModel.deleteAllNotifies(Notification_Activity.this, dialog)
                                    .observe(Notification_Activity.this, new Observer<Defualt>() {
                                        @Override
                                        public void onChanged(Defualt data) {
                                            getNotifiesData("delete");
                                        }
                                    });
                        } else {
                            Toast.makeText(Notification_Activity.this, R.string.no_notifies,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Notification_Activity.this, R.string.no_notifies,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void getNotifiesData(final String type) {
        viewModel.getAllNotifies(this,dialog,viewModel,0).observe(this,
                new Observer<NotificationData>() {
                    @Override
                    public void onChanged(NotificationData notificationData) {
                        onGetNotifiesData(notificationData,type);
                    }
                });
    }


    public void BackButton(View view) {
        finish();
    }

    public void onGetNotifiesData(NotificationData data,String type){
        notificationData = data;
        if (type.equals("delete")){
            recyclerView.setAdapter(new AdapterForNotification(data.getData()
                    ,Notification_Activity.this,viewModel,dialog));
            Toast.makeText(Notification_Activity.this, R.string.delete_notifies,
                    Toast.LENGTH_SHORT).show();
        }
        if (data.getData().size() != 0){
            recyclerView.setAdapter(new AdapterForNotification(data.getData()
                    ,Notification_Activity.this,viewModel,dialog));
        }else {
            recyclerView.setAdapter(new AdapterForNotification(data.getData()
                    ,Notification_Activity.this,viewModel,dialog));
            Toast.makeText(Notification_Activity.this, R.string.no_notifies,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
