package com.dopave.diethub_vendor.UI.TimeWork;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;

public class TimeWorkViewModel extends ViewModel {
    TimeWorkRepository repository;

    public TimeWorkViewModel() {
        this.repository = TimeWorkRepository.getInstance();
    }

    public LiveData<TimeWorks> getTimeWork(final Context context, final ProgressDialog dialog){
        return repository.getTimeWork(context, dialog);
    }
}
