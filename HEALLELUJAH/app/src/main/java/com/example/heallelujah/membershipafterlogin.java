package com.example.heallelujah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class membershipafterlogin extends AppCompatActivity {
    private String checkid ,id, name, age, sex, phonenumber,
            object1, object2, object3, pay, address1,address2, height, weight;
    private long backBtnTime = 0;
    private ImageButton memberonly,memberinfo,membermaching, messagelist;
    private boolean modify = true, signcheck = true, check = true;
    private Member member;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membershipafterlogin);

        // member 객체 받음
        intent = getIntent();
        member = (Member)intent.getSerializableExtra("member");
        //member 변수 셋팅
        checkid = member.Getcheckid();
        id = member.Getid();
        name = member.Getname();
        age = member.Getage();
        sex = member.Getsex();
        phonenumber = member.Getphonenumber();
        object1 = member.Getobject1();
        object2 = member.Getobject2();
        object3 = member.Getobject3();
        pay = member.Getpay();
        address1 = member.Getaddress1();
        address2 = member.Getaddress2();
        height = member.Getheight();
        weight = member.Getweight();

        //쪽지 버튼
        messagelist = (ImageButton) findViewById(R.id.messageMember);
        messagelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membershipmessagelist = new Intent(getApplicationContext(), message_list.class);
                membershipmessagelist.putExtra("id",id);
                membershipmessagelist.putExtra("check",check);
                startActivity(membershipmessagelist);
            }
        });
        //매칭 버튼
        membermaching = (ImageButton) findViewById(R.id.mattchingMember);
        membermaching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membershipmachinglist = new Intent(getApplicationContext(), MatchingList.class);
                membershipmachinglist.putExtra("address1",address1);
                membershipmachinglist.putExtra("address2",address2);
                membershipmachinglist.putExtra("id",id);
                membershipmachinglist.putExtra("name", name);
                membershipmachinglist.putExtra("object1",object1);
                membershipmachinglist.putExtra("object2",object2);
                membershipmachinglist.putExtra("object3",object3);
                membershipmachinglist.putExtra("pay",pay);

                startActivity(membershipmachinglist);
            }
        });
        // 정보수정 버튼
        memberonly = (ImageButton) findViewById(R.id.onlyMember);
        memberonly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membershiponly = new Intent(getApplicationContext(), membershiponly.class);
                membershiponly.putExtra("signup",modify);
                membershiponly.putExtra("member",member);
                startActivity(membershiponly);
                finish();
            }
        });
        //기본정보 수정버튼
        memberinfo = (ImageButton) findViewById(R.id.infoMember);
        memberinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent membershipinfo = new Intent(getApplicationContext(), membershipinfo.class);
                membershipinfo.putExtra("signcheck", signcheck);
                membershipinfo.putExtra("member",member);
                startActivity(membershipinfo);
                finish();
            }
        });


    }
    //뒤로가기 2번 누르면 종료
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
