package com.example.musicapplication.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapplication.R;
import com.example.musicapplication.db.DBManager;
import com.example.musicapplication.db.DateBaseBean;

import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameEt, password1Et, password2Et;
    ImageView backIv;
    Button registerBtn;
    String username, password1, password2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        backIv = findViewById(R.id.register_back);
        usernameEt = findViewById(R.id.username);
        password1Et = findViewById(R.id.password1);
        password2Et = findViewById(R.id.password2);
        registerBtn = findViewById(R.id.register_over);

        backIv.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_over:
                username = usernameEt.getText().toString();     //获取用户名输入内容
                password1 = password1Et.getText().toString();   //获取第一次密码输入内容
                password2 = password2Et.getText().toString();   //获取确认密码输入内容
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2)){
                    Toast.makeText(this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (!password1.equals(password2)){
                        Toast.makeText(this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                    } else {
                        //查询此用户是否注册
                        if (DBManager.queryUserByName(username) == 1) {
                            Toast.makeText(this, "此用户已经注册", Toast.LENGTH_SHORT).show();
                        }else {
                            //将用户数据保存到数据库中，并完成跳转
                            DBManager.addUser(username, password1);
                            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                            intent.setClass(this, HomeActivity.class);
                            //清除所有栈
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }
                }
                break;
        }
    }

    //从注册页面返回的时候要清空所有栈

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
