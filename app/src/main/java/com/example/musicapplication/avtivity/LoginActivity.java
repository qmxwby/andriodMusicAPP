package com.example.musicapplication.avtivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.db.DBManager;
import com.example.musicapplication.db.DateBaseBean;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText usernameEt, passwordEt;
    Button overBtn;
    ImageView backIv;
    List<DateBaseBean> userList;
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
        userList = DBManager.queryAllUser();

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
                    int flag = 0;
                    String pass = "";
                    for (int i = 0; i < userList.size(); i++) {
                        if (TextUtils.equals(username, userList.get(i).getUsername())) {
                             pass = userList.get(i).getPassword();
                            flag = 1;
                            break;
                        }
                    }
                    if (flag==0 || !TextUtils.equals(pass,password)){
                        Toast.makeText(this, "登录失败，请检查用户名和密码", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.setClass(this, MySpaceActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.login_back:
                intent.setClass(this, MySpaceActivity.class);
                startActivity(intent);
                break;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(this, MySpaceActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
