package com.zbie.musicplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbie.musicplayer.R;
import com.zbie.musicplayer.utils.PreferencesUtility;

/**
 * Created by 涛 on 2016/11/21 0021.
 * 项目名           MusicPlayer
 * 包名             com.zbie.musicplayer.fragments
 * 创建时间         2016/11/21 00:29
 * 创建者           zbie
 * 邮箱             hyxt2011@163.com
 * 微博：           hy擦擦(http://weibo.com/u/1994927905)
 * Github:         https://github.com/zbiext
 * CSDN:           http://blog.csdn.net/hyxt2015
 * QQ&WX：         1677208059
 * 描述            主界面 fragment
 */
public class MainFragment extends Fragment {

    private PreferencesUtility mPreferences;
    private ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.fragment_main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mViewPager = (ViewPager) rootView.findViewById(R.id.fragment_main_view_pager);
        if (mViewPager != null) {
//            setupViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(2);
        }

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.fragment_main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return rootView;
    }

    // TODO: 2016/11/21 22:28:07 待完成
}
