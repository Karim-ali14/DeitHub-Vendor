package com.dopave.diethub_vendor.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.dopave.diethub_vendor.UI.SharedPref;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class Common {
    public static final String BaseUrl = "https://api.diethub.martstations.com/";

    public static SignIn currentPosition = null;

    public static String FileName = "TokenFile";
    public static String Token = "Token";

    public static SharedPreferences preferences;

    public static SharedPreferences getPreferences(Context context) {
        if (preferences == null)
            return preferences = context.getSharedPreferences(Common.FileName,MODE_PRIVATE);
        return preferences;
    }

    // TODO get APIRequest
    public static APIRequest getAPIRequest(){
        return RetrofitClient.getRetrofitClient().create(APIRequest.class);
    }

    public static String knowLang(Context context) {
        SharedPref pref = new SharedPref(context);
        if (!pref.getLagu().equals("empty")) {
            if (pref.getLagu().equals("ar")) {
                return "ar";
            }else if (pref.getLagu().equals("en")) {
                return "en";
            }
        }
        else {
            if (Locale.getDefault().getDisplayLanguage().equals("English"))
            {
                return "en";
            }else if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
                return "ar";
            }
        }
        return "";
    }
}
