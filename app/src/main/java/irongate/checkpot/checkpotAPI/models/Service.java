package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("enabled")
    @Expose
    private boolean enabled;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public Integer getDays() {
        return days;
    }

    public String getImage() {
        return image;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
