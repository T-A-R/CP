package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tariff {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("isExtended")
    @Expose
    private Boolean isExtended;
    @SerializedName("count")
    @Expose
    private Long count;
    @SerializedName("isColoredOnMap")
    @Expose
    private Boolean isColoredOnMap = false;
    @SerializedName("base")
    @Expose
    private Boolean base;
    @SerializedName("amout")
    @Expose
    private Long amout;

    public static Tariff clone(Tariff tariff) {
        if (tariff == null) {
            return null;
        }

        Tariff newTariff = new Tariff();
        newTariff.title = tariff.getTitle();
        newTariff.isExtended = tariff.getIsExtended();
        newTariff.count = tariff.getCount();
        newTariff.isColoredOnMap = tariff.getIsColoredOnMap();
        newTariff.base = tariff.getBase();
        newTariff.amout = tariff.getAmout();

        return newTariff;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getIsExtended() {
        return isExtended;
    }

    public void setIsExtended(Boolean isExtended) {
        this.isExtended = isExtended;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Boolean getIsColoredOnMap() {
        return isColoredOnMap;
    }

    public void setIsColoredOnMap(Boolean isColoredOnMap) {
        this.isColoredOnMap = isColoredOnMap;
    }

    public Boolean getBase() {
        return base;
    }

    public void setBase(Boolean base) {
        this.base = base;
    }

    public Long getAmout() {
        return amout;
    }

    public void setAmout(Long amout) {
        this.amout = amout;
    }

}