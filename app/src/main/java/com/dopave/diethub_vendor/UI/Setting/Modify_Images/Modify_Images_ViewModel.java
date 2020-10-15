package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderIMages.GetImages.ProviderImagesResponse;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.MainImage;

import java.io.File;
import java.util.List;

public class Modify_Images_ViewModel extends ViewModel {
    Modify_ImagesRepository repository;

    public Modify_Images_ViewModel() {
        this.repository = Modify_ImagesRepository.getInstance();
    }

    public LiveData<ProviderImagesResponse> getProviderImages(final ProgressDialog dialog,
                                                              final Context context,
                                                              Modify_Images_ViewModel viewModel){
        return repository.getProviderImages(dialog, context, viewModel);
    }

    public LiveData<Defualt> updateImages(final Context context, final ProgressDialog dialog,
                                          File mainImageFile, final List<File> list){
        return repository.updateImages(context, dialog, mainImageFile, list);
    }

    public LiveData<Defualt> deleteImage(final Context context, final ProgressDialog dialog,
                                         List<Integer> list){
        return repository.deleteImage(context, dialog, list);
    }
}
