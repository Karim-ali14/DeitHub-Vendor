
package com.dopave.diethub_vendor.Models.Orders;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("raw")
    @Expose
    private List<Raw> raw = null;
    @SerializedName("count")
    @Expose
    private Integer count;

    public List<Raw> getRaw() {
        return raw;
    }

    public void setRaw(List<Raw> raw) {
        this.raw = raw;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
