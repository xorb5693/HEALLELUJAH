package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class membershipchoice extends AppCompatActivity {
    private boolean signcheck=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membershipchoice);

        Button member = (Button)findViewById(R.id.buttonMember);//회원버튼
        Button trainer = (Button)findViewById(R.id.buttonTrainer);//트레이너버튼

        member.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent membershipinfo = new Intent(getApplicationContext(), membershipinfo.class);//회원 액티비티 인텐트 생성
                membershipinfo.putExtra("signcheck", signcheck);
                startActivity(membershipinfo); //회원 액티비티 전환
            }
        });

        trainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent trainerinfo = new Intent(getApplicationContext(), trainerinfo.class);//트레이너 액티비티 인텐트 생성
                trainerinfo.putExtra("signcheck", signcheck);
                startActivity(trainerinfo); //트레이너 액티비티 전환
            }
        });
    }
}
