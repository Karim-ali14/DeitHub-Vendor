
package com.dopave.diethub_vendor.Models.Orders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unitPricePiastre")
    @Expose
    private Integer unitPricePiastre;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("options")
    @Expose
    private List<Object> options = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public List<Object> getOptions() {
        return options;
    }

    public void setOptions(List<Object> options) {
        this.options = options;
    }

}
