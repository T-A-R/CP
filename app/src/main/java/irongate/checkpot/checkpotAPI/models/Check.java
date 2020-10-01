package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Check {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("s")
    @Expose
    private String s;

    @SerializedName("fn")
    @Expose
    private String fn;

    @SerializedName("i")
    @Expose
    private String i;

    @SerializedName("fp")
    @Expose
    private String fp;

    @SerializedName("t")
    @Expose
    private String t;

    private Map<String, String> data = new HashMap<>();

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getData() {
        data.put("time", t);
        data.put("sum", s);
        data.put("fn", fn);
        data.put("fd", i);
        data.put("fpd", fp);
        return data; }

    public String getS() { return s; }
}
