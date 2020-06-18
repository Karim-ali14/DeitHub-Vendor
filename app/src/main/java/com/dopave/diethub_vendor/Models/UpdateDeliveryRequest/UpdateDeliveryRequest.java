
package com.dopave.diethub_vendor.Models.UpdateDeliveryRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDeliveryRequest {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobilePhone")
    @Expose
    private String mobilePhone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("online")
    @Expose
    private Boolean online;
    @SerializedName("hasTrip")
    @Expose
    private Boolean hasTrip;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;

    public UpdateDeliveryRequest(String email, String mobilePhone, String name, Boolean online, Boolean hasTrip, String status, Image image, Integer cityId) {
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.name = name;
        this.online = online;
        this.hasTrip = hasTrip;
        this.status = status;
        this.image = image;
        this.cityId = cityId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Boolean getHasTrip() {
        return hasTrip;
    }

    public void setHasTrip(Boolean hasTrip) {
        this.hasTrip = hasTrip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

}
