package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapplication.R;

public class MySpaceActivity extends AppCompatActivity implements View.OnClickListener {
    TextView loginTv;
    ImageView toLoginTv;
    String userName = null;  //记录用户名
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

        loginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textname:
                intent.setClass(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
