package org.xtimms.trackbus.util;

import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.components.IntegerPreference;
import org.xtimms.trackbus.ui.StringJoinerCompat;

import java.util.Set;

@SuppressWarnings("ALL")
public abstract class PreferencesUtils {

    public static void bindPreferenceSummary(@Nullable Preference preference) {
        if (preference != null) {
            bindPreferenceSummary(preference, preference.getOnPreferenceChangeListener(), null);
        }
    }

    public static void bindPreferenceSummary(@Nullable Preference preference,
                                             @Nullable Preference.OnPreferenceChangeListener changeListener) {
        bindPreferenceSummary(preference, changeListener, null);
    }

    public static void bindPreferenceSummary(@Nullable Preference preference,
                                             @Nullable Preference.OnPreferenceChangeListener changeListener,
                                             @Nullable String pattern) {
        if (preference == null) {
            return;
        }
        SummaryHandler handler = new SummaryHandler(changeListener, pattern);
        handler.initSummary(preference);
        preference.setOnPreferenceChangeListener(handler);

    }

    public static void bindSummaryMultiple(@NonNull PreferenceFragment fragment, String... keys) {
        for (String key : keys) {
            bindPreferenceSummary(fragment.findPreference(key));
        }
    }

    @NonNull
    public static String buildSummary(@NonNull MultiSelectListPreference preference, @NonNull Set<String> values,
                                      @StringRes int emptySummary, @StringRes int allSummary) {
        if (values.isEmpty()) {
            return preference.getContext().getString(emptySummary);
        }
        final CharSequence[] entries = preference.getEntries();
        final CharSequence[] entryValues = preference.getEntryValues();
        if (allSummary != 0 && entryValues.length == values.size()) {
            return preference.getContext().getString(allSummary);
        }
        final StringJoinerCompat joiner = new StringJoinerCompat(", ");
        for (int i = 0; i < entryValues.length; i++) {
            if (values.contains(entryValues[i].toString())) {
                joiner.add(entries[i]);
            }
        }
        return joiner.toString();
    }

    @SuppressWarnings("deprecation")
    static class SummaryHandler implements Preference.OnPreferenceChangeListener {

        @Nullable
        private final Preference.OnPreferenceChangeListener mChangeListener;
        @Nullable
        private final String mPattern;

        SummaryHandler(@Nullable Preference.OnPreferenceChangeListener changeListener, @Nullable String pattern) {
            mChangeListener = changeListener;
            mPattern = pattern;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (mChangeListener != null && !mChangeListener.onPreferenceChange(preference, newValue)) {
                return false;
            }
            if (preference instanceof ListPreference) {
                int index = ((ListPreference) preference).findIndexOfValue((String) newValue);
                String summ = ((ListPreference) preference).getEntries()[index].toString();
                preference.setSummary(formatSummary(summ));
            } else if (preference instanceof IntegerPreference) {
                preference.setSummary(formatSummary(
                        String.valueOf(newValue)
                ));
            } else if (preference instanceof MultiSelectListPreference) {
                preference.setSummary(buildSummary((MultiSelectListPreference) preference,
                        (Set<String>) newValue, R.string.nothing_selected, R.string.all));
            } else {
                preference.setSummary(formatSummary(String.valueOf(newValue)));
            }
            return true;
        }

        void initSummary(Preference preference) {
            if (preference instanceof EditTextPreference) {
                preference.setSummary(formatSummary(((EditTextPreference) preference).getText()));
            } else if (preference instanceof ListPreference) {
                preference.setSummary(formatSummary(
                        ((ListPreference) preference).getEntries()[
                                ((ListPreference) preference).findIndexOfValue(((ListPreference) preference).getValue())
                                ].toString()
                ));
            } else if (preference instanceof IntegerPreference) {
                preference.setSummary(formatSummary(
                        String.valueOf(((IntegerPreference) preference).getValue())
                ));
            } else if (preference instanceof MultiSelectListPreference) {
                preference.setSummary(buildSummary((MultiSelectListPreference) preference,
                        ((MultiSelectListPreference) preference).getValues(), R.string.nothing_selected, R.string.all));
            } else {
                preference.setSummary(formatSummary(preference.getSharedPreferences()
                        .getString(preference.getKey(), null)));
            }
        }

        private String formatSummary(String value) {
            if (mPattern == null) {
                return value;
            } else {
                return String.format(mPattern, value);
            }
        }
    }
}
