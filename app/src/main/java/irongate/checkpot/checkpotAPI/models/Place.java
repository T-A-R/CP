
package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Place {

    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("photos")
    @Expose
    private List<String> photos = new ArrayList<>();
    @SerializedName("events")
    @Expose
    private List<Event> events = new ArrayList<>();
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("restaurant")
    @Expose
    private Restaurant restaurant;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    public static Place clone(Place place) {
        if (place == null) {
            return null;
        }
        Place newPlace = new Place();
        newPlace.location = Location.clone(place.getLocation());
        newPlace.photos.addAll(place.photos);
        newPlace.events = place.getEvents();
        newPlace.address = place.getAddress();
        newPlace.name = place.getName();
        newPlace.restaurant = Restaurant.clone(place.getRestaurant());
        newPlace.description = place.getDescription();
        newPlace.logo = place.getLogo();
        newPlace.uuid = place.getUuid();
        newPlace.createdAt = place.getCreatedAt();
        newPlace.updatedAt = place.getUpdatedAt();

        return newPlace;
    }

    private Boolean isClicked = false;

    public Boolean isClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        if (logo != null && logo.contains("placeholder"))
            return null;

        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public boolean getIsColoredOnMap() {
        return getRestaurant() != null && getRestaurant().getTariff() != null && getRestaurant().getTariff().getIsColoredOnMap();
    }
}
