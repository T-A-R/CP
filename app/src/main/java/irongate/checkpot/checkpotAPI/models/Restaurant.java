
package irongate.checkpot.checkpotAPI.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("bankAccountNo")
    @Expose
    private String bankAccountNo;
    @SerializedName("bic")
    @Expose
    private String bic;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("balance")
    @Expose
    private Long balance;
    @SerializedName("contractId")
    @Expose
    private Long contractId;
    @SerializedName("isContractSent")
    @Expose
    private Boolean isContractSent;
    @SerializedName("acts")
    @Expose
    private List<Object> acts = new ArrayList<>();
    @SerializedName("documents")
    @Expose
    private List<Object> documents = new ArrayList<>();
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("innkpp")
    @Expose
    private String innkpp;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("appeals")
    @Expose
    private List<Object> appeals = new ArrayList<>();
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("legalAddress")
    @Expose
    private String legalAddress;
    @SerializedName("legalName")
    @Expose
    private String legalName;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("tariff")
    @Expose
    private Tariff tariff;
    @SerializedName("invoices")
    @Expose
    private List<Invoice> invoices = new ArrayList<>();
    @SerializedName("freeEvents")
    @Expose
    private Integer freeEvents;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ogrn")
    @Expose
    private String ogrn;
    @SerializedName("legalManager")
    @Expose
    private String legalManager;


    public static Restaurant clone(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.isActive = restaurant.getIsActive();
        newRestaurant.balance = restaurant.getBalance();
        newRestaurant.contractId = restaurant.getContractId();
        for (int i = 0; i < restaurant.acts.size(); i++) {
            newRestaurant.acts.add(restaurant.getActs().get(i));
        }
        newRestaurant.email = restaurant.getEmail();
        newRestaurant.uuid = restaurant.getUuid();
        for (int i = 0; i < restaurant.appeals.size(); i++) {
            newRestaurant.appeals.add(restaurant.getAppeals().get(i));
        }
        newRestaurant.createdAt = restaurant.getCreatedAt();
        newRestaurant.updatedAt = restaurant.getUpdatedAt();
        newRestaurant.createdBy = CreatedBy.clone(restaurant.getCreatedBy());
        newRestaurant.legalAddress = restaurant.getLegalAddress();
        newRestaurant.legalName = restaurant.getLegalName();
        newRestaurant.phone = restaurant.getPhone();
        newRestaurant.tariff = Tariff.clone(restaurant.getTariff());
        for (int i = 0; i < restaurant.invoices.size(); i++) {
            newRestaurant.invoices.add(Invoice.clone(restaurant.invoices.get(i)));
        }
        return newRestaurant;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Boolean getIsContractSent() {
        return isContractSent;
    }

    public void setIsContractSent(Boolean isContractSent) {
        this.isContractSent = isContractSent;
    }

    public List<Object> getActs() {
        return acts;
    }

    public void setActs(List<Object> acts) {
        this.acts = acts;
    }

    public List<Object> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Object> documents) {
        this.documents = documents;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInnkpp() {
        return innkpp;
    }

    public void setInnkpp(String innkpp) {
        this.innkpp = innkpp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Object> getAppeals() {
        return appeals;
    }

    public void setAppeals(List<Object> appeals) {
        this.appeals = appeals;
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

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Tariff getTariff() {
        return tariff;
    }

    public void setTariff(Tariff tariff) {
        this.tariff = tariff;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Integer getFreeEvents() {
        return freeEvents;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getLegalManager() {
        return legalManager;
    }

    public void setLegalManager(String legalManager) {
        this.legalManager = legalManager;
    }
}