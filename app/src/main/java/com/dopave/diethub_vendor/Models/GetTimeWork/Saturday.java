
package com.dopave.diethub_vendor.Models.GetTimeWork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Saturday {

    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;

    public Saturday(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
