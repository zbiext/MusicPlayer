package com.zbie.musicplayer.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private static MainActivity sMainActivity;
    private String mAction;
    private boolean isDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sMainActivity = this;
        mAction = getIntent().getAction();
        isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
