
package com.dopave.diethub_vendor.Models.Cities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rows")
    @Expose
    private List<CityRow> cityRows = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<CityRow> getCityRows() {
        return cityRows;
    }

    public void setCityRows(List<CityRow> cityRows) {
        this.cityRows = cityRows;
    }

}
