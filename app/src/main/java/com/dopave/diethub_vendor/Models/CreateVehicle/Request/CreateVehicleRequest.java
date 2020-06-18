
package com.dopave.diethub_vendor.Models.CreateVehicle.Request;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateVehicleRequest {

    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("vehicle_type_id")
    @Expose
    private Integer vehicleTypeId;
    @SerializedName("driving_licence")
    @Expose
    private DrivingLicence drivingLicence;
    @SerializedName("vehicle_licence")
    @Expose
    private VehicleLicence vehicleLicence;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    public CreateVehicleRequest(String number, String model, Integer year, Integer vehicleTypeId, DrivingLicence drivingLicence, VehicleLicence vehicleLicence, List<Image> images) {
        this.number = number;
        this.model = model;
        this.year = year;
        this.vehicleTypeId = vehicleTypeId;
        this.drivingLicence = drivingLicence;
        this.vehicleLicence = vehicleLicence;
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

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Integer vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public DrivingLicence getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(DrivingLicence drivingLicence) {
        this.drivingLicence = drivingLicence;
    }

    public VehicleLicence getVehicleLicence() {
        return vehicleLicence;
    }

    public void setVehicleLicence(VehicleLicence vehicleLicence) {
        this.vehicleLicence = vehicleLicence;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

}
