package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class membershiponly extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String height, weight, pay, object1="1지망",object2="2지망",object3="3지망";
    private String MemberId, MemberPass, signup,
            MemberName, MemberSex, MemberAge, Address1, Address2,MemberPhonenumber;
    private EditText heightinput, weightinput, priceinput;
    private Member mem;
    private TextView head;
    private String url = "URL 입력", url2="URL 입력";
    private Intent membershiplogin;
    private Spinner mhealth_area1,mhealth_area2,mhealth_area3;
    private boolean membercheck;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membershiponly);

        //EditText 변수
        heightinput = (EditText)findViewById(R.id.heightMember);
        weightinput = (EditText)findViewById(R.id.weightMember);
        priceinput = (EditText)findViewById(R.id.priceMember);
        // 헬스 분야 스피너 1지망
        final String[] healtharea = getResources().getStringArray(R.array.헬스영역1);
        mhealth_area1 = (Spinner)findViewById(R.id.realmMember);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea);
        mhealth_area1.setAdapter(adapter);
        mhealth_area1.setOnItemSelectedListener(this);
        // 헬스 분야 스피너 2지망
        final String[] healtharea2 = getResources().getStringArray(R.array.헬스영역2);
        mhealth_area2 = (Spinner)findViewById(R.id.realmMember2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea2);
        mhealth_area2.setAdapter(adapter2);
        mhealth_area2.setOnItemSelectedListener(this);
        // 헬스 분야 스피너 3지망
        final String[] healtharea3 = getResources().getStringArray(R.array.헬스영역3);
        mhealth_area3 = (Spinner)findViewById(R.id.realmMember3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea3);
        mhealth_area3.setAdapter(adapter3);
        mhealth_area3.setOnItemSelectedListener(this);

        //멤버체크 불러옴
        intent = getIntent();
        membercheck = intent.getBooleanExtra("signup",false);

        //membercheck이 false면 회원가입 true면 회원정보수정
        if(membercheck == true) {
            mem = (Member)intent.getSerializableExtra("member");
            head = (TextView)findViewById(R.id.textView14);
            head.setText("정보 수정");
            MemberId = mem.Getid();
            height = mem.Getheight();
            weight = mem.Getweight();
            pay = mem.Getpay();
            object1 = mem.Getobject1();
            object2 = mem.Getobject2();
            object3 = mem.Getobject3();

            for(int i=0; i<healtharea.length; i++) {
                if(healtharea[i].equals(object1)) {
                    mhealth_area1.setSelection(i);
                }
            }
            for(int i=0; i<healtharea2.length; i++) {
                if(healtharea2[i].equals(object2)) {
                    mhealth_area2.setSelection(i);
                }
            }
            for(int i=0; i<healtharea3.length; i++) {
                if(healtharea3[i].equals(object3)) {
                    mhealth_area3.setSelection(i);
                }
            }
            heightinput.setText(height);
            weightinput.setText(weight);
            priceinput.setText(pay);
        }
        else if(membercheck == false){
            head = (TextView)findViewById(R.id.textView14);
            head.setText("회원 가입");
        }

        //뒤로가기
        Button prev = (Button) findViewById(R.id.prevmemberInfo);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                if(membercheck == true)
                    membershiplogin = new Intent(getApplicationContext(), membershipafterlogin.class);
                    membershiplogin.putExtra("member",mem);
                    startActivity(membershiplogin);
                    finish();
            }
        });


        //키 입력확인후 로그인액티비티로 이동.
        Button memberlogin = (Button)findViewById(R.id.nextMemberlogin);//확인 후 로그인버튼
        memberlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                membershiplogin = new Intent(getApplicationContext(), LoginActivity.class);//확인 후 로그인 액티비티 인텐트 생성

                //키 입력 확인
                if(heightinput.getText().toString().length() ==0) {
                    Toast.makeText(membershiponly.this,"키를 입력하세요.", Toast.LENGTH_SHORT).show();
                    heightinput.requestFocus();
                    return;
                } else {
                    height = heightinput.getText().toString();
                }
                //몸무게 입력 확인
                if(weightinput.getText().toString().length() ==0) {
                    Toast.makeText(membershiponly.this,"몸무게를 입력하세요.", Toast.LENGTH_SHORT).show();
                    weightinput.requestFocus();
                    return;
                } else {
                    weight = weightinput.getText().toString();
                }
                // 1지망 분야 입력 확인
                if(object1.equals("1지망")) {
                    Toast.makeText(membershiponly.this,"1지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2지망 분야 입력 확인
                if(object2.equals("2지망")) {
                    Toast.makeText(membershiponly.this,"2지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 3지망 분야 입력 확인
                if(object3.equals("3지망")) {
                    Toast.makeText(membershiponly.this,"3지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //횟수당 가격 입력 확인
                if(priceinput.getText().toString().length() ==0) {
                    Toast.makeText(membershiponly.this,"횟수 당 가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    priceinput.requestFocus();
                    return;
                } else {
                    pay = priceinput.getText().toString();
                    if(membercheck == true) {
                        signup = "1";
                        new httpcom().execute(signup, MemberId, pay, height, weight,
                                object1,object2,object3);

                    }
                    else if(membercheck == false){
                        signup = "0";
                        MemberId = intent.getStringExtra("id");
                        MemberPass = intent.getStringExtra("pass");
                        MemberName = intent.getStringExtra("name");
                        MemberSex = intent.getStringExtra("sex");
                        MemberAge = intent.getStringExtra("age");
                        Address1 = intent.getStringExtra("address1");
                        Address2 = intent.getStringExtra("address2");
                        MemberPhonenumber = intent.getStringExtra("phonenumber");
                        /*Toast.makeText(membershiponly.this,
                                signup+" "+MemberId+" "+MemberPass+" " +MemberName +" "+MemberAge +" "+MemberSex +" "+MemberPhonenumber+" "+ Address1 +" "+Address2+" "+ pay + " " +height + " " + weight+ " " +
                                        object1 + " " +object2+ " "+ object3, Toast.LENGTH_LONG).show();*/
                       new httpcom().execute(signup, MemberId,MemberPass,MemberName,MemberAge,MemberSex,
                                MemberPhonenumber,pay,Address1,Address2,height,weight,
                                object1,object2,object3);// http통신 클래스 생성 후 실행
                    }
                }
            }
        });
    }
    // 스피너 item 셀렉트시 확인
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner)parent;
        if(spinner.getId()==R.id.realmMember) {
            object1 =  mhealth_area1.getSelectedItem().toString();
            if(!object1.equals("1지망")){
                Toast.makeText(membershiponly.this,object1+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(spinner.getId()==R.id.realmMember2) {
            object2 = mhealth_area2.getSelectedItem().toString();
            if(!object2.equals("2지망")){
                Toast.makeText(membershiponly.this,object2+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(spinner.getId()==R.id.realmMember3) {
            object3 = mhealth_area3.getSelectedItem().toString();
            if(!object3.equals("3지망")){
                Toast.makeText(membershiponly.this,object3+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onBackPressed() {
        if (membercheck == true) {
            membershiplogin = new Intent(getApplicationContext(), membershipafterlogin.class);
            membershiplogin.putExtra("member", mem);
            startActivity(membershiplogin);
            finish();
        } else if (membercheck == false) {
            super.onBackPressed();
        }
    }
    //AsyncTask를 통한 인터넷 쓰레드 동작.
        //매개변수 String, void, 결과값 String
        public class httpcom extends AsyncTask<String, Void, String> {
            @Override
            //백그라운드에서 http php 통신
            protected String doInBackground(String... data) {
                try {
                    //membercheck로 url 선택
                    String membercheck = data[0];
                    StringBuilder postData = new StringBuilder();
                    URL obj = null;
                    HttpURLConnection conn;
                    if(membercheck.contains("1")) {
                        obj = new URL(url2);
                    }
                    else if(membercheck.contains("0")) {
                        obj = new URL(url);
                    }

                    conn = (HttpURLConnection) obj.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("POST");//post 메소드로 동작
                    conn.setDoInput(true); // 입력 셋팅
                    conn.setDoOutput(true); // 출력 셋팅
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");



                    if(membercheck.contains("1")) {
                        //signup, MemberId, pay, height, weight, object1,object2,object3
                        String id = data[1];
                        String pay = data[2];
                        String height = data[3];
                        String weight = data[4];
                        String object1 = data[5];
                        String object2 = data[6];
                        String object3 = data[7];

                        // "id"=id&"pay"=pay&"address"=address&"height"=height&"weight"=weight&
                        // "object1"=object1&"object2"=object2&"object3"=object3 생성
                        postData.append(URLEncoder.encode("id", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(id, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("pay", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(pay, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("height", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(height, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("weight", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(weight, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object1", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object1, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object2", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object2, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object3", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object3, "UTF-8"));
                    }
                    else if(membercheck.contains("0")) {
                        //signup, MemberId,MemberPass,MemberName,MemberAge,MemberSex,
                        //MemberPhonenumber,pay,Address1,Address2,height,weight,
                        //object1,object2,object3
                        String id = data[1]; // 위의 execute의 id 변수
                        String password = data[2];
                        String name = data[3];
                        String age = data[4];
                        String checkid = "1";
                        String sex = data[5];
                        String phonenumber = data[6];
                        String pay = data[7];
                        String address1 = data[8];
                        String address2 = data[9];
                        String height = data[10];
                        String weight = data[11];
                        String object1 = data[12];
                        String object2 = data[13];
                        String object3 = data[14];
                        // "id"=id&"password"=password&"name"=name&"age"=age"&
                        // "checkid"=checkid&"sex"=sex&"phonenumber"=phonenumber&
                        // "pay"=pay&"address1"=address1&"address2"=address2&"height"=height&"weight"=weight&
                        // "object1"=object1&"object2"=object2&"object3"=object3 생성
                        postData.append(URLEncoder.encode("id", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(id, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("password", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(password, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("name", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(name, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("age", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(age, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("checkid", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(checkid, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("sex", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(sex, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("phonenumber", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(phonenumber, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("pay", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(pay, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("address1", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(address1, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("address2", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(address2, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("height", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(height, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("weight", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(weight, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object1", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object1, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object2", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object2, "UTF-8"));
                        postData.append("&");
                        postData.append(URLEncoder.encode("object3", "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(object3, "UTF-8"));
                    }

                    byte[] postdatabytes = postData.toString().getBytes("UTF-8");

                    conn.setRequestProperty("Content-Length", String.valueOf(postdatabytes.length));
                    conn.getOutputStream().write(postdatabytes); //스트림에 write

                    // echo값 받아서 저장후 연결 끊고 return해줌
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null) {
                        sb.append(line);
                    }
                    conn.disconnect();
                    return sb.toString();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
            // doinBackground에서 값을 리턴 받음 그후 output에 echo텍스트 출력.
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.length() == 9) {
                    Log.d("insert", s);
                    Toast.makeText(membershiponly.this,s, Toast.LENGTH_SHORT).show();
                    membershiplogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(membershiplogin); //확인 후 로그인 액티비티 전환
                    finish();
                }
                else if(s.contains("회원 정보 수정 완료!")) {
                    Toast.makeText(membershiponly.this, s, Toast.LENGTH_SHORT).show();
                    mem.Setheight(height);
                    mem.Setweight(weight);
                    mem.Setobject1(object1);
                    mem.Setobject2(object2);
                    mem.Setobject3(object3);
                    mem.Setpay(pay);
                    membershiplogin = new Intent(getApplicationContext(), membershipafterlogin.class);
                    membershiplogin.putExtra("member",mem);
                    startActivity(membershiplogin);
                    finish();
                }
                else {
                    Log.d("insert", s);
                    Toast.makeText(membershiponly.this, s, Toast.LENGTH_SHORT).show();
                }
            }
    }
}
