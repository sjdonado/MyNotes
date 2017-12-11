package com.example.juan.mynotes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Note;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;

/**
 * Created by juan on 9/12/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private List<Note> notes;
    private int layout;
    private OnLongClickNoteListener longClickListener;
    private OnClickListener clickListener;

    public NotesAdapter(List<Note> notes, int layout, OnLongClickNoteListener longClickListener, OnClickListener clickListener) {
        this.notes = notes;
        this.layout = layout;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(notes.get(position), longClickListener, clickListener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleNote;
        public TextView contentNote;
        public TextView dateNote;

        public ViewHolder(View itemView) {
            super(itemView);
            titleNote = (TextView) itemView.findViewById(R.id.title_note);
            contentNote = (TextView) itemView.findViewById(R.id.content_note);
        }

        public void bind(final Note note, final OnLongClickNoteListener longClickListener, final OnClickListener clickListener){
            titleNote.setText(note.getTitle());
            contentNote.setText(note.getContent());
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onLongClick(note);
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(note);
                }
            });
        }
    }

    public interface OnLongClickNoteListener{
        void onLongClick(Note note);
    }

    public interface OnClickListener{
        void onClick(Note note);
    }

    private Note getNoteNotSorted(Note note, List<Note> notesNotSorted){
        Note noteResponse = null;
        int i = 0;
        while(i < notesNotSorted.size() && noteResponse == null){
            if(notesNotSorted.get(i).getId() == note.getId()) noteResponse = notesNotSorted.get(i);
            i++;
        }
        return noteResponse;
    }

    private List<Note> sortNotes(List<Note> notes){
        Realm realm = Realm.getDefaultInstance();
        List<Note> sort = realm.copyFromRealm(notes);
        //Sort notes
        Collections.sort(sort, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                if(o1.getCreatedAt().after(o2.getCreatedAt())){
                    return -1;
                }else if(o1.getCreatedAt().before(o2.getCreatedAt())){
                    return 1;
                }else if(o1.getCreatedAt().equals(o2.getCreatedAt())){
                    return 0;
                }else{
                    return o1.getCreatedAt().compareTo(o2.getCreatedAt());
                }
            }
        });
        Log.i("SIZE PERRO HPTA", sort.size() + "");
        return sort;
    }
}
