package irongate.checkpot.view.screens.delegate.aboutWinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleCardRiffle {

    String raffleFon;
    String raffleId;
    String raffleName;
    String raffleDate;
    String raffleUuid;
    List<SimpleUserProfile> userProfileList = new ArrayList<>();
    List<SimpleUserProfile> userProfilePrizedList = new ArrayList<>();

    public String getRaffleUuid() {
        return raffleUuid;
    }

    public void setRaffleUuid(String raffleUuid) {
        this.raffleUuid = raffleUuid;
    }

    public String getRaffleFon() {
        return raffleFon;
    }

    public void setRaffleFon(String raffleFon) {
        this.raffleFon = raffleFon;
    }

    public String getRaffleId() {
        return raffleId;
    }

    public void setRaffleId(String raffleId) {
        this.raffleId = raffleId;
    }

    public String getRaffleName() {
        return raffleName;
    }

    public void setRaffleName(String raffleName) {
        this.raffleName = raffleName;
    }

    public String  getRaffleDate() {
        return raffleDate;
    }

    public void setRaffleDate(String raffleDate) {
        this.raffleDate = raffleDate;
    }

    public List<SimpleUserProfile> getUserProfileList() {
        return userProfileList;
    }

    public void setUserProfileList(List<SimpleUserProfile> userProfileList) {
        this.userProfileList = userProfileList;
    }

    public List<SimpleUserProfile> getUserProfilePrizedList() {
        return userProfilePrizedList;
    }

    public void setUserProfilePrizedList(List<SimpleUserProfile> userProfilePrizedList) {
        this.userProfilePrizedList = userProfilePrizedList;
    }

}
