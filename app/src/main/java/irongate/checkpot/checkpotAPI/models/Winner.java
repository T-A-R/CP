
package irongate.checkpot.checkpotAPI.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Winner {


    @SerializedName("isPrizeDelivered")
    @Expose
    private Boolean isPrizeDelivered;
    @SerializedName("isWinner")
    @Expose
    private Boolean isWinner;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("receipt")
    @Expose
    private String receipt;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public static Winner clone(Winner winner){
        if(winner == null) {
            return null;
        }
       Winner newWinner = new Winner();

       newWinner.isPrizeDelivered = winner.isPrizeDelivered;
       newWinner.isWinner = winner.getIsWinner();
       newWinner.id = winner.getId();
       newWinner.user = winner.getUser();
       newWinner.receipt = winner.getReceipt();
       newWinner.createdAt = winner.getCreatedAt();
       newWinner.updatedAt = winner.getUpdatedAt();

       return newWinner;
    }

    public Boolean getIsPrizeDelivered() {
        return isPrizeDelivered;
    }

    public void setIsPrizeDelivered(Boolean isPrizeDelivered) {
        this.isPrizeDelivered = isPrizeDelivered;
    }

    public Boolean getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(Boolean isWinner) {
        this.isWinner = isWinner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
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
