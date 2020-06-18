package com.dopave.diethub_vendor.UI.Fragments.Deliveries;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dopave.diethub_vendor.Adapter.AdapterForDelegate;
import com.dopave.diethub_vendor.Common.Common;

import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.CreateDelivery.CreateDeliveryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayout AddDelegateLayout;
    DeliveryViewModel viewModel;
    public DeliveryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);
        ProgressDialog dialog = new ProgressDialog(getActivity());
        viewModel = ViewModelProviders.of(this).get(DeliveryViewModel.class);
        recyclerView = view.findViewById(R.id.recyclerForDelegate);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AddDelegateLayout = view.findViewById(R.id.AddDelegateLayout);
        AddDelegateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateDeliveryActivity.class).putExtra("type","normal"));
            }
        });
        getAllDelivery(dialog);
        return view;
    }

    private void getAllDelivery(ProgressDialog dialog){
        dialog.show();
        viewModel.getAllDeliveries(dialog,getActivity()).observe(this,
                new Observer<GetDeliveriesData>() {
            @Override
            public void onChanged(GetDeliveriesData getDeliveriesData) {
                if (getDeliveriesData.getData().getDeliveryRows().size() != 0)
                    recyclerView.setAdapter(new AdapterForDelegate(getDeliveriesData.getData().getDeliveryRows(),getContext(),recyclerView));
                else
                    Toast.makeText(getActivity(), "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
