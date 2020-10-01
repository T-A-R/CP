
package irongate.checkpot.checkpotAPI.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Permissions {

    @SerializedName("owner")
    @Expose
    private List<Place> owner = new ArrayList<>();
    @SerializedName("admin")
    @Expose
    private List<Object> admin = new ArrayList<>();
    @SerializedName("steward")
    @Expose
    private List<Object> steward = new ArrayList<>();

    public static Permissions clone(Permissions permissions) {
        if (permissions == null) {
            return null;
        }
        Permissions newPermissions = new Permissions();
        for (int i = 0; i < permissions.owner.size(); i++) {
            newPermissions.owner.add(permissions.getOwner().get(i));
        }
        for (int i = 0; i < permissions.admin.size(); i++) {
            newPermissions.admin.add(permissions.getAdmin().get(i));
        }
        for (int i = 0; i < permissions.steward.size(); i++) {
            newPermissions.steward.add(permissions.getSteward().get(i));
        }

        return newPermissions;
    }

    public List<Place> getOwner() {
        return owner;
    }

    public void setOwner(List<Place> owner) {
        this.owner = owner;
    }

    public List<Object> getAdmin() {
        return admin;
    }

    public void setAdmin(List<Object> admin) {
        this.admin = admin;
    }

    public List<Object> getSteward() {
        return steward;
    }

    public void setSteward(List<Object> steward) {
        this.steward = steward;
    }

}
