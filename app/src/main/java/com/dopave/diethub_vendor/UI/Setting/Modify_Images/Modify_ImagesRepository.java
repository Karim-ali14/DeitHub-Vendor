package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.dopave.diethub_vendor.Models.UpdateVehicle.DeleteImageFromList;
import com.dopave.diethub_vendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
                                if (response.code() >= 500){
                                    Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                                }else if (response.code() == 401){
                                    Common.onCheckTokenAction(context);
                                }else {
                                    try {
                                        String message = new JSONObject(response.errorBody().string())
                                                .getString("message");
                                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
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

    public MutableLiveData<Defualt> updateImages(final Context context, final ProgressDialog dialog,
                                                 File mainImageFile, final List<File> list){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();

        MultipartBody.Part mainImage = null;
        List<MultipartBody.Part> Images = new ArrayList<>();
        try {
            if (mainImageFile != null) {
                RequestBody mainImageFileRequest = RequestBody.create(MediaType.parse("multipart/form-data"), mainImageFile);
                mainImage = MultipartBody.Part.createFormData("main_image", URLEncoder.encode(mainImageFile.getName(), "utf-8"), mainImageFileRequest); // image
            }
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Images.add(MultipartBody.Part.createFormData("images", URLEncoder.encode(list.get(i).getName(), "utf-8"),
                            RequestBody.create(MediaType.parse("multipart/form-data"), list.get(i))));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Common.getAPIRequest().updateImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                mainImage,Images).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                dialog.dismiss();
                Log.i("ImageProPP",response.code()+"");
                if (response.code() == 200) {
                    mutableLiveData.setValue(response.body());
                } else {
                    if (response.code() >= 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 401){
                        Common.onCheckTokenAction(context);
                    }else {
                        try {
                            Log.i("ImageProPP",response.code()+"");
                            JSONArray errors = new JSONObject(response.errorBody().string()).getJSONArray("errors");
                            Log.i("ImageProPP",errors+"");
                            String message = new JSONObject(response.errorBody().string())
                                    .getString("message");
                            Log.i("ImageProPP",errors+""+message+"   "+response.message());
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Defualt> call, Throwable t) {
                dialog.dismiss();
                Log.i("ImageProPP",t.getMessage());
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

    public MutableLiveData<Defualt> deleteImage(final Context context, final ProgressDialog dialog,
                                                List<Integer> list){
        dialog.show();
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();

        Common.getAPIRequest().deleteSettingImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                new DeleteImageFromList(list)).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
                dialog.dismiss();

                if (response.code() == 200) {
                    mutableLiveData.setValue(response.body());
                } else {
                    if (response.code() >= 500){
                        Toast.makeText(context, R.string.Server_problem, Toast.LENGTH_SHORT).show();
                    }else if (response.code() == 401){
                        Common.onCheckTokenAction(context);
                    }else {
                        try {
                            Log.i("ImageProPP",response.code()+"");
                            JSONArray errors = new JSONObject(response.errorBody().string()).getJSONArray("errors");
                            Log.i("ImageProPP",errors+"");
                            String message = new JSONObject(response.errorBody().string())
                                    .getString("message");
                            Log.i("ImageProPP",errors+""+message+"   "+response.message());
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Defualt> call, Throwable t) {
                dialog.dismiss();
                Log.i("ImageProPP",t.getMessage());
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
