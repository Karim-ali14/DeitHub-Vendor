package com.dopave.diethub_vendor.API;

import com.dopave.diethub_vendor.Models.Cities.Cities;
import com.dopave.diethub_vendor.Models.CreateDelivery.Request.CreateDeliveryRequest;
import com.dopave.diethub_vendor.Models.CreateDelivery.Response.CreateDeliveryResponse;
import com.dopave.diethub_vendor.Models.CreateVehicle.Request.CreateVehicleRequest;
import com.dopave.diethub_vendor.Models.CreateVehicle.Response.CreateVehicleRespons;
import com.dopave.diethub_vendor.Models.Defualt;
import com.dopave.diethub_vendor.Models.GetDeliveries.GetDeliveriesData;
import com.dopave.diethub_vendor.Models.GetTimeWork.TimeWorks;
import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.dopave.diethub_vendor.Models.GetVehicles.GetVehicleData;
import com.dopave.diethub_vendor.Models.Notifications.NotificationData;
import com.dopave.diethub_vendor.Models.Orders.Orders;
import com.dopave.diethub_vendor.Models.ProviderIMages.GetImages.ProviderImagesResponse;
import com.dopave.diethub_vendor.Models.ProviderIMages.Update.ImagesProvider;
import com.dopave.diethub_vendor.Models.ProviderInfo.ProviderInformation;
import com.dopave.diethub_vendor.Models.ProviderInfo.Request.ProviderInfoRequest;
import com.dopave.diethub_vendor.Models.ResetPassword.ResetPassword;
import com.dopave.diethub_vendor.Models.Settings.Settings;
import com.dopave.diethub_vendor.Models.SignIn.SignIn;
import com.dopave.diethub_vendor.Models.Subscriptions.Subscriptions;
import com.dopave.diethub_vendor.Models.Subscriptions.UpdateStatus.UpdateSubscriptionStatus;
import com.dopave.diethub_vendor.Models.UpdateDeliveryRequest.UpdateDeliveryRequest;
import com.dopave.diethub_vendor.Models.UpdateVehicle.UpdateVehicle;
import com.dopave.diethub_vendor.Models.VehicleTypes.VehicleTypes;
import com.dopave.diethub_vendor.Models.Years.Years;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIRequest {
    //Todo Sign in
    @FormUrlEncoded
    @POST("provider/signin")
    Call<ProviderInformation> signIn (@Field("mobilePhone") String mobilePhone
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
                                              @Query("include_city") boolean include_city,
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
    @POST("provider/{id}/deliveryrep/{deliveryId}/vehicle")
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
    @PUT("provider/{providerId}/vehicle/{vehicleId}")
    Call<Data> updateVehicle (@Header("Authorization") String Auth,
                             @Path("providerId") String providerId,
                             @Path("vehicleId") String vehicleId,
                             @Body UpdateVehicle updateVehicle);

    @GET("provider/{providerId}/subscription")
    Call<Subscriptions> getAllSubscriptionbyProviderId(@Header("Authorization") String Auth,
                                                       @Path("providerId") String providerId,
                                                       @Query("include_client") boolean include_client,
                                                       @Query("include_package") boolean include_package,
                                                       @Query("status") String status,
                                                       @Query("limit") int limit,
                                                       @Query("skip") int skip);

    @PATCH("provider/{providerId}/subscription/{id}")
    Call<Subscriptions> UpdateSubscriptionStatus (@Header("Authorization") String Auth,
                                                  @Path("providerId") String providerId,
                                                  @Path("id") String id,
                                                  @Body UpdateSubscriptionStatus updateSubscriptionStatus);

    @GET("provider/{providerId}/order")
    Call<Orders> getAllOrders(@Header("Authorization") String Auth,
                              @Path("providerId") String providerId,
                              @Query("include_address") boolean include_address,
                              @Query("include_details") boolean include_details,
                              @Query("include_client") boolean include_client,
                              @Query("status") String [] status,
                              @Query("limit") int limit,
                              @Query("skip") int skip);

    @PUT("provider/{providerId}/order/{orderId}/status")
    Call<Orders> updateOrder(@Header("Authorization") String Auth,
                             @Path("providerId") String providerId,
                             @Path("orderId") String orderId,
                             @Body UpdateSubscriptionStatus updateSubscriptionStatus);

    @GET("notification")
    Call<NotificationData> getAllNotifications (@Header("Authorization") String Auth);

    @POST("provider/{providerId}/order/{orderId}/assgin")
    Call<Orders> assignOrder(@Header("Authorization") String Auth,
                             @Path("providerId") String providerId,
                             @Path("orderId") String orderId,
                             @Body HashMap<String,String> deliveryId);

    @GET("setting")
    Call<Settings> getSettings();

    @DELETE("notification/{notifyId}")
    Call<Defualt> deleteSpecificNotify (@Header("Authorization") String Auth,
                                                 @Path("notifyId") String providerId);

    @DELETE("notification")
    Call<Defualt> deleteAllNotifies (@Header("Authorization") String Auth);

    @GET("provider/{providerId}/worktime")
    Call<TimeWorks> getTimeWork (@Path("providerId") String providerId);

    @PATCH("provider/{providerId}/worktime")
    Call<Defualt> getCreateTimeWork(@Header("Authorization") String Auth,
                                    @Path("providerId") String providerId,
                                    @Body com.dopave.diethub_vendor.Models.GetTimeWork.Data data);

    @PUT("provider/{providerId}/images")
    Call<Defualt> updateImages(@Header("Authorization") String Auth,
                                      @Path("providerId") String providerId,
                                      @Body ImagesProvider imagesProvider);

    @GET("provider/{providerId}/images")
    Call<ProviderImagesResponse> getProviderImages(@Header("Authorization") String Auth,
                                                   @Path("providerId") String providerId);

    @GET("operation/provider/{providerId}")
    Call<ProviderInformation> getProviderInfo(@Header("Authorization") String Auth,
                                              @Path("providerId") String providerId);
    @PUT("provider/{providerId}")
    Call<Defualt> updateProviderInfo(@Header("Authorization") String Auth,
                               @Path("providerId") String providerId,
                               @Body ProviderInfoRequest providerInfo);

    @PUT("provider/{id}/device-id")
    Call<Defualt> setFirebaseDeviceId(@Header("Authorization") String Auth,
                                      @Path("id") String id,
                                      @Body HashMap<String,String> deviceId);}
