package com.dopave.diethub_vendor.Models.UpdateVehicle;

import com.dopave.diethub_vendor.Models.CreateVehicle.Request.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeleteImageFromList {
    @SerializedName("deletedImages")
    @Expose
    private List<Integer> images = null;

    public DeleteImageFromList(List<Integer> images) {
        this.images = images;
    }

    public List<Integer> getImages() {
        return images;
    }

    public void setImages(List<Integer> images) {
        this.images = images;
    }
}
