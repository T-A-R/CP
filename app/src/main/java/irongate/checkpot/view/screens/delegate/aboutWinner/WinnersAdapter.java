package irongate.checkpot.view.screens.delegate.aboutWinner;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import irongate.checkpot.R;
import irongate.checkpot.view.screens.common.WebFragment;

public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.Holder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private List<SimpleUserProfile> userProfileList = new ArrayList<>();

    ImageView added;
    ImageView deleted;
    private int typeWinners = 0;
    private ItemSwipeListener itemSwipeListener;
//    private WebClickListener webClickListener;

    public WinnersAdapter(int typeWinners) {
        this.typeWinners = typeWinners;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes = 0;
        if (typeWinners == 0) {
            layoutRes = R.layout.user_profile_item;
        } else {
            layoutRes = R.layout.user_profile_item_get_prize;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        if (typeWinners == 0) {
            return new WinnerHolder(view);
        } else {
            return new WinnerPrizedHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setUserProfile(userProfileList.get(position));
    }

    @Override
    public int getItemCount() {
        return userProfileList.size();
    }

    public void setItemSwipeListener(ItemSwipeListener itemSwipeListener) {
        this.itemSwipeListener = itemSwipeListener;
    }

    protected class Holder extends RecyclerView.ViewHolder {

        public Holder(View view) {
            super(view);
        }

        protected void setUserProfile(SimpleUserProfile userProfile) {
        }

    }

    protected class WinnerHolder extends Holder {

        ImageView avatar;
        TextView winnerName;
        TextView winnerId;
        TextView winnerPhone;
        TextView raffleName;

        SimpleUserProfile userProfile;

        private String url;

        public WinnerHolder(View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            winnerName = itemView.findViewById(R.id.winner_name);
            winnerId = itemView.findViewById(R.id.winner_id);
            winnerPhone = itemView.findViewById(R.id.winner_phone);
            raffleName = itemView.findViewById(R.id.raffle_name);
            added = itemView.findViewById(R.id.btn_accept);
            deleted = itemView.findViewById(R.id.btn_decline);

            listener();
        }


        @SuppressLint("SetTextI18n")
        protected void setUserProfile(SimpleUserProfile userProfile) {
            this.userProfile = userProfile;
            avatar.setImageResource(R.drawable.ico_profile);
            winnerName.setText(userProfile.getWinnerName());
            winnerId.setText(userProfile.getWinnerId());
            url = "https://vk.com/id" + userProfile.getVk_user_id();
            winnerPhone.setText(url);
//            winnerPhone.setMovementMethod(LinkMovementMethod.getInstance());
//            raffleName.setText(userProfile.getRaffleName().get(0));
            String namesList ="";
            for(int i = 0; i < userProfile.getRaffleName().size(); i++)
            {
                namesList = namesList + userProfile.getRaffleName().get(i) + "\n";
            }
            raffleName.setText(namesList.substring(0, namesList.length() - 1));
        }

        private void listener() {

            added.setOnClickListener(v -> itemSwipeListener.onSwiped(0, userProfile));
            deleted.setOnClickListener(v -> itemSwipeListener.onSwiped(1, userProfile));
            winnerPhone.setOnClickListener(v -> itemSwipeListener.onSwiped(2, userProfile));

        }
    }

    protected class WinnerPrizedHolder extends Holder {

        ImageView avatar;
        TextView winnerName;
        TextView winnerId;
        TextView winnerPhone;
        TextView raffleName;

        SimpleUserProfile userProfile;

        String url;

        public WinnerPrizedHolder(View itemView) {
            super(itemView);

            avatar = itemView.findViewById(R.id.avatar);
            winnerName = itemView.findViewById(R.id.winner_name);
            winnerId = itemView.findViewById(R.id.winner_id);
            winnerPhone = itemView.findViewById(R.id.winner_phone);
            raffleName = itemView.findViewById(R.id.raffle_name);

        }


        @SuppressLint("SetTextI18n")
        protected void setUserProfile(SimpleUserProfile userProfile) {
            this.userProfile = userProfile;
            avatar.setImageResource(R.drawable.ico_profile);
            winnerName.setText(userProfile.getWinnerName());
            winnerId.setText(userProfile.getWinnerId());
            url = "https://vk.com/id" + userProfile.getVk_user_id();
            winnerPhone.setText(url);
            String namesList ="";
            for(int i = 0; i < userProfile.getRaffleName().size(); i++)
            {
                namesList = namesList + userProfile.getRaffleName().get(i) + "\n";
            }
            raffleName.setText(namesList.substring(0, namesList.length() - 1));
            winnerPhone.setOnClickListener(v -> itemSwipeListener.onSwiped(2, userProfile));
        }
    }

    public interface ItemSwipeListener {
        void onSwiped(int direction, SimpleUserProfile userProfile);
    }

//    public interface WebClickListener {
//        void onClick(String url);
//    }


    public void update(int type, List<SimpleUserProfile> userProfile) {
        this.typeWinners = type;
        this.userProfileList.clear();
        this.userProfileList.addAll(userProfile);
        notifyDataSetChanged();
    }


}
