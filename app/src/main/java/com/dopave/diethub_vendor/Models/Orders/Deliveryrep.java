package com.dopave.diethub_vendor.Models.Orders;
import android.os.Parcel;
import android.os.Parcelable;

import com.dopave.diethub_vendor.Models.GetDeliveries.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deliveryrep implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobilePhone")
    @Expose
    private String mobilePhone;
    @SerializedName("image")
    @Expose
    private Image image;

    protected Deliveryrep(Parcel in) {
        name = in.readString();
        mobilePhone = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mobilePhone);
        dest.writeParcelable(image, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Deliveryrep> CREATOR = new Creator<Deliveryrep>() {
        @Override
        public Deliveryrep createFromParcel(Parcel in) {
            return new Deliveryrep(in);
        }

        @Override
        public Deliveryrep[] newArray(int size) {
            return new Deliveryrep[size];
        }
    };

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}