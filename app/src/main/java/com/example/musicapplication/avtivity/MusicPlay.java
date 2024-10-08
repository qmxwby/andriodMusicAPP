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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;
import com.example.musicapplication.db.DBManager;
import com.example.musicapplication.service.PlayMusicService;

import java.util.List;

public class MusicPlay extends AppCompatActivity implements View.OnClickListener, ServiceConnection {
    private ObjectAnimator discObjectAnimator,neddleObjectAnimator;
    PlayMusicService playMusicService;
    ImageView nextIv2,playIv2,lastIv2,backIv,shareIv,recycleIv,likeIv;
    TextView songTv2;
    private SeekBar seekBar;
    private Boolean isBind = false;
    int flag=0;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_play);
        initView();
        animators();
        myHandler.post(updateUI);
    }
    public void initView() {
        /*初始化控件的函数*/
        nextIv2 = findViewById(R.id.local_music__bottom_iv_next2);
        playIv2 = findViewById(R.id.local_music__bottom_iv_play2);
        lastIv2 = findViewById(R.id.local_music__bottom_iv_last2);
        songTv2 = findViewById(R.id.local_music_bottom_tv_song2);
        backIv = findViewById(R.id.back);
        shareIv = findViewById(R.id.share);
        seekBar = findViewById(R.id.progress);
        recycleIv = findViewById(R.id.local_music__bottom_iv_recycle);
        likeIv = findViewById(R.id.local_music__bottom_iv_like);

        //绑定Playmusicservice服务
        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, this, Service.BIND_AUTO_CREATE);
        nextIv2.setOnClickListener(this);
        lastIv2.setOnClickListener(this);
        playIv2.setOnClickListener(this);
        backIv.setOnClickListener(this);
        shareIv.setOnClickListener(this);
        recycleIv.setOnClickListener(this);
        likeIv.setOnClickListener(this);
        //seekBar.setOnClickListener(this);
    }
    public void animators(){
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
        discObjectAnimator.setDuration(10000);
        //使ObjectAnimator动画匀速平滑旋转
        discObjectAnimator.setInterpolator(new LinearInterpolator());
        //无限循环旋转
        discObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        discObjectAnimator.setRepeatMode(ValueAnimator.RESTART);


        neddleObjectAnimator = ObjectAnimator.ofFloat(needleImage, "rotation", -25, 0);
        needleImage.setPivotX(-25);
        needleImage.setPivotY(-25);
        neddleObjectAnimator.setDuration(400);
        neddleObjectAnimator.setInterpolator(new LinearInterpolator());
    }
    public void seekbar(){
        seekBar.setMax(playMusicService.mediaPlayer.getDuration());
        System.out.println("1111111总长度："+playMusicService.mediaPlayer.getDuration());
        seekBar.setProgress(playMusicService.mediaPlayer.getCurrentPosition());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                playMusicService.recycleStyle(playMusicService.recycle);
                seekBar.setMax(playMusicService.mediaPlayer.getDuration());
                songTv2.setText(playMusicService.musicList.get(playMusicService.currentPosition).getSong());
                if(fromUser){
                    //seekBar.getProgress()为进度条拖到的地方
                    playMusicService.mediaPlayer.seekTo(seekBar.getProgress());
                    //更新playMusicService记录进度条的位置
                    playMusicService.currentPausePosition = seekBar.getProgress();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //值改变后
            @Override
            public void onStopTrackingTouch(SeekBar seekBar2) {
                playMusicService.recycleStyle(playMusicService.recycle);
                seekBar.setMax(playMusicService.mediaPlayer.getDuration());
                songTv2.setText(playMusicService.musicList.get(playMusicService.currentPosition).getSong());
                //playMusicService.mediaPlayer.seekTo(seekBar2.getProgress());

                //new myThread().start();
            }
        });
    }
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            //获取歌曲进度并在进度条上展现
            seekBar.setProgress(playMusicService.mediaPlayer.getCurrentPosition());
            //获取播放位置
            // timeTextView.setText(time.format(mediaPlayer.getCurrentPosition()) + "s");
            myHandler.postDelayed(updateUI,1000);

        }

    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music__bottom_iv_last2:
                //System.out.println("cccccccccccccccc"+playMusicService.currentPosition);
                if (playMusicService.currentPosition <= 0) playMusicService.currentPosition = playMusicService.musicList.size()-1;
                else playMusicService.currentPosition--;
                setSong();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseIcon();
                seekbar();
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
                    seekbar();
                }
                break;
            case R.id.local_music__bottom_iv_next2:
                if (playMusicService.currentPosition == playMusicService.musicList.size()-1) playMusicService.currentPosition = 0;
                else playMusicService.currentPosition++;
                setSong();
                playMusicService.playMusicInPosition(playMusicService.currentPosition);
                setPauseIcon();
                seekbar();
                break;
            case R.id.back:
                Intent intent = new Intent(this, MusicMainActivity.class);
                //musicMainActivity为单例模式，不会套娃
                startActivity(intent);
                break;
            case R.id.share:
                String song = playMusicService.musicList.get(playMusicService.currentPosition).getSong();
                shareMusic("快来和我一起听"+song+"这首歌吧，新歌发行，好听到爆");
                break;
            case R.id.local_music__bottom_iv_recycle:
                if(playMusicService.recycle==0){
                    playMusicService.recycle=1;
                    recycleIv.setImageResource(R.mipmap.recycle2);
                    Toast.makeText(this, "开启单曲循环", Toast.LENGTH_SHORT).show();
                }else{
                    playMusicService.recycle=0;
                    recycleIv.setImageResource(R.mipmap.recycle1);
                    Toast.makeText(this, "开启列表循环", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.local_music__bottom_iv_like:
                if(playMusicService.like==0){
                    playMusicService.like=1;
                    likeIv.setImageResource(R.mipmap.like2);
                    Toast.makeText(this, "已添加到我喜欢的音乐",Toast.LENGTH_SHORT).show();
                    DBManager.addMusic(playMusicService.musicList.get(playMusicService.currentPosition).getId(), playMusicService.musicList.get(playMusicService.currentPosition).getSong());
                }else{
                    playMusicService.like=0;
                    likeIv.setImageResource(R.mipmap.like);
                }
                break;
        }
    }

    //修改手机自带返回功能
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, MusicMainActivity.class);
            //清空所有任务栈，防止套娃
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    //分享音乐功能，调用安卓本身的接口
    private void shareMusic(String msg) {
        //安卓自带的分享软件函数
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(intent, "音乐播放器"));
    }

    //设置播放图标
    private void setPlayIcon() {
        discObjectAnimator.pause();
        neddleObjectAnimator.reverse();
        playIv2.setImageResource(R.mipmap.play2);
    }
    //设置暂停图标
    private void setPauseIcon() {
        if(flag==1){
            discObjectAnimator.start();
            flag=0;
        }else
        discObjectAnimator.resume();
        neddleObjectAnimator.start();
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
        seekbar();
        if (playMusicService.isPlaying){
            setPauseIcon();
            discObjectAnimator.start();
            neddleObjectAnimator.start();
        } else {
            flag=1;
            setPlayIcon();

        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

}
