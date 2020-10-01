
package irongate.checkpot.checkpotAPI.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import irongate.checkpot.model.User;
import irongate.checkpot.utils.TimeUtils;

public class Event {
    @SerializedName("isDone")
    @Expose
    private Boolean isDone;
    @SerializedName("banned")
    @Expose
    private Boolean banned;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("end")
    @Expose
    private Long end;
    @SerializedName("excluded")
    @Expose
    private List<UserModel> excluded = new ArrayList<>();
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("prizes")
    @Expose
    private List<Prize> prizes = new ArrayList<>();
    @SerializedName("start")
    @Expose
    private Long start;
    @SerializedName("totalCost")
    @Expose
    private Long totalCost;
    @SerializedName("place")
    @Expose
    private Place place;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("digitalId")
    @Expose
    private String digitalId;
    @SerializedName("appeals")
    @Expose
    private List<Appeal> appeals = new ArrayList<>();
    @SerializedName("members")
    @Expose
    private List<Member> members = new ArrayList<>();
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Long v;
    @SerializedName("rulesFile")
    @Expose
    private String rulesFile;
    @SerializedName("membersCount")
    @Expose
    private Long membersCount;
    @SerializedName("tariff_uuid")
    @Expose
    private String tariff_uuid;
    @SerializedName("services_uuid")
    @Expose
    private List<String> services_uuid = new ArrayList<>();
    @SerializedName("paymentId")
    @Expose
    private String paymentId;
    @SerializedName("ended")
    @Expose
    private Long ended;
    @SerializedName("price")
    @Expose
    private Float price;

    public static Event clone(Event event) {

        if (event == null) {
            return null;
        }
        Event newEvent = new Event();
        newEvent.isDone = event.getIsDone();
        newEvent.banned = event.getBanned();
        newEvent.description = event.getDescription();
        newEvent.end = event.getEnd();
        for (int i = 0; i < event.excluded.size(); i++) {
            newEvent.excluded.add(UserModel.clone(event.excluded.get(i)));
        }
        newEvent.id = event.getId();
        newEvent.prizes = event.getPrizes();
        newEvent.totalCost = event.getTotalCost();
        newEvent.place = event.getPlace();
        newEvent.uuid = event.getUuid();
        newEvent.digitalId = event.getDigitalId();
        for (int i = 0; i < event.appeals.size(); i++) {
            newEvent.appeals.add(Appeal.clone(event.appeals.get(i)));
        }
        newEvent.members = event.getMembers();
        newEvent.createdAt = event.getCreatedAt();
        newEvent.updatedAt = event.getUpdatedAt();
        newEvent.v = event.getV();
        newEvent.rulesFile = event.getRulesFile();
        newEvent.membersCount = event.getMembersCount();

        return newEvent;
    }


    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public List<UserModel> getExcluded() {
        return excluded;
    }

    public void setExcluded(List<UserModel> excluded) {
        this.excluded = excluded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Prize> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Long totalCost) {
        this.totalCost = totalCost;
    }

    public Long getMembersCount() {
        return membersCount;
    }

    public void setMembersCount(Long membersCount) {
        this.membersCount = membersCount;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDigitalId() {
        return digitalId;
    }

    public void setDigitalId(String digitalId) {
        this.digitalId = digitalId;
    }

    public List<Appeal> getAppeals() {
        return appeals;
    }

    public void setAppeals(List<Appeal> appeals) {
        this.appeals = appeals;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
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

    public Long getV() {
        return v;
    }

    public void setV(Long v) {
        this.v = v;
    }

    public String getRulesFile() {
        return rulesFile;
    }

    public void setRulesFile(String rulesFile) {
        this.rulesFile = rulesFile;
    }

    public String getTariff_uuid() {
        return tariff_uuid;
    }

    public void setTariff_uuid(String tariff_uuid) {
        this.tariff_uuid = tariff_uuid;
    }

    public List<String> getServices_uuid() {
        return services_uuid;
    }

    public void setServices_uuid(List<String> services_uuid) {
        this.services_uuid = services_uuid;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Long getEnded() {
        return ended;
    }

    public Float getPrice() {
        return price;
    }


    public Prize getMainPrize() {
        if (prizes == null)
            return null;

        for (int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (prize.getIsRandom())
                return prize;
        }
        return null;
    }

    public Prize getGuaranted() {
        if (prizes == null)
            return null;

        for (int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (!prize.getIsRandom())
                return prize;
        }
        return null;
    }

    public List<Prize> getRandomPrizes() {
        if (prizes == null)
            return null;

        ArrayList<Prize> another = new ArrayList<>();
        for (int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (prize.getIsRandom())
                another.add(prize);
        }
        return another;
    }

    public Prize getMyRandomPrize() {
        if (!isDone || prizes == null)
            return null;

        String myId = User.getUser().getUserId();

        for (int i = 0; i < prizes.size(); i++) {
            Prize prize = prizes.get(i);
            if (!prize.getIsRandom())
                continue;

            List<Winner> winners = prize.getWinners();
            if (winners == null)
                continue;

            for (int j = 0; j < winners.size(); j++) {
                Winner w = winners.get(j);
                if (w.getUser().equals(myId))
                    return prize;
            }
        }

        return null;
    }

    public Prize getMyGuarantedPrize() {
        Prize guaranted = getGuaranted();
        if (!isDone || guaranted == null)
            return null;

        List<Winner> winners = guaranted.getWinners();
        if (winners == null) {
            return null;
        }

        String myId = User.getUser().getUserId();
        for (int j = 0; j < winners.size(); j++) {
            Winner w = winners.get(j);
            if (w.getUser().equals(myId))
                return guaranted;
        }

        return null;
    }

    public Prize getMyPrize() {
        if (getMyRandomPrize() != null)
            return getMyRandomPrize();

        if (getMyGuarantedPrize() != null)
            return getMyGuarantedPrize();

        return null;
    }

    /* Возвращает меня как победителя **/
    public Winner getMyAsWinner() {
        Prize myPrize = getMyRandomPrize();
        if (myPrize == null)
            myPrize = getMyGuarantedPrize();

        if (myPrize == null)
            return null;

        String myId = User.getUser().getUserId();
        for (Winner w : myPrize.getWinners()) {
            if (w.getUser().equals(myId))
            return w;
        }

        return null;
    }

    /* Мной ли создан розыгрыш. Мне ли принадлежит это заведение. **/
    public boolean isCreatedByMe() {
        if (place == null)
            return false;

        return place.getUuid().equals(User.getUser().getPlace().getUuid());
    }

    public int getNumSuccMembers() {
        if (members == null)
            return 0;

        int num = 0;
        for (Member m : members) {
            if (m != null) {
                if (m.getCheck() != null && m.getCheck().getStatus() != null && m.getCheck().getStatus().equals("SUCCESS"))
                    num++;
                else if (m.getReceipt() != null && !m.getReceipt().isEmpty())
                    num++;
            }
        }
        return num;
    }
}
