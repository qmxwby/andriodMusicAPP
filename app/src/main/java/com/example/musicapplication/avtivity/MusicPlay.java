package com.example.musicapplication.avtivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;
import com.example.musicapplication.service.PlayMusicService;

import java.util.List;

public class MusicPlay extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private ObjectAnimator discObjectAnimator,neddleObjectAnimator;
    PlayMusicService playMusicService;
    ImageView nextIv2,playIv2,lastIv2,backIv;
    TextView songTv2;
    private Boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play);
        initView();
        //最外部的半透明边线
        OvalShape ovalShape0 = new OvalShape();
        ShapeDrawable drawable0 = new ShapeDrawable(ovalShape0);
        drawable0.getPaint().setColor(0x10000000);
        drawable0.getPaint().setStyle(Paint.Style.FILL);
        drawable0.getPaint().setAntiAlias(true);

        //黑色唱片边框
        RoundedBitmapDrawable drawable1 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.disc));
        drawable1.setCircular(true);
        drawable1.setAntiAlias(true);

        //内层黑色边线
        OvalShape ovalShape2 = new OvalShape();
        ShapeDrawable drawable2 = new ShapeDrawable(ovalShape2);
        drawable2.getPaint().setColor(Color.BLACK);
        drawable2.getPaint().setStyle(Paint.Style.FILL);
        drawable2.getPaint().setAntiAlias(true);

        //最里面的图像
        RoundedBitmapDrawable drawable3 = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.xusong));
        drawable3.setCircular(true);
        drawable3.setAntiAlias(true);

        Drawable[] layers = new Drawable[4];
        layers[0] = drawable0;
        layers[1] = drawable1;
        layers[2] = drawable2;
        layers[3] = drawable3;

        LayerDrawable layerDrawable = new LayerDrawable(layers);

        int width = 10;
        //针对每一个图层进行填充，使得各个圆环之间相互有间隔，否则就重合成一个了。
        //layerDrawable.setLayerInset(0, width, width, width, width);
        layerDrawable.setLayerInset(1, width , width, width, width );
        layerDrawable.setLayerInset(2, width * 11, width * 11, width * 11, width * 11);
        layerDrawable.setLayerInset(3, width * 12, width * 12, width * 12, width * 12);

        final View discView = findViewById(R.id.myView);
        discView.setBackgroundDrawable(layerDrawable);
        ImageView needleImage= (ImageView) findViewById(R.id.needle);

        discObjectAnimator = ObjectAnimator.ofFloat(discView, "rotation", 0, 360);
        discObjectAnimator.setDuration(20000);
        //使ObjectAnimator动画匀速平滑旋转
        discObjectAnimator.setInterpolator(new LinearInterpolator());
        //无限循环旋转
        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discObjectAnimator.setRepeatMode(ValueAnimator.RESTART);


        neddleObjectAnimator = ObjectAnimator.ofFloat(needleImage, "rotation", -25, 0);
        needleImage.setPivotX(-25);
        needleImage.setPivotY(-25);
        neddleObjectAnimator.setDuration(800);
        neddleObjectAnimator.setInterpolator(new LinearInterpolator());
        discObjectAnimator.start();
        neddleObjectAnimator.start();
    }
    public void initView() {
        /*初始化控件的函数*/
        nextIv2 = findViewById(R.id.local_music__bottom_iv_next2);
        playIv2 = findViewById(R.id.local_music__bottom_iv_play2);
        lastIv2 = findViewById(R.id.local_music__bottom_iv_last2);
        songTv2 = findViewById(R.id.local_music_bottom_tv_song2);
        backIv = findViewById(R.id.back);

        //绑定Playmusicservice服务
        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, this, Service.BIND_AUTO_CREATE);
        nextIv2.setOnClickListener(this);
        lastIv2.setOnClickListener(this);
        playIv2.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music__bottom_iv_last2:
                if (playMusicService.currentPosition == 0) playMusicService.currentPosition = playMusicService.musicList.size()-1;
                else playMusicService.currentPosition--;
                setSong();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseIcon();
                break;
            case R.id.local_music__bottom_iv_play2:
                if (playMusicService.isPlaying) {
                    //正在播放，需要暂停
                    playMusicService.pauseMusic();
                    setPlayIcon();
                } else {
                    //暂停中，需要播放
                    playMusicService.playMusic();
                    setPauseIcon();
                }
                break;
            case R.id.local_music__bottom_iv_next2:
                if (playMusicService.currentPosition == playMusicService.musicList.size()-1) playMusicService.currentPosition = 0;
                else playMusicService.currentPosition++;
                setSong();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseIcon();
                break;
            case R.id.back:
                Intent intent = new Intent(this, MusicMainActivity.class);
                startActivity(intent);
                break;
        }
    }

    //设置播放图标
    private void setPlayIcon() {
        playIv2.setImageResource(R.mipmap.play2);
    }
    //设置暂停图标
    private void setPauseIcon() {
        playIv2.setImageResource(R.mipmap.pause2);
    }

    //    设置当前歌曲
    public void setSong(){
    //获取当前正在播放的音乐对象
        LocalMusicBean musicBean = playMusicService.musicList.get(playMusicService.currentPosition);
    //将歌曲名称显示在上边
        songTv2.setText(musicBean.getSong());
    }
    //绑定服务成功之后调用的函数
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        isBind = true;
        PlayMusicService.MusicBinder musicBinder = (PlayMusicService.MusicBinder) iBinder;
        //获取服务对象
        playMusicService = musicBinder.getService();
        setSong();
        //如果是暂停状态进入则还是暂停图标
        if (playMusicService.isPlaying){
            setPauseIcon();
        } else {
            setPlayIcon();
        }
    }
    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
