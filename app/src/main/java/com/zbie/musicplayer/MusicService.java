package com.zbie.musicplayer;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by 涛 on 2016/11/14 0014.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer
 * 创建时间         2016/11/14 22:36
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */
@SuppressLint("NewApi")
public class MusicService extends Service {

    public static final String PREFIX            = "com.zbie.music.";
    public static final String META_CHANGED      = PREFIX + "metachanged";
    public static final String PLAYSTATE_CHANGED = PREFIX + "playlistchanged";
    public static final String REFRESH           = PREFIX + "refresh";
    public static final String PLAYLIST_CHANGED  = PREFIX + "playlistchanged";
    public static final String TRACK_ERROR       = PREFIX + "trackerror";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public interface TrackErrorExtra {
        String TRACK_NAME = "trackname";
    }
}
