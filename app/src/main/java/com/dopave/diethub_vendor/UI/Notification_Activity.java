package com.dopave.diethub_vendor.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForNotification;
import com.dopave.diethub_vendor.Common.Common;
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

        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Common.getAPIRequest().getAllNotifications("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken()).enqueue(new Callback<NotificationData>() {
            @Override
            public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {
                if (response.code() == 200){
                    if (response.body().getData().size() != 0){
                        recyclerView.setAdapter(new AdapterForNotification(response.body().getData()
                                ,Notification_Activity.this));
                    }else {
                        Toast.makeText(Notification_Activity.this, R.string.no_notifies, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(Notification_Activity.this,message, Toast.LENGTH_SHORT).show();

                        Log.i("TTTTTT",new JSONObject(response.errorBody().string())
                                .getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<NotificationData> call, Throwable t) {

            }
        });
    }

    private List<String> getData() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }

    public void BackButton(View view) {
        finish();
    }
}
