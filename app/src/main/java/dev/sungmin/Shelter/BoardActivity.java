package dev.sungmin.Shelter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BoardActivity extends Activity {
    private Button button1,button2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Board> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;




    /* 메뉴 바 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        BoardAdapter boardAdapter = new BoardAdapter(arrayList,this);
        setContentView(R.layout.activity_board);

        button1 =(Button) findViewById(R.id.home);
        button2 =(Button) findViewById(R.id.write);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BoardActivity.this,HomeActivity.class));
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BoardActivity.this,WriteActivity.class));
            }
        });

        recyclerView = findViewById(R.id.board);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("board");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //반복문으로 데이터리스트 추출함
                    Board board = snapshot.getValue(Board.class); //만들어뒀던 객체에 데이터를 담는다
                    arrayList.add(board);
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생시
                Log.e( "BoardActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new BoardAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);



    }




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
        public void onBindViewHolder(@NonNull final BoardViewHolder holder, final int position) {

        /*Glide.with(holder.itemView)
                .load(arrayList.get(position).getGood())
                .into(holder.iv_good);*/
            holder.nickname.setText(arrayList.get(position).getNickname());
            holder.title.setText(arrayList.get(position).getTitle());
            holder.content.setText(arrayList.get(position).getContent());
            holder.date.setText(arrayList.get(position).getDate());
            holder.goodcount.setText(String.valueOf(arrayList.get(position).getGoodCount()));
            holder.Mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemposition = holder.getAdapterPosition();
                    holder.title.getText();
                    String board_key=arrayList.get(itemposition).b_key;
                    String nickname=arrayList.get(itemposition).nickname;
                    Intent intent = new Intent(BoardActivity.this,BoardDetailAcitivity.class);
                    intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,board_key);
                    intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname);
                    startActivity(intent);



                    Toast.makeText(BoardActivity.this,""+board_key,Toast.LENGTH_SHORT).show();


                }
            });

        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class BoardViewHolder extends RecyclerView.ViewHolder {
            View Mview;
            //ImageView iv_good;
            TextView title;
            TextView content;
            TextView date;
            TextView goodcount;
            TextView nickname;

            public BoardViewHolder(@NonNull View itemView) {
                super(itemView);
                Mview=itemView;

                //this.iv_good = itemView.findViewById(R.id.iv_good);
                this.content = itemView.findViewById(R.id.content);
                this.title = itemView.findViewById(R.id.title);
                this.nickname = itemView.findViewById(R.id.nickname);
                this.goodcount = itemView.findViewById(R.id.goodcount);
                this.date = itemView.findViewById(R.id.date);


            }


        }

    }
}

