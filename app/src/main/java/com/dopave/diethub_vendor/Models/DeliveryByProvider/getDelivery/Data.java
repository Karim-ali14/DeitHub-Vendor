
package com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("rows")
    @Expose
    private List<DeliveryRow> deliveryRows = null;

    public List<DeliveryRow> getDeliveryRows() {
        return deliveryRows;
    }

    public void setDeliveryRows(List<DeliveryRow> deliveryRows) {
        this.deliveryRows = deliveryRows;
    }

}
