package com.example.myapplication.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.myapplication.AdapterComment;
import com.example.myapplication.Interface.IonClickItem;
import com.example.myapplication.R;
import com.example.myapplication.VideoItem;
import com.example.myapplication.data.Favorites_Video;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.myapplication.R.*;
import static com.example.myapplication.R.drawable.*;


public class VideoHotAdapter extends RecyclerView.Adapter<VideoHotAdapter.Viewholder> {

    List<VideoItem> list = new ArrayList<>();
    IonClickItem ionClickItem;
    Context context;
    Favorites_Video favorites_video;
    int d;

    int position_playing = -1;

    List<VideoHotAdapter.Viewholder> viewholderList = new ArrayList<>() ;
    List<String> viewholderTitleList = new ArrayList<>() ;


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

           viewholderList.add(viewholder);


        return  viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final VideoItem videoItem;
        videoItem = list.get(position);


        favorites_video = new Favorites_Video(context);

        favorites_video.check(videoItem);


        holder.ratingBar.setRating(videoItem.getRate());

        if (!videoItem.getRecent().equalsIgnoreCase("null")){
            holder.imgrecent.setVisibility(View.VISIBLE);
            holder.tvrecent.setText(videoItem.getRecent()); }

        if (videoItem.isLike()==1){ holder.imglike.setBackgroundResource(like); }
        else { holder.imglike.setBackgroundResource(unlike); }

        Picasso.get().load(videoItem.getAvatar()).resize(500,320).into(holder.imgava);
        holder.tvnameitem.setText(videoItem.getTitle());
//        Toast.makeText(context, videoItem.getFile_mp4(), Toast.LENGTH_SHORT).show();
        holder.videoView.setVideoPath(videoItem.getFile_mp4());


        holder.tvtimepublic_unfold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
               int d = holder.videoView.getDuration();
                holder.pb.setMax(d);
                holder.tvtime_total.setText(String.format("%02d" , (d/1000)/60)+":"+ String.format("%02d" , (d/1000)%60)) ;
            }
        });

        holder.videoView.start();
        holder.tvtimepublic.setText(context.getResources().getString(R.string.UploadTime) +videoItem.getDate_published());
        holder.tvnameunfold.setText(videoItem.getTitle());
        holder.tvtimepublic_unfold.setText(context.getResources().getString(R.string.UploadTime) +videoItem.getDate_published());

        holder.tvname.setText(videoItem.getTitle());
        holder.fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickItem.onClickAva();
            }
        });


        holder.imgava.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {

                DateFormat df = new SimpleDateFormat("d/M yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                videoItem.setRecent(date);
                favorites_video.addFavoriteVideo(videoItem);

                if (position_playing == -1){
                    position_playing = position;
                } else {
                    ionClickItem.onPosition( VideoHotAdapter.this,position_playing,position);
                    position_playing = position;
                }

                holder.tvrecent.setText(date);
                holder.imgrecent.setVisibility(View.VISIBLE);
                holder.tvrecent_unfold.setText(date);

                holder.space.setVisibility(View.GONE);
                holder.fc.unfold(false);

                holder.videoView.start();
                holder.edt_cmt.setClickable(true);
            }
        });
        holder.imglike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoItem.isLike()==0) {
                    videoItem.setLike(1);
                    holder.imglike.setBackgroundResource(like);
                    favorites_video.addFavoriteVideo(videoItem);
                } else {videoItem.setLike(0);holder.imglike.setBackgroundResource(unlike);
                    favorites_video.addFavoriteVideo(videoItem);}
            }

        });



        holder.imgfullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), videoItem.getFile_mp4(), Toast.LENGTH_SHORT).show();
                ionClickItem.onClickName(videoItem.getFile_mp4(),holder.videoView.getCurrentPosition(),position,view);
            }
        });


        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int videoDuration = holder.videoView.getDuration();

                 String duration=convertMillieToHMmSs(videoDuration);
                holder.tvinfo.setText(context.getResources().getString(string.Duration) + duration);

                d = holder.videoView.getDuration();
            }
        });


