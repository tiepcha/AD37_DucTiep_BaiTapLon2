package com.example.myapplication.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.myapplication.Category;
import com.example.myapplication.IonClickItem;
import com.example.myapplication.Playing;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.databinding.FragmentVideoCategoryBinding;
import com.example.myapplication.databinding.FragmentVideoHotBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VideoCategoryItem extends Fragment implements Serializable{

    FragmentVideoCategoryBinding binding;
    String urlApi = "https://demo5639557.mockable.io/getItemCategory";
    List<VideoItem> listHotVideo = new ArrayList<>();
    String result0 = "";
    RecyclerView recyclerView;
    VideoCategoryAdapter videoCategoryAdapter;
    Category category;
    String getCategory = "https://demo5639557.mockable.io/getCategory";
    List<Category> categoryList = new ArrayList<>();


    public VideoCategoryItem() {

    }

    public static VideoCategoryItem newInstance() {
        VideoCategoryItem fragment = new VideoCategoryItem();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    public VideoCategoryItem(Category category) {
//        this.category = category;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_video_category, container, false);


//        Toast.makeText(getContext(), category.getThumb(), Toast.LENGTH_SHORT).show();
//
//        String a = category.getThumb();

        new getCtgrData(getCategory).execute();

        new getData(urlApi).execute();

        binding.imgcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), category.getThumb(), Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();

    }
    class getCtgrData extends AsyncTask<Void,Void,Void>{
        String result0="";

        String urlApi;

        public getCtgrData(String urlApi) {
            this.urlApi = urlApi;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(urlApi);
                URLConnection connection = url.openConnection();
                InputStream inputStream = connection.getInputStream();
                int byteChar ;
                while ((byteChar = inputStream.read()) != -1){
                    result0+=(char) byteChar;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//            Toast.makeText(getContext(), result0, Toast.LENGTH_SHORT).show();
            getCategoryObject(result0);
//            Picasso.get().load(categoryList.get(3).getThumb()).into(binding.imgcategory);
        }
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
            videoCategoryAdapter = new VideoCategoryAdapter(listHotVideo,getContext());
            videoCategoryAdapter.setIonClickItem(new IonClickItem() {
                @Override
                public void onClickName(String name, int position, View v) {
                    Intent intent = new Intent(v.getContext(), Playing.class);
                    intent.putExtra("mp4", name);
                    startActivity(intent);
                }

                @Override
                public void onClickAva(int p) {

                }
            });
            binding.rcv.setLayoutManager(layoutManager);
            binding.rcv.setAdapter(videoCategoryAdapter);

//            int resId = R.anim.layout_animation_fall_down;
//            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
//            binding.rcv.setLayoutAnimation(animation);
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
    void getCategoryObject(String string) {


        try {
            JSONArray jsonArray = new JSONArray(string);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Category category = new Category();

                category.setCategory(jsonObject.getString("title"));
                category.setThumb(jsonObject.getString("thumb"));
                categoryList.add(category);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
