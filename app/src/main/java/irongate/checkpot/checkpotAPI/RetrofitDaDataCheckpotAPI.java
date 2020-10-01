package irongate.checkpot.checkpotAPI;

import irongate.checkpot.checkpotAPI.models.DaDataQueryBody;
import irongate.checkpot.checkpotAPI.models.DataResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitDaDataCheckpotAPI {

    @POST("/suggestions/api/4_1/rs/suggest/party")
    Call<DataResponse> getPartyInfo(@Body DaDataQueryBody body);
}
