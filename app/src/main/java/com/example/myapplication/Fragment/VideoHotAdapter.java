package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.IonClickItem;
import com.example.myapplication.Playing;
import com.example.myapplication.VideoItem;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.myapplication.R.*;
import static com.example.myapplication.R.drawable.*;


public class VideoHotAdapter extends RecyclerView.Adapter<VideoHotAdapter.Viewholder> {
    @Override
    public void onViewAttachedToWindow(@NonNull Viewholder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull Viewholder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.fc.fold(true);
    }

    @Override
    public void onViewRecycled(@NonNull Viewholder holder) {
        super.onViewRecycled(holder);

    }

    List<VideoItem> list = new ArrayList<>();
    IonClickItem ionClickItem;
    Context context;



    VideoView videoView ;
    TextView tvtimecr;
    SeekBar pb;
    int d;
    private Handler updateHandler = new Handler();
    MediaController mediaController;
    String timecr;
    Boolean timerun = true;

    public void setIonClickItem(IonClickItem ionClickItem) {
        this.ionClickItem = ionClickItem;
    }

    public VideoHotAdapter(List<VideoItem> list ,Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout.videohot_item,parent,false);

        Viewholder viewholder = new Viewholder(view);

        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final VideoItem videoItem;
        videoItem = list.get(position);

//
//        if (videoItem.getId()=="0"){
//            holder.fc.unfold(true);
//        }

//        if(holder.fc.isUnfolded()){
//            holder.fc.fold(false);
//        }
        Picasso.get().load(videoItem.getAvatar()).into(holder.imgava);
        holder.tvnameitem.setText(videoItem.getTitle());
        holder.videoView.setVideoPath(videoItem.getFile_mp4());
//        MediaController mediaController = new MediaController(holder.videoView.getContext());
//                    mediaController.setMediaPlayer(holder.videoView);
//                    holder.videoView.setMediaController(mediaController);


        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
               int d = holder.videoView.getDuration();
               videoItem.setId("1");


                holder.pb.setMax(d);
                updateHandler.postDelayed(updateVideoTime, 1000);
                holder.tvtime_total.setText(String.format("%02d" , (d/1000)/60)+":"+ String.format("%02d" , (d/1000)%60)) ;
            }
        });



        holder.videoView.start();


        holder.tvtimepublic.setText("Upload Time  : "+videoItem.getDate_published());
        holder.tvnameunfold.setText(videoItem.getTitle());
        holder.tvtimepublic_unfold.setText("Upload Time  : "+videoItem.getDate_published());

        holder.tvname.setText(videoItem.getTitle());


        holder.imgava.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
//                for (VideoItem vi :list){
//                    vi.setId("1");
//                }

                ionClickItem.onClickAva(position);
                holder.fc.unfold(false);
//                videoItem.setId("0");
                holder.videoView.start();
//                notifyDataSetChanged();
//                MediaController mediaController = new MediaController(holder.videoView.getContext());
//                mediaController.setAnchorView(holder.videoView);
//                holder.videoView.setMediaController(mediaController);


//                holder.videoView.setZOrderOnTop(true);

//                videoItem.setId("1");





            }
        });

//        holder.pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                int a = holder.pb.getProgress();
//               holder.videoView.seekTo(a);
//
//
//            }
//        });

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

                d = holder.videoView.getDuration();


//                holder.pb.setMax(d);
//                updateHandler.postDelayed(updateVideoTime, 1000);
//                holder.tvtime_total.setText(String.format("%02d" , (d/1000)/60)+":"+ String.format("%02d" , (d/1000)%60)) ;

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
        ImageView imgpause;
         VideoView videoView;
        TextView tvnameitem;
        TextView tvinfo;
        TextView tvtime_total;

        TextView tvtimepublic;
        TextView tvtimepublic_unfold;
        ImageView imgrecent_unfold;
        ImageView imgrecent;
        TextView tvrecent;
        TextView tvrecent_unfold;


        TextView tvname;
        RatingBar ratingBar;
        ImageView imglike;
        ImageView imgfullscreen;
        TextView tvtimecr;
        SeekBar pb;
        int d;
        private Handler updateHandler = new Handler();
        MediaController mediaController;
        String timecr;
        Boolean timerun = true;

        RelativeLayout cell_title_view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);


            cell_title_view = itemView.findViewById(id.cell_title_view);
            imgfullscreen = itemView.findViewById(id.imgfullscreen);
            fc = itemView.findViewById(id.folding_cell);
            ratingBar = itemView.findViewById(id.ratingbar);
            tvtime_total = itemView.findViewById(id.tvtime_total);
            pb = itemView.findViewById(id.pb);
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
            tvname = itemView.findViewById(id.tvname);
            tvinfo = itemView.findViewById(id.tvduration);
            tvtimepublic = itemView.findViewById(id.tvtimepublic);
            tvtimepublic_unfold = itemView.findViewById(id.tvtimepublic_unfold);

//            fc.initialize(1, color.black60, 50);
// or with camera height parameter
            fc.initialize(180, 1000, color.black60, 0);
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
    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            long currentPosition = videoView.getCurrentPosition();
            pb.setProgress((int) currentPosition);


            int timecurrent = videoView.getCurrentPosition()/1000;

            int min = 0 ;
            if (timerun) {
                while (timecurrent>60){
                    min++;
                    timecurrent-=60 ;
                }
                timecr = String.format("%02d", min)+":"+String.format("%02d", timecurrent);
                tvtimecr.setText(timecr);
            }

            else {
                int timecurrent_left = d / 1000 - timecurrent;
                while (timecurrent_left > 60) {
                    min++;
                    timecurrent_left -= 60;
                }

                timecr = "-"+String.format("%02d", min)+":"+String.format("%02d", timecurrent_left);
                tvtimecr.setText(timecr);
            }








            updateHandler.postDelayed(this, 100);



        }
    };





}
