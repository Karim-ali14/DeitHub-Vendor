
package com.dopave.diethub_vendor.Models.ProviderIMages.Update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

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

    public Image(String action, String base64) {
        this.action = action;
        this.base64 = base64;
    }

    public Image(Integer id, String action) {
        this.id = id;
        this.action = action;
    }

    public Image(Integer id, String action, String base64) {
        this.id = id;
        this.action = action;
        this.base64 = base64;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
