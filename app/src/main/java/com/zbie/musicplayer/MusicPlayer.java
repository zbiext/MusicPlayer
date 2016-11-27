package com.zbie.musicplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.zbie.musicplayer.utils.ZbieUtils;
import com.zbie.musicplayer.utils.ZbieUtils.IdType;

import java.util.Arrays;
import java.util.WeakHashMap;

/**
 * Created by 涛 on 2016/11/14 0014.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer
 * 创建时间         2016/11/14 22:31
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            音乐播放类
 */
public class MusicPlayer {

    // 界面与绑定的远程服务的对应 集合
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;

    private static final long[] sEmptyList;
    public static IMusicService mService = null;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
        sEmptyList = new long[0];
    }

    public static final ServiceToken bindToService(Context context, ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicService.class));
        ServiceBinder binder = new ServiceBinder(callback, contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(new Intent().setClass(contextWrapper, MusicService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    public static final void unBindFromService(ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder  mBinder         = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mService = null;
        }
    }

    public static final int getAudioSessionId() {
        if (mService != null) {
            try {
                return mService.getAudioSessionId();
            } catch (final RemoteException ignored) {
            }
        }
        return -1;
    }

    /** 随机播放 */
    public static void shuffleAll(Context context) {
        ZbieUtils.showToastS(context, "随机播放一首");
    }

    public static long[] getQueue() {
        try {
            if (mService != null) {
                return mService.getQueue();
            }
        } catch (RemoteException e) {
            // e.printStackTrace();
        }
        return sEmptyList;
    }

    /**
     * 移除曲目(删除歌曲)
     *
     * @param id
     * @return
     */
    public static final int removeTrack(final long id) {
        try {
            if (mService != null) {
                return mService.removeTrack(id);
            }
        } catch (final RemoteException e) {
            // e.printStackTrace();
        }
        return 0;
    }

    public static int getQueuePosition() {
        try {
            if (mService != null) {
                return mService.getQueuePosition();
            }
        } catch (final RemoteException e) {
            // e.printStackTrace();
        }
        return 0;
    }

    public static void setQueuePosition(final int position) {
        if (mService != null) {
            try {
                mService.setQueuePosition(position);
            } catch (final RemoteException e) {
                // e.printStackTrace();
            }
        }
    }

    public static final class ServiceToken {

        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }

    private static final class ServiceBinder implements ServiceConnection {

        private final ServiceConnection mCallback;
        private final Context           mContext;

        public ServiceBinder(ServiceConnection callback, Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }

    public static void playAll(final Context context, final long[] list, int position,
                               final long sourceId, final IdType sourceType,
                               final boolean forceShuffle) {
        if (list == null || list.length == 0 || mService == null) {
            return;
        }
        try {
            if (forceShuffle) {
                mService.setShuffleMode(MusicService.SHUFFLE_NORMAL);
            }
            final long currentId = mService.getAudioId();
            final int currentQueuePosition = getQueuePosition();
            if (position != -1 && currentQueuePosition == position && currentId == list[position]) {
                final long[] playlist = getQueue();
                if (Arrays.equals(list, playlist)) {
                    mService.play();
                    return;
                }
            }
            if (position < 0) {
                position = 0;
            }
            mService.open(list, forceShuffle ? -1 : position, sourceId, sourceType.mId);
            mService.play();
        } catch (final RemoteException ignored) {
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
