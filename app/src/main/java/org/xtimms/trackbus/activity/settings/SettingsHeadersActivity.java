package org.xtimms.trackbus.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xtimms.trackbus.App;
import org.xtimms.trackbus.R;
import org.xtimms.trackbus.activity.AppBaseActivity;

import java.util.ArrayList;

public final class SettingsHeadersActivity extends AppBaseActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_SETTINGS = 12;

    private ArrayList<SettingsHeader> mHeaders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_headers);
        setSupportActionBar(R.id.toolbar);
        enableHomeAsUp();

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        mHeaders = new ArrayList<>();
        mHeaders.add(new SettingsHeader(this, 1, R.string.general, R.drawable.ic_build_24dp));
        //mHeaders.add(new SettingsHeader(this, 2, R.string.appearance, R.drawable.ic_brush_24dp));
        mHeaders.add(new SettingsHeader(this, 2, R.string.debug, R.drawable.ic_bug_report_black_24dp));
        //mHeaders.add(new SettingsHeader(this, 3, R.string.tabs, R.drawable.ic_tab_white_24dp));

        SettingsAdapter mAdapter = new SettingsAdapter(mHeaders, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SettingsHeader header = mHeaders.get(position);
        Intent intent;
        switch (header.id) {
            case 1:
                intent = new Intent(view.getContext(), SettingsActivity.class)
                        .setAction(SettingsActivity.ACTION_SETTINGS_GENERAL);
                break;
            case 2:
                intent = new Intent(view.getContext(), SettingsActivity.class)
                        .setAction(SettingsActivity.ACTION_SETTINGS_DEBUG);
                break;
            default:
                return;
        }
        startActivityForResult(intent, REQUEST_SETTINGS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTINGS && resultCode == SettingsActivity.RESULT_RESTART) {
            new AlertDialog.Builder(this, R.style.Theme_AlertDialog)
                    .setMessage(R.string.need_restart)
                    .setNegativeButton(R.string.postpone, null)
                    .setPositiveButton(R.string.restart, (dialog, which) ->
                            App.from(SettingsHeadersActivity.this).restart())
                    .create()
                    .show();
        }
    }
}
