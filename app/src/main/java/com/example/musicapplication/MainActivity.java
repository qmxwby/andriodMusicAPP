package com.example.musicapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import com.example.musicapplication.base.MPermissionsActivity;

public class MainActivity extends MPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //请求读写权限
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
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
