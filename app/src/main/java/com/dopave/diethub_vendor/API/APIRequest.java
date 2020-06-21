package com.dopave.diethub_vendor.API;

import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.UpdateDeliveryRequest;
import com.dopave.diethub_vendor.Models.UpdateVehicle.UpdateVehicle;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

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
import retrofit2.http.Query;

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
    Call<CreateDeliveryResponse> createDeliveryByProvider (@Header("Authorization") String Auth,
                                                           @Body CreateDeliveryRequest requestBody,
                                                           @Path("id") String id);

    //Todo update Delivery by provider id
    @PUT("provider/{id}/deliveryrep/{deliveryId}")
    Call<GetDeliveriesData> updateDeliveryByProvider (@Header("Authorization") String Auth,
                                                       @Body UpdateDeliveryRequest requestBody,
                                                       @Path("id") String id,
                                                       @Path("deliveryId") String deliveryId);

    //Todo delete Delivery by provider id
    @DELETE("provider/{id}/deliveryrep/{deliveryId}")
    Call<GetDeliveriesData> deleteDeliveryByProvider (@Header("Authorization") String Auth,
                                                       @Path("id") String id,
                                                       @Path("deliveryId") String deliveryId);

    //Todo get all Delivery by provider id
    @GET("provider/{id}/deliveryrep")
    Call<GetDeliveriesData> getAllDeliveries (@Header("Authorization") String Auth,
                                              @Path("id") String id,
                                              @Query("include_image") boolean include_image,
                                              @Query("include_vehicle") boolean include_vehicle);

//  Todo get all Delivery by provider id
    @GET("provider/{id}/deliveryrep/{deliveryId}/vehicle")
    Call<GetVehicleData> getVehicleByDeliveryId (@Header("Authorization") String Auth,
                                                @Path("id") String id,
                                                @Path("deliveryId") String deliveryId,
                                                @Query("include_images") boolean include_images,
                                                @Query("include_driving_licence") boolean include_driving_licence,
                                                @Query("include_vehicle_licence") boolean include_vehicle_licence);

    //Todo Create Vehicle
    @POST("provider/{id}/delivery/{deliveryId}/vehicle")
    Call<CreateVehicleRespons> createVehicle (@Header("Authorization") String Auth,
                                              @Path("id") String id,
                                              @Path("deliveryId") String deliveryId,
                                              @Body CreateVehicleRequest createVehicleRequest);

    //Todo get All Cities
    @GET("city")
    Call<Cities> getAllCities();

    //Todo get All Vehicle Types
    @GET("vehicle/type")
    Call<VehicleTypes> getAllVehicleTypes();

    //Todo get All Vehicle Types
    @GET("vehicle/manufacture-years")
    Call<Years> getAllYears();

    //Todo Update Vehicle
    @PUT("deliveryrep/{deliveryId}/vehicle/vehicleId")
    Call<Data> updateVehicle (@Header("Authorization") String Auth,
                             @Path("deliveryId") String deliveryId,
                             @Path("vehicleId") String vehicleId,
                             @Body UpdateVehicle updateVehicle);
}
