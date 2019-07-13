package com.example.sqlite2.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;
import com.example.sqlite2.utils.OnItemTouchListener;
import com.example.sqlite2.utils.OnPageTouchListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewPage extends Activity {

    View view;
    private FloatingActionButton editfab;
    private Button backButton;
    private Button share;
    private Button delete;
    private Button copy;

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
    public TextView location;

    public TextView wordnumber_icon;
    public TextView weather_icon;
    public TextView location_icon;
    public TextView kind_icon;
    public TextView inshort_icon;
    public TextView updatetime_icon;
    public TextView mood_icon;

//    private

    boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;
    private GestureDetector gestureDetector=null;
    int dbcount;

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
        inshort = findViewById(R.id.page_inshort);
        editfab = findViewById(R.id.edit_fab);
        backButton = findViewById(R.id.page_back);

        copy = findViewById(R.id.copy_page);
        share = findViewById(R.id.share_page);
        delete = findViewById(R.id.delete_page);

        output.setMovementMethod(ScrollingMovementMethod.getInstance());

        ScrollView scroll = findViewById(R.id.src_over);
        scroll.setVerticalScrollBarEnabled(false);

        wordnumber = findViewById(R.id.page_wordnumber);
        weather = findViewById(R.id.page_weather);
        location = findViewById(R.id.page_location);
        kind = findViewById(R.id.page_kind);
        updatetime = findViewById(R.id.page_time);
        mood = findViewById(R.id.page_mood);

        inshort_icon = findViewById(R.id.page_inshort_icon);
        wordnumber_icon = findViewById(R.id.page_wordnumber_icon);
        weather_icon = findViewById(R.id.page_weather_icon);
        location_icon = findViewById(R.id.page_location_icon);
        kind_icon = findViewById(R.id.page_kind_icon);
        updatetime_icon = findViewById(R.id.page_time_icon);
        mood_icon = findViewById(R.id.page_mood_icon);

        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        inshort_icon.setTypeface(tf);
        backButton.setTypeface(tf);
        wordnumber_icon.setTypeface(tf);
        weather_icon.setTypeface(tf);
        location_icon.setTypeface(tf);
        kind_icon.setTypeface(tf);
        inshort_icon.setTypeface(tf);
        updatetime_icon.setTypeface(tf);
        mood_icon.setTypeface(tf);
        delete.setTypeface(tf);
        copy.setTypeface(tf);
        share.setTypeface(tf);


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

        copy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData mClipData = ClipData.newPlainText("Label", output.getText());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(getApplicationContext(), "复制成功!", Toast.LENGTH_LONG).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlterDialog();
            }

        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        View test = findViewById(R.id.RelativeLayout_page);
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

        //if use only one TextView could in a line, looks more cool, just getText()+""+data

        note = db.getNote(id);
        date.setText(formatDate(note.getTimestamp()));
        output.setText(note.getNote());
        weather.setText(note.getWeather());

        //oncode
        wordnumber.setText(note.getWordnumber()+"字");
        //time
        kind.setText(note.getKind());
        inshort.setText(note.getInshort());
        updatetime.setText(note.getUpdatetime());
        mood.setText(note.getMood()+"°");
        location.setText(note.getLocation());

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

    private String formatDate(String dateStr){
        try{
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd");
            return fmtOut.format(date);
        }catch (ParseException e){

        }
        return "";
    }

    private void showAlterDialog(){
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(ViewPage.this);
        alterDiaglog.setIcon(R.drawable.logo_white);//图标
        alterDiaglog.setTitle("提示");//文字
        alterDiaglog.setMessage("确定删除吗");//提示消息
        //积极的选择
        alterDiaglog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //消极的选择

        alterDiaglog.setNeutralButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteNote(note);
                Intent intent = new Intent(ViewPage.this, MainActivity.class);
                Toast.makeText(ViewPage.this,"已删除",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        //显示
        alterDiaglog.show();
    }
}
