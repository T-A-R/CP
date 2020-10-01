package irongate.checkpot.view.screens.delegate.tariffAndPayment;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import irongate.checkpot.App;
import irongate.checkpot.GPS;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Location;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.utils.TinkoffPayment;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;

public class ChooseTariffFragment extends ScreenFragment implements CheckpotAPI.GetTariffsListener {
    static public final int PAYMENT_REQUEST_CODE = 12345;
    static public final String PAYMENT_ID_KEY = "payment_id";

    private TextView subtitleTariff;
    private LinearLayout llScroll;
    private String lastOrderId;
    private List<Tariff> tariffs;
    private Tariff selectedTariff;

    public ChooseTariffFragment() {
        super(R.layout.fragment_choose_tariff);
    }

    @Override
    public boolean isMenuShown() {
        return true;
    }

    @Override
    protected void onReady() {
        TextView titleTariff = (TextView) findViewById(R.id.title_tariff);
        subtitleTariff = (TextView) findViewById(R.id.subtitle_tariff);

        llScroll = (LinearLayout) findViewById(R.id.ll_scroll);

        titleTariff.setTypeface(Fonts.getFuturaPtMedium());
        subtitleTariff.setTypeface(Fonts.getFuturaPtBook());

        showScreensaver(true);
        if(getUser() != null && getUser().getPlace() != null) {
            Location location = getUser().getPlace().getLocation();
            if(location != null)
            GPS.getAddress(getContext(), location.getLat(), location.getLng(), address -> {
                String city = address != null ? address.getLocality() : "Неизвестный";
                CheckpotAPI.getTariffs(city, this);
            });
        }
    }

    @Override
    public void onTariffs(List<Tariff> tariffs) {
        this.tariffs = tariffs;
        Context context = getContext();
        if (context == null)
            return;

        if (tariffs == null) {
            Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
            replaceFragment(new RafflesFragment());
            return;
        }

        hideScreensaver();

        for (int i = 0; i < tariffs.size(); i++) {
            final int index = i;
            TariffView tariffView = new TariffView(context);
            subtitleTariff.post(() -> {
                int width = subtitleTariff.getWidth();
                LinearLayout.LayoutParams paramsSimple = (LinearLayout.LayoutParams) tariffView.getLayoutParams();
                paramsSimple.width = width - 35;
                tariffView.setLayoutParams(paramsSimple);
            });
            tariffView.setTarif(index + 1, tariffs.get(index));
            llScroll.addView(tariffView);

            Button btnSupport = tariffView.findViewById(R.id.btn_tariff_support);
            btnSupport.setOnClickListener(v -> onPayBtn(index));
        }
    }

    public void onPayBtn(int tariffIndex) {
        showScreensaver(true);

        selectedTariff = tariffs.get(tariffIndex);
        CheckpotAPI.patchTariff(getUser().getRestaurant().getUuid(), selectedTariff, this::onPatchTariff);
    }

    private void onPatchTariff(boolean ok) {
        if (!ok) {
            hideScreensaver();
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
            return;
        }

        CheckpotAPI.getBill(getUser().getRestaurant().getUuid(), selectedTariff.getAmout(), this::onGetBill);
    }

    private void onGetBill(String orderId) {
        if (orderId == null) {
            hideScreensaver();
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
            return;
        }

        lastOrderId = orderId;

        //TinkoffPayment.startPaymentActivity(getActivity(), lastOrderId, selectedTariff.getTitle(), selectedTariff.getAmout(), PAYMENT_REQUEST_CODE);
    }

    public void onTinkoffResult(int resultCode, Intent data) {
        if (resultCode == 0) {
            hideScreensaver();
            return;
        }

        if (data == null || data.getExtras() == null || data.getExtras().get(PAYMENT_ID_KEY) == null) {
            Toast.makeText(getContext(), "Не удалось провести платеж", Toast.LENGTH_SHORT).show();
            hideScreensaver();
            return;
        }

        long payment_id = (long) data.getExtras().get("payment_id");
        CheckpotAPI.submitPayment(getUser().getRestaurant().getUuid(), lastOrderId, payment_id, ChooseTariffFragment.this::onSubmitPayment);
    }

    private void onSubmitPayment(boolean ok) {
        if (!ok) {
            hideScreensaver();
            Toast.makeText(getContext(), R.string.error_submit, Toast.LENGTH_SHORT).show();
            return;
        }
        App.getMetricaLogger().log( "Оунер купил первый тариф");
        Toast.makeText(getContext(), "Оплачено", Toast.LENGTH_SHORT).show();
        getUser().updateUser(ok1 -> replaceFragment(new RafflesFragment()));
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new RafflesFragment());
        return true;
    }
}
