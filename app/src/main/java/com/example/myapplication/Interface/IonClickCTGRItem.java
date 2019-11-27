package com.example.myapplication.Interface;

import com.example.myapplication.VideoItem;

public interface IonClickCTGRItem {

    void onClickExpand(int position);

    void onRefresh();

    void onClickAvatar(VideoItem videoItem, int position);
}
