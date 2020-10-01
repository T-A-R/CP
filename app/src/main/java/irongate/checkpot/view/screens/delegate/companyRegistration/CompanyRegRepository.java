package irongate.checkpot.view.screens.delegate.companyRegistration;

public class CompanyRegRepository {

    public static final CompanyRegRepository INSTANCE = new CompanyRegRepository();
    private CompanyData companyData = new CompanyData();

    public CompanyData getCompanyData() {
        return companyData;
    }

    public void reset() {
        companyData = new CompanyData();
    }
}
