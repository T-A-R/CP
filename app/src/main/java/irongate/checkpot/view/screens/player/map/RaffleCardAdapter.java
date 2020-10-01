package irongate.checkpot.view.screens.player.map;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.checkpotAPI.models.Event;

public class RaffleCardAdapter extends RecyclerView.Adapter<RaffleCardViewHolder> {

    private List<Event> events;
    private RaffleCardViewHolder.Listener listener;

    public RaffleCardAdapter(List<Event> events, RaffleCardViewHolder.Listener listener) {
        this.events = events;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RaffleCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_raffle_card_2, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.height = (int) (parent.getContext().getResources().getDisplayMetrics().density * 383);
        view.setLayoutParams(params);
        return new RaffleCardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RaffleCardViewHolder holder, int position) {
        holder.setEvent(events.get(position), position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RaffleCardViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
