
package com.dopave.diethub_vendor.Models.ProviderInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("loginPhone")
    @Expose
    private String loginPhone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameEn")
    @Expose
    private String nameEn;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("addressEn")
    @Expose
    private String addressEn;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("descriptionEn")
    @Expose
    private String descriptionEn;
    @SerializedName("delivery_price")
    @Expose
    private Object deliveryPrice;
    @SerializedName("main_image_id")
    @Expose
    private Integer mainImageId;
    @SerializedName("main_image")
    @Expose
    private MainImage mainImage;

    public Data(Integer id, String loginPhone, String email, String name, String nameEn, Integer cityId, String address, String addressEn, Double latitude, Double longitude, String description, String descriptionEn, Object deliveryPrice, Integer mainImageId, MainImage mainImage) {
        this.id = id;
        this.loginPhone = loginPhone;
        this.email = email;
        this.name = name;
        this.nameEn = nameEn;
        this.cityId = cityId;
        this.address = address;
        this.addressEn = addressEn;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.descriptionEn = descriptionEn;
        this.deliveryPrice = deliveryPrice;
        this.mainImageId = mainImageId;
        this.mainImage = mainImage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginPhone() {
        return loginPhone;
    }

    public void setLoginPhone(String loginPhone) {
        this.loginPhone = loginPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public Object getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(Object deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public Integer getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(Integer mainImageId) {
        this.mainImageId = mainImageId;
    }

    public MainImage getMainImage() {
        return mainImage;
    }

    public void setMainImage(MainImage mainImage) {
        this.mainImage = mainImage;
    }

}
