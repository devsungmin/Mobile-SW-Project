package com.example.shelter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private ArrayList<Comment> arrayList;
    private Context context;

    public CommentAdapter(ArrayList<Comment> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent,false);
        CommentViewHolder holder = new CommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.comment.setText(arrayList.get(position).getComment());
        holder.nickname.setText(arrayList.get(position).getNickname());
        holder.date.setText(arrayList.get(position).getDate());
        holder.goodcount.setText(String.valueOf(arrayList.get(position).getGoodcount()));
        holder.badcount.setText(String.valueOf(arrayList.get(position).getBadcount()));


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView comment;
        TextView nickname;
        TextView date;
        TextView goodcount;
        TextView badcount;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.comment=itemView.findViewById(R.id.comment);
            this.nickname=itemView.findViewById(R.id.nickname);
            this.date=itemView.findViewById(R.id.date);
            this.goodcount=itemView.findViewById(R.id.goodcount);
            this.badcount=itemView.findViewById(R.id.badcount);
        }
    }
}
