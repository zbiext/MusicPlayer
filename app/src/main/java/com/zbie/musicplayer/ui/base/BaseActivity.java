package com.zbie.musicplayer.ui.base;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.appthemeengine.ATE;
import com.afollestad.appthemeengine.ATEActivity;
import com.zbie.musicplayer.IMusicService;
import com.zbie.musicplayer.MusicPlayer;
import com.zbie.musicplayer.MusicService;
import com.zbie.musicplayer.R;
import com.zbie.musicplayer.listeners.MusicStateListener;
import com.zbie.musicplayer.utils.Helpers;
import com.zbie.musicplayer.utils.NavigationUtils;
import com.zbie.musicplayer.utils.ZbieUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.zbie.musicplayer.MusicPlayer.mService;

/**
 * Created by 涛 on 2016/11/14 0014.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.ui.base
 * 创建时间         2016/11/14 22:30
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            activities 基类
 */
public class BaseActivity extends ATEActivity implements ServiceConnection, MusicStateListener {

    private final ArrayList<MusicStateListener> mMusicStateListener = new ArrayList<>();
    private MusicPlayer.ServiceToken mToken;
    private PlaybackStatus           mPlaybackStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToken = MusicPlayer.bindToService(this, this);
        mPlaybackStatus = new PlaybackStatus(this);
        // 即使音乐现在没播放,音量键改变多媒体音量(全局控音)
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        // 播放、暂停(状态)改变
        intentFilter.addAction(MusicService.PLAYSTATE_CHANGED);
        // 曲目改变
        intentFilter.addAction(MusicService.META_CHANGED);
        // 更新播放列表，可能是播放的fragment
        intentFilter.addAction(MusicService.REFRESH);
        // 如果播放列表改变，通知大家
        intentFilter.addAction(MusicService.PLAYLIST_CHANGED);
        // 存在一个错误的正在播放的曲目
        intentFilter.addAction(MusicService.TRACK_ERROR);

        // 注册一条 播放后台状态 的广播
        registerReceiver(mPlaybackStatus, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMetaChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑service
        if (mToken != null) {
            MusicPlayer.unBindFromService(mToken);
            mToken = null;
        }

        try {
            unregisterReceiver(mPlaybackStatus);
        } catch (final Throwable e) {
        }
        mMusicStateListener.clear();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IMusicService.Stub.asInterface(service);
        onMetaChanged();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }

    @Override
    public void restartLoader() {
        // 刷新列表的回调
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.restartLoader();
            }
        }
    }

    @Override
    public void onPlaylistChanged() {
        // 播放列表改变的回调
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onPlaylistChanged();
            }
        }
    }

    @Override
    public void onMetaChanged() {
        // meta已改变的回调
        for (final MusicStateListener listener : mMusicStateListener) {
            if (listener != null) {
                listener.onMetaChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (!ZbieUtils.hasEffectsPanel(BaseActivity.this)) {
            menu.removeItem(R.id.action_equalizer);
        }
        ATE.applyMenu(this, getATEKey(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_shuffle:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MusicPlayer.shuffleAll(BaseActivity.this);
                    }
                }, 80);
                return true;
            case R.id.action_equalizer:
                NavigationUtils.navigateToEqualizer(this);
                return true;
            case R.id.action_settings:
                NavigationUtils.navigateToSettings(this);
                return true;
            case R.id.action_search:
                NavigationUtils.navigateToSearch(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    protected String getATEKey() {
        return Helpers.getATEKey(this);
    }

    private class PlaybackStatus extends BroadcastReceiver {

        private final WeakReference<BaseActivity> mReference;

        public PlaybackStatus(BaseActivity activity) {
            mReference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String       action       = intent.getAction();
            BaseActivity baseActivity = mReference.get();
            if (baseActivity != null) {
                if (action.equals(MusicService.META_CHANGED)) { // 改变meta
                    baseActivity.onMetaChanged();
                } else if (action.equals(MusicService.PLAYSTATE_CHANGED)) { // 改变播放状态
                    // baseActivity.mPlayPauseProgressButton.getPlayPauseButton().updateState();
                } else if (action.equals(MusicService.REFRESH)) { // 刷新
                    baseActivity.restartLoader();
                } else if (action.equals(MusicService.PLAYLIST_CHANGED)) { // 播放列表改变
                    baseActivity.onPlaylistChanged();
                } else if (action.equals(MusicService.TRACK_ERROR)) { // 曲目错误
                    String errorMsg = context.getString(R.string.error_playing_track,
                            intent.getStringExtra(MusicService.TrackErrorExtra.TRACK_NAME));
                    Toast.makeText(baseActivity, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
