package irongate.checkpot.view.screens.delegate.tariffAndPayment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Tariff;
import irongate.checkpot.view.Fonts;

public class TariffView extends RelativeLayout {

    private ImageView rectangle;
    private ImageView iconTariff;
    private TextView tariffPackage;
    private TextView tariffName;
    private TextView tariffPrice;
    private TextView tariffDescription;
    private ImageView icMap;
    private TextView tvTariffCountRaffles;
    private TextView tvTariffSettings;
    private TextView tvTarifSupport;
    private TextView tvMap;

    public TariffView(Context context) {
        super(context);
        initComponent();

    }

    public TariffView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public TariffView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_tariff_details, this);

        rectangle = findViewById(R.id.view_tariff);
        iconTariff = findViewById(R.id.ic_tariff);
        tariffPackage = findViewById(R.id.tariff_package);
        tariffName = findViewById(R.id.tariff_name);
        tariffPrice = findViewById(R.id.tariff_price);
        tariffDescription = findViewById(R.id.tariff_description);
        icMap = findViewById(R.id.ic_map);

        tvTariffCountRaffles = findViewById(R.id.tv_tariff_count_raffles);
        tvTariffSettings = findViewById(R.id.tv_tariff_settings);
        tvTarifSupport = findViewById(R.id.tv_tariff_support);
        tvMap = findViewById(R.id.tv_map);
        Button btnTariffSupport = findViewById(R.id.btn_tariff_support);

        tariffPackage.setTypeface(Fonts.getFuturaPtBook());
        tariffName.setTypeface(Fonts.getFuturaPtMedium());
        tariffPrice.setTypeface(Fonts.getFuturaPtMedium());
        tariffDescription.setTypeface(Fonts.getFuturaPtBook());
        tvTariffCountRaffles.setTypeface(Fonts.getFuturaPtBook());
        tvTariffSettings.setTypeface(Fonts.getFuturaPtBook());
        tvTarifSupport.setTypeface(Fonts.getFuturaPtBook());
        tvMap.setTypeface(Fonts.getFuturaPtBook());
        btnTariffSupport.setTypeface(Fonts.getFuturaPtBook());
    }

    public void setTarif(int id, Tariff tarif) {
        switch (id) {
            case 1:
                rectangle.setImageResource(R.drawable.bg_frame_simple);
                iconTariff.setImageResource(R.drawable.ic_simple);
                tariffDescription.setText(R.string.tariff_description_simple);
                break;
            case 2:
                rectangle.setImageResource(R.drawable.bg_frame_optima);
                iconTariff.setImageResource(R.drawable.ic_optima);
                tariffDescription.setText(R.string.tariff_description_optima);
                break;
            case 3:
                rectangle.setImageResource(R.drawable.bg_frame_powerfull);
                iconTariff.setImageResource(R.drawable.ic_powerful);
                tariffDescription.setText(R.string.tariff_description_powerful);
                break;
            case 4:
                rectangle.setImageResource(R.drawable.bg_frame_royal);
                iconTariff.setImageResource(R.drawable.ic_royal);
                tariffDescription.setText(R.string.tariff_description_royal);
            break;
        }

        tariffPackage.setText(R.string.tariff_package);
        tariffName.setText(tarif.getTitle());
        tariffPrice.setText(String.format("%s руб/мес", String.valueOf(tarif.getAmout())));
        int numEvents = tarif.getCount().intValue();
        tvTariffCountRaffles.setText(String.format("%s в месяц", getResources().getQuantityString(R.plurals.raffles, numEvents, numEvents)));
        tvTariffSettings.setText(tarif.getIsExtended() ? R.string.tariff_settings_max : R.string.tariff_settings_min);
        tvTarifSupport.setText(R.string.tariff_support);
        icMap.setVisibility(tarif.getIsColoredOnMap() ? VISIBLE : GONE);
        tvMap.setVisibility(tarif.getIsColoredOnMap() ? VISIBLE : GONE);
    }
}
