package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sms {
    @SerializedName("smsid")
    @Expose
    private String smsid;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("expiredIn")
    @Expose
    private String expiredIn;

    public String getSmsid() {
        return smsid;
    }

    public String getCode() {
        return code;
    }

    public String getExpiredIn() {
        return expiredIn;
    }

    public void setSmsid(String smsid) {
        this.smsid = smsid;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpiredIn(String expiredIn) {
        this.expiredIn = expiredIn;
    }
}
