package dev.sungmin.Shelter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class infomationActivity extends Activity {
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer);
        button=(Button) findViewById(R.id.cheakButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /* 메뉴바 선택 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_btn0:
                //지도 보기
                finish();
                startActivity(new Intent(infomationActivity.this,MainActivity.class));
                return true;
            case R.id.action_btn1:
                //게시판
                finish();
                startActivity(new Intent(infomationActivity.this,BoardActivity.class));
                return true;
            case R.id.action_btn2:
                //개발자
                startActivity(new Intent(infomationActivity.this,infomationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
