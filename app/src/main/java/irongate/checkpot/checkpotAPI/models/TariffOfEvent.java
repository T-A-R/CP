package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TariffOfEvent {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("maxMembers")
    @Expose
    private Integer maxMembers;
    @SerializedName("canBeFree")
    @Expose
    private boolean canBeFree;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public Integer getDays() {
        return days;
    }

    public Integer getMaxMembers() {
        return maxMembers;
    }

    public boolean isCanBeFree() {
        return canBeFree;
    }
}
