
package irongate.checkpot.checkpotAPI.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
    private List<Double> coordinates = new ArrayList<>();

    public Location() {

    }

    public Location(double lat, double lng) {
        coordinates.add(lng);
        coordinates.add(lat);
    }

    public static Location clone(Location location) {
        if (location == null) {
            return null;
        }
        Location newLocation = new Location();
        newLocation.type = location.getType();
        for (int i = 0; i < location.coordinates.size(); i++) {
            newLocation.coordinates.add(location.getCoordinates().get(i));
        }

        return newLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public double getLng() {
        return coordinates.get(0);
    }

    public double getLat() {
        return coordinates.get(1);
    }

}
