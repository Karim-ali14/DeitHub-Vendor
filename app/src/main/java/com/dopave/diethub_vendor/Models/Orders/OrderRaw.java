
package com.dopave.diethub_vendor.Models.Orders;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderRaw implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;
    @SerializedName("totalPricePiastre")
    @Expose
    private TotalPricePiastre totalPricePiastre;
    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("client_id")
    @Expose
    private Integer clientId;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("client")
    @Expose
    private Client client;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    protected OrderRaw(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        status = in.readString();
        if (in.readByte() == 0) {
            addressId = null;
        } else {
            addressId = in.readInt();
        }
        if (in.readByte() == 0) {
            clientId = null;
        } else {
            clientId = in.readInt();
        }
        updatedAt = in.readString();
        createdAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(status);
        if (addressId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(addressId);
        }
        if (clientId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(clientId);
        }
        dest.writeString(updatedAt);
        dest.writeString(createdAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderRaw> CREATOR = new Creator<OrderRaw>() {
        @Override
        public OrderRaw createFromParcel(Parcel in) {
            return new OrderRaw(in);
        }

        @Override
        public OrderRaw[] newArray(int size) {
            return new OrderRaw[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TotalPricePiastre getTotalPricePiastre() {
        return totalPricePiastre;
    }

    public void setTotalPricePiastre(TotalPricePiastre totalPricePiastre) {
        this.totalPricePiastre = totalPricePiastre;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

}
