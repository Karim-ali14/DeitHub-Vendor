package com.dopave.diethub_vendor.API;

import com.dopave.diethub_vendor.Models.DeliveryByProvider.DeliveryByProvider;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.DeliveryByProviderRequest;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.UpdateDeliveryRequest;
import com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery.GetDeliveryByProviderId;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIRequest {
    //Todo Sign in
    @FormUrlEncoded
    @POST("provider/signin")
    Call<SignIn> signIn (@Field("mobilePhone") String mobilePhone
            ,@Field("password") String password);

    //Todo forgot password
    @FormUrlEncoded
    @POST("provider/forgot-password")
    Call<ResetPassword> sendCode (@Field("email") String email);

    //Todo forgot password
    @FormUrlEncoded
    @POST("provider/reset-password")
    Call<ResetPassword> reset_password (@Field("email") String email,
                                        @Field("passwordResetToken") String passwordResetToken,
                                        @Field("password") String password);


    //Todo create Delivery by provider id
    @POST("provider/{id}/deliveryrep")
    Call<DeliveryByProvider> createDeliveryByProvider (@Header("Authorization") String Auth,
                                                       @Body DeliveryByProviderRequest requestBody,
                                                       @Path("id") String id);

    //Todo update Delivery by provider id
    @PUT("provider/{id}/deliveryrep/{deliveryId}")
    Call<DeliveryByProvider> updateDeliveryByProvider (@Header("Authorization") String Auth,
                                                       @Body UpdateDeliveryRequest requestBody,
                                                       @Path("id") String id,
                                                       @Path("deliveryId") String deliveryId);

    //Todo delete Delivery by provider id
    @DELETE("provider/{id}/deliveryrep/{deliveryId}")
    Call<DeliveryByProvider> deleteDeliveryByProvider (@Header("Authorization") String Auth,
                                                       @Path("id") String id,
                                                       @Path("deliveryId") String deliveryId);

    //Todo get all Delivery by provider id
    @GET("provider/{id}/deliveryrep")
    Call<GetDeliveryByProviderId> getDeliveryByProvider (@Header("Authorization") String Auth,
                                                         @Path("id") String id);

}
