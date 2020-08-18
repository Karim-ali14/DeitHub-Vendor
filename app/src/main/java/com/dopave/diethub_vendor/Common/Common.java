package com.dopave.diethub_vendor.Common;

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

import com.dopave.diethub_vendor.API.APIRequest;
import com.dopave.diethub_vendor.API.RetrofitClient;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Common {
    public static final String BaseUrl = "https://d03c4f968e68.ngrok.io/";

    // TODO get APIRequest
    public static APIRequest getAPIRequest(){
        return RetrofitClient.getRetrofitClient().create(APIRequest.class);
    }

    public static SignIn currentPosition = null;


}
