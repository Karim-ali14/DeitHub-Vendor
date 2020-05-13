package com.dopave.diethub_vendor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DelegateFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout AddDelegateLayout;
    public DelegateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delegate, container, false);
        recyclerView = view.findViewById(R.id.recyclerForDelegate);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new AdapterForDelegate(getData(),getContext()));
        AddDelegateLayout = view.findViewById(R.id.AddDelegateLayout);
        AddDelegateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Add_DelegateActivity.class));
            }
        });
        return view;
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
}
