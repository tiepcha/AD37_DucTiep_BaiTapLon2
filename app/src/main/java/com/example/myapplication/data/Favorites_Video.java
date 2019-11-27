package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.VideoItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Favorites_Video extends SQLiteOpenHelper {
    Context context;



    public Favorites_Video(@Nullable Context context) {
        super(context,TABLE_NAME, null, 1);
    }

    public static final String TABLE_NAME = "favorites";
    public static final String ID = "id";
    public static final String RECENT_TIME = "recent_time";
    public static final String ISLIKE = "islike";
    public static final String STAR = "star";
    public static final String TITLE = "title";
    public static final String AVATAR = "avatar";
    public static final String FILE_MP4 = "file_mp4";


    private  String CREATE_TABLE = "CREATE TABLE favorites (id integer , recent_time TEXT, islike integer , star integer , title TEXT , avatar TEXT , file_mp4 TEXT )";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addFavoriteVideo(VideoItem videoItem){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String select_query = "SELECT * FROM "+ TABLE_NAME;
        String b = "";
        ArrayList<Integer> list = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(select_query,null);


        ContentValues values = new ContentValues();
        values.put(ID,videoItem.getId());
        values.put(RECENT_TIME,videoItem.getRecent());
        values.put(ISLIKE,videoItem.isLike());
        values.put(STAR,videoItem.getRate());
        values.put(TITLE,videoItem.getTitle());
        values.put(AVATAR,videoItem.getAvatar());
        values.put(FILE_MP4,videoItem.getFile_mp4());


        if(!cursor.moveToFirst()){b = "chưa có gì";}
        else {
            do {
                list.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst() && list.contains(videoItem.getId())){
            b = "có rồi và chính nó";
        }else if (cursor.moveToFirst() && !list.contains(videoItem.getId())){
            b = "có nhưng không phải";
        }
        switch (b) {
            case "chưa có gì" :
                sqLiteDatabase.insert(TABLE_NAME,null,values);
                break;

            case "có rồi và chính nó" :
                sqLiteDatabase.update(TABLE_NAME,values,"ID = "+videoItem.getId(),null);
                break;
            case "có nhưng không phải" :
                sqLiteDatabase.insert(TABLE_NAME,null,values);
                break;

        }

        sqLiteDatabase.close();
    }




    public VideoItem check(VideoItem videoItem) {


        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String select_query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);
        String b = "";
        ArrayList<Integer> list = new ArrayList<>();
        int like = -1;
        String recent = "null";
        int rate = 3;

        if(!cursor.moveToFirst()){b = "chưa có gì";}
        else {
            do {
                list.add(cursor.getInt(0));
                if (cursor.getInt(0)==videoItem.getId()){
                    like = cursor.getInt(2);
                    recent = cursor.getString(1);
                    rate = cursor.getInt(3);
                }
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst() && list.contains(videoItem.getId())){
            b = "có rồi và chính nó";
        }else if (cursor.moveToFirst() && !list.contains(videoItem.getId())){
            b = "có nhưng không phải";
        }
        switch (b) {
            case "chưa có gì" :
                break;

            case "có rồi và chính nó" :
               videoItem.setRecent(recent);
               videoItem.setLike(like);
               videoItem.setRate(rate);
                break;
            case "có nhưng không phải" :
                break;

        }

        sqLiteDatabase.close();

        return videoItem;

    }

    public void checkList(List<VideoItem> listRecent ,List<VideoItem> listFavorite ){

        listFavorite.clear();
        listRecent.clear();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String select_query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        if (cursor.moveToFirst()){
            do {
                    VideoItem videoItem = new VideoItem();
                    videoItem.setId(cursor.getInt(0));

                    videoItem.setTitle(cursor.getString(4));
                    videoItem.setAvatar(cursor.getString(5));
                    videoItem.setFile_mp4(cursor.getString(6));
                    videoItem.setRecent(cursor.getString(1));
                    videoItem.setLike(cursor.getInt(2));

                    if (!videoItem.getRecent().equalsIgnoreCase("null")) {
                        listRecent.add(videoItem);

                    }
                    if ((videoItem.isLike()==1)){
                        listFavorite.add(videoItem);
                    }

            }while (cursor.moveToNext());



            }


        sqLiteDatabase.close();

    }


}
