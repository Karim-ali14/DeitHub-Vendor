
package com.dopave.diethub_vendor.Models.UpdateVehicle;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateVehicle {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("driving_licence_image")
    @Expose
    private DrivingLicenceImage drivingLicenceImage;
    @SerializedName("vehicle_licence_image")
    @Expose
    private VehicleLicenceImage vehicleLicenceImage;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public UpdateVehicle(String number, String model, Integer year, DrivingLicenceImage drivingLicenceImage, VehicleLicenceImage vehicleLicenceImage, List<Image> images) {
        this.number = number;
        this.model = model;
        this.year = year;
        this.drivingLicenceImage = drivingLicenceImage;
        this.vehicleLicenceImage = vehicleLicenceImage;
        this.images = images;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public DrivingLicenceImage getDrivingLicenceImage() {
        return drivingLicenceImage;
    }

    public void setDrivingLicenceImage(DrivingLicenceImage drivingLicenceImage) {
        this.drivingLicenceImage = drivingLicenceImage;
    }

    public VehicleLicenceImage getVehicleLicenceImage() {
        return vehicleLicenceImage;
    }

    public void setVehicleLicenceImage(VehicleLicenceImage vehicleLicenceImage) {
        this.vehicleLicenceImage = vehicleLicenceImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
