package com.dopave.diethub_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopave.diethub_vendor.Adapter.AdapterForOrder;

import java.util.ArrayList;
import java.util.List;

public class Orders_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    public static int conut = 0;
    public static int conut2 = 0;
    public static boolean b1 = false;
    public static boolean b2 = false;
    public static boolean b3 = false;
    LinearLayout ProcessedLayout , DProgressLayout , ProcessingLayout;
    TextView ProcessedText , DProgressText , ProcessingText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_);
        init();
    }

    private void init (){
        getWindow().getDecorView().setSystemUiVisibility
                (View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AdapterForOrder(getData(),this,0));

        ProcessedLayout = findViewById(R.id.ProcessedLayout);
        DProgressLayout = findViewById(R.id.DProgressLayout);
        ProcessingLayout = findViewById(R.id.ProcessingLayout);

        ProcessedText = findViewById(R.id.ProcessedText);
        DProgressText = findViewById(R.id.DProgressText);
        ProcessingText = findViewById(R.id.ProcessingText);
    }
    private List<String> getData(){
        List<String> list = new ArrayList<>();
        list.clear();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        return list;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.ProcessedLayout) {
            recyclerView.setAdapter(new AdapterForOrder(getData(), this, 0));

            ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            ProcessedText.setTextColor(getResources().getColor(R.color.white));

            DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            DProgressText.setTextColor(getResources().getColor(R.color.colorPrimary));

            ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ProcessingText.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else if (view.getId() == R.id.DProgressLayout) {
            ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ProcessedText.setTextColor(getResources().getColor(R.color.colorPrimary));

            DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            DProgressText.setTextColor(getResources().getColor(R.color.white));

            ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ProcessingText.setTextColor(getResources().getColor(R.color.colorPrimary));


            if (b3)
                recyclerView.setAdapter(new AdapterForOrder(getData(), this, 3));
            else if (!b2)
                recyclerView.setAdapter(new AdapterForOrder(getData(), this, 1));
        }else if (view.getId() == R.id.ProcessingLayout) {
            ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            ProcessedText.setTextColor(getResources().getColor(R.color.colorPrimary));

            DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
            DProgressText.setTextColor(getResources().getColor(R.color.colorPrimary));

            ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
            ProcessingText.setTextColor(getResources().getColor(R.color.white));

            if (!b3)
                recyclerView.setAdapter(new AdapterForOrder(getData(), this, 2));
        }
    }

    public void closeKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
}
