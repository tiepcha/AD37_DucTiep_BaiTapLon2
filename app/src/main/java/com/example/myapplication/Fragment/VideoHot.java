package com.example.myapplication.Fragment;



import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.VideoView;

import com.example.myapplication.IonClickItem;
import com.example.myapplication.Playing;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.databinding.FragmentVideoHotBinding;

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

public class VideoHot extends Fragment implements Serializable{

    FragmentVideoHotBinding binding;
    String urlApi = "https://demo5639557.mockable.io/getVideoHot";
    List<VideoItem> listHotVideo = new ArrayList<>();
    String result0 = "";
    RecyclerView recyclerView;
    VideoHotAdapter videoHotAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    IonClickItem ionClickItem;

    public VideoHot() {

    }

    public static VideoHot newInstance() {
        VideoHot fragment = new VideoHot();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_video_hot, container, false);


        new getData(urlApi).execute();



        return binding.getRoot();
    }






    class getData extends AsyncTask<Void,Void,Void> {

        String result="";

        String urlApi;

        public getData(String urlApi) {
            this.urlApi = urlApi;
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

//                Log.d(TAG, "doInBackground: " + result);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getJsonObj(result);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

            Collections.shuffle(listHotVideo);
            videoHotAdapter = new VideoHotAdapter(listHotVideo,getContext());
            videoHotAdapter.setIonClickItem(new IonClickItem() {
                @Override
                public void onClickName(String name, int position, View v) {
                    Intent intent = new Intent(v.getContext(), Playing.class);
                    intent.putExtra("mp4", name);
                    intent.putExtra("list", (Serializable) listHotVideo);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }

                @Override
                public void onClickAva(int position) {
                }
            });
            binding.rcv.setLayoutManager(layoutManager);
            binding.rcv.setAdapter(videoHotAdapter);
//            binding.rcv.setRecyclerListener(new RecyclerView.RecyclerListener() {
//                @Override
//                public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
//                    VideoView videoView = (VideoView)holder.findViewById(R.id.VideoView);
//                    videoView.stopPlayback();
//                }


//            });
            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
            binding.rcv.setLayoutAnimation(animation);
        }
    }

    void getJsonObj(String urlApi){


        try {
            JSONArray jsonArray = new JSONArray(urlApi);

            for (int i = 0 ; i <jsonArray.length();i++) {
                VideoItem videoItem = new VideoItem();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                videoItem.setId(jsonObject.getString("id"));
                videoItem.setTitle(jsonObject.getString("title"));
                videoItem.setAvatar(jsonObject.getString("avatar"));
                videoItem.setFile_mp4( jsonObject.getString("file_mp4"));
                videoItem.setFile_mp4_size( jsonObject.getString("file_mp4_size"));
                videoItem.setDate_created( jsonObject.getString("date_created"));
                videoItem.setDate_modified(jsonObject.getString("date_modified"));
                videoItem.setDate_published( jsonObject.getString("date_published"));
                videoItem.setYoutube_url( jsonObject.getString("youtube_url"));
                videoItem.setStatus(jsonObject.getString("status"));
                videoItem.setLike(false);
                listHotVideo.add(videoItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
