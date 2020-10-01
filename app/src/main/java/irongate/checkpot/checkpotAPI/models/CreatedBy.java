
package irongate.checkpot.checkpotAPI.models;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedBy {

    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("contacts")
    @Expose
    private List<Object> contacts = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public static CreatedBy clone(CreatedBy createdBy) {
        if (createdBy == null) {
            return null;
        }

        CreatedBy newCreatedBy = new CreatedBy();
        newCreatedBy.rating = createdBy.getRating();
        newCreatedBy.phone = createdBy.getPhone();
        newCreatedBy.uuid = createdBy.getUuid();
        newCreatedBy.contacts = createdBy.getContacts();
        newCreatedBy.createdAt = createdBy.getCreatedAt();
        newCreatedBy.updatedAt = createdBy.getUpdatedAt();

        return newCreatedBy;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Object> getContacts() {
        return contacts;
    }

    public void setContacts(List<Object> contacts) {
        this.contacts = contacts;
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
