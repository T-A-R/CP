package irongate.checkpot.checkpotAPI.models;

public class DaDataQueryBody {
    private String query;

    public DaDataQueryBody(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}