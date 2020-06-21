
package com.dopave.diethub_vendor.Models.UpdateVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleLicenceImage {

    @SerializedName("base64")
    @Expose
    private String base64;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
