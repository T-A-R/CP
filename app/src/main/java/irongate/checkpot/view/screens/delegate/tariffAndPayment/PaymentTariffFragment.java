package irongate.checkpot.view.screens.delegate.tariffAndPayment;

import android.view.View;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;

public class PaymentTariffFragment extends ScreenFragment implements View.OnClickListener {

    private View contBankCard;
    private View contYandexPay;
    private View contGooglePay;
    private View contQiwe;

    public PaymentTariffFragment() {
        super(R.layout.fragment_payment_tariff);
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.payment_method_title);
        TextView desc = (TextView) findViewById(R.id.payment_method_subtitle);
        View panel = findViewById(R.id.panel);
        TextView bankCardTitle = (TextView) findViewById(R.id.bank_card_title);
        TextView bankCardSubtitle = (TextView) findViewById(R.id.bank_card_subtitle);
        TextView yandexPayTitle = (TextView) findViewById(R.id.yandex_pay_title);
        TextView yandexPaySubtitle = (TextView) findViewById(R.id.yandex_pay_subtitle);
        TextView googlePayTitle = (TextView) findViewById(R.id.google_pay_title);
        TextView googlePaySubtitle = (TextView) findViewById(R.id.google_pay_subtitle);
        TextView qiwiTitle = (TextView) findViewById(R.id.qiwi_title);
        TextView qiwiSubtitle = (TextView) findViewById(R.id.qiwi_subtitle);

        contBankCard = findViewById(R.id.cont_bank_card);
        contYandexPay = findViewById(R.id.cont_yandex_pay);
        contGooglePay = findViewById(R.id.cont_google_pay);
        contQiwe = findViewById(R.id.cont_qiwi);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        bankCardTitle.setTypeface(Fonts.getFuturaPtBook());
        bankCardSubtitle.setTypeface(Fonts.getFuturaPtBook());
        yandexPayTitle.setTypeface(Fonts.getFuturaPtBook());
        yandexPaySubtitle.setTypeface(Fonts.getFuturaPtBook());
        googlePayTitle.setTypeface(Fonts.getFuturaPtBook());
        googlePaySubtitle.setTypeface(Fonts.getFuturaPtBook());
        qiwiTitle.setTypeface(Fonts.getFuturaPtBook());
        qiwiSubtitle.setTypeface(Fonts.getFuturaPtBook());

        title.setAnimation(Anim.getAppearSlide(getContext()));
        desc.setAnimation(Anim.getAppearSlide(getContext(), 200));
        panel.setAnimation(Anim.getAppearSlide(getContext(), 500));

        contBankCard.setOnClickListener(this);
        contYandexPay.setOnClickListener(this);
        contGooglePay.setOnClickListener(this);
        contQiwe.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == contBankCard)
            replaceFragment(new BankCardFragment());
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new ChooseTariffFragment());
        return true;
    }
}
