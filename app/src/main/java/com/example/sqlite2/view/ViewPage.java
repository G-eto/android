package com.example.sqlite2.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.DebugUtils;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;
import com.example.sqlite2.utils.OnItemTouchListener;
import com.example.sqlite2.utils.OnPageTouchListener;

public class ViewPage extends Activity {

    View view;
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
    private GestureDetector gestureDetector=null;
    int dbcount;

    //@SuppressLint("ClickableViewAccessibility")
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);
        view = findViewById(R.id.view_page);

        db = new DatabaseHelper(this);
        dbcount = db.getNotesCount();
        output = findViewById(R.id.page_text);
        date = findViewById(R.id.page_date);
        //weather = findViewById(R.id.weather);

        editfab = findViewById(R.id.edit_fab);
        backButton = findViewById(R.id.page_back);
        output.setMovementMethod(ScrollingMovementMethod.getInstance());

        ScrollView scroll = findViewById(R.id.src_over);
        scroll.setVerticalScrollBarEnabled(false);

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

        gestureDetector=new GestureDetector(this, new OnPageTouchListener() {
            @Override
            public void onPageFling(char lr) {
                if(lr == 'R' && note_id < dbcount){
                    Intent intent = new Intent(ViewPage.this, ViewPage.class);
                    intent.putExtra("note_id",note_id + 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_out, R.anim.left_in);
                }
                else if(lr == 'L' && note_id > 1){
                    Intent intent = new Intent(ViewPage.this, ViewPage.class);
                    intent.putExtra("note_id",note_id - 1);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }
        });
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        Display("ACTION_DOWN",event);
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        Display("ACTION_MOVE",event);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Display("ACTION_UP",event);
//                        break;
//                }
//                return gestureDetector.onTouchEvent(event); //此处不作任何触摸处理，转去手势识别对象进行手势分析
//            }
//        });

        View test = findViewById(R.id.page_text);
        test.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        Display("ACTION_DOWN(rrrrrrrrrrrrrr)",event);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Display("ACTION_MOVE(rrrrrrrrrrrrrr)",event);
                        break;
                    case MotionEvent.ACTION_UP:
                        Display("ACTION_UP(rrrrrrrrrrrrrrr)",event);
                        break;
                }
                return gestureDetector.onTouchEvent(event);
            }
        });

    }

    public void DisplayNote(int id){
        note = db.getNote(id);
        date.setText(note.getTimestamp());
        output.setText(note.getNote());
        //weather.setText(note.getWeather());
    }

    private void Display(String eventType, MotionEvent event){

        int x = (int)event.getX();
        int y = (int)event.getY();
        float pressure = event.getPressure();
        float size = event.getSize();
        int RawX = (int)event.getRawX();
        int RawY = (int)event.getRawY();

        String msg = "";
        msg += "事件类型：" + eventType + "\n";
        msg += "相对坐标："+String.valueOf(x)+","+String.valueOf(y)+"\n";
        msg += "绝对坐标："+String.valueOf(RawX)+","+String.valueOf(RawY)+"\n";
        msg += "触点压力："+String.valueOf(pressure)+"\n";
        msg += "触点尺寸："+String.valueOf(size)+"\n";

        Log.d("hhhhhhhhhhhhhhhhhh:",msg);
    }
}
