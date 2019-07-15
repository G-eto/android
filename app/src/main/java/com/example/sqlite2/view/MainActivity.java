package com.example.sqlite2.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlite2.R;
import com.example.sqlite2.database.DatabaseHelper;
import com.example.sqlite2.database.model.Note;
import com.example.sqlite2.utils.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import com.example.sqlite2.utils.OnItemTouchListener;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class MainActivity extends AppCompatActivity {
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private MaterialViewPager materialViewPager;
    private TextView noNotesView;

    private DatabaseHelper db;

    static final int TAPS = 3;
    private Toolbar mToolbar;
    private String search_kind = "ops";
    private String search_state = "off";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#455399"));
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        //下拉刷新
        RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                notesList.clear();
                notesList.addAll(db.getAllNotes());
                mAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                Toast.makeText(MainActivity.this,"已刷新",Toast.LENGTH_SHORT).show();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000/*,false*/);//传入false表示加载失败
            }
        });

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
//设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
//设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));



        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView = findViewById(R.id.recycler_view);
        //materialViewPager = findViewById(R.id.materialViewPager_everyday);

        noNotesView = findViewById(R.id.empty_notes_view);

        db = new DatabaseHelper(this);

        notesList.addAll(db.getAllNotes());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                startActivity(intent);
                //showNoteDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);



        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);//利用Toolbar代替ActionBar
        //设置导航Button点击事件
//        ImageView date_image = findViewById(R.id.iv_refresh);
////        date_image.setOnClickListener(new View.OnClickListener(){
////            @Override
////            public void onClick(View view) {
////
////            }
////        });

        ImageView back_image = findViewById(R.id.iv_back);
        back_image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"action_settings",Toast.LENGTH_SHORT).show();
            }
        });
        //设置移除图片  如果不设置会默认使用系统灰色的图标
//        mToolbar.setOverflowIcon(getResources().getDrawable(R.drawable.icon_action));
        mToolbar.inflateMenu(R.menu.toolbar_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_search1:
                        search_state = "on";
                        //Toast.makeText(MainActivity.this,"action_search_kind",Toast.LENGTH_SHORT).show();
                        search_kind = "kind";
                        break;
                    case R.id.action_search2:
                        search_state = "on";
                        //Toast.makeText(MainActivity.this,"action_search_inshort",Toast.LENGTH_SHORT).show();
                        search_kind = "inshort";
                        break;
                    case R.id.action_search3:
                        search_state = "on";
                        //Toast.makeText(MainActivity.this,"action_search_note",Toast.LENGTH_SHORT).show();
                        search_kind = "note";
                        break;
                    case R.id.action_search4:
                        //Toast.makeText(MainActivity.this,"action_search_note",Toast.LENGTH_SHORT).show();
                        search_kind = "state";
                        search_state = "on";
                        notesList.clear();
                        notesList.addAll(db.searchBykey(search_kind, null));
                        search_state = "off";
//                        mAdapter.notify();
//                        mAdapter.notifyAll();
                        mAdapter.notifyDataSetChanged();
                        toggleEmptyNotes();
                        Toast.makeText(MainActivity.this,String.valueOf(notesList.size()),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search5:
                        search_state = "on";
                        //Toast.makeText(MainActivity.this,"action_search_note",Toast.LENGTH_SHORT).show();
                        search_kind = "timestamp";
                        break;
                    default:search_kind = "ops";
                        search_state = "off";
                        break;
                }
                Log.d("sjdhfksf:",String.valueOf(notesList.size()));
                return false;
            }
        });



        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
//                recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//                Intent intent = new Intent(MainActivity.this, ViewPage.class);
//                intent.putExtra("note_id", notesList.get(position).getId());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                showActionsDialog(position);
//            }
//        }));


        recyclerView.addOnItemTouchListener(new OnItemTouchListener(recyclerView) {
            //@Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                //item 操作
                Intent intent = new Intent(MainActivity.this, ViewPage.class);
                intent.putExtra("note_id", notesList.get(vh.getAdapterPosition()).getId());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                showActionsDialog(vh.getAdapterPosition());
            }

        });
        toggleEmptyNotes();
    }

    /**
     * Inserting new note in db
     * and refreshing the list
     */
