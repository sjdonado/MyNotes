package com.example.juan.mynotes.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Crud;
import com.example.juan.mynotes.models.Note;
import com.google.gson.Gson;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditorNoteFragment extends Fragment {

    private Realm realm;
    private Board board;
    private EditText title;
    private EditText content;
    private InputMethodManager imgr;
    private Note note;
    private int noteId;

    public EditorNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_editor_note, container, false);

        realm = Realm.getDefaultInstance();

        imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        title = (EditText) view.findViewById(R.id.new_note_title);
        content = (EditText) view.findViewById(R.id.new_note_content);

        //Get extras
        Bundle bundle = getArguments();
        if(bundle != null){
            noteId = bundle.getInt("noteId");
            board = new Gson().fromJson(bundle.getString("board"), Board.class);
            note = new Gson().fromJson(bundle.getString("note"), Note.class);
            if(note != null){
                if(!note.getTitle().equals(getResources().getString(R.string.unknown_title))) title.setText(note.getTitle());
                content.setText(note.getContent());
                getActivity().setTitle(getActivity().getResources().getString(R.string.edit_note_title));
            }else{
                getActivity().setTitle(getActivity().getResources().getString(R.string.new_note_title));

                imgr.showSoftInput(content, 0);
                imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

            }
        }

        content.requestFocus();

        view.findViewById(R.id.save_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote(view);
            }
        });

        view.findViewById(R.id.cancel_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
                getFragmentManager().popBackStack();
            }
        });;

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        imgr.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    private void saveNote(View view){
        String send_content = content.getText().toString();
        String send_title = title.getText().toString();
        if(send_title.isEmpty()) send_title = getResources().getString(R.string.unknown_title);
        if(!send_content.isEmpty()){
            String note_response;
            if(note == null){
                Crud.createNote(realm, board, send_title, send_content);
                note_response = getActivity().getResources().getString(R.string.note_created_info);
            }else{
                note.setTitle(send_title);
                note.setContent(send_content);
                Crud.editNote(realm, board, note, noteId);
                note_response = getActivity().getResources().getString(R.string.note_edited_info);
            }
            getFragmentManager().popBackStack();
            imgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Snackbar.make(view, note_response, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            Snackbar.make(view, getResources().getString(R.string.note_error_empty), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onStop() {
        saveNote(getView());
        super.onStop();
    }

}
