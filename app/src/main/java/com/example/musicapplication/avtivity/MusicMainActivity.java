package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView nextIv,playIv,lastIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    //数据源
    List<LocalMusicBean> mDatas;
    private LocalMusicAdapter adapter;
    //记录当前音乐位置
    private int currentPlayPosition = -1;
    //记录当前音乐暂停进度条
    private int currentMusicPausePosition = 0;
    //设置全局音乐播放器
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        initView();
        mediaPlayer = new MediaPlayer();
        mDatas = new ArrayList<>();
        //创建适配器对象
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        loadLocalMusicData();
        //设置每一项的点击事件
        setEventListener();
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPlayPosition = position;
                playMusicInPositon(position);
            }
        });
    }

    //播放指定位置的音乐
    private void playMusicInPositon(int position) {
        LocalMusicBean musicBean = mDatas.get(position);
        //设置底部显示点击的歌曲和歌手名称
        songTv.setText(musicBean.getSong());
        singerTv.setText(musicBean.getSinger());
        //暂停音乐函数
        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        //设置新的路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载本地数据源
    private void loadLocalMusicData(){
        //加载本地存储当中的音乐mp3文件到集合之中
        //1.获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        //2.获取本地音乐存储的uri地址
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //3.开始查询地址
        Cursor cursor = resolver.query(uri,null,null,null,null);
        //4.遍历cursor
        int id = 0;
        while(cursor.moveToNext()){
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            //将一行当中的数据封装到对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song,singer,album, time, path);
            mDatas.add(bean);
            //数据源变化，提示适配器更新
            adapter.notifyDataSetChanged();
        }
    }


    private void initView() {
        /*初始化控件的函数*/
        nextIv = findViewById(R.id.local_music__bottom_iv_next);
        playIv = findViewById(R.id.local_music__bottom_iv_play);
        lastIv = findViewById(R.id.local_music__bottom_iv_last);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);

        nextIv.setOnClickListener(MusicMainActivity.this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music__bottom_iv_last:
                if (currentPlayPosition == 0) currentPlayPosition = mDatas.size()-1;
                else currentPlayPosition--;
                playMusicInPositon(currentPlayPosition);
                break;
            case R.id.local_music__bottom_iv_play:
                if (currentPlayPosition == -1){
                    Toast.makeText(this, "请选择要播放的音乐", Toast.LENGTH_SHORT).show();
                } else {
                    if (mediaPlayer.isPlaying()){
                        //此时处于播放状态，需要暂停音乐
                        pauseMusic();
                    } else {
                        //否则处于暂停状态，需要播放音乐
                        playMusic();
                    }
                }
                break;
            case R.id.local_music__bottom_iv_next:
                if (currentPlayPosition == mDatas.size()-1) currentPlayPosition = 0;
                else currentPlayPosition++;
                playMusicInPositon(currentPlayPosition);
                break;
        }
    }

    //暂停音乐
    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            currentMusicPausePosition = mediaPlayer.getCurrentPosition(); //记录进度条
            mediaPlayer.pause();    //暂停
            playIv.setImageResource(R.mipmap.icon_play);    //设置为播放图标

        }
    }

    //播放音乐
    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()){
            //从停止到播放
            if (currentMusicPausePosition == 0){
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //否则的话就把进度条移动到暂停时候的位置，并开始播放
                mediaPlayer.seekTo(currentMusicPausePosition);
                mediaPlayer.start();
            }
            playIv.setImageResource(R.mipmap.icon_pause);
        }
    }

    //停止播放音乐
    private void stopMusic() {
        if (mediaPlayer != null){
            currentMusicPausePosition = 0;
            mediaPlayer.pause();    //暂停音乐
            mediaPlayer.seekTo(0); //进度条设置为0
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);  //把中间图标设置为播放

        }
    }
}