//    private void createNote(String note) {
//        // inserting note in db and getting
//        // newly inserted note id
//        long id = db.insertNote(note, "日记", "晴天", note.length());
//        Log.d("before:",String.valueOf(id));
//        // get the newly inserted note from db
//        Note n = db.getNote((int)id);
//
//        if (n != null) {
//            // adding new note to array list at 0 position
//            notesList.add(0, n);
//
//            // refreshing the list
//            mAdapter.notifyDataSetChanged();
//
//            toggleEmptyNotes();
//        }
//    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     * 需要添加
     */
    private void updateNote(String note, int position) {
        Note n = notesList.get(position);
        // updating note text
        n.setNote(note);

        // updating note in db String kind, String weather, int wordnumber
        db.updateNote(n);

        // refreshing the list
        notesList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    private void deleteNote(int position) {
        // deleting the note from db
        db.deleteNote(notesList.get(position));

        // removing the note from the list
        notesList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options
     * Edit - 0
     * Delete - 0
     */
    private void showActionsDialog(final int position) {
        Note n = notesList.get(position);
        CharSequence colors[];
        if(n.getState().equals("star"))
            colors= new CharSequence[]{"修改", "删除", "取消收藏"};
        else
            colors= new CharSequence[]{"修改", "删除", "收藏"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("note_id", notesList.get(position).getId());
                    startActivity(intent);
                    //showNoteDialog(true, notesList.get(position), position);
                } else if(which == 1){
                    deleteNote(position);
                    Toast.makeText(MainActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                }
                else{

                    // updating note text
                    Note n = notesList.get(position);
                    if(n.getState().equals("save"))
                        n.setState("star");
                    else if(n.getState().equals("star"))
                        n.setState("save");
                    // updating note in db String kind, String weather, int wordnumber
                    db.updateNote(n);

                    // refreshing the list
                    notesList.set(position, n);
                    mAdapter.notifyItemChanged(position);

                    toggleEmptyNotes();
                }
            }
        });
        builder.show();
    }

    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNoteDialog(final boolean shouldUpdate, final Note note, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.dialog_input);
        TextView dialogTitle = view.findViewById(R.id.edit_date);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (shouldUpdate && note != null) {
            inputNote.setText(note.getNote());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(MainActivity.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    //createNote(inputNote.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty notes view
     * 切换
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getNotesCount() > 0) {
            noNotesView.setVisibility(View.GONE);
        } else {
            noNotesView.setVisibility(View.VISIBLE);
        }
    }

    //@Override
    public boolean onCreateOptionsMenu1(Menu menu) {
        //Inflate the menu;this adds items to the action bar if it is present.
                //引用menu文件
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        //找到SearchView并配置相关参数

        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) menuItem.getActionView();

        //final SearchView mSearchView = (SearchView) searchItem.getActionView();
        //搜索图标是否显示在搜索框内
        mSearchView.setIconifiedByDefault(false);
        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView.setSubmitButtonEnabled(true);
        //让键盘的回车键设置成搜索
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //搜索框是否展开，false表示展开
        mSearchView.setIconified(true);
        //获取焦点
        mSearchView.setFocusable(true);
        //mSearchView.requestFocusFromTouch();
        //设置提示词
        mSearchView.setQueryHint("请输入关键字");
        //设置输入框文字颜色
        EditText editText = (EditText) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.drawerArrowColor));
        editText.setTextColor(ContextCompat.getColor(this, R.color.drawerArrowColor));

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                //伪搜索
                Log.d("search:", query);
                //清除焦点，收软键盘
                mSearchView.clearFocus();

                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                //do something
                //当没有输入任何内容的时候清除结果，看实际需求
                //if (TextUtils.isEmpty(newText)) mSearchResult.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        return true;
    }

    public void onCustomView(View view) {
        startActivity(new Intent(this,CustomToolBarAct.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(search_kind.equals("ops"))
                    search_kind = "note";
                //伪搜索
                Log.d("search:", query);
                mSearchView.clearFocus();
                notesList.clear();
                notesList.addAll(db.searchBykey(search_kind, query));
                search_state = "off";
//                        mAdapter.notify();
//                        mAdapter.notifyAll();
                mAdapter.notifyDataSetChanged();
                //清除焦点，收软键盘
                toggleEmptyNotes();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if(search_kind.equals("ops"))
                    search_kind = "note";
                notesList.clear();
                notesList.addAll(db.searchBykey(search_kind, newText));
                search_state = "off";
//                        mAdapter.notify();
//                        mAdapter.notifyAll();
                mAdapter.notifyDataSetChanged();
                //清除焦点，收软键盘
                toggleEmptyNotes();
                //do something
                //当没有输入任何内容的时候清除结果，看实际需求
                //if (TextUtils.isEmpty(newText)) mSearchResult.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        return true;
    }
}