package com.example.sqlite2.view;

import android.app.Activity;
import android.os.Bundle;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;

import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhipeng on 16/1/13.
 */
public class EditActivity extends Activity {

    private PictureAndTextEditorView mEditText;
    private EditText inputNote;
    private Button saveButton;
    private Button backButton;
    private TextView date;
    private TextView weather;
//    private

    boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        inputNote =findViewById(R.id.edit_text);
        saveButton = findViewById(R.id.edit_save);
        backButton = findViewById(R.id.edit_back);
        date = findViewById(R.id.edit_date);
        weather = findViewById(R.id.weather);

        Intent intent = getIntent();
        int data = intent.getIntExtra("note_id",-1);

        if(data == -1){
            note_id = db.getNotesCount();
            shouldUpdate = true;
            date.setText("");//日期
        }
        else{
            note_id = data;
            note = db.getNote(note_id);
            date.setText(note.getNote());
            inputNote.setText(note.getNote());
            weather.setText(note.getWeather());
        }






    }

    private void createNote() {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(inputNote.getText().toString(), "日记", weather.getText().toString(), inputNote.getText().toString().length());
        Log.d("before:",String.valueOf(id));
        // get the newly inserted note from db
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     * 需要添加
     */
    private void updateNote() {
        //Note n = notesList.get(position);
        // updating note text
        note.setNote(inputNote.getText().toString());
        //note.setKind();
//kind need to be add, not use get()
        //note.setState();
        note.setWeather(weather.getText().toString());
        note.setWordnumber(inputNote.getText().toString().length());
        // updating note in db String kind, String weather, int wordnumber
        db.updateNote(note);

        //back to mainActivity
        
    }

    private void back2fromActivity(){

    }

}