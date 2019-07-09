package com.example.sqlite2.view;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;
import com.example.sqlite2.utils.OnItemTouchListener;

public class ViewPage extends Activity {

    LinearLayout linearLayout;
    private FloatingActionButton editfab;
    private Button backButton;
    private TextView date;
    private TextView weather;
    private TextView output;

    //oncode
    public TextView wordnumber;
    public TextView state;
    public TextView time;
    public TextView kind;
    public TextView inshort;
    public TextView updatetime;
    public TextView mood;

//    private

    boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        linearLayout = findViewById(R.id.view_page);
        db = new DatabaseHelper(this);

        output = findViewById(R.id.page_text);
        date = findViewById(R.id.page_date);
        //weather = findViewById(R.id.weather);

        editfab = findViewById(R.id.edit_fab);
        backButton = findViewById(R.id.page_back);
        output.setMovementMethod(ScrollingMovementMethod.getInstance());

        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPage.this, EditActivity.class);
                intent.putExtra("note_id", note_id);
                startActivity(intent);
                //showNoteDialog(false, null, -1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewPage.this, MainActivity.class);

                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        int data = intent.getIntExtra("note_id",-1);
        Log.d("oooooooooooooooooooooooooooooooo:id","id:"+data);
        note_id = data;
        DisplayNote(note_id);


    }

    public void DisplayNote(int id){
        note = db.getNote(id);
        date.setText(note.getTimestamp());
        output.setText(note.getNote());
        //weather.setText(note.getWeather());
    }
}
