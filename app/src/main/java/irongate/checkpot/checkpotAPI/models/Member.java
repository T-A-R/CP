
package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Member {

    @SerializedName("isPrizeDelivered")
    @Expose
    private Boolean isPrizeDelivered;
    @SerializedName("isWinner")
    @Expose
    private Boolean isWinner;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("receipt")
    @Expose
    private String receipt;
    @SerializedName("check")
    @Expose
    private Check check;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    @SerializedName("user")
    @Expose
    private UserModel user;

    @SerializedName("declineReason")
    @Expose
    private Object declineReason;

    public static Member clone(Member member) {
        if (member == null) {
            return null;
        }
        Member newMember = new Member();
        newMember.isPrizeDelivered = member.getIsPrizeDelivered();
        newMember.isWinner = member.getIsWinner();
        newMember.id = member.getId();
        newMember.receipt = member.getReceipt();
        newMember.createdAt = member.getCreatedAt();
        newMember.updatedAt = member.getUpdatedAt();
        newMember.declineReason = member.getDeclineReason();
        return newMember;
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

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public Check getCheck() {
        return check;
    }

    public void setCheck(Check check) {
        this.check = check;
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

    public Object getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(Object declineReason) {
        this.declineReason = declineReason;
    }
}
