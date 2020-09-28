
package com.dopave.diethub_vendor.Models.Orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalPricePiastre implements Parcelable {

    @SerializedName("total")
    @Expose
    private Double total;
    @SerializedName("discount")
    @Expose
    private Discount discount;
    @SerializedName("itemsTotalPrice")
    @Expose
    private Double itemsTotalPrice;
    @SerializedName("deliveryPricePiastre")
    @Expose
    private Double deliveryPricePiastre;


    protected TotalPricePiastre(Parcel in) {
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readDouble();
        }
        discount = in.readParcelable(Discount.class.getClassLoader());
        if (in.readByte() == 0) {
            itemsTotalPrice = null;
        } else {
            itemsTotalPrice = in.readDouble();
        }
        if (in.readByte() == 0) {
            deliveryPricePiastre = null;
        } else {
            deliveryPricePiastre = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(total);
        }
        dest.writeParcelable(discount, flags);
        if (itemsTotalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(itemsTotalPrice);
        }
        if (deliveryPricePiastre == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(deliveryPricePiastre);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TotalPricePiastre> CREATOR = new Creator<TotalPricePiastre>() {
        @Override
        public TotalPricePiastre createFromParcel(Parcel in) {
            return new TotalPricePiastre(in);
        }

        @Override
        public TotalPricePiastre[] newArray(int size) {
            return new TotalPricePiastre[size];
        }
    };

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getItemsTotalPrice() {
        return itemsTotalPrice;
    }

    public void setItemsTotalPrice(Double itemsTotalPrice) {
        this.itemsTotalPrice = itemsTotalPrice;
    }

    public Double getDeliveryPricePiastre() {
        return deliveryPricePiastre;
    }

    public void setDeliveryPricePiastre(Double deliveryPricePiastre) {
        this.deliveryPricePiastre = deliveryPricePiastre;
    }
}
