package com.example.musicapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.musicapplication.avtivity.MusicMainActivity;
import com.example.musicapplication.bean.LocalMusicBean;

import java.io.IOException;
import java.util.ArrayList;

public class PlayMusicService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

    public ArrayList<LocalMusicBean> musicList;//音乐集合
    public int currentPosition = -1; //记录当前音乐的播放位置
    public int currentPausePosition = 0; //记录当前音乐暂停时候的位置
    public MediaPlayer mediaPlayer; //音乐播放器对象
    public Boolean isPlaying = false; //当前音乐是否播放
    public int recycle = 0;//默认0为列表循环
    public int like = 0;//默认该歌曲为非收藏歌曲
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
    //播放的循环方式
    public int recycleStyle(int recycle2){
        System.out.println(mediaPlayer.getDuration()+"!!!"+mediaPlayer.getCurrentPosition());
        if(recycle2==0&&mediaPlayer.getDuration()<=mediaPlayer.getCurrentPosition()+1000){
            System.out.println("进入跳转函数");
            if (currentPosition == musicList.size()-1) currentPosition = 0;
            else currentPosition++;
            playMusicInPosition(currentPosition);
            return mediaPlayer.getDuration();
        }
        else if(recycle2==1&&mediaPlayer.getDuration()<=mediaPlayer.getCurrentPosition()+1000){
            playMusicInPosition(currentPosition);
            return mediaPlayer.getDuration();
        }
        return mediaPlayer.getDuration();
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
