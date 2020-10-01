package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import irongate.checkpot.App;
import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.CheckpotAPI;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Prices;
import irongate.checkpot.checkpotAPI.models.TariffOfEvent;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.TinkoffPayment;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.screens.delegate.RaffleSuccessfullyCreatedFragment;

import static irongate.checkpot.MainActivity.TAG;

public class PaymentFragment extends ScreenFragment implements AddsAdapter.AddsListener, CheckpotAPI.CreateEventListener, CheckpotAPI.PatchEventListener {

    private Event event;
    private Prices prices;
    private int addsPrice = 0;
    private int totalPrice = 0;

    private TextView title;
    private TextView txtType;
    private TextView tariff1;
    private TextView tariff2;
    private EditText editMember;
    private RadioGroup radioGroup;
    private RadioButton radioBtnOne;
    private RadioButton radioBtnTwo;
    private RadioButton radioBtnThree;
    private TextView txtxAdds;
    private ListView listAdds;
    private TextView txtPrice;
    private TextView txtTotal;
    private Button btnPublication;
    private TextView txtBack;

    private boolean isTariff1Pressed = false;
    private boolean isAdd1Pressed = false;
    private TariffOfEvent tariff;
    private boolean[] services;
    private List<String> namesForReceipt = new ArrayList<>();
    private List<Long> pricesForReceipt = new ArrayList<>();

    private AddsAdapter adapter;

    final int sdk = android.os.Build.VERSION.SDK_INT;

    static public final int PAYMENT_REQUEST_CODE = 12345;
    static public final String PAYMENT_ID_KEY = "payment_id";

    public PaymentFragment() {
        super(R.layout.fragment_payment);
    }

    public PaymentFragment setData(Event event, Prices prices) {
        this.event = event;
        this.prices = prices;
        return this;
    }


    @Override
    protected void onReady() {

        title = (TextView) findViewById(R.id.title);
        txtType = (TextView) findViewById(R.id.txt_type_raffles);
        tariff1 = (TextView) findViewById(R.id.tariff1);
        tariff2 = (TextView) findViewById(R.id.tariff2);
        editMember = (EditText) findViewById(R.id.edit_member);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioBtnOne = (RadioButton) findViewById(R.id.radio_btn_one);
        radioBtnTwo = (RadioButton) findViewById(R.id.radio_btn_two);
        radioBtnThree = (RadioButton) findViewById(R.id.radio_btn_three);
        txtxAdds = (TextView) findViewById(R.id.txt_amt_member);
        listAdds = (ListView) findViewById(R.id.listview_adds);
        txtPrice = (TextView) findViewById(R.id.txt_price);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        btnPublication = (Button) findViewById(R.id.btn_publication);
        txtBack = (TextView) findViewById(R.id.txt_back_to_step_one);

        title.setTypeface(Fonts.getFuturaPtMedium());
        txtType.setTypeface(Fonts.getFuturaPtBook());
        tariff1.setTypeface(Fonts.getFuturaPtBook());
        tariff2.setTypeface(Fonts.getFuturaPtBook());
        editMember.setTypeface(Fonts.getFuturaPtBook());
        radioBtnOne.setTypeface(Fonts.getFuturaPtBook());
        radioBtnTwo.setTypeface(Fonts.getFuturaPtBook());
        radioBtnThree.setTypeface(Fonts.getFuturaPtBook());
        txtxAdds.setTypeface(Fonts.getFuturaPtBook());
        txtPrice.setTypeface(Fonts.getFuturaPtMedium());
        txtTotal.setTypeface(Fonts.getFuturaPtBook());
        btnPublication.setTypeface(Fonts.getFuturaPtBook());
        txtBack.setTypeface(Fonts.getFuturaPtBook());

        adapter = new AddsAdapter(getContext(), prices.getServices());
        adapter.setListener(this);

        final float scale = getContext().getResources().getDisplayMetrics().density;

        CardView.LayoutParams params = (CardView.LayoutParams) listAdds.getLayoutParams();
        params.height = (int) ((80 * scale + 0.5f) * prices.getServices().size());
        listAdds.setLayoutParams(params);
        listAdds.setAdapter(adapter);

        MainFragment.disableSideMenu();
        showMenu();
        showPriceList();
        setTotalPrice();


        tariff1.setOnClickListener(v -> onTypePressed());
        tariff2.setOnClickListener(v -> onTypePressed());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> setTotalPrice());
        btnPublication.setOnClickListener(v -> onPublicationPressed());
        txtBack.setOnClickListener(v -> onStepBackPressed());

