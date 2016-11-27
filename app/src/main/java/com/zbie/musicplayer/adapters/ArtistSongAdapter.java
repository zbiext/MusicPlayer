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
import com.zbie.musicplayer.utils.NavigationUtils;
import com.zbie.musicplayer.utils.ZbieUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 涛 on 2016/11/27 0027.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.adapters
 * 创建时间         2016/11/27 11:51
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            歌曲数据适配器
 */
public class ArtistSongAdapter extends RecyclerView.Adapter/*<ArtistSongAdapter.ItemHolder>*/ {

    private Activity   mContext;
    private List<Song> arraylist;
    private long       artistID;
    private long[]     songIDs;

    public ArtistSongAdapter(Activity context, List<Song> songArrayList, long artistID) {
        this.mContext = context;
        this.arraylist = songArrayList;
        this.artistID = artistID;
        this.songIDs = getSongIds();
    }

    public long[] getSongIds() {
        List<Song> actualArraylist = new ArrayList<Song>(arraylist);
        actualArraylist.remove(0);
        long[] ret = new long[actualArraylist.size()];
        for (int i = 0; i < actualArraylist.size(); i++) {
            ret[i] = actualArraylist.get(i).id;
        }
        return ret;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0) {
            viewType = 0;
        } else {
            viewType = 1;
        }
        return viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_detail_albums_header, null));
        } else {
            return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_song, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // TODO: 2016/11/27 17:24:26 ArtistSongAdapter 待完成
    }

    @Override
    public int getItemCount() {
        return (null != arraylist ? arraylist.size() : 0);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mItemSongTitle;
        private final TextView mItemSongAlbum;
        private final ImageView mItemSongAlbumArt;
        private final ImageView mItemSongPopupMenu;
        private final RecyclerView mAlbumsRecyclerView;

        public ItemHolder(View itemView) {
            super(itemView);
            // headView
            mAlbumsRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_head_recycler_view_album);

            // itemSongView
            mItemSongAlbumArt = (ImageView) itemView.findViewById(R.id.item_artist_song_albumArt);
            mItemSongTitle = (TextView) itemView.findViewById(R.id.item_artist_song_title);
            mItemSongAlbum = (TextView) itemView.findViewById(R.id.item_artist_song_album);
            mItemSongPopupMenu =(ImageView) itemView.findViewById(R.id.item_artist_song_popup_menu);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MusicPlayer.playAll(mContext, songIDs, getAdapterPosition() - 1, artistID, ZbieUtils.IdType.Artist, false);
                    NavigationUtils.navigateToNowplaying(mContext, true);
                }
            }, 100);
        }
    }
}
