package com.example.sqlite2.database.model;

public class Note {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    //public static final String COLUMN_DATE = "date";
    public static final String COLUMN_KIND = "kind";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_WORDNUMBER = "wordnumber";

    private int id;
    private String note;
    private String timestamp;
    //private String date;
    private String kind;
    private String weather;
    private int wordnumber;

    //未实现
    private String state;//置顶

    //create table sql query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_WORDNUMBER + " INTEGER,"
                    + COLUMN_KIND + " TEXT,"
                    + COLUMN_WEATHER + " TEXT)";
    public Note(){

    }
    public Note (int id, String note, String timestamp, int wordnumber, String weather, String kind){
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.wordnumber = wordnumber;
        this.kind = kind;
        this.weather = weather;
    }
    public int getId(){
        return id;
    }
    public String getNote(){
        return note;
    }
    public String getTimestamp(){
        return timestamp;
    }
    public int getWordnumber(){ return wordnumber; }
    public String getKind(){ return kind; }
    public String getWeather(){ return weather; }
    public String getState(){return state; }

    public void setId(int id){
        this.id = id;
    }
    public void setNote(String note){
        this.note = note;
    }
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }
    public void setWordnumber(int wordnumber) { this.wordnumber = wordnumber; }
    public void setKind(String kind){this.kind = kind; }
    public void setWeather(String weather){this.weather = weather; }
    public void setState(String state){this.state = state;}
}