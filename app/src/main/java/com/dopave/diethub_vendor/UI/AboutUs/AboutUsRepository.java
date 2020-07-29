package com.dopave.diethub_vendor.UI.AboutUs;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Settings.Settings;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Conditions.Conditions_Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutUsRepository {
    public static AboutUsRepository repository;
    public static AboutUsRepository getInstance(){
        if (repository == null)
            repository = new AboutUsRepository();
        return repository;
    }

    public MutableLiveData<Settings> getSetting(final Context context, final ProgressDialog dialog,
                                                final AboutUsViewModels viewModels, final String type){
        final MutableLiveData<Settings> mutableLiveData = new MutableLiveData<>();
        dialog.show();
        Common.getAPIRequest().getSettings().enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                dialog.dismiss();
                if (response.code() == 200)
                    mutableLiveData.setValue(response.body());
                else {
                    try {
                        String message = new JSONObject(response.errorBody().string())
                                .getString("message");
                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
                dialog.dismiss();
                final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
                View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
                TextView Title = view.findViewById(R.id.Title);
                TextView Message = view.findViewById(R.id.Message);
                Button button = view.findViewById(R.id.Again);
                Adialog.setView(view);
                final AlertDialog dialog1 = Adialog.create();
                dialog1.setCanceledOnTouchOutside(false);
                dialog1.setCancelable(false);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        dialog.show();

                        viewModels.getSetting(context, dialog, viewModels,type).observe((LifecycleOwner) context, new Observer<Settings>() {
                            @Override
                            public void onChanged(Settings settings) {
                                if (type.equals("Conditions")){
                                    ((Conditions_Activity)context).onGetDataSettings(settings);
                                }else if (type.equals("AboutUs")){
                                    ((AboutUs_Activity)context).onGetDataSettings(settings);
                                }
                            }
                        });

                    }
                });
                if(t instanceof SocketTimeoutException) {
                    Title.setText(R.string.Unable_contact_server);
                    Message.setText(R.string.Error_downloading_data);
                    dialog1.show();
                }

                else if (t instanceof UnknownHostException) {
                    Title.setText(context.getResources().getString(R.string.no_internet_connection));
                    Message.setText(R.string.Make_sure_you_online);
                    dialog1.show();
                }else {
                    Title.setText(context.getResources().getString(R.string.no_internet_connection));
                    Message.setText(R.string.Error_downloading_data);
                    dialog1.show();
                }
            }
        });
        return mutableLiveData;
    }
}
