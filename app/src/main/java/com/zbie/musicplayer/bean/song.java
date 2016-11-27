package com.zbie.musicplayer.bean;

/**
 * Created by 涛 on 2016/11/23 0023.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.bean
 * 创建时间         2016/11/23 22:14
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            歌曲  java bean(model)
 */

public class Song {

    public final long   id;
    public final String title;
    public final String artistName;
    public final long   albumId;
    public final String albumName;
    public final int    duration;
    public final long   artistId;
    public final int    trackNumber;

    //    final long   id          = mCursor.getLong(0);
    //    final String songName    = mCursor.getString(1);
    //    final String artist      = mCursor.getString(2);
    //    final long   albumId     = mCursor.getLong(3);
    //    final String album       = mCursor.getString(4);
    //    final int    duration    = mCursor.getInt(5);
    //    final long   artistid    = mCursor.getInt(6);
    //    final int    tracknumber = mCursor.getInt(7);

    public Song() {
        this.id = -1; // 数据库表的 id 字段
        this.title = ""; // 数据库表的 歌曲名 字段
        this.artistName = ""; // 数据库表的 歌曲演唱者 字段
        this.albumId = -1; // 数据库表的 曲目id 字段
        this.albumName = ""; // 数据库表的 曲目名称 字段
        this.duration = -1; // 数据库表的 歌曲时间长度 字段
        this.artistId = -1; // 数据库表的 歌曲演唱者id 字段
        this.trackNumber = -1; // 数据库表的 曲目编号 字段
    }

    public Song(long id, String title, String artistName, long albumId, String albumName, int duration, long artistId, int trackNumber) {
        this.id = id;
        this.title = title;
        this.artistName = artistName;
        this.albumId = albumId;
        this.albumName = albumName;
        this.duration = duration;
        this.artistId = artistId;
        this.trackNumber = trackNumber;
    }

//    public Song(long _id, long _albumId, long _artistId, String _title, String _artistName, String _albumName, int _duration, int _trackNumber) {
//        this.id = _id;
//        this.albumId = _albumId;
//        this.artistId = _artistId;
//        this.title = _title;
//        this.artistName = _artistName;
//        this.albumName = _albumName;
//        this.duration = _duration;
//        this.trackNumber = _trackNumber;
//    }

}
