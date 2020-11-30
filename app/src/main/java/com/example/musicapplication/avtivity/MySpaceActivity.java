package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;
import com.example.musicapplication.utils.SearchFile;

import java.util.ArrayList;

public class MySpaceActivity extends AppCompatActivity implements View.OnClickListener {
    TextView loginTv;
    ImageView toLoginTv;
    String userName = null;  //记录用户名
    TextView musicAl,musicLi;
    RecyclerView musicAll,musicLike;
    ArrayList<LocalMusicBean> mDatas;
    private LocalMusicAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_space);
        setUserName();
        initView();

    }

    //设置用户名
    private void setUserName() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        System.out.println(userName);
    }

    private void initView() {
        loginTv = findViewById(R.id.textname);
        toLoginTv = findViewById(R.id.to_login);
        musicAl = findViewById(R.id.text_music_all);
        musicLi = findViewById(R.id.text_music_like);
        mDatas = SearchFile.searchLocalMusic(this);

        loginTv.setOnClickListener(this);
        musicAl.setOnClickListener(this);
        musicLi.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment f= null ;
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textname:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.text_music_all:
                f = new AllMusicFragment();
                break;
            case R.id.text_music_like:
                f = new LikeMusicFragment();
                break;
        }
        ft.replace(R.id.fragment, f);
        ft.commit();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
