package org.xtimms.trackbus.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class StringJoinerCompat {

    private final String mPrefix;
    private final String mDelimiter;
    private final String mSuffix;
    private final String mEmptyValue;
    @Nullable
    private StringBuilder mValue;

    public StringJoinerCompat(@NonNull CharSequence delimiter) {
        this(delimiter, "", "");
    }

    public StringJoinerCompat(@NonNull CharSequence delimiter, @NonNull CharSequence prefix, @NonNull CharSequence suffix) {
        mPrefix = prefix.toString();
        mDelimiter = delimiter.toString();
        mSuffix = suffix.toString();
        mEmptyValue = "";
    }

    @NonNull
    @Override
    public String toString() {
        if (mValue == null) {
            return mEmptyValue;
        } else {
            if (mSuffix.equals("")) {
                return mValue.toString();
            } else {
                int initialLength = mValue.length();
                String result = mValue.append(mSuffix).toString();
                mValue.setLength(initialLength);
                return result;
            }
        }
    }

    public void add(CharSequence newElement) {
        prepareBuilder().append(newElement);
    }

    private StringBuilder prepareBuilder() {
        if (mValue != null) {
            mValue.append(mDelimiter);
        } else {
            mValue = new StringBuilder().append(mPrefix);
        }
        return mValue;
    }

}
