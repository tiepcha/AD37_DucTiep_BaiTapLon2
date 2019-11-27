package com.example.myapplication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.VideoItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class All_Videos extends SQLiteOpenHelper {


    public All_Videos(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    public static final String TABLE_NAME = "allvideos";
    public static final String ID = "id";
    public static final String RECENT_TIME = "recent_time";
    public static final String ISLIKE = "islike";
    public static final String STAR = "star";
    public static final String TITLE = "title";
    public static final String AVATAR = "avatar";
    public static final String FILE_MP4 = "file_mp4";


    private String CREATE_TABLE = "CREATE TABLE allvideos (id integer , recent_time TEXT, islike integer , star integer , title TEXT , avatar TEXT , file_mp4 TEXT )";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void addVideo(VideoItem videoItem) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String select_query = "SELECT * FROM " + TABLE_NAME;
        String b = "";
        ArrayList<Integer> list = new ArrayList<>();


        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);


        ContentValues values = new ContentValues();
        values.put(ID, videoItem.getId());
        values.put(RECENT_TIME, videoItem.getRecent());
        values.put(ISLIKE, videoItem.isLike());
        values.put(STAR, videoItem.getRate());
        values.put(TITLE, videoItem.getTitle());
        values.put(AVATAR, videoItem.getAvatar());
        values.put(FILE_MP4, videoItem.getFile_mp4());


        if (!cursor.moveToFirst()) {
            b = "chưa có gì";
        } else {
            do {
                list.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }

        if (cursor.moveToFirst() && list.contains(videoItem.getId())) {
            b = "có rồi và chính nó";
        } else if (cursor.moveToFirst() && !list.contains(videoItem.getId())) {
            b = "có nhưng không phải";
        }
        switch (b) {
            case "chưa có gì":
                sqLiteDatabase.insert(TABLE_NAME, null, values);
                break;

            case "có rồi và chính nó":
                sqLiteDatabase.update(TABLE_NAME, values, "ID = " + videoItem.getId(), null);
                break;
            case "có nhưng không phải":
                sqLiteDatabase.insert(TABLE_NAME, null, values);
                break;

        }

        sqLiteDatabase.close();
    }

    public void searchVideo(String s, List<VideoItem> list) {
        list.clear();

        if (s.equalsIgnoreCase("")){
            s = "asdhfasdhfashdashdfashfas";
        }
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String select_query = "SELECT * FROM " + TABLE_NAME;
        String b = "";
//        ArrayList<Integer> list = new ArrayList<>();

        Cursor cursor = sqLiteDatabase.rawQuery(select_query, null);

        if (!cursor.moveToFirst()) {

        } else {
            do {
                if (cursor.getString(4).toLowerCase().contains(s.toLowerCase())) {
                    VideoItem videoItem = new VideoItem();
                    videoItem.setTitle(cursor.getString(4));
                    videoItem.setAvatar(cursor.getString(5));
                    videoItem.setFile_mp4(cursor.getString(6));
                    list.add(videoItem);
                }
            } while (cursor.moveToNext());


        }
        sqLiteDatabase.close();

    }
}
