package irongate.checkpot.view.screens.delegate.tariffAndPayment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;

public class BankCardFragment extends ScreenFragment {

    private EditText etNumberCard;
    private EditText etValidityCard;
    private EditText etCVV;
    private EditText etOwnerCard;

    public BankCardFragment() {
        super(R.layout.fragment_bank_card);
    }

    @Override
    protected void onReady() {
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.card_title);
        TextView desc = (TextView) findViewById(R.id.card_description);
        View panel = findViewById(R.id.panel);
        TextView tvNumberCard = (TextView) findViewById(R.id.tv_number_card);
        etNumberCard = (EditText) findViewById(R.id.et_number_card);
        TextView tvValidityCard = (TextView) findViewById(R.id.tv_validity_card);
        etValidityCard = (EditText) findViewById(R.id.et_validity_card);
        TextView tvCVV = (TextView) findViewById(R.id.tv_cvv);
        etCVV = (EditText) findViewById(R.id.et_cvv);
        TextView tvOwnerCard = (TextView) findViewById(R.id.tv_card_owner);
        etOwnerCard = (EditText) findViewById(R.id.et_card_owner);
        Button btnAdd = (Button) findViewById(R.id.btn_add);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        tvNumberCard.setTypeface(Fonts.getFuturaPtBook());
        etNumberCard.setTypeface(Fonts.getFuturaPtBook());
        tvValidityCard.setTypeface(Fonts.getFuturaPtBook());
        etValidityCard.setTypeface(Fonts.getFuturaPtBook());
        tvCVV.setTypeface(Fonts.getFuturaPtBook());
        etCVV.setTypeface(Fonts.getFuturaPtBook());
        tvOwnerCard.setTypeface(Fonts.getFuturaPtBook());
        etOwnerCard.setTypeface(Fonts.getFuturaPtBook());
        btnAdd.setTypeface(Fonts.getFuturaPtBook());

        btnBack.setOnClickListener(v -> onBackPressed());
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayCardData payCardData = new PayCardData();
                payCardData.setCardNumber(etNumberCard.getText().toString());
                payCardData.setCardExpDate(etValidityCard.getText().toString());
                payCardData.setCardCvv(etCVV.getText().toString());
                payCardData.setCardHolderName(etOwnerCard.getText().toString());
                Bundle bundle = new Bundle();
                bundle.putSerializable("payCardData" , payCardData);
                PayOnlineUtil.onPay(payCardData);
            }
        });

        title.setAnimation(Anim.getAppearSlide(getContext()));
        desc.setAnimation(Anim.getAppearSlide(getContext(), 200));
        panel.setAnimation(Anim.getAppearSlide(getContext(), 500));
        btnBack.setAnimation(Anim.getAppear(getContext(), 1000));
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new PaymentTariffFragment());
        return true;
    }
}
