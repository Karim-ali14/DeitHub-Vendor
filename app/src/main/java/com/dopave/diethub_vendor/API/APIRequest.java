package com.dopave.diethub_vendor.API;

import com.dopave.diethub_vendor.Models.SignIn.SignIn;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIRequest {
    @FormUrlEncoded
    @POST("provider/signin")
    Call<SignIn> SignIn (@Field("mobilePhone") String mobilePhone
            ,@Field("password") String password);
}
