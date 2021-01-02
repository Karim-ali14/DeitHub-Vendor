package com.dopave.diethub_vendor.UI.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Fragments.Deliveries.DeliveryFragment;
import com.dopave.diethub_vendor.UI.Fragments.Orders.OrderFragment;
import com.dopave.diethub_vendor.UI.HomeActivity;
import com.dopave.diethub_vendor.UI.SharedPref;
import com.dopave.diethub_vendor.UI.Subscriptions.SubscriptionsActivity;
import com.squareup.picasso.Picasso;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_Fragment extends Fragment {
    LinearLayout Orders_Layout,Delegate_Layout,Setting_Layout,SubscriptionOrders_Layout,Subscription_Layout;
    TextView NameOfRestaurants;
    ImageView ProviderIcon;
    SharedPref pref;

    public Home_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pref = new SharedPref(getActivity());
        NameOfRestaurants = view.findViewById(R.id.NameOfRestaurants);
        Orders_Layout = view.findViewById(R.id.Orders_Layout);
        Delegate_Layout = view.findViewById(R.id.Delegate_Layout);
        Setting_Layout = view.findViewById(R.id.Setting_Layout);
        SubscriptionOrders_Layout = view.findViewById(R.id.SubscriptionOrders_Layout);
        Subscription_Layout = view.findViewById(R.id.Subscription_Layout);
        ProviderIcon = view.findViewById(R.id.ProviderIcon);
        setProviderData();
        Orders_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,new OrderFragment(0)).commit();
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
        Subscription_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SubscriptionsActivity.class)
                        .putExtra("type","nav_Subscription"));
            }
        });
        SubscriptionOrders_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SubscriptionsActivity.class)
                        .putExtra("type","nav_mySubscriptionOrders"));
            }
        });
        return view;
    }

    private void setProviderData() {
        if (Common.currentPosition.getData().getProvider().getMainImage() != null) {
            String path = Common.BaseUrlForImages +
                    Common.currentPosition.getData().getProvider().getMainImage().getFor()
                    + "/" + Uri.encode(Common.currentPosition.getData().getProvider().getMainImage().getName());
            Picasso.with(getActivity()).load(path).into(ProviderIcon);
        }
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                if (Common.currentPosition.getData().getProvider().getName() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getName());
                else if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else
                    NameOfRestaurants.setText("--------");
            }else if (pref.getLagu().equals("en")) {
                if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else if (Common.currentPosition.getData().getProvider().getName() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getName());
                else
                    NameOfRestaurants.setText("--------");
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else if (Common.currentPosition.getData().getProvider().getName() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getName());
                else
                    NameOfRestaurants.setText("--------");
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                if (Common.currentPosition.getData().getProvider().getName() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getName());
                else if (Common.currentPosition.getData().getProvider().getNameEn() != null)
                    NameOfRestaurants.setText(Common.currentPosition.getData().getProvider().getNameEn());
                else
                    NameOfRestaurants.setText("--------");
            }
        }
    }


}