//        holder.videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                holder.videoView.pause();
//                holder.fc.fold(true);
//            }
//        });


        holder.rcv_comment.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        holder.rcv_comment_fold.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
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





        holder.tvcmt_fold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.rcv_comment_fold.getVisibility() == View.GONE){
                    holder.rcv_comment_fold.setVisibility(View.VISIBLE);
                    holder.tvname.setVisibility(View.GONE);
                    holder.ratingBar.setVisibility(View.GONE);
                    holder.imglike.setVisibility(View.GONE);
                }else if (holder.rcv_comment_fold.getVisibility()==View.VISIBLE){
                    holder.rcv_comment_fold.setVisibility(View.GONE);
                    holder.tvname.setVisibility(View.VISIBLE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.imglike.setVisibility(View.VISIBLE);

                }
            }
        });
        holder.imgcmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.rcv_comment.getVisibility() == View.GONE){
                    holder.rcv_comment.setVisibility(View.VISIBLE);
                    holder.tvnameunfold.setVisibility(View.GONE);
                    holder.tvtimepublic_unfold.setVisibility(View.GONE);
                    holder.tvrecent_unfold.setVisibility(View.GONE);
                    holder.tvinfo.setVisibility(View.GONE);
                    holder.imgrecent_unfold.setVisibility(View.GONE);
//                    notifyDataSetChanged();
                }else if (holder.rcv_comment.getVisibility()==View.VISIBLE){
                    holder.rcv_comment.setVisibility(View.GONE);
                    holder.tvnameunfold.setVisibility(View.VISIBLE);
                    holder.tvtimepublic_unfold.setVisibility(View.VISIBLE);
                    holder.tvrecent_unfold.setVisibility(View.VISIBLE);
                    holder.tvinfo.setVisibility(View.VISIBLE);
                    holder.imgrecent_unfold.setVisibility(View.VISIBLE);

                }
            }
        });

        holder.imgcollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.videoView.pause();
                holder.imgpause.setVisibility(View.GONE);


                holder.fc.fold(false);
                holder.space.setVisibility(View.VISIBLE);

            }
        });


        holder.imgrecent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(holder.tvrecent.getVisibility()==View.GONE){
                holder.tvrecent.setVisibility(View.VISIBLE);}
                else {
                    holder.tvrecent.setVisibility(View.GONE);
                }
                return false;
            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                videoItem.setRate((int)v);
                favorites_video.addFavoriteVideo(videoItem);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }







    public  class  Viewholder extends RecyclerView.ViewHolder{
        FoldingCell fc ;

        RecyclerView rcv;
        RecyclerView rcv_comment, rcv_comment_fold;


        ImageView imgava;
        ImageView imgcollapse;
        ImageView imgpause;
        ImageView imgrecent_unfold;
        ImageView imgrecent,imgcmt;
        ImageView imglike;
        ImageView imgfullscreen;

        VideoView videoView;

        TextView tvnameitem;
        TextView tvinfo;
        TextView tvtime_total;
        TextView space;
        TextView tvnameunfold;
        TextView tvtimepublic;
        TextView tvtimepublic_unfold;
        TextView tvcmt_fold;
        TextView tvrecent;
        TextView tvrecent_unfold;
        TextView tvname;

        EditText edt_cmt;

        RelativeLayout ll ;
        RelativeLayout cell_title_view,cell_content_view,full;

        RatingBar ratingBar;


        SeekBar pb;

        GestureDetector gestureDetector;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            final List<String> listcomment = new ArrayList<>();
            listcomment.add("Nhiều chỗ vô lý không hiểu được luôn????");
            listcomment.add("clip hay ");
            listcomment.add("dở ẹc");
            listcomment.add("xem tạm đc");
            listcomment.add("bình thường");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("Kb mn thấy sao chứ mh xem phát tức");
            listcomment.add("Phim mẫu thuẫn vãi ...! Ko hay tý nào");
            listcomment.add("Is the movie all right?");
            listcomment.add("Rất hay vì tao đã kéo xuống xem cmt không lại phí hơn 1 giờ");
            listcomment.add("Tại sao những bộ phim chiếu rạp này lại có engsub vậy, ad biết không");
            listcomment.add("Thật sự là mong chờ phim lâu rồi cứ tưỡng là sẽ không được coi");
            listcomment.add("cực kì thuyết phục");
            listcomment.add("I feel like I watched the entire movie with this trailer.");
            listcomment.add("Đúng là cảnh đẹp như phim. Jo mới xem có lạt hậu quá hok ta. Ai bỉu mình k có thời gian.");
            listcomment.add("6/8/2019\n" +
                    "Mình đã xem rất hay và cảm động");
            listcomment.add("không biết có ai giống mình k, trước khi xem phải đọc bình luận");
            listcomment.add("Phim hay lắm mọi người ơi!!!");
            listcomment.add("Trước khi xem ,dạo qua cmt ,thấy khen nhiều .ok.xem.");
            listcomment.add("Phim hay và đẹp quá , tuyệt vời lắm");
            listcomment.add("Phim hay quá. Nữ chính đẹp lắm. còn nam chính thì hông được như mình thường tưởng tượng. Lâu lâu coi thể loại này cũng thấy cuộc đời tươi hơn tí ^^");
            listcomment.add("OK phong cảnh và phục trang làm tui thích thể loại phim này :D");
            listcomment.add("Phim hay \n" +
                    "Và nghiệm ra rằng : đừng nương tay với kẻ vô lại");
            listcomment.add("Bộ phim xứng đáng với cái tên điện ảnh");
            listcomment.add("3-8-2019 \n" +
                    "Có ai xem hông\n" +
                    "Phim hay hết sức ( ◜‿◝ )♡");
            listcomment.add("Đằng sau 1 thằng con trai thành công là 1 ng con gái hiểu và yêu mk hết lòng do");
            listcomment.add("dv nữ phim này đẹp thật");
            listcomment.add("Xin tên bài hát cuối phim");
            listcomment.add("Phim ko hay! Nói chuyện nhiều");
            listcomment.add("Mới vào kinh này ‘ cho hỏi phim kênh này có hay Ko ae ‘ mình ở Huế");
            listcomment.add("Tam tam");
            Collections.shuffle(listcomment);
            int ran = new Random().nextInt(listcomment.size());
            for (int i = ran ; i <listcomment.size()-1 ; i++){
                listcomment.remove(i);
            }


            rcv_comment = itemView.findViewById(id.rcv_comment);
            ll = itemView.findViewById(id.ll);
            edt_cmt = itemView.findViewById(id.edt_cmt);
            final AdapterComment adapter = new AdapterComment(listcomment);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
           rcv_comment.setLayoutManager(layoutManager);
            rcv_comment.setAdapter(adapter);

            rcv_comment_fold = itemView.findViewById(id.rcv_comment_fold);
           final AdapterComment adapter2 = new AdapterComment(listcomment);
            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
            rcv_comment_fold.setLayoutManager(layoutManager2);
            rcv_comment_fold.setAdapter(adapter2);


            cell_title_view = itemView.findViewById(id.cell_title_view);
            cell_content_view = itemView.findViewById(id.cell_content_view);
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
            space = itemView.findViewById(id.space);
            imgcmt = itemView.findViewById(id.imgcmt);
            tvcmt_fold = itemView.findViewById(id.tvcmt_fold);
            tvcmt_fold.setText(context.getResources().getString(string.Comment)  + " ("+ listcomment.size() +")");
            tvrecent_unfold = itemView.findViewById(id.tvrecent_unfold);
            tvrecent = itemView.findViewById(id.tvrecent);
            imgrecent = itemView.findViewById(id.imgrecent);
            imgrecent_unfold = itemView.findViewById(id.imgrecent_unfold);
            imgpause = itemView.findViewById(id.imgpause);
            imglike = itemView.findViewById(id.imglike);
            tvname = itemView.findViewById(id.tvname);
            tvinfo = itemView.findViewById(id.tvduration);
            tvtimepublic = itemView.findViewById(id.tvtimepublic);
            tvtimepublic_unfold = itemView.findViewById(id.tvtimepublic_unfold);

            fc.initialize(1000, color.black60, 2);
// or with camera height parameter
            fc.initialize(30, 1000, color.black60, 2);
//            tv = itemView.findViewById(R.id.tvitem);
            rcv = itemView.findViewById(id.rcv);
            imgava = itemView.findViewById(id.imgava);
            imgpause = itemView.findViewById(id.imgpause);
            imgcollapse = itemView.findViewById(id.imgcollapse);
            tvnameitem = itemView.findViewById(id.tvnameitem);
//            tvnameitem.setSelected(true);
            videoView = (VideoView)itemView.findViewById(id.vv);
            gestureDetector = new GestureDetector(context,new MyGestures(fc,videoView,imgpause) );
            imgcollapse.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
            tvnameunfold.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    gestureDetector.onTouchEvent(motionEvent);
                    return true;
                }
            });
            edt_cmt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b == false){

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edt_cmt.getWindowToken(), 0);

                    }
                }
            });



                                                                                                        //BẮT SỰ KIỆN ENTER TRÊN COMMENT
            edt_cmt.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                    //If the keyevent is a key-down event on the "enter" button
                    if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if(!edt_cmt.getText().toString().isEmpty()){
                        listcomment.add(0,edt_cmt.getText().toString());
                            edt_cmt.setText("");

                            adapter.notifyDataSetChanged();
                            adapter2.notifyDataSetChanged();}
                        else {
                            Toast.makeText(context, "Comment is empty!!!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                }
            });

        }
    }


    public static String convertMillieToHMmSs(int millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
        else {
            return String.format("%02d:%02d" , minute, second);
        }

    }


        class MyGestures extends GestureDetector.SimpleOnGestureListener {

        FoldingCell fc ;
        VideoView videoView;
        ImageView imgpause;

            public MyGestures(FoldingCell fc, VideoView videoView, ImageView imgpause) {
                this.fc = fc;
                this.videoView = videoView;
                this.imgpause = imgpause;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                if((e1.getY() - e2.getY()) > 30 ){
                    fc.fold(false);
                    imgpause.setVisibility(View.GONE);
                    videoView.pause();
                    position_playing = -1 ;

                } else if ((e2.getY() - e1.getY()) > 50){
                    fc.unfold(false);
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }
}







