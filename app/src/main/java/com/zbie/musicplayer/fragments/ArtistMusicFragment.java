package com.zbie.musicplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.adapters.ArtistSongAdapter;
import com.zbie.musicplayer.utils.Constants;

/**
 * Created by 涛 on 2016/11/27 0027.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/27 11:40
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            ArtistMusicFragment
 */

public class ArtistMusicFragment extends Fragment {

    private RecyclerView mSongsRecyclerView;
    private long artistID = -1;
    private ArtistSongAdapter mSongAdapter;

    public static ArtistMusicFragment newInstance(long id) {
        ArtistMusicFragment fragment = new ArtistMusicFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ARTIST_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artistmusic, container, false);

        mSongsRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_artistmusic_recycler_view_songs);

        setUpSongs();
        return rootView;
    }

    private void setUpSongs() {
        mSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO: 2016/11/27 17:25:51 ArtistMusicFragment 待完成
    }

}
