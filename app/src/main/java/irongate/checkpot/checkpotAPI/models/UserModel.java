
package irongate.checkpot.checkpotAPI.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("rating")
    @Expose
    private Long rating;
    @SerializedName("events")
    @Expose
    private List<Event> events = new ArrayList<>();
    @SerializedName("restaurants")
    @Expose
    private List<Restaurant> restaurants = new ArrayList<>();
    @SerializedName("isOwner")
    @Expose
    private Boolean isOwner;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("sessions")
    @Expose
    private List<Session> sessions = new ArrayList<>();
    @SerializedName("permissions")
    @Expose
    private Permissions permissions;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("digitalId")
    @Expose
    private String digitalId;
    @SerializedName("contacts")
    @Expose
    private List<Object> contacts = new ArrayList<>();
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("vk_user_id")
    @Expose
    private Integer vk_user_id;

    public static UserModel clone(UserModel userModel){
        if (userModel == null){
            return null;
        }
        UserModel newUserModel = new UserModel();
        newUserModel.rating = userModel.getRating();
        for (int i = 0; i < userModel.events.size();i++){
            newUserModel.events.add(Event.clone(userModel.events.get(i)));}
        for (int i = 0; i < userModel.restaurants.size();i++){
            newUserModel.restaurants.add(Restaurant.clone(userModel.restaurants.get(i)));}
        newUserModel.isOwner = userModel.getIsOwner();
        newUserModel.id = userModel.getId();
        newUserModel.phone = userModel.getPhone();
        for (int i = 0; i < userModel.sessions.size();i++){
        newUserModel.sessions.add(Session.clone(userModel.sessions.get(i)));
        }
        newUserModel.permissions = userModel.getPermissions();
        newUserModel.uuid = userModel.getUuid();
        newUserModel.digitalId = userModel.getDigitalId();
        for (int i = 0; i <userModel.contacts.size(); i++) {
            newUserModel.contacts.add(userModel.contacts.get(i));
        }
        newUserModel.createdAt = userModel.getCreatedAt();
        newUserModel.updatedAt = userModel.getUpdatedAt();
        newUserModel.name = userModel.getName();
        newUserModel.photo = userModel.getPhoto();
        newUserModel.vk_user_id = userModel.getVk_user_id();

        return newUserModel;
    }

    public Integer getVk_user_id() {
        return vk_user_id;
    }

    public void setVk_user_id(Integer vk_user_id) {
        this.vk_user_id = vk_user_id;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Boolean getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(Boolean isOwner) {
        this.isOwner = isOwner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
