package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.VideoItem;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class Favorites_Video extends SQLiteOpenHelper {


    public Favorites_Video(@Nullable Context context) {
        super(context,TABLE_NAME, null, 1);
    }

    public static final String TABLE_NAME = "favorites";
    public static final String ID = "id";
    public static final String RECENT_TIME = "recent_time";
    public static final String ISLIKE = "islike";
    public static final String STAR = "star";


    private  String CREATE_TABLE = "CREATE TABLE favorites (id integer , recent_time TEXT, islike integer , star integer )";

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


//        switch (b) {
//
//            case "chưa có gì":
//                return b;
//            break;
//
//            case "có trong db":
//                return b;
//            break;
//
//            case "không có trong db":
//                return b;
//            break;
//
//        }
        return videoItem;

    }


}
