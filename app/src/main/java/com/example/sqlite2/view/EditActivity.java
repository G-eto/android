package com.example.sqlite2.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.sqlite2.utils.HTTPRetrieval;
import com.example.sqlite2.utils.JSONParser;
import com.example.sqlite2.utils.WeatherInfo;
/**
 * Created by zhipeng on 16/1/13.
 */
public class EditActivity extends Activity {

    private PictureAndTextEditorView mEditText;
    private EditText inputNote;
    private Button saveButton;
    private Button backButton;
    private Button mark_icon;
    private TextView marks;
    private TextView date;
    private SeekBar mood;
    public TextView weather;
    private TextView wordnumber;
    public TextView location;
    private TextView inshort;

    private Button date_icon;
    private Button weather_icon;
    private TextView wordnumber_icon;
    private Button location_icon;
    private Button inshort_icon;
    private TextView mood_icon;
    private TextView mood_number_edit;
    private TextView mood_value_icon;

    private static Context context;

    Boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;

    private AlertDialog.Builder builder;
    //private ProgressDialog progressDialog;

    //location


    final String tag = "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "hello edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = new DatabaseHelper(this);


        Log.d(tag, "hello edit2");
        inputNote = findViewById(R.id.edit_text);
        saveButton = findViewById(R.id.edit_save);
        backButton = findViewById(R.id.edit_back);

        marks = findViewById(R.id.edit_mark);
        date = findViewById(R.id.edit_date);
        mood = findViewById(R.id.edit_mood);
        weather = findViewById(R.id.edit_weather);
        location = findViewById(R.id.edit_location);
        inshort = findViewById(R.id.edit_inshort);
        wordnumber = findViewById(R.id.edit_wordnumber);
        //date_icon = findViewById(R.id.edit_date_icon);
        mark_icon = findViewById(R.id.edit_mark_icon);
        weather_icon = findViewById(R.id.edit_weather_icon);
        wordnumber_icon = findViewById(R.id.edit_wordnumber_icon);
        location_icon = findViewById(R.id.edit_location_icon);
        inshort_icon = findViewById(R.id.edit_inshort_icon);
        mood_icon = findViewById(R.id.edit_mood_icon);
        mood_number_edit = findViewById(R.id.edit_mood_value);
        mood_value_icon = findViewById(R.id.edit_mood_value_icon);

        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        saveButton.setTypeface(tf);
        backButton.setTypeface(tf);
        marks.setTypeface(tf);
        weather_icon.setTypeface(tf);
        wordnumber_icon.setTypeface(tf);
        location_icon.setTypeface(tf);
        inshort_icon.setTypeface(tf);
        mark_icon.setTypeface(tf);
        mood_icon.setTypeface(tf);
        mood_value_icon.setTypeface(tf);

