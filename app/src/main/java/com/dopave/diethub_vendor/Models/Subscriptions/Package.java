
package com.dopave.diethub_vendor.Models.Subscriptions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Package implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameEn")
    @Expose
    private String nameEn;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("carb_cal")
    @Expose
    private Integer carbCal;
    @SerializedName("protein_cal")
    @Expose
    private Integer proteinCal;
    @SerializedName("fat_cal")
    @Expose
    private Integer fatCal;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("total_rate")
    @Expose
    private Integer totalRate;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("duration_id")
    @Expose
    private Integer durationId;
    @SerializedName("main_image_id")
    @Expose
    private Integer mainImageId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("packagecategory_id")
    @Expose
    private Integer packagecategoryId;
    @SerializedName("packagesubcategory_id")
    @Expose
    private Object packagesubcategoryId;
    @SerializedName("main_image")
    @Expose
    private MainImage mainImage;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;

    protected Package(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        nameEn = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readInt();
        }
        if (in.readByte() == 0) {
            carbCal = null;
        } else {
            carbCal = in.readInt();
        }
        if (in.readByte() == 0) {
            proteinCal = null;
        } else {
            proteinCal = in.readInt();
        }
        if (in.readByte() == 0) {
            fatCal = null;
        } else {
            fatCal = in.readInt();
        }
        status = in.readString();
        content = in.readString();
        deliveryTime = in.readString();
        if (in.readByte() == 0) {
            totalRate = null;
        } else {
            totalRate = in.readInt();
        }
        createdAt = in.readString();
        updatedAt = in.readString();
        if (in.readByte() == 0) {
            durationId = null;
        } else {
            durationId = in.readInt();
        }
        if (in.readByte() == 0) {
            mainImageId = null;
        } else {
            mainImageId = in.readInt();
        }
        if (in.readByte() == 0) {
            providerId = null;
        } else {
            providerId = in.readInt();
        }
        if (in.readByte() == 0) {
            packagecategoryId = null;
        } else {
            packagecategoryId = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(nameEn);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(price);
        }
        if (carbCal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(carbCal);
        }
        if (proteinCal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(proteinCal);
        }
        if (fatCal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(fatCal);
        }
        dest.writeString(status);
        dest.writeString(content);
        dest.writeString(deliveryTime);
        if (totalRate == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalRate);
        }
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        if (durationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(durationId);
        }
        if (mainImageId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mainImageId);
        }
        if (providerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(providerId);
        }
        if (packagecategoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(packagecategoryId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Package> CREATOR = new Creator<Package>() {
        @Override
        public Package createFromParcel(Parcel in) {
            return new Package(in);
        }

        @Override
        public Package[] newArray(int size) {
            return new Package[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getCarbCal() {
        return carbCal;
    }

    public void setCarbCal(Integer carbCal) {
        this.carbCal = carbCal;
    }

    public Integer getProteinCal() {
        return proteinCal;
    }

    public void setProteinCal(Integer proteinCal) {
        this.proteinCal = proteinCal;
    }

    public Integer getFatCal() {
        return fatCal;
    }

    public void setFatCal(Integer fatCal) {
        this.fatCal = fatCal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(Integer totalRate) {
        this.totalRate = totalRate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDurationId() {
        return durationId;
    }

    public void setDurationId(Integer durationId) {
        this.durationId = durationId;
    }

    public Integer getMainImageId() {
        return mainImageId;
    }

    public void setMainImageId(Integer mainImageId) {
        this.mainImageId = mainImageId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Integer getPackagecategoryId() {
        return packagecategoryId;
    }

    public void setPackagecategoryId(Integer packagecategoryId) {
        this.packagecategoryId = packagecategoryId;
    }

    public Object getPackagesubcategoryId() {
        return packagesubcategoryId;
    }

    public void setPackagesubcategoryId(Object packagesubcategoryId) {
        this.packagesubcategoryId = packagesubcategoryId;
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
