package com.example.sqlite2.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sqlite2.R;
import com.example.sqlite2.database.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import us.feras.mdv.MarkdownView;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private Context context;
    private List<Note> notesList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        //public TextView note_text;
        public MarkdownView item_markdown;
        public TextView dot;
        public TextView timestamp;//date
        public TextView wordnumber;
        public TextView weather;
        public TextView state;
        public TextView time;
        public TextView kind;

        // unCode
        public TextView inshort;
//        public TextView updatetime;
        public TextView mood;
        public TextView location;


        public MyViewHolder(View view){
            super(view);
            item_markdown = view.findViewById(R.id.item_markdown);
            //note_text = view.findViewById(R.id.dialog_input);
            //dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            wordnumber = view.findViewById(R.id.wordnumber);
            weather = view.findViewById(R.id.weather);
            state = view.findViewById(R.id.state);
            time = view.findViewById(R.id.time);
            kind = view.findViewById(R.id.kind);
            //inshort = view.findViewById(R.id.inshort);
            mood = view.findViewById(R.id.mood);
            location = view.findViewById(R.id.location);
        }
    }
    public NotesAdapter(Context context, List<Note> notesList){
        this.context = context;
        this.notesList = notesList;
    }
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row,parent,false);
        return new MyViewHolder(itemView);
    }
    public void onBindViewHolder(MyViewHolder holder, int position){
        Note note = notesList.get(position);
        if(note.getWordnumber() > 105)
            holder.item_markdown.loadMarkdown((note.getInshort()+"\n"+note.getNote()).substring(0, 105));
        else
            holder.item_markdown.loadMarkdown(note.getInshort()+"\n"+note.getNote());
        //holder.dot.setText(Html.fromHtml("&#8226;"));
        holder.timestamp.setText(formatDate(note.getTimestamp()));
        holder.weather.setText("☀"+note.getWeather()+"    ");
        holder.wordnumber.setText("✍ "+String.valueOf(note.getWordnumber())+"字  ");
        if(note.getState().equals("star"))
            holder.state.setVisibility(View.VISIBLE);
        else
            holder.state.setVisibility(View.INVISIBLE);
        holder.time.setText(formatTime(note.getTimestamp()));
        holder.kind.setText("\uD83D\uDCC3"+note.getKind()+"    ");

//        holder.updatetime.setText(note.getUpdatetime());
//        holder.inshort.setText(note.getInshort());
        String emoji = getMoodEmoji(note.getMood());
        holder.mood.setText(emoji+" "+note.getMood()+"°");
        holder.location.setText("\uD83D\uDC7D "+note.getLocation());
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

    public int getItemCount(){
        return notesList.size();
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

    private String formatTime(String dateStr) {
        try{
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
            return fmtOut.format(date);
        }catch (ParseException e){

        }
        return "";
    }
}
