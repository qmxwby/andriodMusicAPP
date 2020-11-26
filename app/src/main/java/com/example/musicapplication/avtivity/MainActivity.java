package com.example.musicapplication.avtivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapplication.R;
import com.example.musicapplication.base.MPermissionsActivity;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener {

    ImageView wechatIv, qqIv, weiboIv, wangyiIv;
    Button registerBtn, experienceBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求读写权限
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        //完成页面初始化
        initView();
    }

    private void initView() {
        wechatIv = findViewById(R.id.wechat_icon);
        qqIv = findViewById(R.id.qq_icon);
        weiboIv = findViewById(R.id.weibo_icon);
        wangyiIv = findViewById(R.id.wangyi_icon);
        registerBtn = findViewById(R.id.register);
        experienceBtn = findViewById(R.id.experience);

        wechatIv.setOnClickListener(this);
        qqIv.setOnClickListener(this);
        weiboIv.setOnClickListener(this);
        wangyiIv.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        experienceBtn.setOnClickListener(this);
    }





    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.wechat_icon:
                Toast.makeText(this,"程序员哥哥正在加班完善该功能中~，稍安勿躁",Toast.LENGTH_SHORT).show();
                break;
            case R.id.qq_icon:
                Toast.makeText(this,"程序员哥哥正在加班完善该功能中~，稍安勿躁",Toast.LENGTH_SHORT).show();
                break;
            case R.id.weibo_icon:
                Toast.makeText(this,"程序员哥哥正在加班完善该功能中~，稍安勿躁",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wangyi_icon:
                Toast.makeText(this,"程序员哥哥正在加班完善该功能中~，稍安勿躁",Toast.LENGTH_SHORT).show();
                break;
            case R.id.experience:
                intent.setClass(this, MusicMainActivity.class);
                startActivity(intent);
                break;
            case R.id.register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 权限获取成功
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        requestPermissionResult(requestCode, true);
    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
        requestPermissionResult(requestCode, false);
    }

    /**
     * 申请结果
     *
     * @param code
     * @param flag
     */
    private void requestPermissionResult(int code, boolean flag) {
        switch (code) {
            case 1000:
                if (flag) {//获取权限成功
                    return;
                } else {//获取权限失败
                    Toast.makeText(this, "获取权限失败，无法扫描本地音乐", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
