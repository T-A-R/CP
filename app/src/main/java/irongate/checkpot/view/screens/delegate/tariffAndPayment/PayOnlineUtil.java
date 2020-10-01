package irongate.checkpot.view.screens.delegate.tariffAndPayment;

import java.net.URI;
import java.net.URISyntaxException;

import ru.payonline.sdk.DefaultPaymentSetting;
import ru.payonline.sdk.PayRequest;
import ru.payonline.sdk.PayResponse;
import ru.payonline.sdk.PaymentClient;
import ru.payonline.sdk.PaymentClientException;
import ru.payonline.sdk.PaymentLinkRequest;
import ru.payonline.sdk.Process3DsRequest;
import ru.payonline.sdk.Process3DsResponse;

public class PayOnlineUtil {

    public static void onPay(PayCardData payCardData) {

        DefaultPaymentSetting setting = null;
        try {
            setting = new DefaultPaymentSetting(new URI("https://secure.payonlinesystem.com/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        PaymentLinkRequest requestLink = new PaymentLinkRequest();
        requestLink.setMerchantId(92574);

        PaymentClient client = new PaymentClient();
        try {
            String paymentFormLink = client.getPaymentLink(requestLink, setting);
        } catch (PaymentClientException e) {
            e.printStackTrace();
        }

        PayRequest request = new PayRequest();
        request.setMerchantId(92574);
        request.setOrderId("ORDER-1983F5");
        request.setAmount(1590.40);
        request.setCurrency("RUB");
        request.setPrivateSecurityKey("0039f27b-d7af-423e-99f9-1d40f99e8195");
        request.setEmail("test@test.com");
        request.setIp("127.0.0.1");
        request.setCardHolderName(payCardData.getCardHolderName());
        request.setCardNumber(payCardData.getCardNumber());
        request.setCardExpDate(payCardData.getCardExpDate());
        request.setCardCvv(payCardData.getCardCvv());

        //PaymentClient clientPay = new PaymentClient();
        try {
            PayResponse response = client.pay(request);
        } catch (PaymentClientException e) {
            e.printStackTrace();
        }

        PayResponse response = null;
        try {
            response = client.pay(request);
        } catch (PaymentClientException e) {
            e.printStackTrace();
        }

     //   PaymentClient client = new PaymentClient();
        PayResponse responsePay = null;
        try {
            responsePay = client.pay(request);
        } catch (PaymentClientException e) {
            e.printStackTrace();
        }

       if (responsePay.getCode() == 6001 && responsePay.getResult() == "Error") {

           Process3DsRequest request3Ds = new Process3DsRequest();
           request3Ds.setMerchantId(92574);
           request3Ds.setTransactionId(responsePay.getId());
           request3Ds.setPares("значение, полученное от банка-эмитента");
           request3Ds.setPd(responsePay.getPd());
           request3Ds.setPrivateSecurityKey("0039f27b-d7af-423e-99f9-1d40f99e8195");

           PaymentClient client3Ds = new PaymentClient();
           try {
               Process3DsResponse response3Ds = client3Ds.process3Ds(request3Ds);
           } catch (PaymentClientException e) {
               e.printStackTrace();
           }
       } else {

       }
    }
}
