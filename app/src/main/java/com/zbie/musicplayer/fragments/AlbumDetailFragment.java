package com.zbie.musicplayer.fragments;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.utils.Constants;

/**
 * Created by 涛 on 2016/11/24 0024.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/24 00:07
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            曲目详情页面 fragment
 */
public class AlbumDetailFragment extends Fragment {


    private ImageView               mIvAlbumArt;
    private ImageView               mIvArtistArt;
    private TextView                mTvAlbumTitle;
    private TextView                mTvAlbumDetails;
    private Toolbar                 mToolbar;
    private FloatingActionButton    mFloatingActionButton;
    private RecyclerView            mRecyclerView;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout            mAppBarLayout;

    @TargetApi(21)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_albumdetail, container, false);

        mIvAlbumArt = (ImageView) rootView.findViewById(R.id.fragment_albumdetail_album_art);
        mIvArtistArt = (ImageView) rootView.findViewById(R.id.fragment_albumdetail_artist_art);
        mTvAlbumTitle = (TextView) rootView.findViewById(R.id.fragment_albumdetail_album_title);
        mTvAlbumDetails = (TextView) rootView.findViewById(R.id.fragment_albumdetail_album_details);

        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_albumdetail_toolbar);

        mFloatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fragment_albumdetail_fab);

        if (getArguments().getBoolean("transition")) {
            mIvAlbumArt.setTransitionName(getArguments().getString("transition"));
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_albumdetail_recycler_view);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.fragment_albumdetail_collapsing_toolbar);
        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.fragment_albumdetail_appBar);
        
        mRecyclerView.setEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // TODO: 2016/11/27 11:09:14 AlbumDetailFragment 待完成
        return rootView;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static AlbumDetailFragment newInstance(long id, boolean useTransition, String transitionName) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putLong(Constants.ALBUM_ID, id);
        args.putBoolean("transition", useTransition);
        if (useTransition) {
            args.putString("transition_name", transitionName);
        }
        fragment.setArguments(args);
        return fragment;
    }

}
