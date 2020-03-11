package com.example.heallelujah;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class trainerafterlogin extends AppCompatActivity {
    private String checkid, TrainerId, time, exp, pay, field1,field2,field3,curriculum,prizecarrer,
    TrainerName, TrainerSex, TrainerAge, Address1,Address2,TrainerPhonenumber;
    private long backBtnTime = 0;
    private ImageButton traineronly, trainerinfo, trainercurriculum,trainermessage;
    private boolean signcheck = true,check=false;
    private Trainer trainer;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainerafterlogin);

        //Trainer 객체 받음
        intent = getIntent();
        trainer = (Trainer)intent.getSerializableExtra("trainer");
        //trainer 변수 셋팅
        checkid = trainer.GetcheckTrainerid();
        TrainerId = trainer.GetTrainerid();
        TrainerName = trainer.GetTrainername();
        TrainerAge = trainer.GetTrainerage();
        TrainerSex = trainer.GetTrainersex();
        TrainerPhonenumber = trainer.GetTrainerphonenumber();
        field1 = trainer.GetTrainerfield1();
        field2 = trainer.GetTrainerfield2();
        field3 = trainer.GetTrainerfield3();
        pay = trainer.Getpay();
        Address1 = trainer.GetTraineraddress1();
        Address2 = trainer.GetTraineraddress2();
        curriculum = trainer.GetTrainercurriculum();
        prizecarrer = trainer.GetTrainercarrer();
        time = trainer.GetTrainertime();
        exp = trainer.GetTrainerexp();

        //쪽지 버튼
        trainermessage = (ImageButton) findViewById(R.id.messageTrainer);
        trainermessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messagelist = new Intent(getApplicationContext(), message_list.class);
                messagelist.putExtra("id",TrainerId);
                messagelist.putExtra("check",check);
                startActivity( messagelist);
            }
        });
        //기본정보 수정버튼
        trainerinfo = (ImageButton) findViewById(R.id.infoTrainer);
        trainerinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainerinfo = new Intent(getApplicationContext(), trainerinfo.class);

                trainerinfo.putExtra("signcheck", signcheck);
                trainerinfo.putExtra("trainer", trainer);
                startActivity(trainerinfo);
                finish();
            }
        });

        //트레이너 정보 수정버튼
        traineronly = (ImageButton) findViewById(R.id.onlyTrainer);
        traineronly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent traineronly = new Intent(getApplicationContext(), traineronly.class);
                traineronly.putExtra("signcheck", signcheck);
                traineronly.putExtra("trainer", trainer);
                startActivity(traineronly);
                finish();
            }
        });
        //커리큘럼 정보 수정버튼
        trainercurriculum = (ImageButton) findViewById(R.id.curriculumTrainer);
        trainercurriculum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainercurriculum = new Intent(getApplicationContext(), trainercurriculum.class);
                trainercurriculum.putExtra("signcheck", signcheck);
                trainercurriculum.putExtra("trainer", trainer);
                startActivity(trainercurriculum);
                finish();
            }
        });
    }
    //뒤로가기 2번 누르면 앱 종료
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
            finish();
            System.exit(0);
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }
}