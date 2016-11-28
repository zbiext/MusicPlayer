package com.zbie.musicplayer.subfragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.appthemeengine.Config;
import com.zbie.musicplayer.MusicPlayer;
import com.zbie.musicplayer.R;
import com.zbie.musicplayer.utils.Helpers;
import com.zbie.musicplayer.widgets.PlayPauseButton;
import com.zbie.musicplayer.widgets.SquareImageView;

import net.steamcrafted.materialiconlib.MaterialIconView;

/**
 * Created by 涛 on 2016/11/27 0027.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.subfragments
 * 创建时间         2016/11/27 18:45
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            快速控制页面 fragment
 */
public class QuickControlsFragment extends Fragment {

    public static RelativeLayout topContainer;
    private       View           rootView;

    private PlayPauseButton mPlayPause;
    private PlayPauseButton mPlayPauseExpanded;

    private View playPauseWrapper;
    private View playPauseWrapperExpanded;

    private MaterialIconView previous;
    private MaterialIconView next;
    private SeekBar mSeekBar;
    private TextView mArtistExpanded;
    private TextView mTitleExpanded;
    private ImageView mBlurredArt;
    private TextView mArtist;
    private TextView mTitle;
    private ProgressBar mProgress;
    private SquareImageView mAlbumArt;

    private boolean play_pause_status = false;

    private View.OnClickListener mPlayPauseListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            play_pause_status = true;
            if (mPlayPause.isPlayed()) {
                mPlayPause.setPlayed(true);
            } else {
                mPlayPause.setPlayed(false);
            }
            mPlayPause.startAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playOrPause();
                }
            }, 200);
        }
    };

    private View.OnClickListener mPlayPauseExpandedListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            play_pause_status = true;
            if (mPlayPauseExpanded.isPlayed()) {
                mPlayPauseExpanded.setPlayed(true);
            } else {
                mPlayPauseExpanded.setPlayed(false);
            }
            mPlayPauseExpanded.startAnimation();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playOrPause();
                }
            }, 200);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_quick_controls, container, false);
        this.rootView = rootView;

        mPlayPause = (PlayPauseButton) rootView.findViewById(R.id.fragment_quick_controls_song_play_pause_Btn);
        mPlayPauseExpanded = (PlayPauseButton) rootView.findViewById(R.id.now_playing_card_play_pause_Btn);
        playPauseWrapper = rootView.findViewById(R.id.fragment_quick_controls_song_play_pause_wrapper);
        playPauseWrapperExpanded = rootView.findViewById(R.id.now_playing_card_play_pause_wrapper);
        playPauseWrapper.setOnClickListener(mPlayPauseListener);
        playPauseWrapperExpanded.setOnClickListener(mPlayPauseExpandedListener);

        mProgress = (ProgressBar) rootView.findViewById(R.id.now_playing_card_song_progress_normal);
        mSeekBar = (SeekBar) rootView.findViewById(R.id.fragment_quick_controls_song_progress);
        mTitle = (TextView) rootView.findViewById(R.id.now_playing_card_title);
        mArtist = (TextView) rootView.findViewById(R.id.now_playing_card_artist);
        mTitleExpanded = (TextView) rootView.findViewById(R.id.fragment_quick_controls_song_title);
        mArtistExpanded = (TextView) rootView.findViewById(R.id.fragment_quick_controls_song_artist);
        mAlbumArt = (SquareImageView) rootView.findViewById(R.id.now_playing_card_album_art);
        mBlurredArt = (ImageView) rootView.findViewById(R.id.fragment_quick_controls_blurredAlbumart);
        next = (MaterialIconView) rootView.findViewById(R.id.fragment_quick_controls_song_next_song);
        previous = (MaterialIconView) rootView.findViewById(R.id.fragment_quick_controls_song_previous_song);
        topContainer = (RelativeLayout) rootView.findViewById(R.id.now_playing_card_top_container);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mProgress.getLayoutParams();
        mProgress.measure(0, 0);
        layoutParams.setMargins(0, -(mProgress.getMeasuredHeight() / 2), 0, 0);
        mProgress.setLayoutParams(layoutParams);

        mPlayPause.setColor(Config.accentColor(getActivity(), Helpers.getATEKey(getActivity())));
        mPlayPauseExpanded.setColor(Color.WHITE);







        // return super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }
}
