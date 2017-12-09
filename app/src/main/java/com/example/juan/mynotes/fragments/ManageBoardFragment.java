package com.example.juan.mynotes.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.adapters.BoardsAdapter;
import com.example.juan.mynotes.models.Board;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageBoardFragment extends Fragment implements RealmChangeListener<RealmResults<Board>>{

    private Realm realm;
    private RecyclerView recycler;
    private BoardsAdapter adapter;
    private RealmResults<Board> boards;
    private RecyclerView.LayoutManager layoutManager;

    public ManageBoardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_board, container, false);

        // Set realm instance
        realm = Realm.getDefaultInstance();

        // Get all boards
        boards = realm.where(Board.class).findAll();
        boards.addChangeListener(this);

        // Instance recycler view
        recycler = view.findViewById(R.id.recycler_listBoards);
        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);

        // Instance board adapter
        adapter = new BoardsAdapter(boards, R.layout.recycler_view_board_item, new BoardsAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Board board, int position) {
                deleteBoard(board);
            }
        });

        recycler.setAdapter(adapter);

        return view;
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        adapter.notifyDataSetChanged();
    }

    private void deleteBoard(Board board) {
        realm.beginTransaction();
        board.deleteFromRealm();
        realm.commitTransaction();
    }
}
