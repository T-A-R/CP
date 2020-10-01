package irongate.checkpot.view.screens.delegate.addedRaffle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Service;

import static irongate.checkpot.MainActivity.TAG;

public class AddsAdapter extends BaseAdapter {

    private Context context;
    private List<Service> services;
    private boolean[] isPressed;

    AddsListener listener;


    public AddsAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
        isPressed = new boolean[services.size()];
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.add_item_view, null);
        TextView name = (TextView) v.findViewById(R.id.txt_add);
        TextView desc = (TextView) v.findViewById(R.id.txt_add_price);
        ImageView img = (ImageView) v.findViewById(R.id.img_add);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        isPressed[position] = false;

        Log.d(TAG, "getView: " + position);

        if (services != null) {
            String url = services.get(position).getImage();
            name.setText(services.get(position).getName());
            desc.setText(services.get(position).getDescription() + ", " + (int) services.get(position).getPrice() + " руб");
            Picasso.get().load(url).into(img);
            v.setTag(services.get(position).getUuid());
        }



        v.setOnClickListener(v1 -> {
            if (!isPressed[position]) {
                if (services.size() == 1) {
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        v.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_8));
                    } else {
                        v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_8));
                    }
                } else {
                    if (position == 0) {
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_top_8));
                        } else {
                            v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_top_8));
                        }
                    } else if (position == (services.size() - 1)) {
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_bottom_8));
                        } else {
                            v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_bottom_8));
                        }
                    } else {
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            v.setBackgroundDrawable(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_8));
                        } else {
                            v.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(context), R.drawable.bg_cardview_blue_8));
                        }
                    }
                }
                name.setTextColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.colorWhite));
                isPressed[position] = true;
            } else {
                v.setBackgroundColor(Color.TRANSPARENT);
                name.setTextColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.darkTint));
                isPressed[position] = false;
            }
            listener.onAddPress(position, isPressed[position]);
        });

        return v;
    }

    public interface AddsListener {
        void onAddPress(int position, boolean isPressed);
    }

    public void setListener(AddsListener listener) {
        this.listener = listener;
    }
}
