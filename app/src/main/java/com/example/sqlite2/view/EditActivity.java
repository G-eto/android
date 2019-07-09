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

    Boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;

    final String tag = "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag,"hello edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new DatabaseHelper(this);

        Log.d(tag,"hello edit2");
        inputNote =findViewById(R.id.edit_text);
        saveButton = findViewById(R.id.edit_save);
        backButton = findViewById(R.id.edit_back);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(tag,"save");

                if(note_id > -1){
                    Log.d(tag,"updatestart");
                    updateNote();
                    Log.d(tag,"updone");
                    intent2ViewPage();
                }
                else if( note_id == -1 && inputNote.getText().toString().length() > 0){
                    Log.d(tag,"createstart");
                    createNote();
                    Log.d(tag,"createdone");
                    intent2Main();

                }
                Log.d(tag,"saveok");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(tag,"backback");
                finish();
            }
        });


        date = findViewById(R.id.edit_date);
        //weather = findViewById(R.id.weather);

        Intent intent = getIntent();
        int data = intent.getIntExtra("note_id",-1);
        note_id = data;
        Log.d(tag,"id:"+data);
        if(data == -1){
            //note_id = db.getNotesCount();
            shouldUpdate = true;
            date.setText("7/7");//日期
            //inputNote.setText("bugbugbug");
        }
        else{
            note = db.getNote(note_id);
            date.setText(note.getTimestamp());
            inputNote.setText(note.getNote());
            //weather.setText(note.getWeather());
        }
    }

    private void createNote() {
        // inserting note in db and getting
        // newly inserted note id

        note.setState("save");
        note.setWeather("下雪");
        note.setKind("杂记");
        note.setMood(10);
        note.setInshort("happy day");
        note.setLocation("南口");


        //note.setWeather(weather.getText().toString());
        note.setNote(inputNote.getText().toString());
        note.setWordnumber(note.getNote().length());
        //note.setKind(kind.getText().toString);
        //note.setState(state.getText().toString());
        Log.d(tag,note.getNote()+",f,vx");
        long id = db.insertNote(note.getNote(), "日记", "晴天",
                note.getWordnumber(),note.getLocation(), note.getInshort(), note.getState(), note.getMood());
        Log.d(tag,"backID:"+id);

        note_id = (int)id;
        note.setId(note_id);
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
//kind need to be add, not use get()
        note.setState("save");
        //note.setWeather(weather.getText().toString());
        note.setWeather("下雪");
        note.setKind("杂记");
        note.setMood(10);
        note.setInshort("happy day");
        note.setLocation("南口");
        note.setUpdatetime("new time");
        note.setWordnumber(inputNote.getText().toString().length());
        // updating note in db String kind, String weather, int wordnumber
        db.updateNote(note);

        //back to mainActivity

    }

    private void intent2ViewPage(){
        Log.d(tag,"inteng2PageView"+note_id);
        Intent intent = new Intent(EditActivity.this,ViewPage.class);
        intent.putExtra("note_id", note_id);
        startActivity(intent);
    }

    private void intent2Main(){
        Log.d(tag,"inteng2Main"+note_id);
        Intent intent = new Intent(EditActivity.this,MainActivity.class);
        //intent.putExtra("note_id", note_id);
        startActivity(intent);
    }

}