package irongate.checkpot.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import irongate.checkpot.MainActivity;
import irongate.checkpot.checkpotAPI.models.Event;

import static irongate.checkpot.model.User.getUser;

public class CompareCheck {

    private Event event;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String sum;
    private String fn;
    private String fd;
    private String fpd;
    private int successCounter;

    public CompareCheck(Event event) {
        this.event = event;
    }

    public CompareCheck(Event event, String year, String month, String day, String hour, String minute, String sum, String fn, String fd, String fpd) {
        this.event = event;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.sum = sum;
        this.fn = fn;
        this.fd = fd;
        this.fpd = fpd;
    }

    public boolean isNewCheck() {
        if (event != null) {
            Log.d(MainActivity.TAG, " ******************************************************* ");
            if (event.getMembers() != null) {
                for (int k = 0; k < event.getMembers().size(); k++) {
                    if (event.getMembers().get(k).getCheck() != null) {
                        if (event.getMembers().get(k).getCheck().getData() != null) {

                            Date date = new Date((Long.parseLong(Objects.requireNonNull(event.getMembers().get(k).getCheck().getData().get("time")))) * 1000L);
                            Calendar calendar = new GregorianCalendar();
                            calendar.setTime(date);
                            calendar.add(Calendar.MILLISECOND, -calendar.getTimeZone().getOffset(calendar.getTimeInMillis()));
                            Log.d(MainActivity.TAG, "Event check Time: " + calendar.get(Calendar.YEAR)
                                    + " " + (calendar.get(Calendar.MONTH) + 1)
                                    + " " + calendar.get(Calendar.DAY_OF_MONTH)
                                    + " " + calendar.get(Calendar.HOUR_OF_DAY)
                                    + " " + calendar.get(Calendar.MINUTE)
                                    + " Sum: " + event.getMembers().get(k).getCheck().getData().get("sum")
                                    + " FN: " + event.getMembers().get(k).getCheck().getData().get("fn")
                                    + " FD: " + event.getMembers().get(k).getCheck().getData().get("fd")
                                    + " FPD: " + event.getMembers().get(k).getCheck().getData().get("fpd")
                            );
                            Log.d(MainActivity.TAG, "Scanned check Time: " + year
                                    + " " + month
                                    + " " + day
                                    + " " + hour
                                    + " " + minute
                                    + " Sum: " + sum
                                    + " FN: " + fn
                                    + " FD: " + fd
                                    + " FPD: " + fpd
                            );

                            if (String.valueOf(calendar.get(Calendar.YEAR)).equals(year)
                                    && calendar.get(Calendar.MONTH) == Integer.parseInt(month) - 1
                                    && calendar.get(Calendar.DAY_OF_MONTH) == Integer.parseInt(day)
                                    && calendar.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(hour)
                                    && calendar.get(Calendar.MINUTE) == Integer.parseInt(minute)
                                    && Float.parseFloat(Objects.requireNonNull(event.getMembers().get(k).getCheck().getData().get("sum"))) == Float.parseFloat(sum)
                                    && Long.parseLong(Objects.requireNonNull(event.getMembers().get(k).getCheck().getData().get("fn"))) == Long.parseLong(fn)
                                    && Long.parseLong(Objects.requireNonNull(event.getMembers().get(k).getCheck().getData().get("fd"))) == Long.parseLong(fd)
                                    && Long.parseLong(Objects.requireNonNull(event.getMembers().get(k).getCheck().getData().get("fpd"))) == Long.parseLong(fpd)
                            ) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public int getSuccessfullCkecksCounter() {

        successCounter = 0;
        if (event != null) {
            if (event.getMembers() != null) {
                if (getUser().getCurrentUser() != null) {
                    for (int k = 0; k < event.getMembers().size(); k++) {
                        if (event.getMembers().get(k).getUser().getUuid().equals(getUser().getCurrentUser().getUuid())) {
                            if (event.getMembers().get(k).getCheck() != null && event.getMembers().get(k).getCheck().getStatus() != null) {
                                if (event.getMembers().get(k).getCheck().getStatus().equals("SUCCESS")) {
                                    successCounter++;
                                }
                            } else {
                                if (event.getMembers().get(k).getReceipt() != null && !event.getMembers().get(k).getReceipt().isEmpty())
                                    successCounter++;
                            }
                        }
                    }
                }
            }
        }
        Log.d(MainActivity.TAG, "getSuccessfullCkecksCounter: " + successCounter);
        return successCounter;
    }
}
