package com.zbie.musicplayer.listeners;

/**
 * Created by 涛 on 2016/11/14 0014.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.listeners
 * 创建时间         2016/11/14 23:57
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            音乐状态 回调
 *                 监听播放(回放)改变状态，发送给绑定fragment的activity
 */
public interface MusicStateListener {

    /** 当{@link com.zbie.MusicService#REFRESH} 被引用时，被调用 */
    void restartLoader();

    /** 当{@link com.zbie.MusicService#PLAYLIST_CHANGED} 被引用时，被调用 */
    void onPlaylistChanged();

    /** 当{@link com.zbie.MusicService#META_CHANGED} 被引用时，被调用 */
    void onMetaChanged();
}
