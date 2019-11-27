package com.example.myapplication.Fragment;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.example.myapplication.Interface.IonClickItem;
import com.example.myapplication.Playing;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.data.All_Videos;
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
    VideoHotAdapter videoHotAdapter;
    All_Videos all_videos ;
    GestureDetector gestureDetector;
    VideoSearchAdapter videoSearchAdapter ;
    List<VideoItem> listSearch = new ArrayList<>();
    int position_open ;


    public VideoHot() {}

    public static VideoHot newInstance() {
        VideoHot fragment = new VideoHot();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listHotVideo.clear();


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_video_hot, container, false);
        binding.edtSearch.setVisibility(View.GONE);
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
        protected void onPreExecute() {
            binding.pgbar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override                                                         /*nhận json*/
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

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override                                                         //xử lý json và tạo list video hot
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getJsonObj(result);
            LinearLayoutManager l = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);


            RecyclerView.LayoutManager layoutManager = l;
            //            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

            Collections.shuffle(listHotVideo);
            videoHotAdapter = new VideoHotAdapter(listHotVideo,getContext());
            videoHotAdapter.setIonClickItem(new IonClickItem() {
                @Override
                public void onClickName(String name, int video_position, int position, View v) {
                    Intent intent = new Intent(v.getContext(), Playing.class);
                    intent.putExtra("mp4", name);
                    intent.putExtra("list", (Serializable) listHotVideo);
                    intent.putExtra("position",position);
                    intent.putExtra("video_position",video_position);

                    startActivity(intent); }

                @Override
                public void onClickAva() {

                    if (binding.edtSearch.getVisibility()== View.VISIBLE) {
                        binding.rcvSearch.setVisibility(View.GONE);
                        binding.edtSearch.setVisibility(View.GONE);
                        binding.imgSearch.setVisibility(View.GONE);
                        hidekeyboard();
                    } else {
                        TranslateAnimation animate = new TranslateAnimation(0, 0, 150, 0);
                        animate.setDuration(200);
                        animate.setFillAfter(false);
                        binding.edtSearch.startAnimation(animate);
                        binding.edtSearch.setVisibility(View.VISIBLE);
                        binding.imgSearch.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onPosition(VideoHotAdapter adapter, int position, int i) {
                    adapter.notifyItemChanged(position);
//                    binding.rcv.getLayoutManager().smoothScrollToPosition(binding.rcv,new RecyclerView.State(), binding.rcv.getAdapter().getItemCount());

//                    VideoHotAdapter.Viewholder viewholder = null;
//                    adapter.bindViewHolder(viewholder , position);
//                    viewholder.fc.fold(true);
//                    viewholder.videoView.pause();


                }
            });
            videoHotAdapter.setHasStableIds(true);

            binding.rcv.setLayoutManager(layoutManager);
            binding.pgbar.setVisibility(View.GONE);
            binding.rcv.setAdapter(videoHotAdapter);
            binding.rcv.setHasFixedSize(true);
            binding.rcv.setItemViewCacheSize(10);


            int resId = R.anim.layout_animation_fall_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getContext(), resId);
            binding.rcv.setLayoutAnimation(animation);

            binding.rcv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                boolean check_ScrollingUp = false;

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 20) {
                        // Scrolling up
                        if (check_ScrollingUp) {

                            hidekeyboard();
                            binding.rcvSearch.setVisibility(View.GONE);
                            binding.edtSearch.setVisibility(View.GONE);
                            binding.tvv.setText("");
                            binding.imgSearch.setVisibility(View.GONE);


                            check_ScrollingUp = false;
                        }
                    }
                    else if(dy<-20) {
                        // User scrolls down
                        if(!check_ScrollingUp ) {
                            hidekeyboard();
                            binding.rcvSearch.setVisibility(View.GONE);

                            TranslateAnimation animate = new TranslateAnimation(0,0,150,0);
                            animate.setDuration(100);
                            animate.setFillAfter(false);
                            binding.edtSearch.startAnimation(animate);
                            binding.tvv.setText("");
                            binding.edtSearch.setText("");
                            binding.edtSearch.setVisibility(View.VISIBLE);
                            binding.imgSearch.setVisibility(View.VISIBLE);
                            check_ScrollingUp = true;
                        }
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                }
            });

            gestureDetector = new GestureDetector(getContext(),new MyGestures(binding.rcvSearch));

            binding.imgSearch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });


            binding.edtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {


                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b){
                        hidekeyboard();
                        binding.rcvSearch.setVisibility(View.GONE);
                        binding.edtSearch.setVisibility(View.GONE);
                        binding.imgSearch.setVisibility(View.GONE);


                    }
                }
            });

            binding.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listSearch.clear();
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                   all_videos.searchVideo(binding.edtSearch.getText().toString(),listSearch);

                   if (listSearch.size()==1){

                       binding.rcvSearch.getLayoutParams().height = 220;
                   }
                   else if (listSearch.size()==2){
                       binding.rcvSearch.getLayoutParams().height = 420;
                   }
                   else if (listSearch.size()==3){
                       binding.rcvSearch.getLayoutParams().height = 620;
                   }else {
                       binding.rcvSearch.getLayoutParams().height = 820;
                   }

                   if (listSearch.isEmpty()){
                       binding.rcvSearch.setVisibility(View.GONE);
                       binding.tvv.setText("No Videos");
                   } else {
                       binding.tvv.setText("");
                       binding.rcvSearch.setVisibility(View.VISIBLE);
                       videoSearchAdapter = new VideoSearchAdapter(listSearch, getContext());

                       RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                       binding.rcvSearch.setLayoutManager(layoutManager);
                       binding.rcvSearch.setAdapter(videoSearchAdapter);


                       videoSearchAdapter.setIonClickItem(new IonClickItem() {
                           @Override
                           public void onClickName(String name, int video_position, int position, View v) {
                               Intent intent = new Intent(v.getContext(), Playing.class);
                               intent.putExtra("mp4", name);
                               intent.putExtra("list", (Serializable) listSearch);
                               intent.putExtra("position", position);
                               intent.putExtra("video_position", video_position);

                               startActivity(intent);
                           }

                           @Override
                           public void onClickAva() {
//                               binding.rcv.findViewHolderForAdapterPosition(1).
                           }

                           @Override
                           public void onPosition(VideoHotAdapter adapter, int position, int i) {
                           }
                       });
                   }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

        }
    }//------------------------------------------------------------------------------------------------------------------------------------------------


                                                           //TẠO LIST CÁC VIDEO HOT

    void getJsonObj(String urlApi){

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

                listHotVideo.add(videoItem);
                all_videos.addVideo(videoItem);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


       public void hidekeyboard(){
    InputMethodManager imm = (InputMethodManager) binding.edtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
}

        public void showkeyboard(){
    InputMethodManager imm = (InputMethodManager) binding.edtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.showSoftInput(binding.edtSearch, InputMethodManager.SHOW_IMPLICIT);}

    public void checkkeyboard(){

        InputMethodManager imm = (InputMethodManager) binding.edtSearch.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    class MyGestures extends GestureDetector.SimpleOnGestureListener {

        RecyclerView recyclerView;

        public MyGestures(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            //VUỐT XUỐNG
          if ((e2.getY() - e1.getY()) > 50){
                hidekeyboard();
                recyclerView.setVisibility(View.GONE);
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            binding.edtSearch.setFocusableInTouchMode(true);
            binding.edtSearch.setFocusable(true);
            binding.edtSearch.requestFocus();

            showkeyboard();

            return super.onSingleTapUp(e);
        }

    }


}
