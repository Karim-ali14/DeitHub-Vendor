package com.dopave.diethub_vendor.UI.NewPassword;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;

public class NewPassword_ViewModel extends ViewModel {
    NewPassword_Repository repository;

    public NewPassword_ViewModel() {
        this.repository = NewPassword_Repository.getInstance();
    }

    public LiveData<ResetPassword> reset_password(final Context context, final ProgressDialog dialog,
                                                  String Email, String Code, String Password) {
        return repository.reset_password(context, dialog, Email, Code, Password);
    }
}
