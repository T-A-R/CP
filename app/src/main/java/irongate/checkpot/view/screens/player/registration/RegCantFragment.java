package irongate.checkpot.view.screens.player.registration;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import irongate.checkpot.R;
import irongate.checkpot.view.Anim;
import irongate.checkpot.view.Fonts;
import irongate.checkpot.view.ScreenFragment;

public class RegCantFragment extends ScreenFragment implements View.OnClickListener {
    static final public int PHONE_REQUEST_CODE = 1;

    private Button btn;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    TextView txtPhone;
    TextView txtMail;
    TextView txtTeleg;

    public RegCantFragment() {
        super(R.layout.fragment_reg_cant);
    }

    @Override
    protected void onReady() {
        TextView title = (TextView) findViewById(R.id.txt_register_cant_title);
        TextView desc = (TextView) findViewById(R.id.txt_register_cant_desc);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        ImageView ico1 = (ImageView) findViewById(R.id.img_register_cant_ico1);
        ImageView ico2 = (ImageView) findViewById(R.id.img_register_cant_ico2);
        ImageView ico3 = (ImageView) findViewById(R.id.img_register_cant_ico3);
        txtPhone = (TextView) findViewById(R.id.txt_register_cant_phone);
        txtMail = (TextView) findViewById(R.id.txt_register_cant_mail);
        txtTeleg = (TextView) findViewById(R.id.txt_register_cant_teleg);
        btn = (Button) findViewById(R.id.btn_register_cant);

        title.setTypeface(Fonts.getFuturaPtMedium());
        desc.setTypeface(Fonts.getFuturaPtBook());
        txtPhone.setTypeface(Fonts.getFuturaPtBook());
        txtMail.setTypeface(Fonts.getFuturaPtBook());
        txtTeleg.setTypeface(Fonts.getFuturaPtBook());
        btn.setTypeface(Fonts.getFuturaPtBook());
        btn.setTransformationMethod(null);

        title.startAnimation(Anim.getAppearSlide(getContext()));
        desc.startAnimation(Anim.getAppearSlide(getContext(), 200));
        img1.startAnimation(Anim.getAppearSlide(getContext(), 500));
        ico1.startAnimation(Anim.getAppearSlide(getContext(), 500));
        txtPhone.startAnimation(Anim.getAppearSlide(getContext(), 500));
        img2.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        ico2.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        txtMail.startAnimation(Anim.getAppearSlide(getContext(), 1000));
        img3.startAnimation(Anim.getAppearSlide(getContext(), 1500));
        ico3.startAnimation(Anim.getAppearSlide(getContext(), 1500));
        txtTeleg.startAnimation(Anim.getAppearSlide(getContext(), 1500));
        btn.startAnimation(Anim.getAppearSlide(getContext(), 2000));

        btn.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        txtPhone.setOnClickListener(this);
        txtMail.setOnClickListener(this);
        txtTeleg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            onBackPressed();
        } else if (view == img1 || view == txtPhone) {
            call();
        } else if (view == img2 || view == txtMail) {
            mail();
        } else if (view == img3 || view == txtTeleg) {
            telegram();
        }
    }

    public void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + getResources().getString(R.string.register_cant_phone_intent)));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CALL_PHONE }, PHONE_REQUEST_CODE);
            return;
        }
        getContext().startActivity(intent);
    }

    private void mail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { getResources().getString(R.string.register_cant_mail) });
        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.register_cant_mail_subj));
        startActivity(Intent.createChooser(intent, ""));
    }

    private void telegram() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.register_cant_telegram_url)));
        startActivity(browserIntent);
    }

    @Override
    public boolean onBackPressed() {
        replaceFragment(new Reg1Fragment().setEnter(true));
        return true;
    }
}
