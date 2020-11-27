package com.example.musicapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.musicapplication.bean.LocalMusicBean;

import java.io.IOException;
import java.util.ArrayList;

public class PlayMusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

    private ArrayList<LocalMusicBean> musicList;//音乐集合
    public int currentPosition = -1; //记录当前音乐的播放位置
    private int currentPausePosition = 0; //记录当前音乐暂停时候的位置
    private MediaPlayer mediaPlayer; //音乐播放器对象
    public Boolean isPlaying; //当前音乐是否播放

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    //播放指定位置的音乐
    public void playMusicInPosition(int position) {
        currentPosition = position;
        //获取当前位置的对象
        LocalMusicBean musicBean = musicList.get(position);
        //首先停止所有音乐播放
        stopMusic();
        //重置多媒体对象
        mediaPlayer.reset();
        //重新设置播放源
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //暂停播放
    public void pauseMusic() {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            //记录进度条
            currentPausePosition = mediaPlayer.getCurrentPosition();
            //暂停播放
            mediaPlayer.pause();
            //设置isplaying状态
            isPlaying = false;
        }
    }


    //播放音乐
    public void playMusic() {
        if (mediaPlayer!=null && !mediaPlayer.isPlaying()) {
            //从停止到播放
            if (currentPausePosition == 0){
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //否则的话就把进度条移动到暂停时候的位置，并开始播放
                mediaPlayer.seekTo(currentPausePosition);
                mediaPlayer.start();
            }
            isPlaying = true;
        }
    }

    //停止音乐播放
    public void stopMusic() {
        if (mediaPlayer != null){
            currentPausePosition = 0;
            mediaPlayer.pause();    //暂停音乐
            mediaPlayer.seekTo(0); //进度条设置为0
            mediaPlayer.stop();
            isPlaying = false;
        }
    }


    /*获得服务对象*/
    public class MusicBinder extends Binder {
        public PlayMusicService getService(){
            return PlayMusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        musicList = intent.getParcelableArrayListExtra("music");
        return new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();    //初始化音乐播放器对象
    }

}
