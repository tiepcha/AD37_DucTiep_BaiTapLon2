package com.example.myapplication.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.myapplication.IonClickItem;
import com.example.myapplication.VideoItem;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.myapplication.R.color;
import static com.example.myapplication.R.drawable.like;
import static com.example.myapplication.R.drawable.unlike;
import static com.example.myapplication.R.id;
import static com.example.myapplication.R.layout;


public class VideoCategoryAdapter extends RecyclerView.Adapter<VideoCategoryAdapter.Viewholder> {

    IonClickItem ionClickItem;
    Context context;


    public void setIonClickItem(IonClickItem ionClickItem) {
        this.ionClickItem = ionClickItem;
    }

    List<VideoItem> list = new ArrayList<>();

    public VideoCategoryAdapter(List<VideoItem> list,Context context) {
        this.list = list; this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout.videocategory_item,parent,false);

        Viewholder viewholder = new Viewholder(view);

        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final VideoItem videoItem;
        videoItem = list.get(position);

        Picasso.get().load(videoItem.getAvatar()).resize(500,320).into(holder.imgava);
        holder.tvnameitem.setText(videoItem.getTitle());
        holder.videoView.setVideoPath(videoItem.getFile_mp4());
        holder.tvtimepublic.setText("Upload Time  : "+videoItem.getDate_published());
        holder.tvnameunfold.setText(videoItem.getTitle());
        holder.tvtimepublic_unfold.setText("Upload Time  : "+videoItem.getDate_published());

        holder.tvname.setText(videoItem.getTitle());


        holder.imgava.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {


                holder.fc.toggle(false);
                holder.videoView.start();

            }
        });

        holder.imgfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), videoItem.getFile_mp4(), Toast.LENGTH_SHORT).show();
                ionClickItem.onClickName(videoItem.getFile_mp4(),position,view);

            }
        });


        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int videoDuration = holder.videoView.getDuration();

                 String duration=convertMillieToHMmSs(videoDuration);
                holder.tvinfo.setText("Duration : "+ duration);
                
            }
        });

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.videoView.isPlaying()){
                    holder.videoView.pause();
                    holder.imgpause.setVisibility(View.VISIBLE);
                }else {
                    holder.videoView.start();
                    holder.imgpause.setVisibility(View.GONE);

                }

            }
        });


        holder.imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!videoItem.isLike()) {
                    videoItem.setLike(true);
                    holder.imglike.setBackgroundResource(like);

                } else {videoItem.setLike(false);holder.imglike.setBackgroundResource(unlike);}
            }

        });

        holder.imgcollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.videoView.pause();
                holder.imgpause.setVisibility(View.GONE);
                holder.fc.toggle(false);

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class  Viewholder extends RecyclerView.ViewHolder{
        FoldingCell fc ;
        TextView tvnameunfold;
        RecyclerView rcv;
        ImageView imgava;
        ImageView imgcollapse;
        ImageView imgfullscreen;
        ImageView imgpause;
         VideoView videoView;
        TextView tvnameitem;
        TextView tvinfo;

        TextView tvtimepublic;
        TextView tvtimepublic_unfold;
        ImageView imgrecent_unfold;
        ImageView imgrecent;
        TextView tvrecent;
        TextView tvrecent_unfold;


        TextView tvname;
        RatingBar ratingBar;
        ImageView imglike;

        RelativeLayout cell_title_view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);


            cell_title_view = itemView.findViewById(id.cell_title_view);
            fc = itemView.findViewById(id.folding_cell);
            ratingBar = itemView.findViewById(id.ratingbar);
            LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#ffffbb33"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.parseColor("#B4FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.parseColor("#B4FFFFFF"),PorterDuff.Mode.SRC_ATOP);

            tvnameunfold = itemView.findViewById(id.tvnameunfold);
            tvrecent_unfold = itemView.findViewById(id.tvrecent_unfold);
            tvrecent = itemView.findViewById(id.tvrecent);
            imgrecent = itemView.findViewById(id.imgrecent);
            imgrecent_unfold = itemView.findViewById(id.imgrecent_unfold);
            imglike = itemView.findViewById(id.imglike);
            imgfullscreen = itemView.findViewById(id.imgfullscreen);
            tvname = itemView.findViewById(id.tvname);
            tvinfo = itemView.findViewById(id.tvduration);
            tvtimepublic = itemView.findViewById(id.tvtimepublic);
            tvtimepublic_unfold = itemView.findViewById(id.tvtimepublic_unfold);

            fc.initialize(1000, color.black60, 0);
// or with camera height parameter
            fc.initialize(30, 1000, color.black60, 0);
//            tv = itemView.findViewById(R.id.tvitem);
            rcv = itemView.findViewById(id.rcv);
            imgava = itemView.findViewById(id.imgava);
            imgpause = itemView.findViewById(id.imgpause);
            imgcollapse = itemView.findViewById(id.imgcollapse);
            tvnameitem = itemView.findViewById(id.tvnameitem);
            tvnameitem.setSelected(true);
            videoView = (VideoView)itemView.findViewById(id.vv);


        }
    }
    public static String convertMillieToHMmSs(int millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }

}
