package com.example.myapplication;

import android.content.Context;
import android.view.View;

public interface IonClickItem {

    void onClickName(String name,int video_position ,int position, View v);
    void onClickAva(int position);
}
