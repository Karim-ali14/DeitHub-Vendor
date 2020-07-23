
package com.dopave.diethub_vendor.Models.GetDeliveries;

import android.os.Parcel;
import android.os.Parcelable;

import com.dopave.diethub_vendor.Models.GetVehicles.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryRow implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobilePhone")
    @Expose
    private String mobilePhone;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("city_id")
    @Expose
    private Integer cityId;
    @SerializedName("vehicle")
    @Expose
    private Data vehicle;
    @SerializedName("image")
    @Expose
    private Image image;
    @SerializedName("city")
    @Expose
    private City city;

    protected DeliveryRow(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        mobilePhone = in.readString();
        status = in.readString();
        email = in.readString();
        if (in.readByte() == 0) {
            cityId = null;
        } else {
            cityId = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(mobilePhone);
        dest.writeString(status);
        dest.writeString(email);
        if (cityId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cityId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeliveryRow> CREATOR = new Creator<DeliveryRow>() {
        @Override
        public DeliveryRow createFromParcel(Parcel in) {
            return new DeliveryRow(in);
        }

        @Override
        public DeliveryRow[] newArray(int size) {
            return new DeliveryRow[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Data getVehicle() {
        return vehicle;
    }

    public void setVehicle(Data vehicle) {
        this.vehicle = vehicle;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
