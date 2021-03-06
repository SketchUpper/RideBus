package org.xtimms.trackbus.util;

public enum TypeOfDay {
    WEEKDAYS(1), WEEKEND(2), ON_SATURDAYS(3),
    ON_SUNDAYS(4), ON_FRIDAY(5);

    private final int mIdInDatabase;

    TypeOfDay(int id) {
        this.mIdInDatabase = id;
    }

    public int getIdInDatabase() {
        return mIdInDatabase;
    }

}
