package com.example.musicapplication.avtivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.musicapplication.R;

public class LikeMusicFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //从布局文件news.xml 加载一个布局文件
        View view = inflater.inflate(R.layout.like_music, container);
        return view;
    }
}
