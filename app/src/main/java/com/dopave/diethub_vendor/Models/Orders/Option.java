
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
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("optionName")
    @Expose
    private String optionName;
    @SerializedName("optionNameEn")
    @Expose
    private String optionNameEn;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    private Object deletedAt;
    @SerializedName("order_detail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("option_id")
    @Expose
    private Integer optionId;

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
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        optionName = in.readString();
        optionNameEn = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        orderDetailId = in.readString();
        if (in.readByte() == 0) {
            optionId = null;
        } else {
            optionId = in.readInt();
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
        if (unitPricePiastre == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(unitPricePiastre);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        dest.writeString(optionName);
        dest.writeString(optionNameEn);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(orderDetailId);
        if (optionId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(optionId);
        }
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionNameEn() {
        return optionNameEn;
    }

    public void setOptionNameEn(String optionNameEn) {
        this.optionNameEn = optionNameEn;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getOptionId() {
        return optionId;
    }

    public void setOptionId(Integer optionId) {
        this.optionId = optionId;
    }

}
