package com.zbie.musicplayer.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.audiofx.AudioEffect;

import com.zbie.musicplayer.MusicPlayer;

/**
 * Created by 涛 on 2016/11/15 0015.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.utils
 * 创建时间         2016/11/15 22:34
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            我的工具类
 */
public class ZbieUtils {

    public static boolean hasEffectsPanel(final Activity activity) {
        final PackageManager packageManager = activity.getPackageManager();
        return packageManager.resolveActivity(createEffectsIntent(), PackageManager.MATCH_DEFAULT_ONLY) != null;
    }

    private static Intent createEffectsIntent() {
        final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, MusicPlayer.getAudioSessionId());
        return effects;
    }
}