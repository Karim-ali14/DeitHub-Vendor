
package com.dopave.diethub_vendor.Models.DeliveryByProvider.getDelivery;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

}
