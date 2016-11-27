package com.zbie.musicplayer.dataloaders;

import android.content.Context;

import com.zbie.musicplayer.bean.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涛 on 2016/11/23 0023.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.dataloaders
 * 创建时间         2016/11/23 22:28
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            播放队列加载器 (基于查询数据库的游标)
 */
public class QueueLoader {


    private static NowPlayingCursor mCursor;

    public static List<Song> getQueueSongs(Context context) {

        final ArrayList<Song> mSongList = new ArrayList<>();
        mCursor = new NowPlayingCursor(context);

        if (mCursor != null && mCursor.moveToFirst()) {

            do {
                // 查询数据库的表
                final long   id          = mCursor.getLong(0);
                final String songName    = mCursor.getString(1);
                final String artist      = mCursor.getString(2);
                final long   albumId     = mCursor.getLong(3);
                final String album       = mCursor.getString(4);
                final int    duration    = mCursor.getInt(5);
                final long   artistid    = mCursor.getInt(6);
                final int    tracknumber = mCursor.getInt(7);
                // final Song   song        = new Song(id, albumId, artistid, songName, artist, album, duration, tracknumber);
                final Song   song        = new Song(id, songName, artist, albumId, album, duration, artistid, tracknumber);
                mSongList.add(song);
            }
            while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }
}
