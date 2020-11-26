package com.example.musicapplication.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapplication.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView backIv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        backIv = findViewById(R.id.register_back);


        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
        }
    }
}
