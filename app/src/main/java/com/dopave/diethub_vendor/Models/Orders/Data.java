
package com.dopave.diethub_vendor.Models.Orders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("raw")
    @Expose
    private List<OrderRaw> orderRaw = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<OrderRaw> getOrderRaw() {
        return orderRaw;
    }

    public void setOrderRaw(List<OrderRaw> orderRaw) {
        this.orderRaw = orderRaw;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
