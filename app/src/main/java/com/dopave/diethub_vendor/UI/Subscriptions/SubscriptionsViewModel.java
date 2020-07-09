package com.dopave.diethub_vendor.UI.Subscriptions;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;

public class SubscriptionsViewModel extends ViewModel {
    SubscriptionsRepository repository;

    public SubscriptionsViewModel() {
        this.repository = SubscriptionsRepository.getInstance();
    }

    public LiveData<Subscriptions> getAllSubscriptions(final Context context,
                                                       final ProgressDialog dialog,
                                                       final SubscriptionsViewModel viewModel,
                                                       int type,String status){
        return repository.getAllSubscriptions(context, dialog, viewModel,type,status);
    }
}
