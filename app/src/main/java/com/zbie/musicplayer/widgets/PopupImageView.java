package com.zbie.musicplayer.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.afollestad.appthemeengine.util.TintHelper;

/**
 * Created by 涛 on 2016/11/22 0022.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.widgets
 * 创建时间         2016/11/22 22:20
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO
 */
public class PopupImageView extends ImageView {

    public PopupImageView(Context context) {
        super(context);
        tint();
    }

    public PopupImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        tint();
    }

    public PopupImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tint();
    }

    @TargetApi(21)
    public PopupImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        tint();
    }

    private void tint() {
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("dark_theme", false)) {
            TintHelper.setTint(this, Color.parseColor("#eeeeee"));
        } else {
            TintHelper.setTint(this, Color.parseColor("#434343"));
        }
    }

}
