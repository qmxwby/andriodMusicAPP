package com.example.musicapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    public static SQLiteDatabase db;
    /*初始化数据库信息*/
    public static void initSQLite(Context context){
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    //添加用户信息
    public static long addUser(String userName, String passWord){
        ContentValues values = new ContentValues();
        values.put("username", userName);
        values.put("password", passWord);
        return db.insert("user", null, values);
    }

    //查询所有用户信息
    public static List<DateBaseBean> queryAllUser(){
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        List<DateBaseBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String userName = cursor.getString(cursor.getColumnIndex("username"));
            String passWord = cursor.getString(cursor.getColumnIndex("password"));
            list.add(new DateBaseBean(id, userName, passWord));
        }
        return list;
    }
    //修改用户密码
    public static int updatePass(String username, String pass) {
        ContentValues values = new ContentValues();
        values.put("password", pass);
        return db.update("user", values, "username=?", new String[]{username});
    }
    //查询指定用户是否注册
    public static int queryUserByName(String name) {
        int flag = 0;
        Cursor cursor = db.query("user", null, "username=?", new String[]{name}, null, null, null);
        //判断是否存在该用户
        if (cursor.getCount() > 0) {
            flag = 1;
        }
        return flag;
    }

    //增加收藏
    public static long addMusic(String sid, String musicName){
        ContentValues values = new ContentValues();
        values.put("sid", sid);
        values.put("musicname", musicName);
        return db.insert("musiclike", null, values);
    }
    //查询所有收藏歌曲
}
