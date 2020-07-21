package com.dopave.diethub_vendor.UI.Fragments.Orders;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Orders.Orders;

public class OrdersViewModel extends ViewModel {
    public OrdersRepository repository;

    public OrdersViewModel() {
        this.repository = OrdersRepository.getInstance();
    }

    public LiveData<Orders> getAllOrders (String [] Status, OrdersViewModel viewModel,
                                          final ProgressDialog dialog,
                                          final Context context,int limit, int skip,int typeRequest){
        return repository.getAllOrders(Status, viewModel, dialog, context,limit,skip,typeRequest);
    }
}
