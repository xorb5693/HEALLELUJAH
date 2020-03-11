package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Handler starthandler = new Handler(); // 2초후에 액티비티 전환 핸들러
            starthandler.postDelayed(new Runnable() {
                // @Override
                public void run() {
                    Intent login_intent = new Intent(getApplicationContext(), LoginActivity.class);// 로그인액티비티 인텐트 생성
                    startActivity(login_intent); //로그인 액티비티 전환
                    finish(); // Main액티비티 종료
                }
            }, 2000); // 2s
        }
}

