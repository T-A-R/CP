package irongate.checkpot.view.screens.player.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Place;

class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Context context;
    private List<Place> placeList;
    private List<Place> copyPlaceList = new ArrayList<>();
    public ItemClickListener itemClickListener;


    public SearchAdapter(Context context, List<Place> placeList) {
        this.context = context;
        this.placeList = placeList;
        copyPlaceList.addAll(placeList);
    }

    public void setList(List<Place> places) {
        placeList.addAll(places);
        copyPlaceList.addAll(places);
        notifyDataSetChanged();
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_rv, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.name.setText(placeList.get(position).getName());
        holder.setPlace(place);
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<Place> filter(String queryText) {
        placeList.clear();

        if (queryText.isEmpty()) {
            placeList.addAll(copyPlaceList);
        } else {
            for (Place place : copyPlaceList) {
                String name = place.getName();
                if (name.toLowerCase().contains(queryText.toLowerCase())) {
                    placeList.add(place);
                }
            }

        }
        notifyDataSetChanged();

        return copyPlaceList;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public SearchViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
        }

        public void setPlace(Place place) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(place);
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        void onClick(Place place);
    }


}
