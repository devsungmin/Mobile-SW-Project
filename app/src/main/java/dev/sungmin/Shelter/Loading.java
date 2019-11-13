package dev.sungmin.Shelter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        ExampleThread thread = new ExampleThread();
        thread.start();

    }

    private class ExampleThread extends Thread {
        private static final String TAG = "ExampleThread";
        public ExampleThread() {
            // 초기화 작업
            }
            public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(Loading.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
             }

    }