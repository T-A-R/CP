package irongate.checkpot.view.screens.delegate.aboutWinner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WinnersViewAdapter extends RecyclerView.Adapter<WinnersViewAdapter.WinnersViewHolder> {
    private int type;

//    public WinnersViewAdapter(WinnersAdapter.ItemSwipeListener itemSwipeListener) {
//        this.itemSwipeListener = itemSwipeListener;
//    }

    public WinnersViewAdapter(int type, WinnersAdapter.ItemSwipeListener itemSwipeListener) {
        this.itemSwipeListener = itemSwipeListener;
        this.type = type;
    }

    List<SimpleCardRiffle> cardRiffleList = new ArrayList<>();
    WinnersAdapter.ItemSwipeListener itemSwipeListener;

    @NonNull
    @Override
    public WinnersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardWinnersView cardWinnersView = new CardWinnersView(viewGroup.getContext(), type, itemSwipeListener);
        return new WinnersViewHolder(cardWinnersView);
    }

    @Override
    public void onBindViewHolder(@NonNull WinnersViewHolder winnersViewHolder, int i) {
        winnersViewHolder.setItem(cardRiffleList.get(i));
    }

    @Override
    public int getItemCount() {
        return cardRiffleList.size();
    }

    public void addItems(int type, List<SimpleCardRiffle> cardRiffles) {
        this.type = type;
        cardRiffleList.addAll(cardRiffles);
        notifyDataSetChanged();
    }

    public void clear() {
        cardRiffleList.clear();
        notifyDataSetChanged();
    }

    protected class WinnersViewHolder extends RecyclerView.ViewHolder {

        CardWinnersView winnersView;

        public WinnersViewHolder(@NonNull CardWinnersView itemView) {
            super(itemView);
            winnersView = itemView;
        }

        void setItem(SimpleCardRiffle cardRiffle) {
            winnersView.setData(type, cardRiffle);
        }

    }


}

