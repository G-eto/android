package com.example.sqlite2.database.model;

public class Note {
    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_UPDATETIME = "updatetime";
    public static final String COLUMN_KIND = "kind";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_WORDNUMBER = "wordnumber";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_INSHORT = "inshort";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_TEMPERATURE = "temperature";

    private int id;
    private String note;
    private String timestamp;
    private String kind;
    private String weather;
    private int wordnumber;

    private String updatetime;
    private String location;
    private String inshort;
    private int mood;
    private String state;//置顶
    private int temperature;

    //未实现


    //create table sql query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NOTE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT ( datetime( 'now', 'localtime' ) ),"
                    + COLUMN_WORDNUMBER + " INTEGER,"
                    + COLUMN_KIND + " TEXT,"
                    + COLUMN_WEATHER + " TEXT,"
                    + COLUMN_UPDATETIME + " DATETIME DEFAULT ( datetime( 'now','localtime' ) ),"
                    + COLUMN_LOCATION + " TEXT,"
                    + COLUMN_INSHORT + " TEXT,"
                    + COLUMN_MOOD + " INTEGER,"
                    + COLUMN_STATE + " TEXT,"
                    + COLUMN_TEMPERATURE + " INTEGER)";
    public Note(){

    }
    public Note (int id, String note, String timestamp, int wordnumber, String kind, String weather,
                 String updatetime, String location, String inshort, int mood, String state, int temperature){
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
        this.wordnumber = wordnumber;
        this.kind = kind;
        this.weather = weather;
        this.mood = mood;
        this.inshort = inshort;
        this.updatetime = updatetime;
        this.location = location;
        this.state = state;
        this.temperature = temperature;
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
    public String getLocation(){return location;}
    public String getUpdatetime(){return updatetime;}
    public String getInshort(){return inshort;}
    public int getMood(){return mood;}
    public int getTemperature(){return temperature;}

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
    public void setLocation(String location){this.location = location;}
    public void setUpdatetime(String updatetime){this.updatetime = updatetime;}
    public void setInshort(String inshort){this.inshort = inshort;}
    public void setMood(int mood){this.mood = mood;}
    public void setTemperature(int temperature){this.temperature = temperature;}
}