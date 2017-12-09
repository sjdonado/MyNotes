package com.example.juan.mynotes.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Board;
import com.google.gson.Gson;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    private int id;
    private Board board;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Bundle bundle = getArguments();
        if(bundle != null){
            id = bundle.getInt("id");
            board = new Gson().fromJson(bundle.getString("board"), Board.class);
            getActivity().setTitle(board.getTitle());
        }

        return view;
    }

}
