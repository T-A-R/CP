
package irongate.checkpot.checkpotAPI.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prize {

    @SerializedName("photos")
    @Expose
    private List<String> photos = new ArrayList<>();
    @SerializedName("isRandom")
    @Expose
    private Boolean isRandom;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("winners")
    @Expose
    private List<Winner> winners = new ArrayList<>();
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("minReceipt")
    @Expose
    private Integer minReceipt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public static Prize clone(Prize prize) {
        if (prize == null) {
            return null;
        }
        Prize newPrize = new Prize();
        for (int i = 0; i < prize.photos.size(); i++) {
            newPrize.photos.add(prize.getPhotos().get(i));
        }
        newPrize.isRandom = prize.getIsRandom();
        newPrize.uuid = prize.getUuid();
        newPrize.winners = prize.getWinners();
        newPrize.id = prize.getId();
        newPrize.name = prize.getName();
        newPrize.count = prize.getCount();
        newPrize.minReceipt = prize.getMinReceipt();
        newPrize.createdAt = prize.getCreatedAt();
        newPrize.updatedAt = prize.getUpdatedAt();

        return newPrize;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public Boolean getIsRandom() {
        return isRandom;
    }

    public void setIsRandom(Boolean isRandom) {
        this.isRandom = isRandom;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMinReceipt() {
        return minReceipt;
    }

    public void setMinReceipt(Integer minReceipt) {
        this.minReceipt = minReceipt;
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

}
