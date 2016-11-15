package com.zbie.musicplayer.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by 涛 on 2016/11/15 0015.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.utils
 * 创建时间         2016/11/15 23:13
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */

public class Helpers {

    public static final String getATEKey(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("dark_theme", false) ?
                "dark_theme" : "light_theme";
    }
}