        editMember.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                checkMembersCounter(editable.toString());
            }
        });

        if (prices != null) {
            boolean servicesIsEmpty = true;
            if (prices.getServices() != null) {
                services = new boolean[prices.getServices().size()];

                for (int i = 0; i < prices.getServices().size(); i++) {
                    if (prices.getServices().get(i).isEnabled())
                        servicesIsEmpty = false;
                }
            }
            if (prices.getServices().size() == 0 || prices.getServices() == null || servicesIsEmpty)
                txtxAdds.setVisibility(View.GONE);
        }
//        onTypePressed();
        onTypePressed();
//        setTotalPrice();

    }

    @SuppressLint("SetTextI18n")
    private void onTypePressed() {
        if (prices != null) {
            if (!isTariff1Pressed) {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tariff1.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_cardview_blue_left_8));
                } else {
                    tariff1.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_cardview_blue_left_8));
                }
                tariff2.setBackgroundColor(Color.TRANSPARENT);
                tariff1.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorWhite));
                tariff2.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                editMember.setVisibility(View.GONE);

                radioBtnTwo.setVisibility(View.VISIBLE);

                if (prices.getTariffs().get(0) != null)
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(0).isCanBeFree())
                            radioBtnOne.setText(prices.getTariffs().get(0).getName() + " (" + (int) prices.getTariffs().get(0).getPrice() + " руб)");
                        else
                            radioBtnOne.setText(prices.getTariffs().get(0).getName() + " (бесплатно)");
                if (prices.getTariffs().get(1) != null)
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(1).isCanBeFree())
                            radioBtnTwo.setText(prices.getTariffs().get(1).getName() + " (" + (int) prices.getTariffs().get(1).getPrice() + " руб)");
                        else
                            radioBtnTwo.setText(prices.getTariffs().get(1).getName() + " (бесплатно)");
                if (prices.getTariffs().get(2) != null)
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(2).isCanBeFree())
                            radioBtnThree.setText(prices.getTariffs().get(2).getName() + " (" + (int) prices.getTariffs().get(2).getPrice() + " руб)");
                        else
                            radioBtnThree.setText(prices.getTariffs().get(2).getName() + " (бесплатно)");

                radioBtnOne.setChecked(true);
                radioGroup.setActivated(true);
                enableRadioGroup(true);

                isTariff1Pressed = true;
            } else {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    tariff2.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_cardview_blue_right_8));
                } else {
                    tariff2.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.bg_cardview_blue_right_8));
                }
                tariff1.setBackgroundColor(Color.TRANSPARENT);
                tariff2.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorWhite));
                tariff1.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                editMember.setVisibility(View.VISIBLE);

                radioBtnTwo.setVisibility(View.GONE);

                if (prices.getTariffs().get(3) != null)
                    radioBtnOne.setText(prices.getTariffs().get(3).getName() + " (" + (int) prices.getTariffs().get(3).getPrice() + " руб)");
                if (prices.getTariffs().get(4) != null)
                    radioBtnThree.setText(prices.getTariffs().get(4).getName() + " (" + (int) prices.getTariffs().get(4).getPrice() + " руб)");

                radioBtnOne.setChecked(true);
                if (editMember.getText() != null)
                    checkMembersCounter(editMember.getText().toString());
                enableRadioGroup(false);

                isTariff1Pressed = false;
            }
            setTotalPrice();
        } else {
            Toast.makeText(getContext(), R.string.error_prices, Toast.LENGTH_SHORT).show();
        }
    }

    private void onStepBackPressed() {
        replaceFragment(new AddRaffleFragment().setEvent(event));
    }

    private void onPublicationPressed() {

        if (tariff != null) {
            if (!isTariff1Pressed && !checkMembersCounter(editMember.getText().toString())) {
                editMember.setError("Введите число от 1 до 1000");
                Toast.makeText(getContext(), R.string.members_error, Toast.LENGTH_SHORT).show();
                return;
            }

            long membersCount = isTariff1Pressed ? 0 : Integer.parseInt(editMember.getText().toString());

            Date dateBegin = new Date(System.currentTimeMillis());

            long start = dateBegin.getTime() / 1000;
            long end = !isTariff1Pressed ? 0 : ((dateBegin.getTime() / 1000) + (86400 * tariff.getDays()));

            event.setStart(start);
            event.setEnd(end);
            event.setMembersCount(membersCount);
            event.setTariff_uuid(tariff.getUuid());
            List<String> services_uuids = new ArrayList<>();
            for (int i = 0; i < services.length; i++)
                if (services[i])
                    services_uuids.add(prices.getServices().get(i).getUuid());
            event.setServices_uuid(services_uuids);

            if (event.getUuid() == null)
                new Thread(this::submit).start();
            else
                new Thread(this::patchEvent).start();
//            Toast.makeText(getContext(), "OK!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new AddRaffleFragment().setEvent(event));
        return true;
    }

    private void setTotalPrice() {
        int price = 0 + addsPrice;
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (isAdd1Pressed) price = price + 1200;

        if (isTariff1Pressed) {
            switch (checkedRadioButtonId) {
                case R.id.radio_btn_one:
                    tariff = prices.getTariffs().get(0);
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(0).isCanBeFree())
                            price = price + (int) tariff.getPrice();

                    radioBtnOne.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                    radioBtnTwo.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    radioBtnThree.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    break;
                case R.id.radio_btn_two:
                    tariff = prices.getTariffs().get(1);
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(1).isCanBeFree())
                            price = price + (int) tariff.getPrice();
                    radioBtnTwo.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                    radioBtnOne.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    radioBtnThree.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    break;
                case R.id.radio_btn_three:
                    tariff = prices.getTariffs().get(2);
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(2).isCanBeFree())
                            price = price + (int) tariff.getPrice();
                    radioBtnThree.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                    radioBtnTwo.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    radioBtnOne.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    break;
                default:
                    break;
            }
        } else {
            switch (checkedRadioButtonId) {
                case R.id.radio_btn_one:
                    tariff = prices.getTariffs().get(3);
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(3).isCanBeFree())
                            price = price + (int) tariff.getPrice();
                    radioBtnOne.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                    radioBtnTwo.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    radioBtnThree.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    break;
                case R.id.radio_btn_two:
                    break;
                case R.id.radio_btn_three:
                    tariff = prices.getTariffs().get(4);
                    if (getUser().getRestaurant() != null && getUser().getRestaurant().getFreeEvents() != null)
                        if (getUser().getRestaurant().getFreeEvents() == 0 || !prices.getTariffs().get(4).isCanBeFree())
                            price = price + (int) tariff.getPrice();
                    radioBtnThree.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.blue));
                    radioBtnTwo.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    radioBtnOne.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.darkTint));
                    break;
                default:
                    break;
            }
        }


        if (price == 0) txtPrice.setText("Бесплатно");
        else txtPrice.setText("" + price + " руб");
        totalPrice = price;

    }

    private void enableRadioGroup(boolean sw) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(sw);
        }
    }

    private boolean checkMembersCounter(String text) {
        if (text.isEmpty()) {
            return false;
        }
        int number = Integer.valueOf(text);
        if (number < 1 || number > 1000) {
            editMember.setError("Введите число от 1 до 1000");
            return false;
        }
        if (number >= 1 && number < 100) {
            radioBtnOne.setChecked(true);
            return true;

        }
        if (number >= 100 && number <= 1000) {
            radioBtnThree.setChecked(true);
            return true;
        }
        return false;
    }

    private void showPriceList() {
        if (prices != null) {
            Log.d(TAG, "My free events: " + getUser().getRestaurant().getFreeEvents());
            Log.d(TAG, "------------------------------- TARIFFS ----------------------------------------");
            for (int i = 0; i < prices.getTariffs().size(); i++) {
                Log.d(TAG, "showPriceList: " + i +
                        ", type: " + prices.getTariffs().get(i).getType() +
                        ", name: " + prices.getTariffs().get(i).getName() +
                        ", price: " + prices.getTariffs().get(i).getPrice() +
                        ", days: " + prices.getTariffs().get(i).getDays() +
                        ", members: " + prices.getTariffs().get(i).getMaxMembers() +
                        ", isFree: " + prices.getTariffs().get(i).isCanBeFree());
            }
            Log.d(TAG, "------------------------------- SERVICES ----------------------------------------");
            for (int i = 0; i < prices.getServices().size(); i++) {
                Log.d(TAG, "showPriceList: " + i +
                        ", name: " + prices.getServices().get(i).getName() +
                        ", price: " + prices.getServices().get(i).getPrice() +
                        ", desc: " + prices.getServices().get(i).getDescription() +
                        ", days: " + prices.getServices().get(i).getDays() +
                        ", enabled: " + prices.getServices().get(i).isEnabled() +
                        ", image: " + prices.getServices().get(i).getImage());
            }
        } else
            Log.d(TAG, "showPriceList: ERROR! prices = null");
    }

    @Override
    public void onAddPress(int position, boolean isPressed) {
        if (isPressed) {
            addsPrice = addsPrice + (int) prices.getServices().get(position).getPrice();
            services[position] = true;
        } else {
            addsPrice = addsPrice - (int) prices.getServices().get(position).getPrice();
            services[position] = false;
        }
        setTotalPrice();
    }

    private void submit() {
        showScreensaver(false);
        CheckpotAPI.createEvent(User.getUser().getPlace().getUuid(), event, this);
    }

    @Override
    public void onCreateEvent(Event eventFromServer) {
        final Activity activity = getActivity();
        if (activity == null)
            return;

        if (eventFromServer == null) {
            hideScreensaver();
            activity.runOnUiThread(() -> Toast.makeText(getContext(), R.string.add_raffles_fail, Toast.LENGTH_SHORT).show());
            return;
        }
        event = eventFromServer;
        if (totalPrice != 0) {
            payment(eventFromServer.getUuid());
        } else {
            finishCreatingEvent();
        }
    }

    private void finishCreatingEvent() {
        App.getMetricaLogger().log("Оунер опубликовал первый розыгрыш");
        getUser().updateUser(ok -> {
            hideScreensaver();
            event.setPlace(getUser().getPlace());
            replaceFragment(new RaffleSuccessfullyCreatedFragment().setEvent(event));
        });
    }

    private void payment(String orderId) {
        countingTariff();
        TinkoffPayment.startPaymentActivity(getActivity(), orderId, namesForReceipt, pricesForReceipt, (long) totalPrice, PAYMENT_REQUEST_CODE);
    }

    private void countingTariff() {
        namesForReceipt.add(tariff.getName());
        pricesForReceipt.add((long) tariff.getPrice());
        for (int i = 0; i < services.length; i++) {
            if (services[i]) {
                namesForReceipt.add(prices.getServices().get(i).getName());
                pricesForReceipt.add((long) prices.getServices().get(i).getPrice());
            }
        }
    }

    public void onTinkoffResult(int resultCode, Intent data) {
        hideScreensaver();
        if (resultCode == 0) {
            hideScreensaver();
            return;
        }

        if (data == null || data.getExtras() == null || data.getExtras().get(PAYMENT_ID_KEY) == null) {
            if (data == null) {
                Toast.makeText(getContext(), "Не удалось провести платеж (error 1)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.getExtras() == null) {
                Toast.makeText(getContext(), "Не удалось провести платеж (error 2)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.getExtras().get(PAYMENT_ID_KEY) == null) {
                Toast.makeText(getContext(), "Не удалось провести платеж (error 2)", Toast.LENGTH_SHORT).show();
                hideScreensaver();
                return;
            }
            hideScreensaver();
            return;
        }

//        String payment_id = data.getExtras().get("payment_id").toString();
//        Log.d(TAG, "$$$ onTinkoffResult. PaymentId: " + payment_id);
//        event.setPaymentId(payment_id);
        finishCreatingEvent();
    }

    @Override
    public void onResume() {
        hideScreensaver();
        super.onResume();
    }

    public void patchEvent() {
        showScreensaver(false);
        CheckpotAPI.patchEvent(event, this);
    }

    @Override
    public void onPatchEvent(boolean ok) {
        if (ok)
            onCreateEvent(event);
        else
            onCreateEvent(null);
    }
}
