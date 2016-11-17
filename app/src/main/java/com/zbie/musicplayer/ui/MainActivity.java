package com.zbie.musicplayer.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.ui.base.BaseActivity;
import com.zbie.musicplayer.view.SlidingUpPanelLayout;

/**
 * Created by 涛 on 2016/11/17.
 * 包名             com.zbie.musicplayer.ui
 * 创建时间         2016/11/17 23:27
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class MainActivity extends BaseActivity {

    private static MainActivity         sMainActivity;
    private        String               mAction;
    private        boolean              isDarkTheme;
    private        DrawerLayout         mDrawerLayout;
    private        SlidingUpPanelLayout mPanelLayout;
    private        NavigationView       mNavigationView;
    private        ImageView            mAlbumArt;
    private        TextView             mSongTitle;
    private        TextView             mSongArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sMainActivity = this;
        mAction = getIntent().getAction();
        isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_activity_main);
        mPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = mNavigationView.inflateHeaderView(R.layout.nav_header);

        mAlbumArt = (ImageView) headerView.findViewById(R.id.album_art);
        mSongTitle = (TextView) headerView.findViewById(R.id.song_title);
        mSongArtist = (TextView) headerView.findViewById(R.id.song_artist);

    }

    private void initData() {

    }

    private void initListener() {
        setPanelSlideListeners(mPanelLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isNavigatingMain()) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    super.onBackPressed();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNavigatingMain() {
        // TODO: 2016/11/17 23:18
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//        return (currentFragment instanceof MainFragment || currentFragment instanceof QueueFragment
//                || currentFragment instanceof PlaylistFragment);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mPanelLayout.isPanelExpanded())
            mPanelLayout.collapsePanel();
        else {
            super.onBackPressed();
        }
    }
}
