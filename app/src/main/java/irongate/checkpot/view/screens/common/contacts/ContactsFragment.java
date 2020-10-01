package irongate.checkpot.view.screens.common.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.App;
import irongate.checkpot.MainActivity;
import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.MainFragment;
import irongate.checkpot.view.ScreenFragment;
import irongate.checkpot.view.SmartFragment;
import irongate.checkpot.view.screens.delegate.aboutWinner.WinnersFragment;
import irongate.checkpot.view.screens.player.advantage.AdvantageFragment;
import irongate.checkpot.view.screens.player.map.MapFragment;
import irongate.checkpot.view.screens.player.rafles.RafflesFragment;
import irongate.checkpot.view.screens.player.registration.Reg1Fragment;

public class ContactsFragment extends ScreenFragment implements SmartFragment.Listener {
    private Button btn;

    private ContactsHelpFragment fragHelp;

    public ContactsFragment() {
        super(R.layout.fragment_contacts);
    }

    @Override
    protected void onReady() {
        ImageView bg = (ImageView) findViewById(R.id.bg);
        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = (TextView) findViewById(R.id.desc);
        View contMail = findViewById(R.id.cont_mail);
        View contFb = findViewById(R.id.cont_fb);
        View contVk = findViewById(R.id.cont_vk);
        View contTeleg = findViewById(R.id.cont_teleg);
        TextView txtPhone = (TextView) findViewById(R.id.txt_phone);
        TextView txtMail = (TextView) findViewById(R.id.txt_mail);
        TextView txtFB = (TextView) findViewById(R.id.txt_fb);
        TextView txtVK = (TextView) findViewById(R.id.txt_vk);
        TextView txtTeleg = (TextView) findViewById(R.id.txt_teleg);
        btn = (Button) findViewById(R.id.btn);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        txtPhone.setTypeface(Fonts.getFuturaPtBook());
        txtMail.setTypeface(Fonts.getFuturaPtBook());
        txtFB.setTypeface(Fonts.getFuturaPtBook());
        txtVK.setTypeface(Fonts.getFuturaPtBook());
        txtTeleg.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        if (!MainActivity.EVOTOR_MODE)
            showMenu();
        MainFragment.disableSideMenu();

        bg.setImageResource(isDelegateScreen() ? R.color.iceBlue : R.drawable.bg_default);
        btn.setBackgroundResource(isDelegateScreen() ? R.drawable.bg_btn_blue_36 : R.drawable.bg_btn_green_36);
        btn.setText(isDelegateScreen() ? R.string.contacts_btn_deleg : R.string.contacts_btn_player);

        txtMail.setText(isDelegateScreen() ? R.string.register_cant_mail_owner : R.string.register_cant_mail);
        //contFb.setVisibility(isDelegateScreen() ? View.VISIBLE : View.GONE);
        //contVk.setVisibility(isDelegateScreen() ? View.VISIBLE : View.GONE);
        txtTeleg.setText(isDelegateScreen() ? R.string.register_cant_telegram_owner : R.string.register_cant_telegram);

        title.startAnimation(Anim.getAppearSlide(getContext()));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        contMail.startAnimation(Anim.getAppearSlide(getContext(), 400));
        contFb.startAnimation(Anim.getAppearSlide(getContext(), 500));
        contVk.startAnimation(Anim.getAppearSlide(getContext(), 600));
        contTeleg.startAnimation(Anim.getAppearSlide(getContext(), 700));

        btn.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        btnBack.startAnimation(Anim.getAppearSlide(getContext(), 1000));

        btnBack.setOnClickListener(v -> onBackPressed());
        contMail.setOnClickListener(v -> mail());
        contFb.setOnClickListener(v -> facebook());
        contVk.setOnClickListener(v -> vkontakte());
        contTeleg.setOnClickListener(v -> telegram());
        btn.setOnClickListener(v -> {
            onBtn();
            App.getMetricaLogger().log("Для бизнеса");
        });
    }

    private void showHelp() {
        FragmentActivity activity = getActivity();
        if (activity == null)
            return;

        btn.setEnabled(false);
        fragHelp = new ContactsHelpFragment();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.cont_panel, fragHelp).commit();
        fragHelp.setListener(this);
    }

    @Override
    public void fragmentIntent(SmartFragment fragment, String intent) {
        Activity activity = getActivity();
        if (activity == null || fragHelp == null)
            return;

        fragHelp.setListener(null);
        getActivity().getSupportFragmentManager().beginTransaction().remove(fragHelp).commit();
        fragHelp = null;

        btn.setEnabled(true);
    }

    /*   public void call() {
          Intent intent = new Intent(Intent.ACTION_CALL);
           intent.setData(Uri.parse("tel:" + getResources().getString(R.string.register_cant_phone_intent)));
           if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CALL_PHONE }, PHONE_REQUEST_CODE);
               return;
           }
           getContext().startActivity(intent);
       }
   */
    private void mail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        int mailRes = isDelegateScreen() ? R.string.register_cant_mail_owner : R.string.register_cant_mail;
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(mailRes)});
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.register_cant_mail_subj));
        startActivity(Intent.createChooser(intent, ""));
    }

    private void telegram() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(isDelegateScreen() ? R.string.register_cant_telegram_owner_url : R.string.register_cant_telegram_url)));
        startActivity(browserIntent);
    }

    private void facebook() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.register_cant_facebook_url)));
        startActivity(intent);
    }

    private void vkontakte() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.register_cant_vkontakte_url)));
        startActivity(intent);
    }

    private void onBtn() {
        if (!getUser().isAuthorized())
            replaceFragment(new Reg1Fragment().setEnter(true));
        else if (isDelegateScreen())
            showHelp();
        else {
            if (getUser().getRestaurant() == null) {
                replaceFragment(new AdvantageFragment());
            } else {
                getUser().setDelegateMode(true);
                replaceFragment(new RafflesFragment());
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        MainFragment.enableSideMenu();
//        replaceFragment(MainActivity.EVOTOR_MODE ? new WinnersFragment() : isDelegateScreen() ? new RafflesFragment() : new MapFragment());
        replaceFragment(MainActivity.EVOTOR_MODE ? new WinnersFragment() : new RafflesFragment());
        return true;
    }
}
