
package com.dopave.diethub_vendor.Models.GetTimeWork;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("saturday")
    @Expose
    private Saturday saturday;
    @SerializedName("sunday")
    @Expose
    private Sunday sunday;
    @SerializedName("monday")
    @Expose
    private Monday monday;
    @SerializedName("tuesday")
    @Expose
    private Tuesday tuesday;
    @SerializedName("wednesday")
    @Expose
    private Wednesday wednesday;
    @SerializedName("thursday")
    @Expose
    private Thursday thursday;
    @SerializedName("friday")
    @Expose
    private Friday friday;

    public Data(Saturday saturday, Sunday sunday, Monday monday, Tuesday tuesday, Wednesday wednesday, Thursday thursday, Friday friday) {
        this.saturday = saturday;
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
    }

    public Saturday getSaturday() {
        return saturday;
    }

    public void setSaturday(Saturday saturday) {
        this.saturday = saturday;
    }

    public Sunday getSunday() {
        return sunday;
    }

    public void setSunday(Sunday sunday) {
        this.sunday = sunday;
    }

    public Monday getMonday() {
        return monday;
    }

    public void setMonday(Monday monday) {
        this.monday = monday;
    }

    public Tuesday getTuesday() {
        return tuesday;
    }

    public void setTuesday(Tuesday tuesday) {
        this.tuesday = tuesday;
    }

    public Wednesday getWednesday() {
        return wednesday;
    }

    public void setWednesday(Wednesday wednesday) {
        this.wednesday = wednesday;
    }

    public Thursday getThursday() {
        return thursday;
    }

    public void setThursday(Thursday thursday) {
        this.thursday = thursday;
    }

    public Friday getFriday() {
        return friday;
    }

    public void setFriday(Friday friday) {
        this.friday = friday;
    }

}
