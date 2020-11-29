package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.db.DBManager;
import com.example.musicapplication.db.DateBaseBean;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameEt, passwordEt;
    Button overBtn;
    ImageView backIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        usernameEt = findViewById(R.id.login_username);
        passwordEt = findViewById(R.id.login_password);
        overBtn = findViewById(R.id.login_over);
        backIv = findViewById(R.id.login_back);

        overBtn.setOnClickListener(this);
        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.login_over:
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "输入框内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (DBManager.queryUserByusername(username) == null) {
                        System.out.println("111");
                    }else{
                        System.out.println(222);
                    }
                }
                break;
            case R.id.login_back:
                finish();
                break;
        }
    }
}
