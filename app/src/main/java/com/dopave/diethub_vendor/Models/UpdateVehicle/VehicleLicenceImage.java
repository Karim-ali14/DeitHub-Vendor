
package com.dopave.diethub_vendor.Models.UpdateVehicle;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleLicenceImage {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("base64")
    @Expose
    private String base64;
    @SerializedName("alt")
    @Expose
    private String alt;
    @SerializedName("description")
    @Expose
    private String description;

    public VehicleLicenceImage(String action, String base64) {
        this.action = action;
        this.base64 = base64;
    }

    public VehicleLicenceImage(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

}
