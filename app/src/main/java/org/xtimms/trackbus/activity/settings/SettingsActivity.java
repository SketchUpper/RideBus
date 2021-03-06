package org.xtimms.trackbus.activity.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.AppBaseActivity;
import org.xtimms.trackbus.activity.LogsActivity;
import org.xtimms.trackbus.util.AppUtils;
import org.xtimms.trackbus.util.LogUtils;
import org.xtimms.trackbus.util.TextUtils;

public class SettingsActivity extends AppBaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    public static final String ACTION_SETTINGS_APPEARANCE = "org.xtimms.trackbus.ACTION_SETTINGS_APPEARANCE";
    public static final String ACTION_SETTINGS_GENERAL = "org.xtimms.trackbus.ACTION_SETTINGS_GENERAL";
    public static final String ACTION_SETTINGS_DEBUG = "org.xtimms.trackbus.DEBUG_SETTINGS_GENERAL";

    public static final int RESULT_RESTART = Activity.RESULT_FIRST_USER + 1;

    private PreferenceFragment mFragment;
    private SharedPreferences mDefaultPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setSupportActionBar(R.id.toolbar);
        enableHomeAsUp();
        mDefaultPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String action = TextUtils.notNull(getIntent().getAction());
        switch (action) {
            case ACTION_SETTINGS_APPEARANCE:
                mFragment = new AppearanceSettingsFragment();
                break;
            case ACTION_SETTINGS_GENERAL:
                mFragment = new GeneralSettingsFragment();
                break;
            case ACTION_SETTINGS_DEBUG:
                mFragment = new DebugSettingsFragment();
                break;
            default:
                finish();
        }
        LogsActivity mLogFragment = new LogsActivity();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDefaultPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        mDefaultPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("theme".equals(key)) {
            setResult(RESULT_RESTART);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if ("bugreport".equals(preference.getKey())) {
            LogUtils.sendLog(this);
            return true;
        }
        if ("checkdb".equals(preference.getKey())) {
            if (!AppUtils.isValidSQLite(getDatabasePath("trackbus.db").getAbsolutePath())) {
                new AlertDialog.Builder(this, R.style.Theme_AlertDialog)
                        .setTitle(getString(R.string.uh_oh))
                        .setMessage(getString(R.string.database_corrupted))
                        .setPositiveButton(android.R.string.ok, ((dialog, which) -> dialog.dismiss()))
                        .show();
                return false;
            } else {
                new AlertDialog.Builder(this, R.style.Theme_AlertDialog)
                        .setTitle(getString(R.string.success))
                        .setMessage(getString(R.string.db_integrity_check))
                        .setPositiveButton(android.R.string.ok, ((dialog, which) -> dialog.dismiss()))
                        .show();
                return true;
            }
        }
        if ("checkserver".equals(preference.getKey())) {
            if (!AppUtils.isURLReachable(this)) {
                new AlertDialog.Builder(this, R.style.Theme_AlertDialog)
                        .setTitle(getString(R.string.uh_oh))
                        .setMessage(getString(R.string.server_unavailable))
                        .setPositiveButton(android.R.string.ok, ((dialog, which) -> dialog.dismiss()))
                        .show();
                return false;
            } else {
                new AlertDialog.Builder(this, R.style.Theme_AlertDialog)
                        .setTitle(getString(R.string.success))
                        .setMessage(getString(R.string.succeed_response))
                        .setPositiveButton(android.R.string.ok, ((dialog, which) -> dialog.dismiss()))
                        .show();
                return true;
            }
        }
        if ("viewlog".equals(preference.getKey())) {
            Intent intent = new Intent();
            intent.setClass(SettingsActivity.this, LogsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
