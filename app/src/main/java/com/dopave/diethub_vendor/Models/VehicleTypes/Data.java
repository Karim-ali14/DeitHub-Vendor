
package com.dopave.diethub_vendor.Models.VehicleTypes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rows")
    @Expose
    private List<RowVehicleTypes> rowVehicleTypes = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<RowVehicleTypes> getRowVehicleTypes() {
        return rowVehicleTypes;
    }

    public void setRowVehicleTypes(List<RowVehicleTypes> rowVehicleTypes) {
        this.rowVehicleTypes = rowVehicleTypes;
    }

}
