package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Playing extends AppCompatActivity  {

    VideoView videoView ;
    TextView tvtimecr;
    TextView tvrewind_forward,tvTitle;
    SeekBar pb,sb ;
    SeekBar sb_volume , sb_volume_controlerbar ;

    ImageView img_prev, img_rewind,img_pause , img_forward,img_next,img_mute;
    int d;
    private Handler updateHandler = new Handler();

    String timecr;
    Boolean timerun = true;
    RelativeLayout rlplay;
    RelativeLayout rl_mcontroler;


    AudioManager audioManager;
    List<VideoItem> list;
    int position;
    int video_position;
    int volume  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_playing);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
         int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        Intent intent = getIntent();
        String a = intent.getStringExtra("mp4");
        list = (List<VideoItem>) intent.getSerializableExtra("list");
          position = intent.getIntExtra("position",0);
          video_position = intent.getIntExtra("video_position",0);

          tvTitle = findViewById(R.id.tv_title);

          tvTitle.setText(list.get(position).getTitle());


        videoView = findViewById( R.id.vv);
        tvtimecr = findViewById( R.id.tvtimecr);
        tvrewind_forward = findViewById( R.id.tvrewind_forward);
        rlplay = findViewById( R.id.rlplay);
        img_prev = findViewById( R.id.imgprev);
        img_rewind = findViewById( R.id.imgrewind);
        img_pause = findViewById( R.id.imgpause);
        img_forward = findViewById( R.id.imgforward);
        img_next = findViewById( R.id.imgnext);
        img_mute = findViewById( R.id.imgmute);
        sb_volume_controlerbar = findViewById(R.id.sb_volume_controlerbar);
        sb_volume_controlerbar.setVisibility(View.GONE);

        rl_mcontroler = findViewById( R.id.rl_mcontroler);
        pb = findViewById( R.id.pb);
        sb = findViewById( R.id.sb);
        sb_volume = findViewById( R.id.sb_volume);
        final TextView tvtime_total = (TextView) findViewById(R.id.tvtime_total);
        videoView.setVideoPath(a);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    d = videoView.getDuration();
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                    volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) ;
                    sb_volume_controlerbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    sb_volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                    sb_volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                    sb_volume_controlerbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                    updateHandler.postDelayed(mcontrolerhide,3000);

                    pb.setMax(d);
                    sb.setMax(d);
                    updateHandler.postDelayed(updateVideoTime, 0);
                    tvtime_total.setText(String.format("%02d" , (d/1000)/60)+":"+ String.format("%02d" , (d/1000)%60)) ;
                }
            });

        videoView.seekTo(video_position);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.seekTo(0);
                videoView.start();
            }
        });


        img_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.removeCallbacks(sbvolumehide);
                videoView.seekTo(videoView.getCurrentPosition()-10000);
                tvrewind_forward.setText("-10s");

                updateHandler.postDelayed(mcontrolerhide,3000);
                updateHandler.postDelayed(sbvolumehide,300);
            }
        });
        img_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.seekTo(videoView.getCurrentPosition()+10000);
                tvrewind_forward.setText("+10s");
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.removeCallbacks(sbvolumehide);
                updateHandler.postDelayed(mcontrolerhide,3000);
                updateHandler.postDelayed(sbvolumehide,300);
            }
        });
        img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    img_pause.setBackgroundResource(R.drawable.button_play);
                    updateHandler.removeCallbacks(mcontrolerhide);
                    updateHandler.removeCallbacks(sbvolumehide);
                    updateHandler.postDelayed(mcontrolerhide,3000);
                    updateHandler.postDelayed(sbvolumehide,300);
                }
                else {
                    videoView.start();
                    img_pause.setBackgroundResource(R.drawable.button_pause);
                    updateHandler.removeCallbacks(mcontrolerhide);
                    updateHandler.removeCallbacks(sbvolumehide);
                    updateHandler.postDelayed(mcontrolerhide,3000);
                    updateHandler.postDelayed(sbvolumehide,300);
                }
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position<list.size()-1){
                    position = position + 1;
                videoView.setVideoPath(list.get(position).getFile_mp4());
                tvTitle.setText(list.get(position).getTitle());

                }else {
                    position=0;
                    videoView.setVideoPath(list.get(0).getFile_mp4());
                    tvTitle.setText(list.get(0).getTitle());}
            }
        });
        img_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position>0){
                    position--;
                videoView.setVideoPath(list.get(position).getFile_mp4()) ;
                tvTitle.setText(list.get(position).getTitle());;
                }else {
                    position = list.size()-1;
                    videoView.setVideoPath(list.get(position).getFile_mp4());
                    tvTitle.setText(list.get(position).getTitle());}
            }
        });


        img_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) != 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    tvrewind_forward.setText(R.string.MUTE);
                    img_mute.setBackgroundResource(R.drawable.mute);
                    updateHandler.removeCallbacks(mcontrolerhide);
                    updateHandler.removeCallbacks(sbvolumehide);
                    updateHandler.postDelayed(mcontrolerhide,3000);
                    updateHandler.postDelayed(sbvolumehide,300);
                } else {
                        img_mute.setBackgroundResource(R.drawable.unmute);
                        sb_volume_controlerbar.setProgress(5);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, 0);
                    updateHandler.removeCallbacks(mcontrolerhide);
                    updateHandler.removeCallbacks(sbvolumehide);
                    updateHandler.postDelayed(mcontrolerhide,3000);
                    updateHandler.postDelayed(sbvolumehide,300);
                }
            }
        });

        tvtimecr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerun = !timerun;
            }
        });

                                                                                            // THANH SEEKBAR VIDEO
        pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateHandler.removeCallbacks(mcontrolerhide);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = pb.getProgress();
                videoView.seekTo(a);
                updateHandler.postDelayed(mcontrolerhide,3000);
            }
        });
                                                                                    //XỬ LÝ VUỐT TUA VIDEO
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            float start = -1 ; float end; float current; int time;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                tvrewind_forward.setVisibility(View.VISIBLE);
                if (start <0) {
                    start = sb.getProgress();
                }

                current = sb.getProgress();
                time = (int) ((current-start)/10000);
                if (time>0){
                    tvrewind_forward.setText("+"+String.valueOf(time)+"s");
                }else if (time <0){
                    tvrewind_forward.setText(" "+String.valueOf(time)+"s");
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rl_mcontroler.setVisibility(View.VISIBLE);
                tvTitle.setVisibility(View.VISIBLE);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                start = -1;
                if(time!= 0 ){
                videoView.seekTo((int) (videoView.getCurrentPosition()+time*1000));
                }
                tvrewind_forward.setText("");
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.postDelayed(mcontrolerhide,3000);
            }});
                                                                                                        //XỬ LÝ VUỐT TĂNG GIẢM ÂM LƯỢNG
        sb_volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int x = sb_volume.getProgress();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, x, 0);
                tvrewind_forward.setText(String.valueOf(x));
                updateHandler.removeCallbacks(sbvolumehide);
                updateHandler.postDelayed(sbvolumehide,1000);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            long currentPosition = videoView.getCurrentPosition();
            pb.setProgress((int) currentPosition);
            int timecurrent = videoView.getCurrentPosition()/1000;
            int min = 0 ;
            if (timerun) {
            while (timecurrent>=60){
                    min++;
                    timecurrent-=60 ;
            }
                timecr = String.format("%02d", min)+":"+String.format("%02d", timecurrent);
                tvtimecr.setText(timecr);
            }
            else {
                int timecurrent_left = d / 1000 - timecurrent;
                while (timecurrent_left >= 60) {
                    min++;
                    timecurrent_left -= 60;
                }
                timecr = "-"+String.format("%02d", min)+":"+String.format("%02d", timecurrent_left);
                tvtimecr.setText(timecr);
            }
            updateHandler.postDelayed(this, 1000);
        }
    };

    Runnable sbvolumehide = new Runnable() {
        @Override
        public void run() {
            tvrewind_forward.setText("");
        }
    };
    Runnable mcontrolerhide = new Runnable() {
        @Override
        public void run() {
            rl_mcontroler.setVisibility(View.GONE);
            tvTitle.setVisibility(View.GONE);

        }
    };








}

