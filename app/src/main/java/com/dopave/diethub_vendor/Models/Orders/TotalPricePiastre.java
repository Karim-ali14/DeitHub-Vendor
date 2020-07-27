
package com.dopave.diethub_vendor.Models.Orders;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalPricePiastre implements Parcelable {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("discount")
    @Expose
    private Discount discount;
    @SerializedName("itemsTotalPrice")
    @Expose
    private Integer itemsTotalPrice;
    @SerializedName("deliveryPricePiastre")
    @Expose
    private Integer deliveryPricePiastre;

    protected TotalPricePiastre(Parcel in) {
        if (in.readByte() == 0) {
            total = null;
        } else {
            total = in.readInt();
        }
        if (in.readByte() == 0) {
            itemsTotalPrice = null;
        } else {
            itemsTotalPrice = in.readInt();
        }
        if (in.readByte() == 0) {
            deliveryPricePiastre = null;
        } else {
            deliveryPricePiastre = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (total == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(total);
        }
        if (itemsTotalPrice == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(itemsTotalPrice);
        }
        if (deliveryPricePiastre == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(deliveryPricePiastre);
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

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Integer getItemsTotalPrice() {
        return itemsTotalPrice;
    }

    public void setItemsTotalPrice(Integer itemsTotalPrice) {
        this.itemsTotalPrice = itemsTotalPrice;
    }

    public Integer getDeliveryPricePiastre() {
        return deliveryPricePiastre;
    }

    public void setDeliveryPricePiastre(Integer deliveryPricePiastre) {
        this.deliveryPricePiastre = deliveryPricePiastre;
    }

}
