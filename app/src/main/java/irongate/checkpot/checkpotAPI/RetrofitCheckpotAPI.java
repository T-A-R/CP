package irongate.checkpot.checkpotAPI;

import java.util.List;

import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Prices;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.checkpotAPI.models.Sms;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.checkpotAPI.models.Token;
import irongate.checkpot.checkpotAPI.models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitCheckpotAPI {
    @POST("push/touch")
    Call<Void> pushTouch(@Header("Authorization") String authorization, @Body() CheckpotAPI.PushTouchBody body);

    @POST("auth/submit")
    Call<Token> submitSmsCode(@Body() CheckpotAPI.SubmitSmsCodeBody body);

    @POST("auth/touch")
    Call<Sms> getSms(@Body() CheckpotAPI.GetSmsBody body);

    @GET("user")
    Call<UserModel> getCurrentUserEvotor(@Header("eauth") String authorization);

    @GET("user")
    Call<UserModel> getCurrentUser(@Header("Authorization") String authorization);

    //@PATCH("user")
    @POST("user/patch")
    Call<UserModel> patchCurrentUser(@Header("Authorization") String authorization, @Body() CheckpotAPI.PatchUserBody body);

    @POST("user/sign/patch")
    Call<Void> postUserSign(@Header("Authorization") String authorization, @Body() CheckpotAPI.SignUserBody body);

    @POST("auth/link")
    Call<Void> postEmail(@Header("Authorization") String authorization, @Body() CheckpotAPI.EmailBody body);

    @GET("")
    Call<List<Place>> getPlacesListViaGeoPosition(@Header("Authorization") String authorization, @Url String url);

    @PUT("")
    Call<Place> putPlace(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.PlaceBody body);

    @PUT("")
    Call<Restaurant> putRestaurant(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.RestaurantBody body);

    //@PATCH("")
    @POST("")
    Call<Place> patchPlace(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.PlaceBody body);

    //@PUT("")
    @POST("")
    Call<Event> createEvent(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.EventBody body);

    @POST("")
    Call<Void> joinEvent(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.JoinBody body);

    @GET("")
    Call<Void> sendCheck(@Header("Authorization") String authorization, @Url String url);

    @GET("")
    Call<List<Tariff>> getTariffs(@Header("Authorization") String authorization, @Url String url);

    @GET("")
    Call<Prices> getPrices(@Header("Authorization") String authorization, @Url String url);

    //@PATCH("")
    @POST("")
    Call<Void> patchTariff(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.PatchTariffBody body);

    @POST("")
    Call<Restaurant> getBill(@Header("Authorization") String authorization, @Url String url);

    @POST("")
    Call<Void> submitPayment(@Header("Authorization") String authorization, @Url String url, @Body() CheckpotAPI.SubmitPaymentBody body);

    @POST("user/promo")
    Call<Void> sendPromo(@Header("Authorization") String authorization, @Body() CheckpotAPI.PromoBody body);

    //@PATCH("event/decline/{uuid}")
    @POST("event/decline/{uuid}/patch")
    Call<Void> patchEventDecline(@Header("Authorization") String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.PatchEventDecline body);

    //@PATCH("event/accept/{uuid}")
    @POST("event/accept/{uuid}/patch")
    Call<Void> patchEventAccept(@Header("Authorization") String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.PatchEventAccept body);

    @PUT("event/appeal/{uuid}")
    Call<Void> sendAppeal(@Header("Authorization")String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.SendEventAppeal body);

    //Не используется. На будущее.
    //@PATCH("event/accept/{uuid}")
    @POST("event/accept/{uuid}/patch")
    Call<Void> patchEventPaymentId(@Header("Authorization") String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.PatchEventPaymentId body);

    @PATCH("event/{uuid}")
    Call<Void> patchEvent(@Header("Authorization") String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.EventBody body);

    @POST("/restaurant/{uuid}/patch")
    Call<Void> patchRestaurant(@Header("Authorization") String authorization, @Path("uuid") String uuid, @Body() CheckpotAPI.RestaurantBody body);
}