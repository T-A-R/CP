package irongate.checkpot.model;

public class CardModel {

    private String max_bill, name_place, time, distance, photoLink;
    private Integer likes, presentsCount;

    public CardModel(String max_bill, String name_place, String time, String distance, String photoLink, Integer likes, Integer presentsCount) {
        this.max_bill = max_bill;
        this.name_place = name_place;
        this.time = time;
        this.distance = distance;
        this.photoLink = photoLink;
        this.likes = likes;
        this.presentsCount = presentsCount;
    }

    public String getMax_bill() {
        return max_bill;
    }

    public String getName_place() {
        return name_place;
    }

    public String getTime() {
        return time;
    }

    public String getDistance() {
        return distance;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getPresentsCount() {
        return presentsCount;
    }
}
