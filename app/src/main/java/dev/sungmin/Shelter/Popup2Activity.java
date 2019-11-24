package dev.sungmin.Shelter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Popup2Activity extends Activity {
    TextView txtText;
    int PageNum=1;
    RadioGroup radioGroup;
    //RadioButton radioButton1,radioButton2,radioButton3,radioButton4,radioButton5,radioButton6,radioButton7,radioButton18;
    //RadioButton radioButton9,radioButton10,radioButton11,radioButton12,radioButton13,radioButton14,radioButton15,radioButton16;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup2_activity);

        txtText = (TextView)findViewById(R.id.txtText);
        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
    }
    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if(i==R.id.radioButton1){
                PageNum=1;
            }
            else if(i==R.id.radioButton2){
                PageNum=2;
            }
            else if(i==R.id.radioButton3){
                PageNum=3;
            }
            else if(i==R.id.radioButton4){
                PageNum=4;
            }
            else if(i==R.id.radioButton5){
                PageNum=5;
            }
            else if(i==R.id.radioButton6){
                PageNum=6;
            }
            else if(i==R.id.radioButton7){
                PageNum=7;
            }
            else if(i==R.id.radioButton8){
                PageNum=8;
            }
            else if(i==R.id.radioButton9){
                PageNum=9;
            }
            else if(i==R.id.radioButton10){
                PageNum=10;
            }
            else if(i==R.id.radioButton11){
                PageNum=11;
            }
            else if(i==R.id.radioButton12){
                PageNum=12;
            }
            else if(i==R.id.radioButton13){
                PageNum=13;
            }
            else if(i==R.id.radioButton14){
                PageNum=14;
            }
            else if(i==R.id.radioButton15){
                PageNum=15;
            }
            else if(i==R.id.radioButton16){
                PageNum=16;
            }


        }
    };


    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", Integer.toString(PageNum));
        setResult(RESULT_OK, intent);
        finish();
    }
    public void mOncancle(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}