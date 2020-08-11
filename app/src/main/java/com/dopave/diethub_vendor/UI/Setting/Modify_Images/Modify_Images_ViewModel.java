package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderIMages.Image;

import java.util.List;

public class Modify_Images_ViewModel extends ViewModel {
    Modify_ImagesRepository repository;

    public Modify_Images_ViewModel() {
        this.repository = Modify_ImagesRepository.getInstance();
    }

    public LiveData<Defualt> updateImages(final Context context, final ProgressDialog dialog ,
                                          String mainimage, List<Image> list){
        return repository.updateImages(context, dialog, mainimage, list);
    }
}
