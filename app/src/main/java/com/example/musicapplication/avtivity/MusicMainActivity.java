package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;
import com.example.musicapplication.service.PlayMusicService;
import com.example.musicapplication.utils.ActivityMananer;
import com.example.musicapplication.utils.SearchFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    ImageView nextIv,playIv,lastIv,enterToPlayIv;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    //数据源
    ArrayList<LocalMusicBean> mDatas;
    private LocalMusicAdapter adapter;

    //播放服务对象
    PlayMusicService playMusicService;
    //是否绑定服务
    private boolean isBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        initView();
        //创建适配器对象
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //绑定服务
        loadLocalMusicData();
        //设置每一项的点击事件
        setEventListener();
        ActivityMananer.addActivity(this);
        System.out.println("我是oncreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
//        this.setIntent(intent);
        super.onNewIntent(intent);
        clickChange();
        System.out.println("我是onnewIntent");
    }

    private void setEventListener() {
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                //如果点击同一首歌两次，则进入播放页面
                if (playMusicService.currentPosition == position) {
                    Intent intent = new Intent(MusicMainActivity.this, MusicPlay.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    playMusicService.currentPosition = position;
                    //设置底部显示
                    clickChange();
                    //调用音乐服务对象，播放指定位置的音乐
                    playMusicService.playMusicInPosition(position);
                    setPauseicon();
                }
            }
        });
    }

    //根据当前位置修改歌曲名称和歌手名称
    public void clickChange(){
        if (playMusicService.currentPosition == -1) return;
        //获取当前位置的对象
        LocalMusicBean musicBean = mDatas.get(playMusicService.currentPosition);
        //设置底部显示点击的歌曲和歌手名称
        songTv.setText(musicBean.getSong());
        singerTv.setText(musicBean.getSinger());

        //如果正在播放，则为暂停图标
        if (playMusicService.isPlaying) {
            setPauseicon();
        } else {
            setPlayicon();
        }
    }

    //加载本地数据源
    private void loadLocalMusicData(){
        //同时绑定Playservice对象
        Intent intent = new Intent(this, PlayMusicService.class);
        System.out.println("开始绑定service对象");
        intent.putParcelableArrayListExtra("music", mDatas);
        bindService(intent,this, Service.BIND_AUTO_CREATE);
    }


    private void initView() {
        /*初始化控件的函数*/
        nextIv = findViewById(R.id.local_music__bottom_iv_next);
        playIv = findViewById(R.id.local_music__bottom_iv_play);
        lastIv = findViewById(R.id.local_music__bottom_iv_last);
        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        musicRv = findViewById(R.id.local_music_rv);
        enterToPlayIv = findViewById(R.id.local_music__bottom_iv_icon);

        //获取数据源
        mDatas = SearchFile.searchLocalMusic(this);

        nextIv.setOnClickListener(MusicMainActivity.this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);
        enterToPlayIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music__bottom_iv_last:
                if (playMusicService.currentPosition <= 0) playMusicService.currentPosition = mDatas.size()-1;
                else playMusicService.currentPosition--;
                clickChange();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseicon();
                break;
            case R.id.local_music__bottom_iv_play:
                if (playMusicService.currentPosition == -1){
                    Toast.makeText(this, "请选择要播放的音乐", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("cccccccccccccccc"+playMusicService.isPlaying);
                    if (playMusicService.isPlaying){
                        //此时处于播放状态，需要暂停音乐
                        playMusicService.pauseMusic();
                        setPlayicon();
                    } else {
                        //否则处于暂停状态，需要播放音乐
                        playMusicService.playMusic();
                        setPauseicon();
                    }
                }
                break;
            case R.id.local_music__bottom_iv_next:
                if (playMusicService.currentPosition == mDatas.size()-1) playMusicService.currentPosition = 0;
                else playMusicService.currentPosition++;
                clickChange();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseicon();
                break;
            case R.id.local_music__bottom_iv_icon:
                if (playMusicService.currentPosition == -1){
                    Toast.makeText(this, "请选择要播放的音乐", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, MusicPlay.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //设置播放图标
    public void setPlayicon(){
        playIv.setImageResource(R.mipmap.icon_play);
    }
    //设置暂停图标
    public void setPauseicon(){
        playIv.setImageResource(R.mipmap.icon_pause);
    }

    //与service建立链接之后调用
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        isBind = true;
        PlayMusicService.MusicBinder musicBinder = (PlayMusicService.MusicBinder) iBinder;
        //获取service对象
        playMusicService = musicBinder.getService();
        clickChange();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
