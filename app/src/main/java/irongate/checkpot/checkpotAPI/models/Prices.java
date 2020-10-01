package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Prices {

    @SerializedName("tariffs")
    @Expose
    private List<TariffOfEvent> tariffs = new ArrayList<>();
    @SerializedName("services")
    @Expose
    private List<Service> services = new ArrayList<>();

    public List<TariffOfEvent> getTariffs() {
        return tariffs;
    }

    public List<Service> getServices() {
        return services;
    }
}
