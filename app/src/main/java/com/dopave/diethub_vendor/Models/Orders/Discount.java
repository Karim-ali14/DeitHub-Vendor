
package com.dopave.diethub_vendor.Models.Orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Discount implements Parcelable{

    @SerializedName("offer")
    @Expose
    private Offer offer;
    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("coupon")
    @Expose
    private Double coupon;


    protected Discount(Parcel in) {
        offer = in.readParcelable(Offer.class.getClassLoader());
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readDouble();
        }
        if (in.readByte() == 0) {
            coupon = null;
        } else {
            coupon = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(offer, flags);
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(total);
        }
        if (coupon == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(coupon);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Discount> CREATOR = new Creator<Discount>() {
        @Override
        public Discount createFromParcel(Parcel in) {
            return new Discount(in);
        }

        @Override
        public Discount[] newArray(int size) {
            return new Discount[size];
        }
    };

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Double getCoupon() {
        return coupon;
    }

    public void setCoupon(Double coupon) {
        this.coupon = coupon;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
