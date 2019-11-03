package com.example.shelter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder> {

    private ArrayList<Board> arrayList;
    private Context context;

    public BoardAdapter(ArrayList<Board> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);
        BoardViewHolder holder = new BoardViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BoardViewHolder holder, int position) {
        /*Glide.with(holder.itemView)
                .load(arrayList.get(position).getGood())
                .into(holder.iv_good);*/
        holder.nickname.setText(arrayList.get(position).getNickname());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.content.setText(arrayList.get(position).getContent());
        holder.date.setText(arrayList.get(position).getDate());
        holder.goodcount.setText(String.valueOf(arrayList.get(position).getGoodCount()));

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder {
        //ImageView iv_good;
        TextView title;
        TextView content;
        TextView date;
        TextView goodcount;
        TextView nickname;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            //this.iv_good = itemView.findViewById(R.id.iv_good);
            this.content = itemView.findViewById(R.id.content);
            this.title = itemView.findViewById(R.id.title);
            this.nickname = itemView.findViewById(R.id.nickname);
            this.goodcount = itemView.findViewById(R.id.goodcount);
            this.date = itemView.findViewById(R.id.date);

        }
    }
}
