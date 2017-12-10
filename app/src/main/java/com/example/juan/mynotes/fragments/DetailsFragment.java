package com.example.juan.mynotes.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Crud;
import com.google.gson.Gson;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private Realm realm;
    private Board board;
    private EditText title;
    private EditText content;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_details, container, false);

        realm = Realm.getDefaultInstance();

        //Get extras
        Bundle bundle = getArguments();
        if(bundle != null){
            board = new Gson().fromJson(bundle.getString("board"), Board.class);
        }

        getActivity().setTitle(getActivity().getResources().getString(R.string.new_note_title));

        title = (EditText) view.findViewById(R.id.new_note_title);
        content = (EditText) view.findViewById(R.id.new_note_content);

        content.requestFocus();
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(content, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        view.findViewById(R.id.save_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() && !content.getText().toString().isEmpty()){
                    Crud.createNote(realm, board, title.getText().toString(), content.getText().toString());
                    Snackbar.make(view, getActivity().getResources().getString(R.string.note_created_info), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(view, "ERROR", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return view;
    }

}
