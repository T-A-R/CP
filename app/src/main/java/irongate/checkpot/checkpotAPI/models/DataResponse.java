package irongate.checkpot.checkpotAPI.models;

import java.util.List;

public class DataResponse {
    private List<Suggestions> suggestions;

    public List<Suggestions> getSuggestions ()
    {
        return suggestions;
    }

    public void setSuggestions (List<Suggestions> suggestions)
    {
        this.suggestions = suggestions;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [suggestions = "+suggestions+"]";
    }
}