        context = getApplicationContext();
        weather_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                HttpThread ht = new HttpThread();
                ht.start();
            }
        });

        location_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDialog(location, location_icon);
            }
        });

        mark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(marks, mark_icon);
            }
        });

        inshort_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(inshort, inshort_icon);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(tag, "save");

                if (note_id > -1) {
                    Log.d(tag, "updatestart");
                    updateNote();
                    Log.d(tag, "updone");
                    Toast.makeText(EditActivity.this,"已更新",Toast.LENGTH_SHORT).show();
                    intent2ViewPage();
                } else if (note_id == -1 && inputNote.getText().toString().length() > 0) {
                    Log.d(tag, "createstart");
                    createNote("save");
                    Log.d(tag, "createdone");
                    Toast.makeText(EditActivity.this,"已保存",Toast.LENGTH_SHORT).show();
                    intent2Main();
                }
                else{
                    Toast.makeText(EditActivity.this,"Tips:您还没有写什么呢！",Toast.LENGTH_SHORT).show();
                }
                Log.d(tag, "saveok");

            }
        });

        mood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mood_number_edit.setText(String.valueOf(mood.getProgress()-100)+"°");
                mood_icon.setText(getMoodEmoji(mood.getProgress()-100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(tag, "backback");
                showThree();
                //finish();
            }
        });


        date = findViewById(R.id.edit_date);
        //weather = findViewById(R.id.weather);
        inputNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override

            public void afterTextChanged(Editable editable) {

                //长度发生变化，监听到输入的长度为 editText.getText().length()
                wordnumber.setText(String.valueOf(inputNote.getText().length()) + "字");
            }
        });

        Intent intent = getIntent();
        int data = intent.getIntExtra("note_id", -1);
        note_id = data;
        Log.d(tag, "id:" + data);
        if (data == -1) {
            //note_id = db.getNotesCount();
            shouldUpdate = true;
            date.setText("今天");//日期
            //inputNote.setText("bugbugbug");
        } else {
            note = db.getNote(note_id);
            date.setText(formatDate(note.getTimestamp()));
            inputNote.setText(note.getNote());
            weather.setText(note.getWeather());
            inshort.setText(note.getInshort());
            location.setText(note.getLocation());
            marks.setText(note.getKind());
            mood.setProgress(note.getMood()+100);
        }
    }

    private void createNote(String state) {
        // inserting note in db and getting
        // newly inserted note id

        note.setState(state);
        note.setWeather(weather.getText().toString());
        note.setKind(marks.getText().toString());
        note.setInshort(inshort.getText().toString());
        note.setLocation(location.getText().toString());
        note.setTemperature(27);
        note.setMood(mood.getProgress()-100);

        //note.setWeather(weather.getText().toString());
        note.setNote(inputNote.getText().toString());
        note.setWordnumber(note.getNote().length());
        //note.setState(state.getText().toString());
        Log.d(tag, note.getNote() + ",f,vx");
        long id = db.insertNote(note.getNote(), "日记", note.getWeather(),
                note.getWordnumber(), note.getLocation(), note.getInshort(), note.getState(), note.getMood(), note.getTemperature());
        Log.d(tag, "backID:" + id);

        note_id = (int) id;
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
        //note.setWeather(weather.getText().toString());
        note.setWeather(weather.getText().toString());
        note.setKind(marks.getText().toString());
        note.setMood(mood.getProgress()-100);
        note.setTemperature(27);
        note.setInshort(inshort.getText().toString());
        note.setLocation(location.getText().toString());
        //note.setUpdatetime();
        note.setWordnumber(inputNote.getText().toString().length());
        // updating note in db String kind, String weather, int wordnumber
        db.updateNote(note);

        //back to mainActivity

    }

    private void intent2ViewPage() {
        Log.d(tag, "inteng2PageView" + note_id);
        Intent intent = new Intent(EditActivity.this, ViewPage.class);
        intent.putExtra("note_id", note_id);
        startActivity(intent);
    }

    private void intent2Main() {
        Log.d(tag, "inteng2Main" + note_id);
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        //intent.putExtra("note_id", note_id);
        startActivity(intent);
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }

    public class HttpThread extends Thread {
        private Handler hd = new Handler();

        @Override
        public void run() {
            super.run();
            HTTPRetrieval hr = new HTTPRetrieval();
            // 城市代码
            String weatherString = hr.HTTPWeatherGET("101010100");

            WeatherInfo wi = new WeatherInfo();
            JSONParser jp = new JSONParser();
            // 调用自定义的 JSON 解析类解析获取的 JSON 数据
            wi = jp.WeatherParse(weatherString);

            final WeatherInfo finalWi = wi;
            // 多线程更新 UI
            hd.post(new Runnable() {
                @Override
                public void run() {
//                    location.setText(finalWi.getCity());
                    Log.d("ccccccccccccccccccccity:",finalWi.getCity());
                    //note.setTemperature(finalWi.getHighTemp()); //单位
                    weather.setText(finalWi.getDescription() + " " + finalWi.getHighTemp());
                    //txt.setText(finalWi.getCity() + finalWi.getHighTemp() + finalWi.getLowTemp() + finalWi.getDescription() + finalWi.getPublishTime());
                }
            });
        }
    }

    private void showDialog(final TextView textView, final Button button){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(EditActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.dialog_input);
        final TextView dialogTitle = view.findViewById(R.id.dialog_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        dialogTitle.setText(button.getText());
        dialogTitle.setTypeface(tf);
        boolean should = false;
        inputNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override

            public void afterTextChanged(Editable editable) {

                //长度发生变化，监听到输入的长度为 editText.getText().length()
                dialogTitle.setText(String.valueOf(button.getText()+""+inputNote.getText().length()) + "字");
            }
        });
        if (textView.getText() != null) {
            inputNote.setText(textView.getText());
            should = true;
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(should ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(EditActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (inputNote.getText() != null) {
                    textView.setText(inputNote.getText());
                }
            }
        });
    }

    private void showThree() {
        builder = new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher).setTitle("提示")
                .setMessage("是否保存本次编辑？").setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        //Toast.makeText(EditActivity.this, "确定按钮", Toast.LENGTH_LONG).show();
                        saveButton.callOnClick();
                    }
                }).setNeutralButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        //finish();
                    }
                });
        builder.create().show();

    }

    public String getMoodEmoji(int m){
        int e = (int)Math.round(1.0*m/20) + 5;
        switch (e){
            case 6:return "\uD83D\uDE05";
            case 7:return "\uD83D\uDE02";
            case 8:return "\uD83D\uDE1D";
            case 9:return "\uD83D\uDE0D";
            case 10:return "\uD83D\uDE08";
            case 0:return "\uD83D\uDE2D";
            case 1:return "\uD83D\uDE24";
            case 2:return "\uD83D\uDE2B";
            case 3:return "\uD83D\uDE23";
            case 4:return "\uD83D\uDE37";
            case 5:return "\uD83D\uDE36";
            default : return "\uD83D\uDC7F";
        }
    }

}