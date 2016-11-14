package com.zbie.musicplayer;

import android.app.Application;

/**
 * Created by 涛 on 2016/11/12 0012.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer
 * 创建时间         2016/11/12 18:36
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            MusicPlayer application类
 */
public class MusicPlayerApplication extends Application {

    private static MusicPlayerApplication ourInstance;

    public static synchronized MusicPlayerApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
    }
}
