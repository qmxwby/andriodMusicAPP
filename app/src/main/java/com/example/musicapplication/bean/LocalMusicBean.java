package com.example.musicapplication.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class LocalMusicBean implements Parcelable {
    private String id;
    private String song;
    private String singer;
    private String album;
    private String duration;
    private String path;

    public LocalMusicBean(String id, String song, String singer, String album, String duration, String path) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }

    public LocalMusicBean() {
    }

    protected LocalMusicBean(Parcel in) {
        id = in.readString();
        song = in.readString();
        singer = in.readString();
        album = in.readString();
        duration = in.readString();
        path = in.readString();
    }

    public static final Creator<LocalMusicBean> CREATOR = new Creator<LocalMusicBean>() {
        @Override
        public LocalMusicBean createFromParcel(Parcel in) {
            return new LocalMusicBean(in);
        }

        @Override
        public LocalMusicBean[] newArray(int size) {
            return new LocalMusicBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(song);
        parcel.writeString(singer);
        parcel.writeString(album);
        parcel.writeString(duration);
        parcel.writeString(path);
    }
}
