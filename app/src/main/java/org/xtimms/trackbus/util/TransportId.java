package org.xtimms.trackbus.util;

public enum TransportId {

    BUS(1),
    TRAM(2),
    EXPRESS(3),
    MINIBUS(4);

    private final int mIdInDatabase;

    TransportId(int id) {
        this.mIdInDatabase = id;
    }

    public int getIdInDatabase() {
        return mIdInDatabase;
    }

}
