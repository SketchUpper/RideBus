package org.xtimms.trackbus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static org.xtimms.trackbus.util.ConstantUtils.HOURS;
import static org.xtimms.trackbus.util.ConstantUtils.MINUTES;

public class DateTime {
    private static final StringBuilder mTempString = new StringBuilder();

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
        //return new SimpleDateFormat("EEEE", Locale.getDefault()).format(new Date());
    }

    public static String formatRemainingTime(String remainingTime) {
        final int SIXTYMINUTES = 60;
        String stringRemainingTime;

        if (remainingTime.equals(ConstantUtils.TIME_EMPTY)) {
            return "-";
        }

        int intRemainingTime = Integer.parseInt(remainingTime);

        if (intRemainingTime < 0) {
            stringRemainingTime = remainingTime.concat(MINUTES);
        } else {

            if (intRemainingTime < SIXTYMINUTES) {
                stringRemainingTime = remainingTime.concat(MINUTES);
            } else {
                int hours = (int) TimeUnit.MINUTES.toHours(intRemainingTime);
                long minutes = intRemainingTime - (int) TimeUnit.HOURS.toMinutes(hours);
                mTempString.delete(0, mTempString.length());
                stringRemainingTime = mTempString.append(hours)
                        .append(HOURS)
                        .append(ConstantUtils.ONE_SPACE)
                        .append(minutes)
                        .append(MINUTES).toString();
            }
        }

        return stringRemainingTime;
    }

    public int getTypeDay(String currentDate, List<Integer> typeDayList) throws ParseException {

        int typeDay = TypeOfDay.WEEKDAYS.getIdInDatabase();
        //Date date = new SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).parse(currentDate);
        Date date = new SimpleDateFormat("EEEE", Locale.getDefault()).parse(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull(date));

        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        if (currentDay == Calendar.SATURDAY) {
            typeDay = TypeOfDay.ON_SATURDAYS.getIdInDatabase();

            if (!typeDayList.contains(typeDay)) {
                typeDay = TypeOfDay.WEEKEND.getIdInDatabase();
            }
        }

        if (currentDay == Calendar.SUNDAY) {
            typeDay = TypeOfDay.ON_SUNDAYS.getIdInDatabase();

            if (!typeDayList.contains(typeDay)) {
                typeDay = TypeOfDay.WEEKEND.getIdInDatabase();
            }
        }

        if (currentDay == Calendar.FRIDAY) {
            typeDay = TypeOfDay.ON_FRIDAY.getIdInDatabase();

            if (!typeDayList.contains(typeDay)) {
                typeDay = TypeOfDay.WEEKDAYS.getIdInDatabase();
            }
        }

        return typeDay;
    }

    public ResultTime getRemainingClosestTime(List<String> timeList, String currentTime) throws ParseException {
        String[] hiLoTimes = closestTime(timeList, currentTime);
        String loClosestTime = hiLoTimes[0];
        String hiClosestTime = hiLoTimes[1];
        long subTime;
        boolean flag = false;

        String remainingTime = ConstantUtils.TIME_EMPTY;

        if (!loClosestTime.isEmpty()) {
            subTime = parseTimeToMilliseconds(loClosestTime)
                    - parseTimeToMilliseconds(currentTime);

            if (Math.abs(subTime) < TimeUnit.MINUTES.toMillis(ConstantUtils.MINUTES_PASS)) {
                remainingTime = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(subTime));
                flag = true;
            }
        }

        String closestTime = ConstantUtils.TIME_EMPTY;

        if (!hiClosestTime.isEmpty()) {
            closestTime = hiClosestTime;

            if (!flag) {
                subTime = subTime(currentTime, hiClosestTime);
                remainingTime = String.valueOf(subTime);
            }
        }

        return new ResultTime(remainingTime, closestTime);
    }

    private long parseTimeToMilliseconds(String time) throws ParseException {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date date = format.parse(time);
        return Objects.requireNonNull(date).getTime();
    }

    private Date parseDate(String time) throws ParseException {
        DateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return format.parse(time);
    }

    private String[] closestTime(List<String> timeList, String currentTime) throws ParseException {
        NavigableSet<Date> times = new TreeSet<>();
        Date now = parseDate(currentTime);

        for (String time : timeList) {
            if (time.equals(currentTime)) return new String[]{ConstantUtils.EMPTY_STRING, currentTime};
            times.add(parseDate(time));
        }

        String[] hiLoClosetTime = new String[2];

        Date hiClosestTime = times.higher(now);
        Date lowClosestTime = times.lower(now);

        if (lowClosestTime != null) {
            hiLoClosetTime[0] = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(lowClosestTime);
        } else hiLoClosetTime[0] = ConstantUtils.EMPTY_STRING;

        if (hiClosestTime != null) {
            hiLoClosetTime[1] = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(hiClosestTime);
        } else {
            String firstTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(times.pollFirst());

            if (subTime(currentTime, firstTime) < TimeUnit.HOURS.toMinutes(ConstantUtils.HOURS_AFTER_MIDNIGHT)) {
                hiLoClosetTime[1] = firstTime;
            } else hiLoClosetTime[1] = ConstantUtils.EMPTY_STRING;

        }

        return hiLoClosetTime;

    }

    private int subTime(String currentTime, String closestTime) throws ParseException {
        long diff;

        long currentTimeMillis = parseTimeToMilliseconds(currentTime);
        long closestTimeMillis = parseTimeToMilliseconds(closestTime);

        if (closestTimeMillis < currentTimeMillis) {
            diff = (TimeUnit.HOURS.toMillis(ConstantUtils.HOUR_PER_DAY) + closestTimeMillis) - currentTimeMillis;
        } else {
            diff = closestTimeMillis - currentTimeMillis;
        }

        return (int) TimeUnit.MILLISECONDS.toMinutes(diff);
    }

    public static class ResultTime {
        private final String mRemainingTime;
        private final String mClosestTime;

        ResultTime(String remainingTime, String closestTime) {
            this.mRemainingTime = remainingTime;
            this.mClosestTime = closestTime;
        }

        public String getRemainingTime() {
            return mRemainingTime;
        }

        public String getClosestTime() {
            return mClosestTime;
        }
    }

}
