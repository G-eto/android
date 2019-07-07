package com.example.sqlite2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.example.sqlite2.database.model.Note;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note, String kind, String weather, int wordnumber) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("hhhhhhhhhhhhhhhhhhhhhinsert:","文本:"+ note +"天气:"+weather+"类别:"+kind);
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_NOTE, note);
        values.put(Note.COLUMN_KIND, kind);
        values.put(Note.COLUMN_WEATHER, weather);
        values.put(Note.COLUMN_WORDNUMBER, wordnumber);

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);
        Log.d("hgfhfgfhddhyg:",String.valueOf(id));
        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        //id++;
        Log.d("hhhhhgetNote(id)::::::",String.valueOf(id));
        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP,
                        Note.COLUMN_KIND, Note.COLUMN_WEATHER, Note.COLUMN_WORDNUMBER},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)),
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_WORDNUMBER)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_WEATHER)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_KIND)));

        // close the db connection
        cursor.close();
Log.d("娶到媳妇：",note.getNote()+"" +note.getKind()+""+note.getWordnumber());
        return note;
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
                note.setKind(cursor.getString(cursor.getColumnIndex(Note.COLUMN_KIND)));
                note.setWeather(cursor.getString(cursor.getColumnIndex(Note.COLUMN_WEATHER)));
                note.setWordnumber(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_WORDNUMBER)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note.getNote());
        values.put(Note.COLUMN_WORDNUMBER, note.getWordnumber());
        values.put(Note.COLUMN_KIND, note.getKind());
        values.put(Note.COLUMN_WEATHER, note.getWeather());
        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }

    //日历查找
    public List<Note> findNoteByDate(String date){
        List<Note> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Note.TABLE_NAME
                + " WHERE " + Note.COLUMN_TIMESTAMP +" " +" ORDER BY "
                + Note.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));
                note.setKind(cursor.getString(cursor.getColumnIndex(Note.COLUMN_KIND)));
                note.setWeather(cursor.getString(cursor.getColumnIndex(Note.COLUMN_WEATHER)));
                note.setWordnumber(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_WORDNUMBER)));
                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
}