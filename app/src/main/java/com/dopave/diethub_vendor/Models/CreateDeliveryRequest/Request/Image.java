
package com.dopave.diethub_vendor.Models.CreateDeliveryRequest.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("base64")
    @Expose
    private String base64;

    public Image(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
