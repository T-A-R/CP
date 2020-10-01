package irongate.checkpot.view.screens.delegate.aboutWinner;

import java.util.ArrayList;
import java.util.List;

public class SimpleUserProfile {

    String avatar;
    String winnerName;
    String winnerId;
    String winnerPhone;
    List<String> raffleName = new ArrayList<>();
    String raffleId;
    String raffleUuid;
    String winnerUuid;
    int vk_user_id;

    public int getVk_user_id() {
        return vk_user_id;
    }

    public void setVk_user_id(int vk_user_id) {
        this.vk_user_id = vk_user_id;
    }

    public String getRaffleUuid() {
        return raffleUuid;
    }

    public void setRaffleUuid(String raffleUuid) {
        this.raffleUuid = raffleUuid;
    }

    public String getWinnerUuid() {
        return winnerUuid;
    }

    public void setWinnerUuid(String winnerUuid) {
        this.winnerUuid = winnerUuid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerId() { return winnerId; }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getWinnerPhone() { return winnerPhone; }

    public void setWinnerPhone(String winnerPhone) {
        this.winnerPhone = winnerPhone;
    }

    public List<String> getRaffleName() {
        return raffleName;
    }

    public void setRaffleName(String raffleName) {
        this.raffleName.add(raffleName);
    }

    public String getRaffleId() {
        return raffleId;
    }

    public void setRaffleId(String raffleId) {
        this.raffleId = raffleId;
    }

}
