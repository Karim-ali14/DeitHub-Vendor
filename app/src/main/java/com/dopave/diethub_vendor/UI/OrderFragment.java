package com.dopave.diethub_vendor.UI;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopave.diethub_vendor.Adapter.AdapterForOrder;
import com.dopave.diethub_vendor.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    RecyclerView recyclerView;
    public static int conut = 0;
    public static int conut2 = 0;
    public static boolean b1 = false;
    public static boolean b2 = false;
    public static boolean b3 = false;
    LinearLayout ProcessedLayout , DProgressLayout , ProcessingLayout;
    TextView ProcessedText , DProgressText , ProcessingText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_my_order, container, false);
        init(inflate);
        return inflate;
    }

    private void init (View view){

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new AdapterForOrder(getData(),getContext(),0));

        ProcessedLayout = view.findViewById(R.id.ProcessedLayout);
        DProgressLayout = view.findViewById(R.id.DProgressLayout);
        ProcessingLayout = view.findViewById(R.id.ProcessingLayout);

        ProcessedText = view.findViewById(R.id.ProcessedText);
        DProgressText = view.findViewById(R.id.DProgressText);
        ProcessingText = view.findViewById(R.id.ProcessingText);

        ProcessedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), 0));

                ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                ProcessedText.setTextColor(getResources().getColor(R.color.white));

                DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                DProgressText.setTextColor(getResources().getColor(R.color.colorPrimary));

                ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                ProcessingText.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        DProgressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                ProcessedText.setTextColor(getResources().getColor(R.color.colorPrimary));

                DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                DProgressText.setTextColor(getResources().getColor(R.color.white));

                ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                ProcessingText.setTextColor(getResources().getColor(R.color.colorPrimary));


                if (b3)
                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), 3));
                else if (!b2)
                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), 1));
            }
        });

        ProcessingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessedLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                ProcessedText.setTextColor(getResources().getColor(R.color.colorPrimary));

                DProgressLayout.setBackground(getResources().getDrawable(R.drawable.style_button_normal));
                DProgressText.setTextColor(getResources().getColor(R.color.colorPrimary));

                ProcessingLayout.setBackground(getResources().getDrawable(R.drawable.style_button_active));
                ProcessingText.setTextColor(getResources().getColor(R.color.white));

                if (!b3)
                    recyclerView.setAdapter(new AdapterForOrder(getData(), getContext(), 2));
            }
        });
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

    /*public void onClick(View view) {
        if (view.getId() == R.id.ProcessedLayout) {

        }
        else if (view.getId() == R.id.DProgressLayout) {

        }else if (view.getId() == R.id.ProcessingLayout) {

        }
    }

    public void closeKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }*/
}
