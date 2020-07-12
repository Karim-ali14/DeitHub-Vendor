
package com.dopave.diethub_vendor.Models.Orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalPricePiastre {

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
