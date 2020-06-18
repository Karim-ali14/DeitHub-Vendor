
package com.dopave.diethub_vendor.Models.GetDeliveries;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rows")
    @Expose
    private List<DeliveryRow> deliveryRows = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<DeliveryRow> getDeliveryRows() {
        return deliveryRows;
    }

    public void setDeliveryRows(List<DeliveryRow> deliveryRows) {
        this.deliveryRows = deliveryRows;
    }

}
