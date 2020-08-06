package com.dopave.diethub_vendor.UI.Notifications;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.Notifications.NotificationData;

public class NotificationsViewModel extends ViewModel {
    NotificationsRepository repository;

    public NotificationsViewModel() {
        this.repository = NotificationsRepository.getInstance();
    }

    public LiveData<NotificationData> getAllNotifies(final Context context,
                                                     final ProgressDialog dialog,
                                                     NotificationsViewModel viewModel,int type){
        return repository.getAllNotifies(context, dialog, viewModel,type);
    }

    public LiveData<Defualt> deleteSpecificNotify(final Context context,
                                                  final ProgressDialog dialog,
                                                  String NotifyId){
        return repository.deleteSpecificNotify(context, dialog, NotifyId);
    }

    public LiveData<Defualt> deleteAllNotifies(final Context context,
                                                        final ProgressDialog dialog){
        return repository.deleteAllNotifies(context, dialog);
    }
}
