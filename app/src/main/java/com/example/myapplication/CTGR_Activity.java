package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Fragment.VideoSearchAdapter;
import com.example.myapplication.Interface.IonClickCTGRItem;
import com.example.myapplication.data.All_Videos;
import com.example.myapplication.databinding.ActivityCtgrBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTGR_Activity extends AppCompatActivity {
    String urlApi = "https://demo5639557.mockable.io/getItemCategory";
    List<VideoItem> listHotVideo = new ArrayList<>();
    CTGRItemAdapter ctgrItemAdapter;
    Category categoryList;
    String getCategory = "https://demo5639557.mockable.io/getCategory";
    List<Category> categoryListList = new ArrayList<>();
    Context context;

    GestureDetector gestureDetector;
    All_Videos all_videos;

    VideoSearchAdapter videoSearchAdapter ;
    ActivityCtgrBinding binding ;


    List<VideoItem> listSearch = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ctgr_);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        new getData(urlApi).execute();
    }

    class getData extends AsyncTask<Void,Void,Void> {

        String result = "";

        String urlApi;

        public getData(String urlApi) {
            this.urlApi = urlApi;
        }


        @Override
        protected void onPreExecute() {

//            binding.pgbar.setVisibility(View.VISIBLE);
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

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
            Collections.shuffle(listHotVideo);
            ctgrItemAdapter = new CTGRItemAdapter(listHotVideo,getBaseContext());

            binding.rcvOneCtgr.setLayoutManager(layoutManager);
            binding.rcvOneCtgr.setAdapter(ctgrItemAdapter);
            binding.rcvOneCtgr.setHasFixedSize(true);
            binding.rcvOneCtgr.setItemViewCacheSize(20);
            binding.rcvOneCtgr.setDrawingCacheEnabled(true);
            binding.rcvOneCtgr.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            ctgrItemAdapter.setonClickCTGRItem(new IonClickCTGRItem() {
                @Override
                public void onClickExpand(int position) {
                    for ( int i = 0 ; i < listHotVideo.size(); i++){
                        if (i != position){
                            ctgrItemAdapter.notifyItemChanged(i);
                        }
                    }
                }

                @Override
                public void onRefresh() {
                    for ( int i = 0 ; i < listHotVideo.size(); i++){
                            ctgrItemAdapter.notifyItemChanged(i);
                    }
                }

                @Override
                public void onClickAvatar(VideoItem videoItem, int position) {

                }
            });



        }
    }
    void getJsonObj(String urlApi){
        listHotVideo.clear();

        all_videos = new All_Videos(getBaseContext());


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
                listHotVideo.add(videoItem);

                all_videos.addVideo(videoItem);
            }
            Toast.makeText(this, "d"+listHotVideo.size(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
