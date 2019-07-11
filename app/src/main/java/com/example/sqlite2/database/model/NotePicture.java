package com.example.sqlite2.database.model;

import android.media.Image;

public class NotePicture {
    public static final String TABLE_NAME = "notespicture";
    public static final String COLUMN_PICID = "picid";
    public static final String COLUMN_NOTEID = "noteid";
    public static final String COLUMN_PICTURE = "picture";

    private int id;
    private int noteid;
    private Image picture;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_PICID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTEID + " INTEGER,"
                    + COLUMN_PICTURE + " BLOB)";
    public NotePicture(){

    }

    public NotePicture (int noteid, int picid, Image pic) {
        this.noteid = noteid;
        this.picture = pic;
        this.id = picid;
    }

    public int getId(){return id;}
    public int getNoteid(){return noteid;}
    public Image getPicture(){return picture;}

    public void setId(int id){this.id = id;}
    public void setNoteid(int noteid){this.noteid = noteid;}
    public void setPicture(Image pic){this.picture = pic;}
}
