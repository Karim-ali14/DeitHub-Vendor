package com.dopave.diethub_vendor.UI.Setting.TimeWork;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetTimeWork.Data;
import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;

public class TimeWorkViewModel extends ViewModel {
    TimeWorkRepository repository;

    public TimeWorkViewModel() {
        this.repository = TimeWorkRepository.getInstance();
    }

    public LiveData<TimeWorks> getTimeWork(final Context context, final ProgressDialog dialog,
                                           TimeWorkViewModel viewModel){
        return repository.getTimeWork(context, dialog, viewModel);
    }

    public LiveData<Defualt> updateWorkTime(Data data, final Context context,
                                            final ProgressDialog dialog){
        return repository.updateWorkTime(data, context, dialog);
    }
}
