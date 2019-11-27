package com.example.myapplication.Fragment;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.example.myapplication.CTGRItemAdapter;
import com.example.myapplication.Category;
import com.example.myapplication.Interface.IonClickCTGRItem;
import com.example.myapplication.Playing;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.data.All_Videos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static com.example.myapplication.Fragment.VideoHotAdapter.convertMillieToHMmSs;


public class CTGR_Fragment extends Fragment {
    String urlApi = "https://demo5639557.mockable.io/getItemCategory";
    List<VideoItem> list = new ArrayList<>();
    CTGRItemAdapter ctgrItemAdapter;
    Category category;
    String getCategory = "https://demo5639557.mockable.io/getCategory";
    List<Category> categoryListList = new ArrayList<>();
    Context context;

    GestureDetector gestureDetector;
    All_Videos all_videos;

    VideoSearchAdapter videoSearchAdapter ;

    RecyclerView rcv_one_ctgr;
    VideoView vv_ctgr;
    String title_playing;
    int position_inlist;
    LoaderImageView loading;
    LinearLayout load;
    int d ;
    TextView ctgr_name;

    VideoItem item;
    ImageView pause,next,prev,fullscreen;
    TextView playingtime,fulltime,title;
    SeekBar progress;
    RelativeLayout mediacontrol;

    private Handler updateHandler = new Handler();
    String timecr;
    Boolean timerun = true;




    public CTGR_Fragment(Category category,  Context context) {
        this.category = category;
        this.context = context;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final  String TAG = category.getCategory();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ctgr, container, false);
        rcv_one_ctgr = view.findViewById(R.id.rcv_one_ctgr);
        ctgr_name = view.findViewById(R.id.ctgr_name);
        vv_ctgr = view.findViewById(R.id.vv_ctgr);
        load = view.findViewById(R.id.load);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        fullscreen = view.findViewById(R.id.fullsscreen);
        playingtime = view.findViewById(R.id.playtime);
        fulltime = view.findViewById(R.id.fulltime);
        title = view.findViewById(R.id.title);
        progress = view.findViewById(R.id.progress);
        mediacontrol = view.findViewById(R.id.mediacontrol);

        pause = view.findViewById(R.id.pause);

