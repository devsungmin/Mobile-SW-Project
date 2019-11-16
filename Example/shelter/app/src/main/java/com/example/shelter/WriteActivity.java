package com.example.shelter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class WriteActivity extends Activity implements View.OnClickListener{
    Button button1,button2;
    EditText editText1,editText2;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    String []email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


        button1 = (Button) findViewById(R.id.check);
        button2 = (Button) findViewById(R.id.cancel);
        editText1 = (EditText) findViewById(R.id.title);
        editText2 = (EditText) findViewById(R.id.content);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(WriteActivity.this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();
        email = user.getEmail().split("@");


    }
    private void writeBoard(){
        //사용자가 입력하는 title, content를 가져온다.
        String title = editText1.getText().toString().trim();
        String content = editText2.getText().toString().trim();
        String nickname = email[0];
        String b_key;
        int goodCount = 0;
        int badCount=0;
        //title과 content가 비었는지 아닌지를 체크 한다.
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(content)){
            Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        //title과 content가 제대로 입력되어 있다면 계속 진행된다.
        progressDialog.setMessage("글을 작성중 입니다. 기다려주세요...");
        progressDialog.show();

        SimpleDateFormat dateform = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String key = mDatabase.child("board").push().getKey();
        b_key=key;
        Board board=new Board(title,dateform.format(date).toString(),content,nickname,goodCount,b_key,badCount);
        Map<String, Object> boardValues = board.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/board/"+key,boardValues);
        //childUpdates.put("/user-board/"+nickname+"/"+key,boardValues);
        //닉네임별 게시글 저장
        mDatabase.updateChildren(childUpdates);
        progressDialog.dismiss();
        Toast.makeText(this,"글을 작성했습니다.",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(new Intent(WriteActivity.this, BoardActivity.class));





    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.check){
            writeBoard();
        }
        if(v.getId() == R.id.cancel){
            finish();
            startActivity(new Intent(WriteActivity.this, BoardActivity.class));
        }
    }


}
