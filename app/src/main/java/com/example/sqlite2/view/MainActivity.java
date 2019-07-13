package com.example.sqlite2.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private NotesAdapter mAdapter;
    private List<Note> notesList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private MaterialViewPager materialViewPager;
    private TextView noNotesView;

    private DatabaseHelper db;

    static final int TAPS = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

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
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);







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
}