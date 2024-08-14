package com.example.musicapplication.avtivity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.R;
import com.example.musicapplication.adapter.LocalMusicAdapter;
import com.example.musicapplication.bean.LocalMusicBean;

import java.util.ArrayList;

public class AllMusicFragment extends Fragment {
    RecyclerView musicAl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        //从布局文件news.xml 加载一个布局文件
        View view = inflater.inflate(R.layout.all_music, container,false);
        return view;
    }
}
