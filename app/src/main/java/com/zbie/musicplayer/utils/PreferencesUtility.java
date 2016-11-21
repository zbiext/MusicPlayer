package com.zbie.musicplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by 涛 on 2016/11/21 0021.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.utils
 * 创建时间         2016/11/21 00:31
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO PreferencesUtility待完成
 */
public class PreferencesUtility {

    private static final String TOGGLE_PLAYLIST_VIEW   = "toggle_playlist_view";
    private static final String TOGGLE_SHOW_AUTO_PLAYLIST = "toggle_show_auto_playlist";

    private static PreferencesUtility ourInstance;
    private static SharedPreferences mPreferences;

    public static PreferencesUtility getInstance(final Context context) {
        if (ourInstance == null) {
            ourInstance = new PreferencesUtility(context.getApplicationContext());
        }
        return ourInstance;
    }

    private PreferencesUtility(final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getPlaylistView() {
        return mPreferences.getInt(TOGGLE_PLAYLIST_VIEW ,0);
    }

    public boolean showAutoPlaylist() {
        return mPreferences.getBoolean(TOGGLE_SHOW_AUTO_PLAYLIST, true);
    }
}
