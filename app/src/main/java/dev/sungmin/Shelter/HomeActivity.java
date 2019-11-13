package dev.sungmin.Shelter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button button,button2,button3,button4,button5;
    /* 메뉴 바 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);

        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
        button2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomeActivity.this, RegisterActivity.class));
            }
        });
        button3.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomeActivity.this, BoardActivity.class));
            }
        });
        button4.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomeActivity.this, WriteActivity.class));
            }
        });
        button5.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

    }

}
