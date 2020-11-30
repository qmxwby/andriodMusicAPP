package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;
import com.example.musicapplication.db.DBManager;
import com.example.musicapplication.utils.ActivityMananer;
import com.example.musicapplication.utils.SearchFile;

import java.util.ArrayList;

public class MySpaceActivity extends AppCompatActivity implements View.OnClickListener {
    TextView loginTv;
    ImageView toLoginTv;
    Button finishBtn;
    EditText newPass1, newPass2;
    String currentName = "";  //记录用户名
    TextView musicAl,musicLi;
    RecyclerView musicAll,musicLike;
    private LocalMusicAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_space);
        //添加到activity中进行管理
        ActivityMananer.addActivity(this);
        initView();
        loginTv.setText("立即登录");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
        newPass1.setText("");
        newPass2.setText("");
        setUserName();
    }

    //设置用户名
    private void setUserName() {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        if (userName == null) {
            return;
        }
        currentName = userName;
        loginTv.setText("欢迎你，"+userName);
    }

    private void initView() {
        loginTv = findViewById(R.id.textname);
        toLoginTv = findViewById(R.id.to_login);
        musicAl = findViewById(R.id.text_music_all);
        musicLi = findViewById(R.id.text_music_like);
        newPass1 = findViewById(R.id.newpass);
        newPass2 = findViewById(R.id.newpass1);
        finishBtn = findViewById(R.id.space_finish);
//        mDatas = SearchFile.searchLocalMusic(this);

        loginTv.setOnClickListener(this);
        musicAl.setOnClickListener(this);
        musicLi.setOnClickListener(this);
        finishBtn.setOnClickListener(this);
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
                ft.replace(R.id.fragment, f);
                ft.commit();
                break;
            case R.id.text_music_like:
                f = new LikeMusicFragment();
                ft.replace(R.id.fragment, f);
                ft.commit();
                break;
            case R.id.space_finish:
                String pass1 = newPass1.getText().toString();
                String pass2 = newPass2.getText().toString();
                if (TextUtils.isEmpty(pass1) || TextUtils.isEmpty(pass2)) {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.equals(pass1,pass2)) {
                        Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                    } else {
                        System.out.println("测试测试"+currentName+" "+pass2);
                        DBManager.updatePass(currentName, pass2);
                        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                        newPass1.setText("");
                        newPass2.setText("");
                    }
                }
                break;
        }
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
