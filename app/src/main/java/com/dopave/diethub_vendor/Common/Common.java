package com.dopave.diethub_vendor.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.auth0.android.jwt.JWT;
import com.dopave.diethub_vendor.API.APIRequest;
import com.dopave.diethub_vendor.API.RetrofitClient;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.R;
import com.dopave.diethub_vendor.UI.Login.Login_inActivity;
import com.dopave.diethub_vendor.UI.SharedPref;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class Common {
    public static final String BaseUrl = "https://api.diethub.martstations.com/";

    public static ProviderInformation currentPosition = null;

    public static String FileName = "TokenFile";
    public static String Token = "Token";
    public static String ProviderId = "ProviderId";

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

    public static boolean checkToken(String Token){
        JWT jwt = new JWT(Token);
        Date expiresAt = jwt.getExpiresAt();
        return new Date().before(expiresAt) || new Date().equals(expiresAt);
    }

    public static void onCheckTokenAction(final Context context){
        if (!Common.checkToken(Common.currentPosition.getData().getToken().getAccessToken())){
            final AlertDialog.Builder Adialog = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.error_dialog, null);
            TextView Title = view.findViewById(R.id.Title);
            TextView Message = view.findViewById(R.id.Message);
            Title.setText(R.string.Session_Expired);
            Message.setText(R.string.Session_Expired_Message);
            Button button = view.findViewById(R.id.Again);
            button.setText(R.string.login);
            Adialog.setView(view);
            final AlertDialog dialog1 = Adialog.create();
            dialog1.setCanceledOnTouchOutside(false);
            dialog1.setCancelable(false);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                    context.startActivity(new Intent(context, Login_inActivity.class)
                            .setFlags(FLAG_ACTIVITY_NEW_TASK|FLAG_ACTIVITY_CLEAR_TASK));
                }
            });
            dialog1.show();
        }else {
            Toast.makeText(context, R.string.Your_not_Auth, Toast.LENGTH_SHORT).show();
        }
    }

    public static void setAppLocale(String localeCode,Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
}
