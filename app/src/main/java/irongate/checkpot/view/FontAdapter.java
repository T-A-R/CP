package irongate.checkpot.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FontAdapter extends ArrayAdapter<String> {

    public FontAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView text = (TextView) super.getView(position, convertView, parent);
        text.setTypeface(Fonts.getFuturaPtBook());
        return text;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView text = (TextView) super.getDropDownView(position, convertView, parent);
        text.setTypeface(Fonts.getFuturaPtBook());
        return text;
    }
}
