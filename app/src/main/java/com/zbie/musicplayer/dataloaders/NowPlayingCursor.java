package com.zbie.musicplayer.dataloaders;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.zbie.musicplayer.MusicPlayer;

import java.util.Arrays;

import static com.zbie.musicplayer.MusicPlayer.mService;

/**
 * Created by 涛 on 2016/11/23 0023.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.dataloaders
 * 创建时间         2016/11/23 22:43
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            查询 MediaStore 数据库的游标(包装设计模式)
 */
public class NowPlayingCursor extends AbstractCursor {

    private static final String[] PROJECTION = new String[]{
            BaseColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.DURATION,
            // MediaStore.Audio.AudioColumns.TRACK,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.TRACK
    };

    private static final String TAG = "NowPlayingCursor";

    private final Context mContext;
    private       Cursor  mQueueCursor;
    private       long[]  mNowPlaying;
    private       long[]  mCursorIndexes;
    private       int     mSize;
    private       int     mCurPos;


    public NowPlayingCursor(final Context context) {
        mContext = context;
        makeNowPlayingCursor();
    }

    private void makeNowPlayingCursor() {
        mQueueCursor = null;
        mNowPlaying = MusicPlayer.getQueue(); // 获得正在播放队列的歌曲数量
        Log.d(TAG, mNowPlaying.toString() + "   " + mNowPlaying.length);
        mSize = mNowPlaying.length;// 正在播放歌曲的数量
        if (mSize == 0) {
            return;
        }
        // TODO: 2016/11/23 23:05:02 游标初始化 待 理清逻辑
        final StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Audio.Media._ID + " IN (");
        for (int i = 0; i < mSize; i++) {
            selection.append(mNowPlaying[i]);
            if (i < mSize - 1) {
                selection.append(",");
            }
        }
        selection.append(")");
        mQueueCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, PROJECTION, selection.toString(),
                null, MediaStore.Audio.Media._ID);

        if (mQueueCursor == null) {
            mSize = 0;
            return;
        }

        final int playlistSize = mQueueCursor.getCount();
        mCursorIndexes = new long[playlistSize];
        mQueueCursor.moveToFirst();
        final int columnIndex = mQueueCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        for (int i = 0; i < playlistSize; i++) {
            mCursorIndexes[i] = mQueueCursor.getLong(columnIndex);
            mQueueCursor.moveToNext();
        }
        mQueueCursor.moveToFirst();
        mCurPos = -1;

        int removed = 0;
        for (int i = mNowPlaying.length - 1; i >= 0; i--) {
            final long trackId = mNowPlaying[i];
            final int cursorIndex = Arrays.binarySearch(mCursorIndexes, trackId);
            if (cursorIndex < 0) {
                removed += MusicPlayer.removeTrack(trackId);
            }
        }
        if (removed > 0) {
            mNowPlaying = MusicPlayer.getQueue();
            mSize = mNowPlaying.length;
            if (mSize == 0) {
                mCursorIndexes = null;
                return;
            }
        }
    }

    @Override
    public int getCount() {
        // return 0;
        return mSize;
    }

    @Override
    public String[] getColumnNames() {
        // return new String[0];
        return PROJECTION;
    }

    @Override
    public String getString(int column) {
        try {
            return mQueueCursor.getString(column);
        } catch (Exception e) {
            // e.printStackTrace();
            onChange(true);
            return "";
        }
    }

    @Override
    public short getShort(int column) {
        //        try {
        return mQueueCursor.getShort(column);
        //        } catch (Exception e) {
        //            // e.printStackTrace();
        //            onChange(true);
        //            return 0;
        //        }
    }

    @Override
    public int getInt(int column) {
        try {
            return mQueueCursor.getInt(column);
        } catch (final Exception ignored) {
            // e.printStackTrace();
            onChange(true);
            return 0;
        }
    }

    @Override
    public long getLong(int column) {
        try {
            return mQueueCursor.getLong(column);
        } catch (final Exception ignored) {
            // e.printStackTrace();
            onChange(true);
            return 0;
        }
    }

    @Override
    public float getFloat(int column) {
        //        try {
        return mQueueCursor.getFloat(column);
        //        } catch (final Exception ignored) {
        //            // e.printStackTrace();
        //            onChange(true);
        //            return 0;
        //        }
    }

    @Override
    public double getDouble(int column) {
        //        try {
        return mQueueCursor.getDouble(column);
        //        } catch (final Exception ignored) {
        //            // e.printStackTrace();
        //            onChange(true);
        //            return 0;
        //        }
    }

    @Override
    public boolean isNull(int column) {
        //        try {
        return mQueueCursor.isNull(column);
        //        } catch (final Exception ignored) {
        //            // e.printStackTrace();
        //            onChange(true);
        //            return fasle;
        //        }
    }

    @Override
    public int getType(int column) {
        return mQueueCursor.getType(column);
    }

    @Override
    public boolean onMove(final int oldPosition, final int newPosition) {
        if (oldPosition == newPosition) {
            return true;
        }
        // TODO: 2016/11/23 23:23:14 理解一下这个方法的含义
        if (mNowPlaying == null || mCursorIndexes == null || newPosition >= mNowPlaying.length) {
            return false;
        }
        final long id          = mNowPlaying[newPosition];
        final int  cursorIndex = Arrays.binarySearch(mCursorIndexes, id);
        mQueueCursor.moveToPosition(cursorIndex);
        mCurPos = newPosition;
        return true;
        // return super.onMove(oldPosition, newPosition);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void deactivate() {
        // TODO: 2016/11/23 23:26:04 理解一下这个方法的含义
        if (mQueueCursor != null) {
            mQueueCursor.deactivate();
        }
    }

    @Override
    public boolean requery() {
        // TODO: 2016/11/23 23:26:10 理解一下这个方法的含义
        makeNowPlayingCursor();
        return true;
    }

    @Override
    public void close() {
        try {
            if (mQueueCursor != null) {
                mQueueCursor.close();
                mQueueCursor = null;
            }
        } catch (final Exception e) {
            // e.printStackTrace();
        }
        super.close();
    }

    /**
     * 应该是删除歌曲的方法(含义待定) TODO
     * @param which
     * @return
     */
    public boolean removeItem(final int which) {
        try {
            if (mService.removeTracks(which, which) == 0) {
                return false;
            }
            int i = which;
            mSize--;
            while (i < mSize) {
                mNowPlaying[i] = mNowPlaying[i + 1];
                i++;
            }
            onMove(-1, mCurPos);
        } catch (final RemoteException ignored) {
        }
        return true;
    }

}
