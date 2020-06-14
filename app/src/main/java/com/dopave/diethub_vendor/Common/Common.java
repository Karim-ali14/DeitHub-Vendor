package com.dopave.diethub_vendor.Common;

import com.dopave.diethub_vendor.API.APIRequest;
import com.dopave.diethub_vendor.API.RetrofitClient;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;

public class Common {
    public static final String BaseUrl = "https://api.diethub.martstations.com/";

    // TODO get APIRequest
    public static APIRequest getAPIRequest(){
        return RetrofitClient.getRetrofitClient().create(APIRequest.class);
    }

    public static SignIn currentPosition = null;
}
