package com.example.juan.mynotes.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.adapters.NotesAdapter;
import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Crud;
import com.example.juan.mynotes.models.Note;
import com.google.gson.Gson;

import java.util.List;

import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment{

    private int id;
    private Board board;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NotesAdapter adapter;
    private List<Note> notes;
    private Realm realm;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // Instance realm

        realm = Realm.getDefaultInstance();

        //Set floating button
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crud.createNote(realm, board, "Juan", "Probando Reloco");
                adapter.notifyDataSetChanged();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Get extras
        Bundle bundle = getArguments();
        if(bundle != null){
            id = bundle.getInt("id");
            board = new Gson().fromJson(bundle.getString("board"), Board.class);
            notes = board.getNotes();
            getActivity().setTitle(board.getTitle());
        }

        //Instance and recycler
        recyclerView = view.findViewById(R.id.recycler_list_notes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Instance and config adapter
        adapter = new NotesAdapter(notes, R.layout.recycler_view_note_item, new NotesAdapter.OnLongClickNoteListener() {
            @Override
            public void onLongClick(Note note) {
                View dialog_view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete_board, null);

                TextView title = (TextView) dialog_view.findViewById(R.id.dialog_delete_title_board);
                title.setText(getResources().getText(R.string.delete_board_title_board) + " " + board.getTitle());
                TextView notesCount = (TextView) dialog_view.findViewById(R.id.dialog_delete_notes_count);
                notesCount.setText(getResources().getText(R.string.delete_board_count_notes) + " " + board.getNotesSize());
                TextView date = (TextView) dialog_view.findViewById(R.id.dialog_delete_date);
                date.setText(getResources().getText(R.string.delete_board_date) + " " + board.getCreatedAt());

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.delete_board_title))
                        .setView(dialog_view)
                        .setPositiveButton(getResources().getText(R.string.delete), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Crud.deleteBoard(realm, board);
                                Snackbar.make(view, getResources().getText(R.string.deleted_info), Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.cancel), null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

}
