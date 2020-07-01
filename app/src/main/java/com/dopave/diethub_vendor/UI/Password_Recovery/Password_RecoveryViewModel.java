package com.dopave.diethub_vendor.UI.Password_Recovery;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;

public class Password_RecoveryViewModel extends ViewModel {
    public Password_RecoveryRepository repository;

    public Password_RecoveryViewModel() {
        this.repository = Password_RecoveryRepository.getInstance();
    }

    public LiveData<ResetPassword> sendCodeToEmail(String Email ,
                                                   final ProgressDialog dialog,
                                                   final Context context,
                                                   Password_RecoveryViewModel viewModel){
        return repository.sendCodeToEmail(Email, dialog, context, viewModel);
    }
}
