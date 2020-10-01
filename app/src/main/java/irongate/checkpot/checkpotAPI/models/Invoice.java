package irongate.checkpot.checkpotAPI.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoice {
    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("amout")
    @Expose
    private Long amout;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("id")
    @Expose
    private String id;

    public static Invoice clone(Invoice invoice) {
        if (invoice == null){
            return null;
        }

        Invoice newInvoice = new Invoice();
        newInvoice.order = invoice.getOrder();
        newInvoice.amout = invoice.getAmout();
        newInvoice.status = invoice.getStatus();
        newInvoice.id = invoice.getId();

        return newInvoice;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getAmout() {
        return amout;
    }

    public void setAmout(Long amout) {
        this.amout = amout;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
