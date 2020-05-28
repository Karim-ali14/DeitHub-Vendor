package com.dopave.diethub_vendor.Common;

import com.dopave.diethub_vendor.API.APIRequest;
import com.dopave.diethub_vendor.API.RetrofitClient;

public class Common {
    public static final String BaseUrl = "https://api.diethub.martstations.com/";

    // TODO get APIRequest
    public static APIRequest getAPIRequest(){
        return RetrofitClient.getRetrofitClient().create(APIRequest.class);
    }
}
