package com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInfo;
import com.dopave.diethub_vendor.Models.ProviderInfo.Request.ProviderInfoRequest;

public class Modify_Person_info_viewModel extends ViewModel {
    Modify_Person_Info_Repository repository;

    public Modify_Person_info_viewModel() {
        this.repository = Modify_Person_Info_Repository.getInstance();
    }

    public LiveData<ProviderInfo> getProviderInfo(final Context context,
                                                  final ProgressDialog dialog){
        return repository.getProviderInfo(context, dialog);
    }

    public LiveData<Defualt> updateProvideInfo(final Context context,
                                               final ProgressDialog dialog,
                                               final ProviderInfoRequest request){
        return repository.updateProvideInfo(context, dialog, request);
    }
}
