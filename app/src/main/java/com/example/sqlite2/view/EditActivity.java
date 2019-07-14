package com.example.sqlite2.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.sqlite2.utils.HTTPRetrieval;
import com.example.sqlite2.utils.JSONParser;
import com.example.sqlite2.utils.WeatherInfo;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import us.feras.mdv.MarkdownView;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by zhipeng on 16/1/13.
 */
public class EditActivity extends Activity {

    private PictureAndTextEditorView mEditText;
    private EditText inputNote;
    private Button saveButton;
    private Button backButton;
    private TextView marks;
    private TextView date;
    private SeekBar mood;
    public TextView weather;
    private TextView wordnumber;
    public TextView location;
    private TextView inshort;
    private ImageView image;

    private Button date_icon;
    private ImageView weather_icon;
    private ImageView wordnumber_icon;
    private ImageView location_icon;
    private ImageView inshort_icon;
    private ImageView mark_icon;

    private TextView mood_number_edit;
    private ImageView mood_value_icon;

// tools imageview
    private ImageView tool_location;
    private ImageView tool_weather;
    private ImageView tool_inshort;
    private ImageView tool_kind;
    private ImageView tool_mood;
    private ImageView tool_no_md;
    private ImageView tool_edit_and_md;
    private ImageView tool_preview_md;
    private ImageView tool_picture;


    ImageView image1;

    private static Context context;

    Boolean shouldUpdate = false;
    private Note note = new Note();
    private int note_id;
    DatabaseHelper db;

    private AlertDialog.Builder builder;
    //private ProgressDialog progressDialog;

    //location
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    private static final int REQUEST_LIST_CODE = 0;
    private static final int REQUEST_CAMERA_CODE = 1;

    public ISListConfig config;

    MarkdownView markdownView;

    final String tag = "hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(tag, "hello edit");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markdown_edit);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
//            }
//        }
        db = new DatabaseHelper(this);

        markdownView = (MarkdownView) findViewById(R.id.edit_markdownView);
        //markdownView.loadMarkdown("![test](/storage/emulated/0/Android/data/com.example.sqlite2/cache/1563096438650.jpg)");
        tool_location = findViewById(R.id.edit_tool_location_icon);
        tool_weather = findViewById(R.id.edit_tool_weather_icon);
        tool_inshort = findViewById(R.id.edit_tool_inshort_icon);
        tool_kind = findViewById(R.id.edit_tool_kind_icon);
        tool_mood = findViewById(R.id.edit_tool_mood_icon);
        tool_no_md = findViewById(R.id.edit_tool_no_md);
        tool_edit_and_md = findViewById(R.id.edit_tool_and_md);
        tool_preview_md = findViewById(R.id.edit_tool_md_preview);
        tool_picture = findViewById(R.id.edit_tool_image_icon);

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
//        mood_icon = findViewById(R.id.edit_mood_icon);
        mood_number_edit = findViewById(R.id.edit_mood_value);
        mood_value_icon = findViewById(R.id.edit_mood_value_icon);


        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });

        image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d("picpicpcipcipcp:","1601016017061");
                // 自由配置选项
                config = new ISListConfig.Builder()
                        // 是否多选, 默认true
                        .multiSelect(false)
                        // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                        .rememberSelected(false)
                        // “确定”按钮背景色
                        .btnBgColor(Color.GRAY)
                        // “确定”按钮文字颜色
                        .btnTextColor(Color.BLUE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5"))
                        // 返回图标ResId
                        .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
                        // 标题
                        .title("图片")
                        // 标题文字颜色
                        .titleColor(Color.WHITE)
                        // TitleBar背景色
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        // 裁剪大小。needCrop为true的时候配置
                        .cropSize(1, 1, 200, 200)
                        .needCrop(true)
                        // 第一个是否显示相机，默认true
                        .needCamera(false)
                        // 最大选择图片数量，默认9
                        .maxNum(9)
                        .build();
// 跳转到图片选择器
                ISNav.getInstance().toListActivity(EditActivity.this, config, REQUEST_LIST_CODE);
            }
        });

        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        saveButton.setTypeface(tf);
        backButton.setTypeface(tf);
        marks.setTypeface(tf);


        context = getApplicationContext();
        weather_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                HttpThread ht = new HttpThread();
//                ht.start();
                showDialog(weather, weather_icon);
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
//                mood_icon.setText(getMoodEmoji(mood.getProgress()-100));
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
                markdownView.loadMarkdown(inputNote.getText().toString());
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

    private void showDialog(final TextView textView, final ImageView button){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(EditActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.dialog_input);
        final TextView dialogTitle = view.findViewById(R.id.dialog_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        //dialogTitle.setText(button.getText());
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
                dialogTitle.setText(String.valueOf("已输入"+inputNote.getText().length()) + "字");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra("result");
            for (String path : pathList) {
                Log.d("picpath:",path);
                inputNote.append("![](file:///"+path+")" + "\n");
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK && data != null) {
//            String path = data.getStringExtra("result"); // 图片地址
//            inputNote.append(path + "\n");
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    private void showSeekbarDialog(){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.edit_seekbar_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(EditActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final SeekBar seekBar = view.findViewById(R.id.edit_mood);
        final EditText inputNote = view.findViewById(R.id.dialog_input);
        final TextView dialogTitle = view.findViewById(R.id.dialog_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        //dialogTitle.setText(button.getText());
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
                dialogTitle.setText(String.valueOf("已输入"+inputNote.getText().length()) + "字");
            }
        });
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
                    //textView.setText(inputNote.getText());
                }
            }
        });
    }
}