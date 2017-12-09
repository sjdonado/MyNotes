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
    private OnButtonClickListener listener;

    public BoardsAdapter(List<Board> list, int layout, OnButtonClickListener listener) {
        this.list = list;
        this.layout = layout;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageButton deleteBoard;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_board);
            deleteBoard = (ImageButton) itemView.findViewById(R.id.delete_board);
        }

        public void bind(final Board board, final OnButtonClickListener listener){
            title.setText(board.getTitle());
            deleteBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onButtonClick(board, getLayoutPosition());
                }
            });
        }

    }

    public interface OnButtonClickListener{
        void onButtonClick(Board board, int position);
    }
}
