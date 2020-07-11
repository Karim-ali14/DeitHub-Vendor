
package com.dopave.diethub_vendor.Models.Subscriptions.UpdateStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateSubscriptionStatus {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cancelledReason")
    @Expose
    private String cancelledReason;

    public UpdateSubscriptionStatus(String status, String cancelledReason) {
        this.status = status;
        this.cancelledReason = cancelledReason;
    }

    public UpdateSubscriptionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

}
