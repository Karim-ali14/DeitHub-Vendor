
package com.dopave.diethub_vendor.Models.ProviderInfo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Provider implements Parcelable {

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

    protected Provider(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        loginPhone = in.readString();
        email = in.readString();
        name = in.readString();
        nameEn = in.readString();
        if (in.readByte() == 0) {
            cityId = null;
        } else {
            cityId = in.readInt();
        }
        address = in.readString();
        addressEn = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        description = in.readString();
        descriptionEn = in.readString();
        if (in.readByte() == 0) {
            mainImageId = null;
        } else {
            mainImageId = in.readInt();
        }
        mainImage = in.readParcelable(MainImage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(loginPhone);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeString(nameEn);
        if (cityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cityId);
        }
        dest.writeString(address);
        dest.writeString(addressEn);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(description);
        dest.writeString(descriptionEn);
        if (mainImageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mainImageId);
        }
        dest.writeParcelable(mainImage, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Provider> CREATOR = new Creator<Provider>() {
        @Override
        public Provider createFromParcel(Parcel in) {
            return new Provider(in);
        }

        @Override
        public Provider[] newArray(int size) {
            return new Provider[size];
        }
    };

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
