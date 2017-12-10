package com.example.juan.mynotes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.juan.mynotes.R;
import com.example.juan.mynotes.models.Board;

import java.util.List;


/**
 * Created by juan on 8/12/17.
 */

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.ViewHolder>{

    private Context context;
    private List<Board> list;
    private int layout;
    private OnButtonClickListener deleteListener;
    private OnButtonClickListener editListener;

    public BoardsAdapter(List<Board> list, int layout, OnButtonClickListener deleteListener, OnButtonClickListener editListener) {
        this.list = list;
        this.layout = layout;
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), deleteListener, editListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageButton deleteBoard;
        public ImageButton editBoard;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_board);
            deleteBoard = (ImageButton) itemView.findViewById(R.id.delete_board);
            editBoard = (ImageButton) itemView.findViewById(R.id.edit_board);
        }

        public void bind(final Board board, final OnButtonClickListener deleteListener, final OnButtonClickListener editListener){
            title.setText(board.getTitle());
            deleteBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteListener.onButtonClick(board);
                }
            });
            editBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editListener.onButtonClick(board);
                }
            });
        }

    }

    public interface OnButtonClickListener{
        void onButtonClick(Board board);
    }
}