        ctgr_name.setText(category.getCategory());
        ValueAnimator colorAnim = ObjectAnimator.ofInt(ctgr_name, "textColor", RED, BLUE);
        colorAnim.setDuration(5000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vv_ctgr.isPlaying()){
                    vv_ctgr.pause();
                    pause.setImageResource(R.mipmap.pause);
                } else {
                    vv_ctgr.start();
                    pause.setImageResource(R.mipmap.play);
                }
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.postDelayed(mcontrolerhide,3000);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (position_inlist==list.size()-1){
                  title.setText(list.get(0).getTitle());
                  vv_ctgr.setVideoPath(list.get(0).getFile_mp4());
                  ctgrItemAdapter.showplaying(0);
                  ctgrItemAdapter.notifyItemChanged(position_inlist);
                  ctgrItemAdapter.notifyItemChanged(0);
                  position_inlist=0;
              } else {
                  title.setText(list.get(position_inlist+1).getTitle());
                  vv_ctgr.setVideoPath(list.get(position_inlist+1).getFile_mp4());
                  ctgrItemAdapter.showplaying(position_inlist + 1);
                  ctgrItemAdapter.notifyItemChanged(position_inlist);
                  ctgrItemAdapter.notifyItemChanged(position_inlist + 1);
                  position_inlist++;

              }
                vv_ctgr.start();
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.postDelayed(mcontrolerhide,3000);
                item = list.get(position_inlist);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position_inlist==0){
                    title.setText(list.get(list.size()-1).getTitle());
                    vv_ctgr.setVideoPath(list.get(list.size()-1).getFile_mp4());
                    ctgrItemAdapter.showplaying(list.size()-1);
                    ctgrItemAdapter.notifyItemChanged(position_inlist);
                    ctgrItemAdapter.notifyItemChanged(list.size()-1);
                    position_inlist=list.size()-1;
                } else {
                    title.setText(list.get(position_inlist-1).getTitle());
                    vv_ctgr.setVideoPath(list.get(position_inlist-1).getFile_mp4());
                    ctgrItemAdapter.showplaying(position_inlist - 1);
                    ctgrItemAdapter.notifyItemChanged(position_inlist);
                    ctgrItemAdapter.notifyItemChanged(position_inlist - 1);
                    position_inlist--;
                }
                item = list.get(position_inlist);
                vv_ctgr.start();
                updateHandler.removeCallbacks(mcontrolerhide);
                updateHandler.postDelayed(mcontrolerhide,3000);

            }
        });

        vv_ctgr.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                d = vv_ctgr.getDuration();
                progress.setMax(d);
                String sfulltime =   convertMillieToHMmSs(d);
                fulltime.setText(sfulltime);
                updateHandler.postDelayed(updateVideoTime, 0);
                updateHandler.postDelayed(mcontrolerhide,3000);
            }
        });
        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                updateHandler.removeCallbacks(mcontrolerhide);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = progress.getProgress();
                vv_ctgr.seekTo(a);
                updateHandler.postDelayed(mcontrolerhide,3000);
            }
        });

        vv_ctgr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( mediacontrol.getHeight()>100){
                    collapse(mediacontrol,500,0);
                }else {
                    expand(mediacontrol,500,200);
                    updateHandler.removeCallbacks(mcontrolerhide);
                    updateHandler.postDelayed(mcontrolerhide,3000);
                }


            }
        });

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vv_ctgr.pause();
                pause.setImageResource(R.mipmap.pause);
                Intent intent = new Intent(context, Playing.class);
                intent.putExtra("mp4", item.getFile_mp4());
                intent.putExtra("list", (Serializable) list);
                intent.putExtra("position", position_inlist);
                intent.putExtra("video_position", vv_ctgr.getCurrentPosition());

                startActivity(intent);
            }
        });


        new getData(urlApi,rcv_one_ctgr,vv_ctgr).execute();





        return view;
    }
    class getData extends AsyncTask<Void,Void,Void> {

        String result = "";

        String urlApi;
        RecyclerView rcv_one_ctgr;
        VideoView vv_ctgr;



        public getData(String urlApi, RecyclerView rcv_one_ctgr, VideoView vv_ctgr) {
            this.urlApi = urlApi;
            this.rcv_one_ctgr = rcv_one_ctgr;
            this.vv_ctgr = vv_ctgr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream is = connection.getInputStream();
                int byteCharacter;

                while ((byteCharacter = is.read()) != -1) {
                    result += (char) byteCharacter;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getJsonObj(result);
            Collections.shuffle(list);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.VERTICAL, false);

            ctgrItemAdapter = new CTGRItemAdapter(list,getActivity().getBaseContext());

            rcv_one_ctgr.setLayoutManager(layoutManager);
            rcv_one_ctgr.setAdapter(ctgrItemAdapter);
//            loading.setVisibility(View.GONE);
            load.setVisibility(View.GONE);

//            Toast.makeText(getContext(), "d"+list.size(), Toast.LENGTH_SHORT).show();

//            rcv_one_ctgr.setHasFixedSize(true);
            rcv_one_ctgr.setItemViewCacheSize(10);
//            rcv_one_ctgr.setDrawingCacheEnabled(true);
//            rcv_one_ctgr.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//            ctgrItemAdapter.setHasStableIds(true);
            ctgrItemAdapter.setonClickCTGRItem(new IonClickCTGRItem() {
                @Override
                public void onClickExpand(int position) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i != position) {
                            ctgrItemAdapter.notifyItemChanged(i);
                        }
                    }
                }

                @Override
                public void onRefresh() {
                    for (int i = 0; i < list.size(); i++) {
                        ctgrItemAdapter.notifyItemChanged(i);
                    }
                }

                @Override
                public void onClickAvatar(VideoItem videoItem, int position) {

                    position_inlist = position;
                    vv_ctgr.setVisibility(View.VISIBLE);
                    if (title_playing == "null") {
                        item = videoItem;
                        title_playing = videoItem.getTitle();
                        title.setText(videoItem.getTitle());
                        expand(vv_ctgr, 500, 600);
                        vv_ctgr.setVideoPath(videoItem.getFile_mp4());
                        vv_ctgr.start();
                        expand(mediacontrol,500,200);
                        updateHandler.removeCallbacks(mcontrolerhide);
                        updateHandler.postDelayed(mcontrolerhide,3000);


                    } else {
                        if (!videoItem.getTitle().equalsIgnoreCase(title_playing)) {
                            item = videoItem;
                            title.setText(videoItem.getTitle());
                            vv_ctgr.setVideoPath(videoItem.getFile_mp4());
                            expand(vv_ctgr, 500, 600);
                            vv_ctgr.start();
                            expand(mediacontrol,500,200);
                            updateHandler.removeCallbacks(mcontrolerhide);
                            updateHandler.postDelayed(mcontrolerhide,3000);
                            title_playing = videoItem.getTitle();
                            for (int i = 0; i < list.size(); i++) {
                                if (i != position) {
                                    ctgrItemAdapter.notifyItemChanged(i);
                                }
                            }
                        }
                    }



                }
            });


        }
    }

    void getJsonObj(String urlApi){
        list.clear();

        all_videos = new All_Videos(getContext());


        try {
            JSONArray jsonArray = new JSONArray(urlApi);

            for (int i = 0 ; i <jsonArray.length();i++) {
                VideoItem videoItem = new VideoItem();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                videoItem.setId(jsonObject.getInt("id"));
                videoItem.setTitle(jsonObject.getString("title"));
                videoItem.setAvatar(jsonObject.getString("avatar"));
                videoItem.setFile_mp4( jsonObject.getString("file_mp4"));
                videoItem.setFile_mp4_size( jsonObject.getString("file_mp4_size"));
                videoItem.setDate_created( jsonObject.getString("date_created"));
                videoItem.setDate_modified(jsonObject.getString("date_modified"));
                videoItem.setDate_published( jsonObject.getString("date_published"));
                videoItem.setYoutube_url( jsonObject.getString("youtube_url"));
                videoItem.setStatus(jsonObject.getString("status"));
                videoItem.setLike(0);
                videoItem.setRecent("null");
                videoItem.setRate(3);
                list.add(videoItem);
                all_videos.addVideo(videoItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void expand(final View v, int duration, int targetHeight) {

        int prevHeight  = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
        int prevHeight  = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
//        v.setVisibility(View.GONE);
    }
    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            long currentPosition = vv_ctgr.getCurrentPosition();
            progress.setProgress((int) currentPosition);
            int timecurrent = vv_ctgr.getCurrentPosition()/1000;
            int min = 0 ;
            if (timerun) {
                while (timecurrent>=60){
                    min++;
                    timecurrent-=60 ;
                }
                timecr = String.format("%02d", min)+":"+String.format("%02d", timecurrent);
                playingtime.setText(timecr);
            }
            else {
                int timecurrent_left = d / 1000 - timecurrent;
                while (timecurrent_left >= 60) {
                    min++;
                    timecurrent_left -= 60;
                }
                timecr = "-"+String.format("%02d", min)+":"+String.format("%02d", timecurrent_left);
                playingtime.setText(timecr);
            }
            updateHandler.postDelayed(this, 1000);
        }
    };

    Runnable mcontrolerhide = new Runnable() {
        @Override
        public void run() {
            collapse(mediacontrol,500,0);

        }
    };
}
