package com.example.juan.mynotes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Note;

import java.util.List;

/**
 * Created by juan on 9/12/17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private List<Note> notes;
    private int layout;
    private OnLongClickNoteListener longClickListener;

    public NotesAdapter(List<Note> notes, int layout, OnLongClickNoteListener longClickListener) {
        this.notes = notes;
        this.layout = layout;
        this.longClickListener = longClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(notes.get(position), longClickListener);
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

        public void bind(final Note note, final OnLongClickNoteListener longClickListener){
            titleNote.setText(note.getTitle());
            contentNote.setText(note.getContent());
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onLongClick(note);
                    return true;
                }
            });
        }
    }

    public interface OnLongClickNoteListener{
        void onLongClick(Note note);
    }
}
