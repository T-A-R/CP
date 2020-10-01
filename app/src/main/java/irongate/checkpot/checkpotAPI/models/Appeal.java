
package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appeal {

    @SerializedName("type")
    @Expose
    private Long type;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("reason")
    @Expose
    private String reason;

    public static Appeal clone(Appeal appeal) {
        if (appeal == null) {
            return null;
        }

        Appeal newAppeal = new Appeal();
        newAppeal.type = appeal.getType();
        newAppeal.id = appeal.getId();
        newAppeal.author = appeal.getAuthor();
        newAppeal.reason = appeal.getReason();

        return newAppeal;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
