
package com.dopave.diethub_vendor.Models.Orders;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Option implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("unitPricePiastre")
    @Expose
    private Integer unitPricePiastre;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;
    @SerializedName("order_detail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("options")
    @Expose
    private Options options;

    protected Option(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            unitPricePiastre = null;
        } else {
            unitPricePiastre = in.readInt();
        }
        if (in.readByte() == 0) {
            optionId = null;
        } else {
            optionId = in.readInt();
        }
        orderDetailId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        options = in.readParcelable(Options.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        if (unitPricePiastre == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unitPricePiastre);
        }
        if (optionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(optionId);
        }
        dest.writeString(orderDetailId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeParcelable(options, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Option> CREATOR = new Creator<Option>() {
        @Override
        public Option createFromParcel(Parcel in) {
            return new Option(in);
        }

        @Override
        public Option[] newArray(int size) {
            return new Option[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUnitPricePiastre() {
        return unitPricePiastre;
    }

    public void setUnitPricePiastre(Integer unitPricePiastre) {
        this.unitPricePiastre = unitPricePiastre;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

}