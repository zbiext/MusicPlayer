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
    //    private static final long[] sEmptyList;
    public static IMusicService mService = null;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
        //        sEmptyList = new long[0];
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
}
