package com.zbie.musicplayer.utils;

import android.app.Activity;

/**
 * Created by 涛 on 2016/11/15 0015.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.utils
 * 创建时间         2016/11/15 23:25
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            导航栏工具类
 */
public class NavigationUtils {

    public static void navigateToEqualizer(Activity context) {
        ZbieUtils.showToastS(context, "启动均衡器");
//        try {
//            // 调用startActivityForResult 才能启动google MusicFX应用
//            context.startActivityForResult(ZbieUtils.createEffectsIntent(), 666);
//        } catch (final ActivityNotFoundException notFound) {
//            ZbieUtils.showToastS(context, "Equalizer not found");
//        }
    }

    public static void navigateToSettings(Activity context) {
        ZbieUtils.showToastS(context, "启动设置界面");
//        Intent intent = new Intent(context, SettingsActivity.class);
//        if (!PreferencesUtility.getInstance(context).getSystemAnimations()) {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        }
//        intent.setAction(Constants.NAVIGATE_SETTINGS);
//        context.startActivity(intent);
    }

    public static void navigateToSearch(Activity context) {
        ZbieUtils.showToastS(context, "启动搜索界面");
//        Intent intent = new Intent(context, SearchActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        intent.setAction(Constants.NAVIGATE_SEARCH);
//        context.startActivity(intent);
    }

}
