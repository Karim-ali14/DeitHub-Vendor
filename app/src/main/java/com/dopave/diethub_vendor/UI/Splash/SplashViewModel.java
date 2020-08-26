package com.dopave.diethub_vendor.UI.Splash;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;

public class SplashViewModel extends ViewModel {
    SplashRepository repository;

    public SplashViewModel() {
        this.repository = SplashRepository.getInstance();
    }

    public LiveData<ProviderInformation> getProviderInfo(final Context context,String token,String id) {
        return repository.getProviderInfo(context,token,id);
    }
}
