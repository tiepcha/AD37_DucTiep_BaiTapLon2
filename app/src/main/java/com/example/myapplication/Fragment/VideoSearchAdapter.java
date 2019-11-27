package com.example.myapplication.Fragment;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.Interface.IonClickItem;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoSearchAdapter extends RecyclerView.Adapter<VideoSearchAdapter.Viewholder> {

    List<VideoItem> list ;
    Context context;
    IonClickItem ionClickItem;
    GestureDetector gestureDetector;

    public VideoSearchAdapter(List<VideoItem> list , Context context ) {
        this.list = list;
    }

    public  void setIonClickItem(IonClickItem ionClickItem){
        this.ionClickItem = ionClickItem;
    }

    @NonNull
    @Override
    public VideoSearchAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_video_item,parent,false);

        VideoSearchAdapter.Viewholder viewholder = new VideoSearchAdapter.Viewholder(view);

        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoSearchAdapter.Viewholder holder, final int position) {

        final VideoItem videoItem;
        videoItem = list.get(position);
        Picasso.get().load(videoItem.getAvatar()).resize(300,190).into(holder.img_ava);
        holder.tv_name.setText(videoItem.getTitle());


        holder.tv_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ionClickItem.onClickName(videoItem.getFile_mp4(),0,position,view);

            }
        });

        holder.tv_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView tv_name;
        ImageView img_ava;

        RelativeLayout rl_item;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_search_title);
            tv_name.setSelected(true);
            img_ava = itemView.findViewById(R.id.img_search_ava);


        }

    }



}


