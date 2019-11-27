package com.example.myapplication.Interface;

import android.view.View;

import com.example.myapplication.Fragment.VideoHotAdapter;

public interface IonClickItem {

    void onClickName(String name,int video_position ,int position, View v);
    void onClickAva();
    void onPosition(VideoHotAdapter adapter, int position, int i);
//    void passVideoItemHot(VideoItem videoItem);
}
