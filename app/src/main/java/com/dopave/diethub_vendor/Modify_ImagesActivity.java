package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.dopave.diethub_vendor.Adapter.AdapterForResImage;
import com.dopave.diethub_vendor.Models.GetVehicles.Image;

import java.util.ArrayList;
import java.util.List;

public class Modify_ImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify__images);
        getInitchild1();
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
    private void getInitchild1() {
        RecyclerView recyclerView = findViewById(R.id.Recycler_Res_Icons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(new AdapterForResImage(getData(), this,null,"update",getData().size(),recyclerView));
    }

    private List<Image> getData() {
        List<Image> list = new ArrayList<>();
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        list.add(new Image(BitmapFactory.decodeResource(this.getResources(),R.drawable.gg)));
        return list;
    }

    public void BackButton(View view) {
        finish();
    }
}
