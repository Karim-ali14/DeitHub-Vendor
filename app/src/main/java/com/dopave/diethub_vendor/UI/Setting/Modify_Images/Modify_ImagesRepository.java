package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

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
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderIMages.GetImages.ProviderImagesResponse;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.MainImage;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInfo;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Setting.Modify_Personal_info.Modify_personal_infoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Modify_ImagesRepository {
    public static Modify_ImagesRepository repository;
    public static Modify_ImagesRepository getInstance(){
        if (repository == null)
            repository = new Modify_ImagesRepository();
        return repository;
    }

    public MutableLiveData<ProviderImagesResponse> getProviderImages(final ProgressDialog dialog,
                                                                     final Context context,
                                                                     final Modify_Images_ViewModel viewModel){
        final MutableLiveData<ProviderImagesResponse> mutableLiveData = new MutableLiveData<>();
        dialog.show();
        Common.getAPIRequest().getProviderImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"")
                .enqueue(new Callback<ProviderImagesResponse>() {
                    @Override
                    public void onResponse(Call<ProviderImagesResponse> call,
                                           Response<ProviderImagesResponse> response) {
                        dialog.dismiss();
                        if (response.code() == 200) {
                            mutableLiveData.setValue(response.body());
                        } else {
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
                    public void onFailure(final Call<ProviderImagesResponse> call, Throwable t) {
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
                                viewModel.getProviderImages(dialog, context, viewModel)
                                        .observe((LifecycleOwner) context, new Observer<ProviderImagesResponse>() {
                                    @Override
                                    public void onChanged(ProviderImagesResponse providerImagesResponse) {
                                        ((Modify_ImagesActivity)context).onGetProviderInfo(providerImagesResponse);
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

    public MutableLiveData<Defualt> updateImages(final Context context, final ProgressDialog dialog ,
                                                 MainImage mainImage, List<Image> list){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                new ImagesProvider(mainImage,list)).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                dialog.dismiss();
                if (response.code() == 200) {
                    mutableLiveData.setValue(response.body());
                } else {
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
            public void onFailure(Call<Defualt> call, Throwable t) {
                dialog.dismiss();

                if(t instanceof SocketTimeoutException) {
                    Toast.makeText(context, R.string.Unable_contact_server, Toast.LENGTH_SHORT).show();
                }

                else if (t instanceof UnknownHostException) {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }

                else {
                    Toast.makeText(context,R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mutableLiveData;
    }
}
