package com.example.musicapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context){
        super(context, "music.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表的操作
        String sql = "create table user (_id integer primary key autoincrement, username varchar(20) unique not null, password varchar(20) not null)";
        String sql2 = "create table musiclike (_id integer primary key autoincrement, sid varchar(20) unique not null, musicname varchar(20))";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
