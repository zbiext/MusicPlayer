package com.zbie.musicplayer.adapters;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zbie.musicplayer.MusicPlayer;
import com.zbie.musicplayer.R;
import com.zbie.musicplayer.bean.Song;
import com.zbie.musicplayer.utils.Helpers;
import com.zbie.musicplayer.widgets.MusicVisualizer;

import java.util.List;

/**
 * Created by 涛 on 2016/11/21 0021.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.adapter
 * 创建时间         2016/11/21 22:44
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            TODO 待完成
 */
public class PlayingQueueAdapter extends RecyclerView.Adapter<PlayingQueueAdapter.ItemHolder> {

    private final Activity   mContext;
    private       List<Song> arryListOfSong;
    public        int        currentlyPlayingPosition;
    private       String     ateKey; // TODO: 2016/11/23 23:37:52 what's this?

    public PlayingQueueAdapter(Activity context, List<Song> arraylist) {
        mContext = context;
        arryListOfSong = arraylist;
        currentlyPlayingPosition = MusicPlayer.getQueuePosition();
        ateKey = Helpers.getATEKey(context);
    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playing_queue, null);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arryListOfSong != null ? arryListOfSong.size() : 0;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView       mReorder;
        private final ImageView       mAlbumArt;
        private final TextView        mSongTitle;
        private final TextView        mSongArtist;
        private final MusicVisualizer mVisualizer;
        private final ImageView       mPopupMenu;

        public ItemHolder(View itemView) {
            super(itemView);
            mReorder = (ImageView) itemView.findViewById(R.id.item_playing_queue_reorder);
            mAlbumArt = (ImageView) itemView.findViewById(R.id.item_playing_albumArt);
            mSongTitle = (TextView) itemView.findViewById(R.id.item_playing_song_title);
            mSongArtist = (TextView) itemView.findViewById(R.id.item_playing_song_artist);
            mVisualizer = (MusicVisualizer) itemView.findViewById(R.id.item_playing_music_visualizer);
            mPopupMenu = (ImageView) itemView.findViewById(R.id.item_playing_popup_menu);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // 播放点击的曲目, PlayingQueueAdapter item 并进行同步
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.setQueuePosition(getAdapterPosition());
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyItemChanged(currentlyPlayingPosition);
                            notifyItemChanged(getAdapterPosition());
                        }
                    }, 50);
                }
            }, 100);
        }
    }
}
