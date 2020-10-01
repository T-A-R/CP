package irongate.checkpot.utils;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.model.User;
import ru.tinkoff.acquiring.sdk.Item;
import ru.tinkoff.acquiring.sdk.Money;
import ru.tinkoff.acquiring.sdk.PayFormActivity;
import ru.tinkoff.acquiring.sdk.Receipt;
import ru.tinkoff.acquiring.sdk.Tax;
import ru.tinkoff.acquiring.sdk.Taxation;

public class TinkoffPayment {

    static private final String TERMINAL_KEY = "1548047723810";
    static private final String PASSWORD = "q3sb41tp2obgr2u3";
    static private final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv5yse9ka3ZQE0feuGtemYv3IqOlLck8zHUM7lTr0za6lXTszRSXfUO7jMb+L5C7e2QNFs+7sIX2OQJ6a+HG8kr+jwJ4tS3cVsWtd9NXpsU40PE4MeNr5RqiNXjcDxA+L4OsEm/BlyFOEOh2epGyYUd5/iO3OiQFRNicomT2saQYAeqIwuELPs1XpLk9HLx5qPbm8fRrQhjeUD5TLO8b+4yCnObe8vy/BMUwBfq+ieWADIjwWCMp2KTpMGLz48qnaD9kdrYJ0iyHqzb2mkDhdIzkim24A3lWoYitJCBrrB2xM05sm9+OdCI1f7nPNJbl5URHobSwR94IRGT7CJcUjvwIDAQAB";

    static private final String DEBUG_TERMINAL_KEY = "1548047723810DEMO";
    static private final String DEBUG_PASSWORD = "nvpb2nadxr8i9m6r";
    static private final String DEBUG_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv5yse9ka3ZQE0feuGtemYv3IqOlLck8zHUM7lTr0za6lXTszRSXfUO7jMb+L5C7e2QNFs+7sIX2OQJ6a+HG8kr+jwJ4tS3cVsWtd9NXpsU40PE4MeNr5RqiNXjcDxA+L4OsEm/BlyFOEOh2epGyYUd5/iO3OiQFRNicomT2saQYAeqIwuELPs1XpLk9HLx5qPbm8fRrQhjeUD5TLO8b+4yCnObe8vy/BMUwBfq+ieWADIjwWCMp2KTpMGLz48qnaD9kdrYJ0iyHqzb2mkDhdIzkim24A3lWoYitJCBrrB2xM05sm9+OdCI1f7nPNJbl5URHobSwR94IRGT7CJcUjvwIDAQAB";

    static public void startPaymentActivity(Activity activity, String orderId, List<String> tariffNames, List<Long> prices, Long amount, int requestCode) {
        if(!MainActivity.DEBUG_API_ADDRESS) {
            String email = User.getUser().getRestaurant().getEmail();
            PayFormActivity.init(TERMINAL_KEY, PASSWORD, PUBLIC_KEY)
                    .prepare(orderId, Money.ofRubles(amount), "Оплата выбранного тарифа", "", null, email, false, true)
                    .setReceipt(createReceipt(tariffNames, prices, email))
                    .startActivityForResult(activity, requestCode);
        } else {
            String email = User.getUser().getRestaurant().getEmail();
            PayFormActivity.init(DEBUG_TERMINAL_KEY, DEBUG_PASSWORD, DEBUG_PUBLIC_KEY)
                    .prepare(orderId, Money.ofRubles(amount), "Оплата выбранного тарифа", "", null, email, false, true)
                    .setReceipt(createReceipt(tariffNames, prices, email))
                    .startActivityForResult(activity, requestCode);
        }

    }

    static private Receipt createReceipt(List<String> tariffNames, List<Long> prices, String email) {
//        Item[] items = new Item[] {
//                new Item(tariffNames, price, 1, price, Tax.NONE),
//        };
        Item[] items = new Item[tariffNames.size()];
        for (int i = 0; i < tariffNames.size(); i++) {
            items[i] = new Item(tariffNames.get(i), prices.get(i) * 100, 1, prices.get(i) * 100, Tax.NONE);
        }
        return new Receipt(items, email, Taxation.USN_INCOME_OUTCOME);
    }
}
