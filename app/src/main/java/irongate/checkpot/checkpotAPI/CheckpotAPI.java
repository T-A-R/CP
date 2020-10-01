package irongate.checkpot.checkpotAPI;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import irongate.checkpot.App;
import irongate.checkpot.BuildConfig;
import irongate.checkpot.MainActivity;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Invoice;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.PlaceWithBitmaps;
import irongate.checkpot.checkpotAPI.models.Prices;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.checkpotAPI.models.PrizeWithBitmaps;
import irongate.checkpot.checkpotAPI.models.Restaurant;
import irongate.checkpot.checkpotAPI.models.Sms;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.checkpotAPI.models.Token;
import irongate.checkpot.checkpotAPI.models.UserModel;
import irongate.checkpot.checkpotAPI.models.Winner;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.AppLogs;
import irongate.checkpot.utils.ImageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static irongate.checkpot.MainActivity.TAG;


@SuppressWarnings("WeakerAccess")
public class CheckpotAPI {
    static public final String API_URL = (MainActivity.DEBUG_API_ADDRESS && BuildConfig.DEBUG) ? "https://dev-api.checkpot.fun/" : "https://api.checkpot.fun/";
//    static public final String API_URL = "http://192.168.0.121:5000/";    // Локальный адрес Андрея

    static private String smsId;

    static public void getSms(String phone, final GetSmsListener listener) {
        getSms(phone, listener, false);
    }

