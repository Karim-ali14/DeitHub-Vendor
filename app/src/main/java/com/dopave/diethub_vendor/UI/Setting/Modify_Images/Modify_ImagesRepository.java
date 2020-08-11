package com.dopave.diethub_vendor.UI.Setting.Modify_Images;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.dopave.diethub_vendor.Common.Common;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.ProviderIMages.Image;
import com.dopave.diethub_vendor.Models.ProviderIMages.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderIMages.MainImage;
import com.dopave.diethub_vendor.R;

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


    public MutableLiveData<Defualt> updateImages(final Context context, final ProgressDialog dialog ,
                                                 String mainimage, List<Image> list){
        final MutableLiveData<Defualt> mutableLiveData = new MutableLiveData<>();
        Common.getAPIRequest().updateImages("Bearer "+
                        Common.currentPosition.getData().getToken().getAccessToken(),
                Common.currentPosition.getData().getProvider().getId()+"",
                new ImagesProvider(new MainImage(mainimage),list)).enqueue(new Callback<Defualt>() {
            @Override
            public void onResponse(Call<Defualt> call, Response<Defualt> response) {
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
