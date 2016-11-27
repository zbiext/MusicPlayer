package com.zbie.musicplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.utils.Constants;
import com.zbie.musicplayer.utils.PreferencesUtility;

/**
 * Created by 涛 on 2016/11/21 0021.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/21 01:04
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            播放列表 fragment
 */
public class PlaylistFragment extends Fragment {

    private PreferencesUtility mPreferences;
    private boolean isGrid;
    private boolean isDefault;
    private boolean showAuto;
    private LinearLayout mPager;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());
        isGrid = mPreferences.getPlaylistView() == Constants.PLAYLIST_VIEW_GRID;
        isDefault = mPreferences.getPlaylistView() == Constants.PLAYLIST_VIEW_DEFAULT;
        showAuto = mPreferences.showAutoPlaylist();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fragment_playlist_toolbar);
        mPager = (LinearLayout) rootView.findViewById(R.id.fragment_playlist_pager);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_playlist_recycler_view);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.playlists);

        // TODO: 2016/11/21 01:31:11 PlaylistFragment 待完成

        return rootView;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }
}
