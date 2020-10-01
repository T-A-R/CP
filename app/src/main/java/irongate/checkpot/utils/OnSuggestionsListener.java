package irongate.checkpot.utils;

import java.util.List;

import irongate.checkpot.checkpotAPI.models.Suggestions;

public interface OnSuggestionsListener {
    void onSuggestionsReady(List<Suggestions> suggestions);
    void onError(String message);
}
