package com.zbie.musicplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.adapters.PlayingQueueAdapter;
import com.zbie.musicplayer.listeners.MusicStateListener;
import com.zbie.musicplayer.ui.base.BaseActivity;

/**
 * Created by 涛 on 2016/11/21 0021.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/21 22:12
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            播放队列 fragment
 */
public class QueueFragment extends Fragment implements MusicStateListener {

    private PlayingQueueAdapter mAdapter;
    private RecyclerView        mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fragment_queue_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.playing_queue);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_queue_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);

        new loadQueueSongs().execute("");
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }

    private class loadQueueSongs extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
//            mAdapter = new PlayingQueueAdapter(getActivity(), QueueLoader.getQueueSongs(getActivity()));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
