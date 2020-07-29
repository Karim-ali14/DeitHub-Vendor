package com.dopave.diethub_vendor.UI.AboutUs;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.dopave.diethub_vendor.Models.Settings.Settings;

public class AboutUsViewModels extends ViewModel {
    AboutUsRepository repository;

    public AboutUsViewModels() {
        this.repository = AboutUsRepository.getInstance();
    }

    public LiveData<Settings> getSetting(Context context, ProgressDialog dialog,
                                         AboutUsViewModels viewModels, String type){
        return repository.getSetting(context, dialog, viewModels,type);
    }
}
