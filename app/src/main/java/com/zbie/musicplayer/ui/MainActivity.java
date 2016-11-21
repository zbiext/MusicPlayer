package com.zbie.musicplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.zbie.musicplayer.R;
import com.zbie.musicplayer.fragments.MainFragment;
import com.zbie.musicplayer.fragments.PlaylistFragment;
import com.zbie.musicplayer.fragments.QueueFragment;
import com.zbie.musicplayer.ui.base.BaseActivity;
import com.zbie.musicplayer.utils.Constants;
import com.zbie.musicplayer.view.SlidingUpPanelLayout;

import java.util.HashMap;
import java.util.Map;

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
 * 描述            主界面(第一界面)
 */
public class MainActivity extends BaseActivity implements ATEActivityThemeCustomizer {

    private static MainActivity sMainActivity;
    private        String       mAction;
    private        boolean      isDarkTheme;

    private Map<String, Runnable> navigationMap = new HashMap<String, Runnable>();

    private DrawerLayout         mDrawerLayout;
    private SlidingUpPanelLayout mPanelLayout;
    private NavigationView       mNavigationView;
    private ImageView            mAlbumArt;
    private TextView             mSongTitle;
    private TextView             mSongArtist;

    private Runnable mRunnable;
    private Runnable navigateLibrary    = new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_library).setChecked(true);
            Fragment            fragment    = new MainFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
        }
    };
    private Runnable navigatePlaylist   = new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_playlists).setChecked(true);
            Fragment            fragment    = new PlaylistFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    private Runnable navigateQueue      = new Runnable() {
        @Override
        public void run() {
            mNavigationView.getMenu().findItem(R.id.nav_queue).setChecked(true);
            Fragment            fragment    = new QueueFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            transaction.replace(R.id.fragment_container, fragment).commit();
        }
    };
    private Runnable navigateNowplaying = new Runnable() {
        @Override
        public void run() {
            navigateLibrary.run();
            startActivity(new Intent(MainActivity.this, NowPlayingActivity.class));
        }
    };
    private Runnable navigateAlbum      = new Runnable() {
        @Override
        public void run() {
            long            albumID         = getIntent().getExtras().getLong(Constants.ALBUM_ID);
            Fragment        fragment        = AlbumDetailFragment.newInstance(albumID, false, null);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    };
    private Runnable navigateArtist     = new Runnable() {
        @Override
        public void run() {
            long            artistID        = getIntent().getExtras().getLong(Constants.ARTIST_ID);
            Fragment        fragment        = ArtistDetailFragment.newInstance(artistID, false, null);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    };

    private Handler navDrawerRunnable = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sMainActivity = this;
        mAction = getIntent().getAction();
        isDarkTheme = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationMap.put(Constants.NAVIGATE_LIBRARY, navigateLibrary);
        navigationMap.put(Constants.NAVIGATE_PLAYLIST, navigatePlaylist);
        navigationMap.put(Constants.NAVIGATE_QUEUE, navigateQueue);
        navigationMap.put(Constants.NAVIGATE_NOWPLAYING, navigateNowplaying);
        navigationMap.put(Constants.NAVIGATE_ALBUM, navigateAlbum);
        navigationMap.put(Constants.NAVIGATE_ARTIST, navigateArtist);

        initView();
        initData();
        initListener();

        setPanelSlideListeners(mPanelLayout);

        navDrawerRunnable.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDrawerContent(mNavigationView);
                setupNavigationIcons(mNavigationView);
            }
        }, 500);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
//                        updatePosition(menuItem);
                        return true;

                    }
                });
    }

    private void setupNavigationIcons(NavigationView navigationView) {
        /*--------------- Tip ---------------*/
        //material-icon-lib currently doesn't work with navigationview of design support library 22.2.0+
        //set icons manually for now
        //https://github.com/code-mc/material-icon-lib/issues/15

        if (!isDarkTheme) {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.library_music);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.playlist_play);
            navigationView.getMenu().findItem(R.id.nav_queue).setIcon(R.drawable.music_note);
            navigationView.getMenu().findItem(R.id.nav_nowplaying).setIcon(R.drawable.bookmark_music);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.settings);
            navigationView.getMenu().findItem(R.id.nav_about).setIcon(R.drawable.help_circle);
            navigationView.getMenu().findItem(R.id.nav_donate).setIcon(R.drawable.information);
            navigationView.getMenu().findItem(R.id.nav_help).setIcon(R.drawable.payment_black);
        } else {
            navigationView.getMenu().findItem(R.id.nav_library).setIcon(R.drawable.library_music_white);
            navigationView.getMenu().findItem(R.id.nav_playlists).setIcon(R.drawable.playlist_play_white);
            navigationView.getMenu().findItem(R.id.nav_queue).setIcon(R.drawable.music_note_white);
            navigationView.getMenu().findItem(R.id.nav_nowplaying).setIcon(R.drawable.bookmark_music_white);
            navigationView.getMenu().findItem(R.id.nav_settings).setIcon(R.drawable.settings_white);
            navigationView.getMenu().findItem(R.id.nav_about).setIcon(R.drawable.help_circle_white);
            navigationView.getMenu().findItem(R.id.nav_donate).setIcon(R.drawable.information_white);
            navigationView.getMenu().findItem(R.id.nav_help).setIcon(R.drawable.payment_white);
        }

        if(true/*!BillingProcessor.isIabServiceAvailable(this)*/) {
            navigationView.getMenu().removeItem(R.id.nav_donate);
        }

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
        // TODO: 2016/11/20 9:44 空方法体
    }

    public static MainActivity getInstance() {
        return sMainActivity;
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

    /**
     * 设置整体主题颜色
     *
     * @return 返回主题资源(主要是主题颜色)
     */
    @Override
    public int getActivityTheme() {
        return isDarkTheme ? R.style.AppThemeNormalDark : R.style.AppThemeNormalLight;
    }

}
