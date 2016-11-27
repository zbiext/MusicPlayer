package com.zbie.musicplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.utils.Constants;

/**
 * Created by 涛 on 2016/11/27 0027.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/27 11:11
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            作曲家详情页面 fragment
 */

public class ArtistDetailFragment extends Fragment {

    private AppBarLayout            mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView               mIvArtistArt;
    private Toolbar                 mToolbar;

    private long artistID = -1;

    public static ArtistDetailFragment newInstance(long id, boolean useTransition, String transitionName) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle               args     = new Bundle();
        args.putLong(Constants.ARTIST_ID, id);
        args.putBoolean("transition", useTransition);
        if (useTransition) {
            args.putString("transition_name", transitionName);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artistdetail, container, false);

        mAppBarLayout = (AppBarLayout) rootView.findViewById(R.id.fragment_artistdetail_appBar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.fragment_artistdetail_collapsing_toolbar);
        mIvArtistArt = (ImageView) rootView.findViewById(R.id.fragment_artistdetail_artist_art);

        if (getArguments().getBoolean("transition")) {
            mIvArtistArt.setTransitionName(getArguments().getString("transition"));
        }
        mToolbar = (Toolbar) rootView.findViewById(R.id.fragment_artistdetail_toolbar);

        setupToolBar();

        setupArtistDetail();

        getChildFragmentManager().beginTransaction().replace(R.id.fragment_artistdetail_container, ArtistMusicFragment.newInstance(artistID)).commit();

        return rootView;
        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupToolBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        final ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupArtistDetail() {
        // TODO: 2016/11/27 11:39:09 ArtistDetailFragment 待完成
//        final Artist artist = ArtistLoader.getArtist(getActivity(), artistID);

//        mCollapsingToolbarLayout.setTitle(artist.name);

//        LastFmClient.getInstance(getActivity()).getArtistInfo(new ArtistQuery(artist.name), new ArtistInfoListener() {
//            @Override
//            public void artistInfoSucess(final LastfmArtist artist) {
//                if (artist != null) {
//
//                    ImageLoader.getInstance().displayImage(artist.mArtwork.get(4).mUrl, artistArt,
//                            new DisplayImageOptions.Builder().cacheInMemory(true)
//                                    .cacheOnDisk(true)
//                                    .showImageOnFail(R.drawable.ic_empty_music2)
//                                    .build(), new SimpleImageLoadingListener() {
//                                @Override
//                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                    largeImageLoaded = true;
//                                    try {
//                                        new Palette.Builder(loadedImage).generate(new Palette.PaletteAsyncListener() {
//                                            @Override
//                                            public void onGenerated(Palette palette) {
//                                                Palette.Swatch swatch = palette.getVibrantSwatch();
//                                                if (swatch != null) {
//                                                    primaryColor = swatch.getRgb();
//                                                    mCollapsingToolbarLayout.setContentScrimColor(primaryColor);
//                                                    if (getActivity() != null)
//                                                        ATEUtils.setStatusBarColor(getActivity(), Helpers.getATEKey(getActivity()), primaryColor);
//                                                } else {
//                                                    Palette.Swatch swatchMuted = palette.getMutedSwatch();
//                                                    if (swatchMuted != null) {
//                                                        primaryColor = swatchMuted.getRgb();
//                                                        mCollapsingToolbarLayout.setContentScrimColor(primaryColor);
//                                                        if (getActivity() != null)
//                                                            ATEUtils.setStatusBarColor(getActivity(), Helpers.getATEKey(getActivity()), primaryColor);
//                                                    }
//                                                }
//
//                                            }
//                                        });
//                                    } catch (Exception ignored) {
//
//                                    }
//                                }
//                            });
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            setBlurredPlaceholder(artist);
//                        }
//                    }, 100);
//                }
//            }
//
//            @Override
//            public void artistInfoFailed() {
//
//            }
//        });
    }

}
