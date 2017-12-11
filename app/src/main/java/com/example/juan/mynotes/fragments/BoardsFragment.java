package com.example.juan.mynotes.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.activities.MainActivity;
import com.example.juan.mynotes.adapters.BoardsAdapter;
import com.example.juan.mynotes.models.Board;
import com.example.juan.mynotes.models.Crud;


import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoardsFragment extends Fragment implements RealmChangeListener<RealmResults<Board>>{

    private Realm realm;
    private RecyclerView recycler;
    private BoardsAdapter adapter;
    private RealmResults<Board> boards;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab_add_board;

    public BoardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_board, container, false);

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
            public void onButtonClick(final Board board) {
                View dialog_view = inflater.inflate(R.layout.dialog_delete_board, null);

                TextView title = (TextView) dialog_view.findViewById(R.id.dialog_delete_title_board);
                title.setText(getResources().getText(R.string.delete_board_title_board) + " " + board.getTitle());
                TextView notesCount = (TextView) dialog_view.findViewById(R.id.dialog_delete_notes_count);
                notesCount.setText(getResources().getText(R.string.delete_board_count_notes) + " " + board.getNotesSize() + " " + getResources().getText(R.string.delete_board_count_notes_2));
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
        }, new BoardsAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(final Board board) {
                View edit_view = inflater.inflate(R.layout.dialog_edit_board, null);
                final EditText title_board = (EditText) edit_view.findViewById(R.id.edit_title_board);
                title_board.setText(board.getTitle());
                title_board.setSelection(title_board.getText().toString().length());
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.edit_board_title))
                        .setView(edit_view)
                        .setPositiveButton(getResources().getText(R.string.update), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (Crud.editBoard(realm, title_board.getText().toString(), board)) {
                                    Snackbar.make(view, getResources().getText(R.string.edited_info), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(view, getResources().getText(R.string.error_info), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.cancel), null)
                        .create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        }, new BoardsAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(Board board) {
                Bundle bundle = new Bundle();
                NotesFragment notesFragment = new NotesFragment();
                bundle.putInt("id", board.getId());
//                    bundle.putString("board", new Gson().toJson(realm.copyFromRealm(boards.get(id))));
                notesFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragments_container, notesFragment)
                        .commit();
            }
        });

        recycler.setAdapter(adapter);

        getActivity().setTitle("Edit Boards");

        fab_add_board = (FloatingActionButton) view.findViewById(R.id.fab_add_board);
        fab_add_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                View new_view = inflater.inflate(R.layout.dialog_edit_board, null);
                final EditText title_board = (EditText) new_view.findViewById(R.id.edit_title_board);

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(getResources().getString(R.string.new_board_title))
                        .setView(new_view)
                        .setPositiveButton(getResources().getText(R.string.create), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(!title_board.getText().toString().isEmpty() && Crud.createNewBoard(realm, title_board.getText().toString())){
                                    Snackbar.make(view, getResources().getText(R.string.edited_info), Snackbar.LENGTH_LONG)
                                            .setAction(null, null).show();
                                }else{
                                    Snackbar.make(view, getResources().getText(R.string.error_info), Snackbar.LENGTH_LONG)
                                            .setAction(null, null).show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getText(R.string.cancel), null)
                        .create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            }
        });

        setHideShowFAB(fab_add_board);

        return view;
    }

    @Override
    public void onChange(RealmResults<Board> boards) {
        adapter.notifyDataSetChanged();
    }

    private void setHideShowFAB(final FloatingActionButton fab) {
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

}
