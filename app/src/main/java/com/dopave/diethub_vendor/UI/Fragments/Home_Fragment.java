package com.dopave.diethub_vendor.UI.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryFragment;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.HomeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends Fragment {
    LinearLayout Orders_Layout,Delegate_Layout,Setting_Layout;
    TextView NameOfRestaurants;
    public Home_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        NameOfRestaurants = view.findViewById(R.id.NameOfRestaurants);
        NameOfRestaurants.setText((Common.currentPosition.getData().getProvider().getName()));
        Orders_Layout = view.findViewById(R.id.Orders_Layout);
        Delegate_Layout = view.findViewById(R.id.Delegate_Layout);
        Setting_Layout = view.findViewById(R.id.Setting_Layout);
        Orders_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment()).commit();
                HomeActivity.Title.setVisibility(View.VISIBLE);
                HomeActivity.Logo.setVisibility(View.GONE);
                HomeActivity.Notification_Icon.setVisibility(View.GONE);
                HomeActivity.Title.setText(getResources().getString(R.string.Orders));
                HomeActivity.Current_Page = "nav_myOrders";
            }
        });
        Delegate_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new DeliveryFragment()).commit();
                HomeActivity.Title.setVisibility(View.VISIBLE);
                HomeActivity.Logo.setVisibility(View.GONE);
                HomeActivity.Notification_Icon.setVisibility(View.GONE);
                HomeActivity.Title.setText(getResources().getString(R.string.delegates));
                HomeActivity.Current_Page = "nav_delegates";
            }
        });
        Setting_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new SettingsFragment()).commit();
                HomeActivity.Title.setVisibility(View.VISIBLE);
                HomeActivity.Logo.setVisibility(View.GONE);
                HomeActivity.Notification_Icon.setVisibility(View.GONE);
                HomeActivity.Title.setText(getResources().getString(R.string.sittings));
                HomeActivity.Current_Page = "nav_setting";
            }
        });
        return view;
    }
}
