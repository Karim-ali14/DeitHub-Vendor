package com.dopave.diethub_vendor.UI.Login;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;

public class Login_ViewModel extends ViewModel {
    Login_Repository repository;

    public Login_ViewModel() {
        this.repository = Login_Repository.getInstance();
    }

    public LiveData<ProviderInformation> onSignIn(String Phone , String Pass ,
                                                  final Context context ,
                                                  final ProgressDialog dialog, Login_ViewModel viewModel){
        return repository.SignIn(Phone,Pass,context,dialog,viewModel);
    }
}
