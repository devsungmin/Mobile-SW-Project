package dev.sungmin.Shelter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    private TextView titleView,contentView,dateView,nicknameView,goodcountView,deleteView,backView,badcountView;
    private EditText commentView;
    private Button button;
    private ImageButton good,bad;
    String CommentKey;
    String BoardKey;
    boolean goodimage_bool = false;
    boolean badimage_bool = false;

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
        backView=findViewById(R.id.back);
        badcountView=findViewById(R.id.badcount);
        good=findViewById(R.id.ib_good);
        bad=findViewById(R.id.ib_bad);
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference global = database.getReference("board/"+BoardKey);
                onBoardGoodClicked(global);
            }
        });
        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference global = database.getReference("board/"+BoardKey);
                onBoardBadClicked(global);
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(BoardDetailAcitivity.this, BoardActivity.class));
            }
        });
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
        boardReference = database.getReference("board/"+BoardKey);

        //commentReference = database.getReference("board/"+BoardKey+"/comment");
        commentReference = database.getReference("comment");
        commentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //반복문으로 데이터리스트 추출함
                    Comment comment = snapshot.getValue(Comment.class); //만들어뒀던 객체에 데이터를 담는다
                    arrayList.add(comment);
                    if(comment.good.containsKey(getUid())){
                        goodimage_bool=true;
                    }
                    else{
                        goodimage_bool=false;
                    }
                    if(comment.bad.containsKey(getUid())){
                        badimage_bool=true;
                    }
                    else{
                        badimage_bool=false;
                    }
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
                        //comdelReference = database.getReference("board/"+BoardKey+"/comment/");
                        comdelReference = database.getReference("comment/");
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
                badcountView.setText(String.valueOf(board.badCount));
                if(board.good.containsKey(getUid())){
                    good.setImageResource(R.drawable.ongood);
                }
                else {
                    good.setImageResource(R.drawable.offgood);
                }
                if(board.bad.containsKey(getUid())){
                    bad.setImageResource(R.drawable.onbad);
                }
                else{
                    bad.setImageResource(R.drawable.ofbad);
                }

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
        //String key = commentReference.child("board").child(BoardKey).child("comment").push().getKey();
        String key = commentReference.child("comment").push().getKey();
        comkey=key;
        String borkey=BoardKey;
        Comment comment=new Comment(nickname,comment_,dateform.format(date).toString(),goodCount,badCount,comkey,borkey);
        Map<String, Object> boardValues = comment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/board/"+BoardKey+"/comment/"+key,boardValues);
        childUpdates.put("/comment/"+key,boardValues);
        commentReference.updateChildren(childUpdates);
        Toast.makeText(this,"댓글을 작성했습니다.",Toast.LENGTH_SHORT).show();
        finish();
        Intent intent = new Intent(BoardDetailAcitivity.this,BoardDetailAcitivity.class);
        intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,BoardKey);
        intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname_);
        startActivity(intent);
    }
    // [START post_stars_transaction]
    private void onBoardGoodClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Board board = mutableData.getValue(Board.class);
                if (board == null) {
                    return Transaction.success(mutableData);
                }

                if (board.good.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    board.goodCount = board.goodCount - 1;
                    board.good.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    board.goodCount = board.goodCount + 1;
                    board.good.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(board);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]
    private void onBoardBadClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Board board = mutableData.getValue(Board.class);
                if (board == null) {
                    return Transaction.success(mutableData);
                }

                if (board.bad.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    board.badCount = board.badCount - 1;
                    board.bad.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    board.badCount = board.badCount + 1;
                    board.bad.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(board);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
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
        public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {
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
            if(badimage_bool==false){
                holder.badbutton.setImageResource(R.drawable.ofbad);
            }
            else {
                holder.badbutton.setImageResource(R.drawable.onbad);
            }
            if (goodimage_bool==false) {

                holder.goodbutton.setImageResource(R.drawable.offgood);

            }else {

                holder.goodbutton.setImageResource(R.drawable.ongood);
            }

            holder.goodbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ItemPosition = holder.getAdapterPosition();
                    CommentKey=arrayList.get(ItemPosition).comkey;
                    //DatabaseReference global = database.getReference("board/"+BoardKey+"/comment/"+CommentKey);
                    DatabaseReference global = database.getReference("comment/"+CommentKey);
                    onGoodClicked(global);
                    finish();
                    Intent intent = new Intent(BoardDetailAcitivity.this,BoardDetailAcitivity.class);
                    intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,BoardKey);
                    intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname_);
                    startActivity(intent);
                }
            });
            holder.badbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int ItemPosition = holder.getAdapterPosition();
                    CommentKey=arrayList.get(ItemPosition).comkey;
                    //DatabaseReference global = database.getReference("board/"+BoardKey+"/comment/"+CommentKey);
                    DatabaseReference global = database.getReference("comment/"+CommentKey);
                    onBadClicked(global);
                    finish();
                    Intent intent = new Intent(BoardDetailAcitivity.this,BoardDetailAcitivity.class);
                    intent.putExtra(BoardDetailAcitivity.EXTRA_BOARD_KEY,BoardKey);
                    intent.putExtra(BoardDetailAcitivity.NICKNAME,nickname_);
                    startActivity(intent);
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
            ImageView imageView;
            ImageView goodbutton;
            ImageView badbutton;

            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                this.comment=itemView.findViewById(R.id.comment);
                this.nickname=itemView.findViewById(R.id.nickname);
                this.date=itemView.findViewById(R.id.date);
                this.goodcount=itemView.findViewById(R.id.goodcount);
                this.badcount=itemView.findViewById(R.id.badcount);
                this.delete=itemView.findViewById(R.id.delete);
                this.goodbutton=itemView.findViewById(R.id.iv_good);
                this.badbutton=itemView.findViewById(R.id.iv_bad);
            }
        }
        // [START post_stars_transaction]
        private void onGoodClicked(DatabaseReference postRef) {
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Comment comment = mutableData.getValue(Comment.class);
                    if (comment == null) {
                        return Transaction.success(mutableData);
                    }

                    if (comment.good.containsKey(getUid())) {
                        // Unstar the post and remove self from stars
                        comment.goodcount = comment.goodcount - 1;
                        comment.good.remove(getUid());
                        goodimage_bool=false;
                    } else {
                        // Star the post and add self to stars
                        comment.goodcount = comment.goodcount + 1;
                        comment.good.put(getUid(), true);
                        goodimage_bool=true;
                    }

                    // Set value and report transaction success
                    mutableData.setValue(comment);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }
        // [END post_stars_transaction]
        private void onBadClicked(DatabaseReference postRef) {
            postRef.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    Comment comment = mutableData.getValue(Comment.class);
                    if (comment == null) {
                        return Transaction.success(mutableData);
                    }

                    if (comment.bad.containsKey(getUid())) {
                        // Unstar the post and remove self from stars
                        comment.badcount = comment.badcount - 1;
                        comment.bad.remove(getUid());
                    } else {
                        // Star the post and add self to stars
                        comment.badcount = comment.badcount + 1;
                        comment.bad.put(getUid(), true);
                    }

                    // Set value and report transaction success
                    mutableData.setValue(comment);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b,
                                       DataSnapshot dataSnapshot) {
                    // Transaction completed
                    Log.d(TAG, "postTransaction:onComplete:" + databaseError);
                }
            });
        }

    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
