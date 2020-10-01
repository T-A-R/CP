package irongate.checkpot.view.screens.delegate.companyRegistration;

import irongate.checkpot.checkpotAPI.models.Suggestions;
import irongate.checkpot.view.screens.delegate.information.InfoFragment;

public class CompanyData {
    String bankAccountNo;
    String bic;
    String email;
    String innkpp;
    String legalAddress;
    String legalManager;
    String legalName;
    String ogrn;
    String type;
    String inn;
    Suggestions suggestions;

    public Suggestions getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(Suggestions suggestions) {
        this.suggestions = suggestions;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
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

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getLegalManager() {
        return legalManager;
    }

    public void setLegalManager(String legalManager) {
        this.legalManager = legalManager;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
