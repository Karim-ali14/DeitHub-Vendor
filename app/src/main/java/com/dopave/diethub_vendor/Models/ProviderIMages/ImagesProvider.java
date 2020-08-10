
package com.dopave.diethub_vendor.Models.ProviderIMages;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImagesProvider {

    @SerializedName("main_image")
    @Expose
    private MainImage mainImage;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public ImagesProvider(MainImage mainImage, List<Image> images) {
        this.mainImage = mainImage;
        this.images = images;
    }

    public MainImage getMainImage() {
        return mainImage;
    }

    public void setMainImage(MainImage mainImage) {
        this.mainImage = mainImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