    static public void getSms(String phone, final GetSmsListener listener, boolean debug) {
        App.getCheckpotApi().getSms(new GetSmsBody(phone, debug)).enqueue(new Callback<Sms>() {
            @Override
            public void onResponse(@NonNull Call<Sms> call, @NonNull Response<Sms> response) {
                int expiredIn = 0;
                int code = 0;
                int responseCode = response.code();
                if (responseCode == 200 && response.body() != null) {
                    smsId = response.body().getSmsid();
                    if (response.body().getCode() != null)
                        code = Integer.parseInt(response.body().getCode());

                    Log.d(TAG, "+++++++++ SMS CODE: " + code + " +++++++++");

                    listener.onGetSms(true, expiredIn, code);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Sms> call, @NonNull Throwable t) {
                AppLogs.setOnGetSms(t.getMessage());
                Log.d(TAG, "CheckpotAPI.getSms.onFailure() " + t);
                listener.onGetSms(false, 0, 0);
            }
        });
    }

    static public class GetSmsBody {
        public final String phone;
        public final String debug;

        public GetSmsBody(String phone, boolean debug) {
            this.phone = phone;
            if (debug) this.debug = "1";
            else this.debug = null;
        }
    }

    public interface GetSmsListener {
        void onGetSms(boolean ok, int expiredIn, int code);
    }

    static public void submitSmsCode(String code, UtmBody utm,  final SubmitSmsCodeListener listener) {
        App.getCheckpotApi().submitSmsCode(new SubmitSmsCodeBody(code, utm)).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(@NonNull Call<Token> call, @NonNull Response<Token> response) {
                int responseCode = response.code();
                if (responseCode == 200 && response.body() != null) {
                    String token = response.body().getToken();
                    User.getUser().setAuthToken(token);
                    if (MainActivity.EVOTOR_MODE) {
                        AppLogs.setSmsSubmit("code: " + response.code() + " message: " + response.message() + " token: " + response.body().getToken());
                        App.getMetricaLogger().log("response.code(): " + response.code() + " message: " + response.message());
                        Log.d("TARLOGS", "AppLogs.getSmsSubmit():<<<<<<<<<<<<<<<<<<<<<<<<<<< " + AppLogs.getSmsSubmit());
                    }
                    listener.onSubmitSmsCode(true, false);
                    return;
                }
                if (MainActivity.EVOTOR_MODE) {
                    AppLogs.setSmsSubmit("code: " + response.code() + " message: " + response.message());
                    App.getMetricaLogger().log("response.code(): " + response.code() + " message: " + response.message());
                    Log.d("TARLOGS", "AppLogs.getSmsSubmit():<<<<<<<<<<<<<<<<<<<<<<<<<<< " + AppLogs.getSmsSubmit());
                }
                listener.onSubmitSmsCode(false, responseCode == 418); // Код не подходит
            }

            @Override
            public void onFailure(@NonNull Call<Token> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                if (MainActivity.EVOTOR_MODE) {
                    AppLogs.setSmsSubmit("error code: " + t);
                    App.getMetricaLogger().log("error response.code(): " + t);
                    Log.d("TARLOGS", "AppLogs.getSmsSubmit():<<<<<<<<<<<<<<<<<<<<<<<<<<< " + AppLogs.getSmsSubmit());
                }
                listener.onSubmitSmsCode(false, false);
            }
        });
    }

    static public class SubmitSmsCodeBody {
        public final String smsid;
        public final String code;
        public final UtmBody utm;
        public final boolean isEvotor;
        public final boolean isDevelop;


        public SubmitSmsCodeBody(String code, UtmBody utm) {
            this.smsid = CheckpotAPI.smsId;
            this.code = code;
            this.utm = utm;
            this.isEvotor = MainActivity.EVOTOR_MODE;
            this.isDevelop = MainActivity.DEBUG_API_ADDRESS;
        }
    }

    static public class UtmBody {
        public final String utm_source;
        public final String utm_medium;
        public final String utm_campaign;
        public final String utm_term;
        public final String utm_content;

        public UtmBody(String utm_source, String utm_medium, String utm_campaign, String utm_term, String utm_content) {
            this.utm_source = utm_source;
            this.utm_medium = utm_medium;
            this.utm_campaign = utm_campaign;
            this.utm_term = utm_term;
            this.utm_content = utm_content;
        }
    }

    public interface SubmitSmsCodeListener {
        void onSubmitSmsCode(boolean ok, boolean wrong);
    }

    static public void pushTouch(String playerId) {
        App.getCheckpotApi().pushTouch(getAuthToken(), new PushTouchBody(playerId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.onFailure() " + t);
            }
        });
    }

    static public class PushTouchBody {
        public final String player_id;

        public PushTouchBody(String player_id) {
            this.player_id = player_id;
        }
    }

    static public void getCurrentUser(final GetCurrentUserCallback listener) {
        if (!MainActivity.EVOTOR_MODE) {
            App.getCheckpotApi().getCurrentUser(getAuthToken()).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                    listener.onGetCurrentUser(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                    Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                    listener.onGetCurrentUser(null);
                }
            });
        } else {
            App.getCheckpotApi().getCurrentUser(getAuthToken()).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                    Log.d("TARLOGS", "::::::::::::::::::::::::::::::::::::: getUser: " + response.code() + " : " + response.message());
                    AppLogs.setGetUserLogs("getUser: " + response.code() + " : " + response.message());
                    listener.onGetCurrentUser(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                    Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                    AppLogs.setGetUserLogs("getUser error: " + t);
                    listener.onGetCurrentUser(null);
                }
            });
        }
    }

    public interface GetCurrentUserCallback {
        void onGetCurrentUser(UserModel data);
    }

    static public void patchCurrentUser(String name, Bitmap photo, final PatchCurrentUserCallback listener) {
        PatchUserBody body = new PatchUserBody(name, photo != null ? getBase64(photo) : null);
        App.getCheckpotApi().patchCurrentUser(getAuthToken(), body).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                listener.onPatchCurrentUser(true);
            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                listener.onPatchCurrentUser(false);
            }
        });
    }

    static public class PatchUserBody {
        public final String name;
        public final String photo;

        public PatchUserBody(String name, String photo) {
            this.name = name;
            this.photo = photo;
        }
    }

    public interface PatchCurrentUserCallback {
        void onPatchCurrentUser(boolean ok);
    }

    static public void getUiLinkForFillRestaurant(String email, final getUiLinkForFillRestaurantCallback listener) {
        App.getCheckpotApi().postEmail(getAuthToken(), new EmailBody(email)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.onUiLinkForFillRestaurant(response.code() == 200);
                Log.d(TAG, "getUiLinkForFillRestaurant.onResponse: OK");
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d(TAG, "getUiLinkForFillRestaurant.onFailure: FAIL");
                listener.onUiLinkForFillRestaurant(false);
            }
        });
    }

    public static class EmailBody {
        public final String email;

        public EmailBody(String email) {
            this.email = email;
        }
    }

    public interface getUiLinkForFillRestaurantCallback {
        void onUiLinkForFillRestaurant(boolean ok);
    }

    static public void putRestaurant(String bankAccountNo,
                                     String bic,
                                     String email,
                                     String innkpp,
                                     String legalAddress,
                                     String legalManager,
                                     String legalName,
                                     String ogrn,
                                     String type,
                                     Bitmap sign,
                                     final PutRestaurantListener listener) {
        String url = API_URL + "restaurant";

        String[] documents = new String[1];
        documents[0] = getBase64(sign);

        RestaurantBody body = new RestaurantBody(bankAccountNo, bic, email,innkpp,legalAddress,legalManager,legalName,ogrn,type,documents);
        App.getCheckpotApi().putRestaurant(getAuthToken(), url, body).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call, @NonNull Response<Restaurant> response) {
                Log.d(TAG, "putRestaurant.onResponse() " + (response.body() != null ? response.body().getUuid() : null));
                listener.onPutRestaurant(response.body() != null ? response.body().getUuid() : null);
            }

            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                Log.d(TAG, "putRestaurant.onFailure() " + t);
                listener.onPutRestaurant(null);
            }
        });
    }

    public static class RestaurantBody {
        public final String bankAccountNo;
        public final String bic;
        public final String email;
        public final String innkpp;
        public final String legalAddress;
        public final String legalManager;
        public final String legalName;
        public final String ogrn;
        public final String type;
        public final String[] documents;

        public RestaurantBody(String bankAccountNo, String bic, String email, String innkpp,
                              String legalAddress, String legalManager, String legalName,
                              String ogrn, String type, String[] documents) {
            this.bankAccountNo = bankAccountNo;
            this.bic = bic;
            this.documents = documents;
            this.email = email;
            this.innkpp = innkpp;
            this.legalAddress = legalAddress;
            this.legalManager = legalManager;
            this.legalName = legalName;
            this.ogrn = ogrn;
            this.type = type;
        }
    }

    public interface PutRestaurantListener {
        void onPutRestaurant(String uuid);
    }

    static public void putPlace(String RestUuid, PlaceWithBitmaps place, final SendPlaceListener listener) {
        String url = API_URL + "place/" + RestUuid;
        Double[] coordinates = {place.getLocation().getLng(), place.getLocation().getLat()};
        List<Bitmap> photos = place.getPhotosBmp();
        String[] photos64 = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            photos64[i] = getBase64(photos.get(i));
        }
        String logo64 = getBase64(place.getLogoBmp());
        PlaceBody body = new PlaceBody(place.getAddress(), place.getName(), coordinates, place.getDescription(), photos64, logo64);
        App.getCheckpotApi().putPlace(getAuthToken(), url, body).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                listener.onSendPlace(response.body() != null ? response.body().getUuid() : null);
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.d("IRON", "putPlace.onFailure() " + t);
                listener.onSendPlace(null);
            }
        });
    }

    static public void patchPlace(String placeUuid, PlaceWithBitmaps place, final SendPlaceListener listener) {
        String url = API_URL + "place/" + placeUuid + "/patch";
        Double[] coordinates = {place.getLocation().getLng(), place.getLocation().getLat()};
        List<Bitmap> photos = place.getPhotosBmp();
        String[] photos64 = new String[photos.size()];
        for (int i = 0; i < photos.size(); i++) {
            photos64[i] = getBase64(photos.get(i));
        }
        String logo64 = getBase64(place.getLogoBmp());
        PlaceBody body = new PlaceBody(place.getAddress(), place.getName(), coordinates, place.getDescription(), photos64, logo64);
        App.getCheckpotApi().patchPlace(getAuthToken(), url, body).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(@NonNull Call<Place> call, @NonNull Response<Place> response) {
                listener.onSendPlace(response.body() != null ? response.body().getUuid() : null);
            }

            @Override
            public void onFailure(@NonNull Call<Place> call, @NonNull Throwable t) {
                Log.d("IRON", "putPlace.onFailure() " + t);
                listener.onSendPlace(null);
            }
        });
    }

    public static class PlaceBody {
        public final String address;
        public final String name;
        public final Double[] coordinates;
        public final String description;
        public final String[] photos;
        public final String logo;

        public PlaceBody(String address, String name, Double[] coordinates, String description, String[] photos, String logo) {
            this.address = address;
            this.name = name;
            this.coordinates = coordinates;
            this.description = description;
            this.photos = photos;
            this.logo = logo;
        }
    }

    public interface SendPlaceListener {
        void onSendPlace(String uuid);
    }

    static public void createEvent(String placeUuid, Event event, final CreateEventListener listener) {
        List<PrizeBody> prizes = new ArrayList<>();
        for (Prize p : event.getPrizes()) {
            prizes.add(new PrizeBody(p));
        }
        EventBody body = new EventBody(prizes, event.getStart(), event.getEnd(),
                event.getDescription(), event.getTotalCost(), event.getMembersCount(),
                event.getTariff_uuid(), event.getServices_uuid(), event.getPaymentId());

        String url = API_URL + "event/" + placeUuid;
        App.getCheckpotApi().createEvent(getAuthToken(), url, body).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(@NonNull Call<Event> call, @NonNull Response<Event> response) {
                AppLogs.setCreateEventLogs("code: " + response.code() + " message: " + response.message());
                listener.onCreateEvent(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Event> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                AppLogs.setCreateEventLogs("onFail: " + t);
                listener.onCreateEvent(null);
            }
        });
    }

    public static class EventBody {
        public final List<PrizeBody> prizes;
        public final List<String> services_uuid;
        public final long start;
        public final long end;
        public final String description;
        public final String tariff_uuid;
        public final String paymentId;
        public final long totalCost;
        public final long membersCount;

        public EventBody(List<PrizeBody> prizes, long start, long end, String description, long totalCost,
                         long membersCount, String tariff_uuid, List<String> services_uuid, String paymentId) {
            this.prizes = prizes;
            this.start = start;
            this.end = end;
            this.description = description;
            this.totalCost = totalCost;
            this.membersCount = membersCount;
            this.tariff_uuid = tariff_uuid;
            this.services_uuid = services_uuid;
            this.paymentId = paymentId;
        }
    }

    public static class PrizeBody {
        public final boolean isRandom;
        public final String name;
        public final int minReceipt;
        public final String[] photos;

        public PrizeBody(Prize p) {
            this.isRandom = p.getIsRandom();
            this.name = p.getName();
            this.minReceipt = p.getMinReceipt();
            if (p instanceof PrizeWithBitmaps)
                photos = new String[]{getBase64(((PrizeWithBitmaps) p).getPhotosBmp().get(0))};
            else photos = null;
        }
    }

    public interface CreateEventListener {
        void onCreateEvent(Event event);
    }

    public static void patchEvent(Event event, PatchEventListener listener) {

        List<PrizeBody> prizes = new ArrayList<>();
        for (Prize p : event.getPrizes()) {
            prizes.add(new PrizeBody(p));
        }
        EventBody body = new EventBody(prizes, event.getStart(), event.getEnd(),
                event.getDescription(), event.getTotalCost(), event.getMembersCount(),
                event.getTariff_uuid(), event.getServices_uuid(), event.getPaymentId());

        App.getCheckpotApi().patchEvent(getAuthToken(), event.getUuid(), body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        listener.onPatchEvent(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        listener.onPatchEvent(false);
                    }
                });
    }

    public interface PatchEventListener {
        void onPatchEvent(boolean ok);
    }

    public static void patchRestaurant(Restaurant restaurant, PatchRestaurantListener listener) {

        RestaurantBody body = new RestaurantBody(restaurant.getBankAccountNo(),
                restaurant.getBic(),
                restaurant.getEmail(),
                restaurant.getInnkpp(),
                restaurant.getLegalAddress(),
                restaurant.getLegalManager(),
                restaurant.getLegalName(),
                restaurant.getOgrn(),
                restaurant.getType(),
                null);

        App.getCheckpotApi().patchRestaurant(getAuthToken(), restaurant.getUuid(), body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        listener.onPatchRestaurant(true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        listener.onPatchRestaurant(false);
                    }
                });
    }

    public interface PatchRestaurantListener {
        void onPatchRestaurant(boolean ok);
    }

    static public void getPlacesListViaGeo(double lat, double lng, final GetPlacesListViaGeoListener listener) {
        if (MainActivity.DEBUG_MAP_PLACES && BuildConfig.DEBUG) {
            ArrayList<Place> places = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                places.add(getDebugPlace());
            }
            listener.onGetPlacesListViaGeo(places);
            return;
        }

        String url = "place/" + lng + "/" + lat + "/40000";
        App.getCheckpotApi().getPlacesListViaGeoPosition(getAuthToken(), url).enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(@NonNull Call<List<Place>> call, @NonNull Response<List<Place>> response) {
                listener.onGetPlacesListViaGeo(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Place>> call, @NonNull Throwable t) {
                Log.d("IRON", "getPlacesListViaGeo.onFailure() " + t);
                listener.onGetPlacesListViaGeo(null);
            }
        });
    }

    public interface GetPlacesListViaGeoListener {
        void onGetPlacesListViaGeo(List<Place> list);
    }

    static public void joinEvent(String uuid, Bitmap receip, final JoinEventListener listener) {
        String url = API_URL + "user/join/" + uuid;
        App.getCheckpotApi().joinEvent(getAuthToken(), url, new JoinBody(receip)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.onJoinEvent(response.code() == 200);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.joinEvent() " + t);
                listener.onJoinEvent(false);
            }
        });
    }

    public interface JoinEventListener {
        void onJoinEvent(boolean ok);
    }

    static public void sendCheck(String uuid, String check, final SendCheckListener listener) {
        String url = API_URL + "check/check" + "?" + check + "&event=" + uuid;
        App.getCheckpotApi().sendCheck(getAuthToken(), url).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Log.d(TAG, "CheckpotAPI.sendCheck() " + response.code() + " " + response.message());
                listener.onSendCheckEvent(response.code() == 200);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d(TAG, "CheckpotAPI.sendCheck() " + t);
                listener.onSendCheckEvent(false);
            }
        });
    }

    public interface SendCheckListener {
        void onSendCheckEvent(boolean ok);
    }

    static public String getEventRulesUrl(String eventUuid) {
        return API_URL + "event/rules/" + eventUuid;
    }

    static public void getTariffs(String city, final GetTariffsListener listener) {
        String url = API_URL + "user/tariffs/" + city;
        App.getCheckpotApi().getTariffs(getAuthToken(), url).enqueue(new Callback<List<Tariff>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tariff>> call, @NonNull Response<List<Tariff>> response) {
                listener.onTariffs(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Tariff>> call, @NonNull Throwable t) {
                listener.onTariffs(null);
            }
        });
    }

    public interface GetTariffsListener {
        void onTariffs(List<Tariff> tariffs);
    }

    static public void getPrices(final GetPricesListener listener) {
        String url = API_URL + "tariff/";
        App.getCheckpotApi().getPrices(getAuthToken(), url).enqueue(new Callback<Prices>() {
            @Override
            public void onResponse(@NonNull Call<Prices> call, @NonNull Response<Prices> response) {
                Log.d(TAG, "getPrices onResponse: " + response.message());
                listener.onPrices(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Prices> call, @NonNull Throwable t) {
                Log.d(TAG, "getPrices Error: " + t);
                listener.onPrices(null);
            }
        });
    }

    public interface GetPricesListener {
        void onPrices(Prices prices);
    }

    public static void patchEventDecline(String eventUuid, String userUuid, String reason, PatchEventDeclineCallback callback) {
        App.getCheckpotApi().patchEventDecline(getAuthToken(), eventUuid, new PatchEventDecline(userUuid, reason))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        callback.onDeclineEvent();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

    public interface PatchEventDeclineCallback {
        void onDeclineEvent();
    }

    public static class PatchEventDecline {
        String user_uuid;
        String reason;

        public PatchEventDecline(String user_uuid, String reason) {
            this.user_uuid = user_uuid;
            this.reason = reason;
        }
    }

    public static void patchEventAccept(String eventUuid, String userUuid, PatchEventAcceptCallback callback) {
        App.getCheckpotApi().patchEventAccept(getAuthToken(), eventUuid, new PatchEventAccept(userUuid))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        callback.onAcceptEvent();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

    public interface PatchEventAcceptCallback {
        void onAcceptEvent();
    }

    public static class PatchEventAccept {
        String user_uuid;

        public PatchEventAccept(String user_uuid) {
            this.user_uuid = user_uuid;
        }
    }

    public static void sendEventAppeal(String eventUuid, String reason, SendEventAppealCallback callback) {
        App.getCheckpotApi().sendAppeal(getAuthToken(), eventUuid, new SendEventAppeal(reason)).
                enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        callback.onAppealCallback();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        callback.onAppealCallback();
                    }
                });
    }

    public interface SendEventAppealCallback {
        void onAppealCallback();
    }

    public static class SendEventAppeal {
        public final String reason;
        public final String type = "1";

        public SendEventAppeal(String reason) {
            this.reason = reason;
        }
    }

    static public void patchTariff(String restUuid, Tariff tariff, final PatchTariffCallback callback) {
        String url = API_URL + "restaurant/" + restUuid + "/patch";

        App.getCheckpotApi().patchTariff(getAuthToken(), url, new PatchTariffBody(tariff)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                callback.onPatchTariff(response.code() == 200);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.patchTariff() " + t);
                callback.onPatchTariff(false);
            }
        });
    }


    public static class PatchTariffBody {
        public Tariff tariff;

        public PatchTariffBody(Tariff tariff) {
            this.tariff = tariff;
        }
    }

    public interface PatchTariffCallback {
        void onPatchTariff(boolean ok);
    }

    static public void getBill(String restUuid, long rub, final GetBillCallback callback) {
        String url = API_URL + "restaurant/bill/" + restUuid;

        App.getCheckpotApi().getBill(getAuthToken(), url).enqueue(new Callback<Restaurant>() {
            @Override
            public void onResponse(@NonNull Call<Restaurant> call, @NonNull Response<Restaurant> response) {
                Restaurant rest = response.body();
                if (rest == null || rest.getInvoices() == null) {
                    callback.onGetBill(null);
                    return;
                }

                for (int i = rest.getInvoices().size() - 1; i >= 0; i--) {   // Смотрим с конца
                    Invoice inv = rest.getInvoices().get(i);
                    if (!inv.getStatus() && inv.getAmout() == rub) {
                        callback.onGetBill(inv.getOrder());
                        return;
                    }
                }

                callback.onGetBill(null);
            }

            @Override
            public void onFailure(@NonNull Call<Restaurant> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.patchTariff() " + t);
                callback.onGetBill(null);
            }
        });
    }

    public interface GetBillCallback {
        void onGetBill(String orderId);
    }

    static public void submitPayment(String restUuid, String order, long paymentId, final SubmitPaymentCallback listener) {
        String url = API_URL + "bill/submit/" + restUuid;

        App.getCheckpotApi().submitPayment(getAuthToken(), url, new SubmitPaymentBody(order, paymentId)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.onSubmitPayment(response.code() == 200);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.submitPayment() " + t);
                listener.onSubmitPayment(false);
            }
        });
    }

    public static class SubmitPaymentBody {
        public final String order;
        public final long paymentId;

        public SubmitPaymentBody(String order, long paymentId) {
            this.order = order;
            this.paymentId = paymentId;
        }
    }

    public interface SubmitPaymentCallback {
        void onSubmitPayment(boolean ok);
    }

    static public void sendPromo(String email, String communityLink, final SendPromoListener listener) {
        App.getCheckpotApi().sendPromo(getAuthToken(), new PromoBody(email, communityLink)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.onSendPromo(true);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.d("IRON", "CheckpotAPI.onFailure() " + t);
                listener.onSendPromo(false);
            }
        });
    }

    public static class PromoBody {
        public final String email;
        public final String communityLink;

        public PromoBody(String email, String communityLink) {
            this.email = email;
            this.communityLink = communityLink;
        }
    }

    public interface SendPromoListener {
        void onSendPromo(boolean ok);
    }

    static private Map<String, String> getHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Authorization", getAuthToken());
        return map;
    }

    static private String getAuthToken() {
        return User.getUser().getAuthToken();
    }

    static public String getBase64(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        bitmap = cropBmpToServer(bitmap);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 99, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static class JoinBody {
        public final String receipt;

        public JoinBody(Bitmap bmp) {
            Bitmap crop = cropBmpToServer(bmp);
            receipt = getBase64(crop);
        }
    }

    public static Bitmap cropBmpToServer(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        int width = Math.min(bitmap.getWidth(), MainActivity.MAX_PHOTO_WIDTH);
        int height = (int) (bitmap.getHeight() * ((float) width / (float) bitmap.getWidth()));
        return ImageUtils.getCropedBitmap(bitmap, width, height);
    }

    public static Place getDebugPlace() {
        Place place = new Place();

        ArrayList<Double> coords = new ArrayList<>();
        coords.add(37.5848372 + Math.random() * 0.0686517);
        coords.add(55.7688388 - Math.random() * 0.0352163);
        Location loc = new Location();
        loc.setCoordinates(coords);
        place.setLocation(loc);
        place.setPhotos(Collections.singletonList("https://www.hmeli.ru/pub/11/photos/647/h500.jpg"));
        place.setAddress("Москва");
        place.setDescription("It is a debug place");
        place.setUuid("debug");
        place.setRestaurant(getDebugRestaurant());
        place.setCreatedAt("2018-08-17T10:46:32.475Z");
        place.setUpdatedAt("2018-08-17T10:46:32.475Z");
        place.setName("Test" + Math.random() * 10);


        ArrayList<Event> events = new ArrayList<>();
        int numEvents = (int) (Math.random() * 4);
        for (int i = 0; i < numEvents; i++) {
            events.add(getDebugEvent());
        }
        place.setEvents(events);

        return place;
    }

    private static Restaurant getDebugRestaurant() {
        // Указано только то, что требуется
        return new Restaurant();
    }

    private static Event getDebugEvent() {
        Event event = new Event();

        ArrayList<Prize> prizes = new ArrayList<>();
        int numPrizes = (int) (Math.random() * 4);
        for (int i = 0; i < numPrizes; i++) {
            prizes.add(getDebugPrize());
        }
        event.setPrizes(prizes);
        event.setEnd(Calendar.getInstance().getTimeInMillis());
        event.setTotalCost(1000L);
        event.setIsDone(false);
        event.setBanned(false);
        return event;
    }

    private static Prize getDebugPrize() {
        Prize prize = new Prize();
        prize.setCount(1);
        prize.setName("Prize");
        prize.setIsRandom(true);
        prize.setWinners(winners());
        return prize;
    }

    public static List<Winner> winners() {

        List<Winner> winners = new ArrayList<>();
        Winner winner = new Winner();
        winner.setId("1");
        winner.setIsPrizeDelivered(true);
        winner.setIsWinner(true);
        winners.add(winner);

        Winner winner1 = new Winner();
        winner1.setId("2");
        winner1.setIsPrizeDelivered(true);
        winner1.setIsWinner(true);
        winners.add(winner);

        return winners;
    }

    static public void postUserSign(Bitmap sign, final PostUserSignListener listener) {
        App.getCheckpotApi().postUserSign(getAuthToken(), new SignUserBody(sign)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                listener.onPostUserSign(response.code() == 200);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                listener.onPostUserSign(false);
            }
        });
    }

    public static class SignUserBody {
        public final String sign;

        public SignUserBody(Bitmap sign) {
            this.sign = getBase64(sign);
        }
    }

    public interface PostUserSignListener {
        void onPostUserSign(boolean ok);
    }

    public static void patchEventPaymentId(String eventUuid, String paymentId, patchEventPaymentIdCallback callback) {
        App.getCheckpotApi().patchEventPaymentId(getAuthToken(), eventUuid, new PatchEventPaymentId(paymentId))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        callback.onPaymentEvent();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {

                    }
                });
    }

    public interface patchEventPaymentIdCallback {
        void onPaymentEvent();
    }

    public static class PatchEventPaymentId {
        String user_uuid;

        public PatchEventPaymentId(String user_uuid) {
            this.user_uuid = user_uuid;
        }
    }

}
