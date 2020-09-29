package com.dopave.diethub_vendor.Models.UpdateVehicle;

import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteImageFromList {
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public DeleteImageFromList(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
