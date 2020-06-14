
package com.dopave.diethub_vendor.Models.DeliveryByProvider;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryByProviderRequest {

    @SerializedName("mobilePhone")
    @Expose
    private String mobilePhone;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    public DeliveryByProviderRequest(String mobilePhone, String password, String name, String email, Integer cityId) {
        this.mobilePhone = mobilePhone;
        this.password = password;
        this.name = name;
        this.email = email;
        this.cityId = cityId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

}