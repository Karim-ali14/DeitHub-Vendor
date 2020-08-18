
package com.dopave.diethub_vendor.Models.ProviderIMages.GetImages;

import java.util.List;

import com.dopave.diethub_vendor.Models.GetDeliveries.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("rows")
    @Expose
    private List<Image> rows = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Image> getRows() {
        return rows;
    }

    public void setRows(List<Image> rows) {
        this.rows = rows;
    }

}
