package dev.sungmin.Shelter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BoardDetailAcitivity extends Activity implements View.OnClickListener{
    private static final String TAG = "BoardDetailActivity";
    public static final String EXTRA_BOARD_KEY = "board_key";
    public static final String NICKNAME="nickname";
    String []email;
    String nickname_,nick;
    String com_nick;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Comment> arrayList;
    private ValueEventListener mboardListener;
    private FirebaseDatabase database;
    private DatabaseReference boardReference,commentReference,deleteReference,comdelReference;
    private TextView titleView,contentView,dateView,nicknameView,goodcountView,deleteView;
    private EditText commentView;
    private Button button;
    String CommentKey;
    String BoardKey;

    /* 메뉴 바 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardview);
        button = (Button)findViewById(R.id.btn_comment);
        commentView = (EditText)findViewById(R.id.ed_comment);
        button.setOnClickListener(this);
        titleView = findViewById(R.id.title);
        contentView = findViewById(R.id.content);
        dateView = findViewById(R.id.date);
        nicknameView = findViewById(R.id.nickname);
        goodcountView = findViewById(R.id.goodcount);
        deleteView = findViewById(R.id.delete);
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(BoardDetailAcitivity.this, LoginActivity.class));
        }
        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail().split("@");
        nick=email[0];
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardDetailAcitivity.this, PopupActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        BoardKey = getIntent().getStringExtra(EXTRA_BOARD_KEY);
        nickname_ = getIntent().getStringExtra(NICKNAME);
        //com_nick = getIntent().getStringExtra(COMMENTNICKNAME);
        if (BoardKey == null){
            try {
                throw new IllegalAccessException("EXTRA_BOARD_KEY is null");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        database=FirebaseDatabase.getInstance();
        boardReference = database.getReference().child("board").child(BoardKey);

        CommentAdapter commentAdapter = new CommentAdapter(arrayList,this);
        recyclerView = findViewById(R.id.comment);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        commentReference = database.getReference("board/"+BoardKey+"/comment");
        commentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //반복문으로 데이터리스트 추출함
                    Comment comment = snapshot.getValue(Comment.class); //만들어뒀던 객체에 데이터를 담는다
                    arrayList.add(comment);
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던중 에러 발생시
                Log.e( "BoardDetailActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CommentAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                if(result.equals("Delete")){
                    //글 삭제
                    //nickname_  =  게시글 작성자 닉네임
                    //nick = 현재사용자 닉네임
                    if(nickname_.equals(nick)){
                        deleteReference = database.getReference("board");
                        deleteReference.child(BoardKey).removeValue();
                        finish();
                        startActivity(new Intent(BoardDetailAcitivity.this,BoardActivity.class));

                    }
                }
            }
        }
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                //데이터받기
                //com_nick = 댓글 작성자 닉네임
                //nick 현재 사용자 닉네임
                String result = data.getStringExtra("result");
                if(result.equals("Delete")){
                    //댓글삭제
                    if(com_nick.equals(nick)){
                        comdelReference = database.getReference("board/"+BoardKey+"/comment/");
                        comdelReference.child(CommentKey).removeValue();
                        finish();
                        Intent intent = new Intent(BoardDetailAcitivity.this,BoardDetailAcitivity.class);
                        intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname_);
                        intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,BoardKey);
                        startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ValueEventListener boardListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Board board = dataSnapshot.getValue(Board.class);

                titleView.setText(board.title);
                contentView.setText(board.content);
                nicknameView.setText(board.nickname);
                dateView.setText(board.date);
                goodcountView.setText(String.valueOf(board.goodCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadBoard:onCancelled", databaseError.toException());
                Toast.makeText(BoardDetailAcitivity.this,"Failed to load Board.",Toast.LENGTH_SHORT).show();

            }
        };
        boardReference.addValueEventListener(boardListener);
        mboardListener = boardListener;

    }
    private void writeComment(){
        //사용자가 입력하는 comment를 가져온다.
        String comment_ = commentView.getText().toString().trim();
        String nickname = email[0];
        int goodCount = 0;
        int badCount = 0;
        String comkey;
        //comment가 비었는지 아닌지를 체크 한다.

        if(TextUtils.isEmpty(comment_)){
            Toast.makeText(this, "댓글을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateform = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        commentReference = FirebaseDatabase.getInstance().getReference();
        String key = commentReference.child("board").child(BoardKey).child("comment").push().getKey();
        comkey=key;
        Comment comment=new Comment(nickname,comment_,dateform.format(date).toString(),goodCount,badCount,comkey);
        Map<String, Object> boardValues = comment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/board/"+BoardKey+"/comment/"+key,boardValues);
        commentReference.updateChildren(childUpdates);
        Toast.makeText(this,"댓글을 작성했습니다.",Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(BoardDetailAcitivity.this,BoardDetailAcitivity.class);
        intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,BoardKey);
        intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname_);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_comment){
            firebaseAuth = FirebaseAuth.getInstance();
            //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
            if(firebaseAuth.getCurrentUser() == null) {
                finish();
                startActivity(new Intent(BoardDetailAcitivity.this, LoginActivity.class));
            }

            //유저가 있다면, null이 아니면 계속 진행
            FirebaseUser user = firebaseAuth.getCurrentUser();
            email = user.getEmail().split("@");

            writeComment();
        }

    }
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
        public void onBindViewHolder(@NonNull final CommentViewHolder holder, int position) {
            holder.comment.setText(arrayList.get(position).getComment());
            holder.nickname.setText(arrayList.get(position).getNickname());
            holder.date.setText(arrayList.get(position).getDate());
            holder.goodcount.setText(String.valueOf(arrayList.get(position).getGoodcount()));
            holder.badcount.setText(String.valueOf(arrayList.get(position).getBadcount()));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemposition = holder.getAdapterPosition();
                    String nickname=arrayList.get(itemposition).nickname;
                    CommentKey=arrayList.get(itemposition).comkey;
                    com_nick=nickname;
                    Intent intent = new Intent(BoardDetailAcitivity.this, PopupActivity.class);
                    startActivityForResult(intent, 2);
                }
            });


        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            TextView delete;
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
                this.delete=itemView.findViewById(R.id.delete);
            }
        }

    }
}
