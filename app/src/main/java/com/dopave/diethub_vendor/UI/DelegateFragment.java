package com.dopave.diethub_vendor.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery.GetDeliveryByProviderId;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Add_Delegate.Add_DelegateActivity;
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
        AddDelegateLayout = view.findViewById(R.id.AddDelegateLayout);
        AddDelegateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateDeliveryActivity.class).putExtra("type","normal"));
            }
        });
        getAllDelivery();
        return view;
    }

    private void getAllDelivery(){
        Common.getAPIRequest().getDeliveryByProvider("Bearer "+
                Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<GetDeliveryByProviderId>() {
                    @Override
                    public void onResponse(Call<GetDeliveryByProviderId> call, Response<GetDeliveryByProviderId> response) {
                        if (response.code() == 200){
                            if (response.body().getData().getDeliveryRows().size() != 0)
                                recyclerView.setAdapter(new AdapterForDelegate(response.body().getData().getDeliveryRows(),getContext(),recyclerView));
                            else
                                Toast.makeText(getActivity(), "there are't any deliveries yet", Toast.LENGTH_SHORT).show();
                        } else {
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

                    }
                });
    }
}
