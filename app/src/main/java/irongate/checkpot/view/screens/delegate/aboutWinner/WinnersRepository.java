package irongate.checkpot.view.screens.delegate.aboutWinner;


import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import irongate.checkpot.MainActivity;
import irongate.checkpot.checkpotAPI.models.Event;
import irongate.checkpot.checkpotAPI.models.Member;
import irongate.checkpot.checkpotAPI.models.Place;
import irongate.checkpot.checkpotAPI.models.Prize;
import irongate.checkpot.checkpotAPI.models.Winner;
import irongate.checkpot.model.User;
import irongate.checkpot.utils.TimeUtils;

import static irongate.checkpot.MainActivity.TAG;
import static irongate.checkpot.model.User.getUser;

public class WinnersRepository {

    public int type;
    public static final WinnersRepository INSTANCE = new WinnersRepository();
    List<SimpleCardRiffle> cardRifflesList = new ArrayList<>();

    public int getType() {
        return type;
    }

    public List<SimpleCardRiffle> loadDataRaffle(int type) {
        cardRifflesList.clear();
        List<Event> events = null;
        if (getUser() != null) {
            if (getUser().getPlace() != null) {
                events = getUser().getPlace().getEvents();
            }
        }
        if (events != null) {
            if (events.size() != 0) {
                for (int j = 0; j < events.size(); j++) {
                    Event event = events.get(j);
//                    checkTwoWeeks(event);
//                    if (!checkTwoWeeks(event)) {
                    List<Member> members = event.getMembers();
                    if (members.size() > 0) {
                        SimpleCardRiffle cardRiffle = new SimpleCardRiffle();
                        cardRiffle.setRaffleFon(event.getMainPrize().getPhotos().get(0));
                        cardRiffle.setRaffleName(event.getMainPrize().getName());
                        cardRiffle.setRaffleUuid(event.getUuid());
                        cardRiffle.setRaffleId(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
                        cardRiffle.setRaffleDate(TimeUtils.fromLongDateToShort(event.getMainPrize().getCreatedAt()));
                        if (type == 0) {
                            cardRiffle.setUserProfileList(loadDataWinner(event));
                        } else {
                            cardRiffle.setUserProfilePrizedList(loadDataWinnerReceivePrize(event));
                        }
                        cardRifflesList.add(cardRiffle);
                    }
                }
//                } // 2weeks
            }

        }
        return cardRifflesList;
    }

    public List<SimpleCardRiffle> getCardRifflesList() {
        return cardRifflesList;
    }

    public List<SimpleCardRiffle> searchWinnerId(int type, String winnerId) {
        if (winnerId.isEmpty()) {
            return getCardRifflesList();
        }
        List<SimpleCardRiffle> cardRiffleList = new ArrayList<>();

        for (SimpleCardRiffle card : cardRifflesList) {
            List<SimpleUserProfile> userProfileList = new ArrayList<>();
            userProfileList.addAll(card.getUserProfileList());
            userProfileList.addAll(card.getUserProfilePrizedList());

            List<SimpleUserProfile> foundUsers = new ArrayList<>();
            for (SimpleUserProfile user : userProfileList) {
                if (user.getWinnerId() != null && winnerId != null && user.getWinnerId().contains((winnerId))) {
                    foundUsers.add(user);
                }
            }

            if (!foundUsers.isEmpty()) {
                SimpleCardRiffle simpleCardRiffle = new SimpleCardRiffle();
                simpleCardRiffle.setRaffleName(card.getRaffleName());
                simpleCardRiffle.setRaffleDate(card.getRaffleDate());
                simpleCardRiffle.setRaffleId(card.getRaffleId());
                simpleCardRiffle.setRaffleFon(card.getRaffleFon());
                if (type == 0) {
                    simpleCardRiffle.setUserProfileList(foundUsers);
                } else {
                    simpleCardRiffle.setUserProfilePrizedList(foundUsers);
                }
                cardRiffleList.add(simpleCardRiffle);
            }

        }

        return cardRiffleList;
    }


    public List<SimpleUserProfile> loadDataWinner(Event event) {


        List<SimpleUserProfile> userProfilesList = new ArrayList<>();
        List<Member> members = event.getMembers();

        if (!checkTwoWeeks(event)) {

            Log.d(MainActivity.TAG, "----------- Event:" + event.getMainPrize().getName() + " / event.getIsDone: " + event.getIsDone() + " / getRandomPrizes(): " + event.getRandomPrizes().size() + " ----------");
            for (int k = 0; k < members.size(); k++) {
                if (!members.get(k).getIsPrizeDelivered())
                    Log.d(MainActivity.TAG, "Member name: " + members.get(k).getUser().getName() + " isWinner: " + members.get(k).getIsWinner() + " isPrizeDelivered: " + members.get(k).getIsPrizeDelivered());
            }
            Log.d(MainActivity.TAG, "--------------------------------------------------------");

            List<Prize> prizes = event.getPrizes();
            for (int h = 0; h < prizes.size(); h++) {
                Prize prize = prizes.get(h);
                List<Winner> winners = prize.getWinners();
                if (winners.size() > 0) {
                    for (int i = 0; i < winners.size(); i++) {
                        SimpleUserProfile userProfile = new SimpleUserProfile();
                        Winner winner = winners.get(i);
                        for (int j = 0; j < members.size(); j++) {
                            Member member = members.get(j);

                            if (member.getId().equals(winner.getId()) && !member.getIsPrizeDelivered() && member.getDeclineReason() == null) {
                                if (prize.getIsRandom() && winner.getIsWinner()) {
                                    userProfile.setWinnerName(member.getUser().getName());
                                    userProfile.setWinnerId(member.getUser().getDigitalId() != null ? member.getUser().getDigitalId() : member.getUser().getUuid());
                                    userProfile.setWinnerPhone(member.getUser().getPhone());
                                    userProfile.setVk_user_id(member.getUser().getVk_user_id());
                                    userProfile.setRaffleName(event.getPrizes().get(h).getName());
                                    userProfile.setWinnerUuid(member.getUser().getUuid());
                                    userProfile.setRaffleUuid(event.getUuid());
                                    userProfile.setRaffleId(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
                                    boolean exists = false;
                                    for (int f = 0; f < userProfilesList.size(); f++) {
                                        if (userProfile.getWinnerId().equals(userProfilesList.get(f).winnerId) && prize.getIsRandom()) {
                                            exists = true;
                                            userProfilesList.get(f).setRaffleName(event.getPrizes().get(h).getName());
                                        }
                                    }
                                    if (!exists)
                                        userProfilesList.add(userProfile);

                                } else {
                                    userProfile.setWinnerName(member.getUser().getName());
                                    userProfile.setWinnerId(member.getUser().getDigitalId() != null ? member.getUser().getDigitalId() : member.getUser().getUuid());
                                    userProfile.setWinnerPhone(member.getUser().getPhone());
                                    userProfile.setVk_user_id(member.getUser().getVk_user_id());
                                    userProfile.setRaffleName(prize.getName());
                                    userProfile.setWinnerUuid(member.getUser().getUuid());
                                    userProfile.setRaffleUuid(event.getUuid());
                                    userProfile.setRaffleId(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
                                    boolean exists = false;
                                    for (int f = 0; f < userProfilesList.size(); f++) {
                                        if (userProfile.getWinnerId().equals(userProfilesList.get(f).winnerId) && !prize.getIsRandom())
                                            exists = true;
                                    }
                                    if (!exists)
                                        userProfilesList.add(userProfile);
                                }
                            }


                        }
                    }

                }
            }

            Log.d(MainActivity.TAG, "******************** " + event.getMainPrize().getName() + " ********************");
            for (int k = 0; k < userProfilesList.size(); k++) {
                Log.d(MainActivity.TAG, "Winner name: " + userProfilesList.get(k).winnerName);
            }
            Log.d(MainActivity.TAG, "***************************************************");
        } //2weeks
        return userProfilesList;
    }

    public List<SimpleUserProfile> loadDataWinnerReceivePrize(Event event) {

        List<SimpleUserProfile> userProfilesList = new ArrayList<>();
        List<Member> members = event.getMembers();
        List<Prize> prizes = event.getPrizes();

        Log.d(MainActivity.TAG, "----------- Event:" + event.getMainPrize().getName() + " / event.getIsDone: " + event.getIsDone() + " / getRandomPrizes(): " + event.getRandomPrizes().size() + " ----------");
        for (int k = 0; k < members.size(); k++) {
            if (members.get(k).getIsPrizeDelivered())
                Log.d(MainActivity.TAG, "Member name: " + members.get(k).getUser().getName() + " isWinner: " + members.get(k).getIsWinner() + " isPrizeDelivered: " + members.get(k).getIsPrizeDelivered());
        }
        Log.d(MainActivity.TAG, "--------------------------------------------------------");


        for (int h = 0; h < prizes.size(); h++) {
            Prize prize = prizes.get(h);
            List<Winner> winners = prize.getWinners();
            if (winners.size() > 0) {
                for (int i = 0; i < winners.size(); i++) {
                    SimpleUserProfile userProfile = new SimpleUserProfile();
                    Winner winner = winners.get(i);
                    for (int j = 0; j < members.size(); j++) {
                        Member member = members.get(j);
                        if (member.getId().equals(winner.getId()) && member.getIsPrizeDelivered() && member.getDeclineReason() == null) {
                            if (prize.getIsRandom() && winner.getIsWinner()) {
                                userProfile.setWinnerName(member.getUser().getName());
                                userProfile.setWinnerId(member.getUser().getDigitalId() != null ? member.getUser().getDigitalId() : member.getUser().getUuid());
                                userProfile.setWinnerPhone(member.getUser().getPhone());
                                userProfile.setVk_user_id(member.getUser().getVk_user_id());
                                userProfile.setRaffleName(event.getMainPrize().getName());
                                userProfile.setRaffleId(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
                                boolean exists = false;
                                for (int f = 0; f < userProfilesList.size(); f++) {
                                    if (userProfile.getWinnerId().equals(userProfilesList.get(f).winnerId) && prize.getIsRandom()) {
                                        exists = true;
                                        userProfilesList.get(f).setRaffleName(event.getPrizes().get(h).getName());
                                    }
                                }
                                if (!exists)
                                    userProfilesList.add(userProfile);

                            } else {
                                userProfile.setWinnerName(member.getUser().getName());
                                userProfile.setWinnerId(member.getUser().getDigitalId() != null ? member.getUser().getDigitalId() : member.getUser().getUuid());
                                userProfile.setWinnerPhone(member.getUser().getPhone());
                                userProfile.setVk_user_id(member.getUser().getVk_user_id());
                                userProfile.setRaffleName(prize.getName());
                                userProfile.setRaffleId(event.getDigitalId() != null ? event.getDigitalId() : event.getUuid());
                                boolean exists = false;
                                for (int f = 0; f < userProfilesList.size(); f++) {
                                    if (userProfile.getWinnerId().equals(userProfilesList.get(f).winnerId) && !prize.getIsRandom())
                                        exists = true;
                                }
                                if (!exists)
                                    userProfilesList.add(userProfile);
                            }
                        }
                    }

                }
            }
        }

        Log.d(MainActivity.TAG, "******************** " + event.getMainPrize().getName() + " " + userProfilesList.size() + " ********************");
        for (int k = 0; k < userProfilesList.size(); k++) {
            Log.d(MainActivity.TAG, "Winner name: " + userProfilesList.get(k).winnerName);
        }
        Log.d(MainActivity.TAG, "***************************************************");

        return userProfilesList;
    }

    private boolean checkTwoWeeks(Event event) {
        Date date = new Date(System.currentTimeMillis());
        if (event.getIsDone()) {
            if (event.getEnded() != null) {
                if ((date.getTime() / 1000 - event.getEnded()) / 86400 > 14)
                    return true;
            }
        }
        return false;
    }
}