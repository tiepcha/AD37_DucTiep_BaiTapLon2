package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Interface.IonClickCTGRItem;
import com.example.myapplication.data.Favorites_Video;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.myapplication.R.drawable.like;
import static com.example.myapplication.R.drawable.unlike;
import static com.example.myapplication.R.string.awesome;
import static com.example.myapplication.R.string.bad;
import static com.example.myapplication.R.string.idontlikeit;
import static com.example.myapplication.R.string.notbad;

public class CTGRItemAdapter extends RecyclerView.Adapter<CTGRItemAdapter.Viewholder> {

    List<VideoItem> list;
    Context context;
    Favorites_Video favorites_video;
    int position_playing = -1 ;
    int position_expand = -1;

    IonClickCTGRItem ionClickCTGRItem;
    public CTGRItemAdapter(List<VideoItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setonClickCTGRItem(IonClickCTGRItem ionClickCTGRItem){
        this.ionClickCTGRItem = ionClickCTGRItem;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ctgr_item,parent,false);

        Viewholder viewholder = new Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        final VideoItem videoItem = list.get(position);
        favorites_video = new Favorites_Video(context);

        favorites_video.check(videoItem);

        if (position_expand != -1 ){
            holder.overlay.setVisibility(View.VISIBLE);
        }else {
            holder.overlay.setVisibility(View.GONE);
        }

        if (position != position_playing){
            holder.avLoadingIndicatorView.hide();
        }else {
            holder.avLoadingIndicatorView.smoothToShow();
        }

        holder.ratingbar_ctgr_item.setRating(videoItem.getRate());

        if (!videoItem.getRecent().equalsIgnoreCase("null")){
            holder.img_ctgr_recent.setVisibility(View.VISIBLE);
//            holder.tvrecent.setText(videoItem.getRecent());
        }

        if (videoItem.isLike()==1){ holder.img_ctgr_like.setBackgroundResource(like); }
        else { holder.img_ctgr_like.setBackgroundResource(unlike); }

        Picasso.get().load(videoItem.getAvatar()).fit().into(holder.img_ctgr_item);
        holder.tv_ctgr_nameitem.setText(videoItem.getTitle());
        holder.ratingbar_ctgr_item.setRating(videoItem.getRate());
        holder.tv_ctgr_expand_date_public.setText( context.getResources().getString(R.string.UploadTime) +videoItem.getDate_published());

        holder.tv_ctgr_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.rl_ctgr_more_info.getHeight() >10){

                    collapse(holder.rl_ctgr_more_info,500,0);
                    holder.tv_ctgr_expand.setText(context.getResources().getString(R.string.expand));
                    position_expand = -1;
                    ionClickCTGRItem.onRefresh();
                }else {
                    holder.overlay.setVisibility(View.GONE);
                    expand(holder.rl_ctgr_more_info,500,1000);
                    holder.tv_ctgr_expand.setText(context.getResources().getString(R.string.collapse));
                    position_expand = position;
                    ionClickCTGRItem.onClickExpand(position);
                }
            }
        });
        holder.noname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.rl_ctgr_more_info.getHeight() >10){

                    collapse(holder.rl_ctgr_more_info,500,0);
                    holder.tv_ctgr_expand.setText(context.getResources().getString(R.string.expand));
                    position_expand = -1;
                    ionClickCTGRItem.onRefresh();
                }else {
                    holder.overlay.setVisibility(View.GONE);
                    expand(holder.rl_ctgr_more_info,500,1000);
                    holder.tv_ctgr_expand.setText(context.getResources().getString(R.string.collapse));
                    position_expand = position;
                    ionClickCTGRItem.onClickExpand(position);
                }
            }
        });

        holder.img_ctgr_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat df = new SimpleDateFormat("d/M yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                videoItem.setRecent(date);
                favorites_video.addFavoriteVideo(videoItem);

                position_playing = position;
                holder.overlay.setVisibility(View.GONE);

                ionClickCTGRItem.onClickAvatar(videoItem,position);
                holder.avLoadingIndicatorView.smoothToShow();
//                holder.vusikView.start();


//                ionClickCTGRItem.onClickExpand(position);


                holder.img_ctgr_recent.setVisibility(View.VISIBLE);
            }
        });
        holder.tv_ctgr_nameitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormat df = new SimpleDateFormat("d/M yyyy, HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                videoItem.setRecent(date);
                favorites_video.addFavoriteVideo(videoItem);

                position_playing = position;
                holder.overlay.setVisibility(View.GONE);

                ionClickCTGRItem.onClickAvatar(videoItem,position);
                holder.avLoadingIndicatorView.smoothToShow();
                holder.img_ctgr_recent.setVisibility(View.VISIBLE);
            }
        });


        holder.img_ctgr_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoItem.isLike()==0) {
                    videoItem.setLike(1);
                    holder.img_ctgr_like.setBackgroundResource(like);
                    favorites_video.addFavoriteVideo(videoItem);
                } else {videoItem.setLike(0);holder.img_ctgr_like.setBackgroundResource(unlike);
                    favorites_video.addFavoriteVideo(videoItem);}
            }

        });

        holder.ratingbar_ctgr_item.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                videoItem.setRate((int)v);
                favorites_video.addFavoriteVideo(videoItem);
            }
        });

        holder.rl_ctgr_expand_comment.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // Disallow the touch request for parent scroll on touch of  child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        holder.awesome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edt_ctgr_expand_comment.setText(holder.edt_ctgr_expand_comment.getText() + context.getResources().getString(awesome));
            }
        });
        holder.notbad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edt_ctgr_expand_comment.setText(holder.edt_ctgr_expand_comment.getText() + context.getResources().getString(notbad));
            }
        });
        holder.bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edt_ctgr_expand_comment.setText(holder.edt_ctgr_expand_comment.getText() + context.getResources().getString(bad));
            }
        });
        holder.idontlikeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.edt_ctgr_expand_comment.setText(holder.edt_ctgr_expand_comment.getText() + context.getResources().getString(idontlikeit));
            }
        });

        holder.overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//---------------------------------------Set text khi ấn awesom ..v..v.------------------------------------
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder{
        RelativeLayout rl_ctgr_more_info;

        TextView tv_ctgr_expand_date_public,tv_ctgr_expand_comment,
                rl_ctgr_expand_comment_push,tv_ctgr_nameitem,
                tv_ctgr_expand
        ;

        EditText edt_ctgr_expand_comment;

        RecyclerView rl_ctgr_expand_comment;

        ImageView img_ctgr_item,img_ctgr_like,img_ctgr_recent;

        RatingBar ratingbar_ctgr_item;

        TextView overlay;

        AVLoadingIndicatorView avLoadingIndicatorView;

        TextView awesome , notbad , bad , idontlikeit;

        TextView noname;




        public Viewholder(@NonNull View itemView) {
            super(itemView);

//            vusikView = (VusikView) itemView.findViewById(R.id.vusik);
            avLoadingIndicatorView = itemView.findViewById(R.id.avi);
            rl_ctgr_more_info = itemView.findViewById(R.id.rl_ctgr_more_info);
            tv_ctgr_expand_date_public = itemView.findViewById(R.id.tv_ctgr_expand_date_public);
            tv_ctgr_expand_comment = itemView.findViewById(R.id.tv_ctgr_expand_comment);

            rl_ctgr_expand_comment_push = itemView.findViewById(R.id.rl_ctgr_expand_comment_push);
            tv_ctgr_nameitem = itemView.findViewById(R.id.tv_ctgr_nameitem);
            edt_ctgr_expand_comment = itemView.findViewById(R.id.edt_ctgr_expand_comment);
            rl_ctgr_expand_comment = itemView.findViewById(R.id.rl_ctgr_expand_comment);
            img_ctgr_item = itemView.findViewById(R.id.img_ctgr_item);
            img_ctgr_like = itemView.findViewById(R.id.img_ctgr_like);
            img_ctgr_recent = itemView.findViewById(R.id.img_ctgr_recent);
            ratingbar_ctgr_item = itemView.findViewById(R.id.ratingbar_ctgr_item);
            LayerDrawable stars = (LayerDrawable) ratingbar_ctgr_item.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#ffffbb33"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(1).setColorFilter(Color.parseColor("#B4FFFFFF"), PorterDuff.Mode.SRC_ATOP);
            stars.getDrawable(0).setColorFilter(Color.parseColor("#B4FFFFFF"),PorterDuff.Mode.SRC_ATOP);
            overlay = itemView.findViewById(R.id.overlay);

            tv_ctgr_expand = itemView.findViewById(R.id.tv_ctgr_expand);
            noname = itemView.findViewById(R.id.noname);

            awesome = itemView.findViewById(R.id.awesome);
            notbad = itemView.findViewById(R.id.notbad);
            bad = itemView.findViewById(R.id.bad);
            idontlikeit = itemView.findViewById(R.id.idontlikeit);


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

             final AdapterComment adapter = new AdapterComment(listcomment);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.VERTICAL,false);
            rl_ctgr_expand_comment.setLayoutManager(layoutManager);
            rl_ctgr_expand_comment.setAdapter(adapter);
            tv_ctgr_expand_comment.setText(context.getResources().getString(R.string.Comment)  + " ("+ listcomment.size() +")");

            edt_ctgr_expand_comment.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                    //If the keyevent is a key-down event on the "enter" button
                    if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        if(!edt_ctgr_expand_comment.getText().toString().isEmpty()){
                            listcomment.add(0,edt_ctgr_expand_comment.getText().toString());
                            edt_ctgr_expand_comment.setText("");

                            adapter.notifyDataSetChanged();
                          }
                        else {
                            Toast.makeText(context, "Comment is empty!!!", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                }
            });

            edt_ctgr_expand_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b == false){

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edt_ctgr_expand_comment.getWindowToken(), 0);

                    }
                }
            });


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

    public  void showplaying(int p){
    position_playing = p;
    }

}

