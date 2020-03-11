package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class traineronly extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String time, exp, pay, field1="1지망",field2="2지망",field3="3지망";
    private String TrainerId, TrainerPass, TrainerName, TrainerSex, TrainerAge, Address1,Address2,TrainerPhonenumber;
    private EditText timeinput, carrerinput, payinput;
    private Intent trainercurriculum_intent, intent;
    private TextView head;
    private Trainer trainer;
    private Spinner thealth_area1,thealth_area2,thealth_area3;
    private Boolean signcheck = false;
    private String url="URL 입력";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traineronly);

        //EditText 변수
        payinput = (EditText)findViewById(R.id.priceTrainer);
        timeinput = (EditText)findViewById(R.id.timeTrainer);
        carrerinput = (EditText)findViewById(R.id.careerTrainer);
        //textview 변수
        head =(TextView) findViewById(R.id.textView13);
        // 헬스 분야 스피너1
        final String[] healtharea1= getResources().getStringArray(R.array.헬스영역1);
        thealth_area1 = (Spinner)findViewById(R.id.realmTrainer);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea1);
        thealth_area1.setAdapter(adapter1);
        thealth_area1.setOnItemSelectedListener(this);
        // 헬스 분야 스피너2
        final String[] healtharea2 = getResources().getStringArray(R.array.헬스영역2);
        thealth_area2 = (Spinner)findViewById(R.id.realmTrainer2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea2);
        thealth_area2.setAdapter(adapter2);
        thealth_area2.setOnItemSelectedListener(this);
        // 헬스 분야 스피너3
        final String[] healtharea3 = getResources().getStringArray(R.array.헬스영역3);
        thealth_area3 = (Spinner)findViewById(R.id.realmTrainer3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea3);
        thealth_area3.setAdapter(adapter3);
        thealth_area3.setOnItemSelectedListener(this);

        //signcheck 불러옴
        intent = getIntent();
        signcheck = intent.getBooleanExtra("signcheck", false);


        //signcheck이 true면 정보수정 false면 회원가입
        if (signcheck == true) {
            head.setText("정보 수정");
            trainer = (Trainer)intent.getSerializableExtra("trainer");
            TrainerId = trainer.GetTrainerid();
            field1 = trainer.GetTrainerfield1();
            field2 = trainer.GetTrainerfield2();
            field3 = trainer.GetTrainerfield3();
            pay = trainer.Getpay();
            time = trainer.GetTrainertime();
            exp = trainer.GetTrainerexp();

            payinput.setText(pay);
            timeinput.setText(time);
            carrerinput.setText(exp);

            for(int i=0; i<healtharea1.length; i++) {
                if(healtharea1[i].equals(field1)) {
                    thealth_area1.setSelection(i);
                }
            }
            for(int i=0; i<healtharea2.length; i++) {
                if(healtharea2[i].equals(field2)) {
                    thealth_area2.setSelection(i);
                }
            }
            for(int i=0; i<healtharea3.length; i++) {
                if (healtharea3[i].equals(field3)) {
                    thealth_area3.setSelection(i);
                }
            }
        }
        else if(signcheck == false) {
            head.setText("회원 가입");
        }
        //뒤로가기
        Button prev = (Button)findViewById(R.id.prevTrainerInfo);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(signcheck == true) {
                    Intent Trainerafterlogin = new Intent(getApplicationContext(), trainerafterlogin.class);
                    Trainerafterlogin.putExtra("trainer",trainer);
                    startActivity(Trainerafterlogin);
                    finish();
                }
                else if(signcheck == false) {
                    onBackPressed();
                }
            }
        });

        //정보 입력확인후 커리쿨럼액티비티로 이동.
        Button trainercurriculum = (Button)findViewById(R.id.nextTrainercurriculum);//확인 후 커리큘럼 버튼
        trainercurriculum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                trainercurriculum_intent = new Intent(getApplicationContext(), trainercurriculum.class);//확인 후 커리큘럼 액티비티 인텐트 생성

                // 1지망 분야 입력 확인
                if(field1.equals("1지망")) {
                    Toast.makeText(traineronly.this,"1지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    trainercurriculum_intent.putExtra("field1",field1);
                }
                // 2지망 분야 입력 확인
                if(field2.equals("2지망")) {
                    Toast.makeText(traineronly.this,"2지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    trainercurriculum_intent.putExtra("field2",field2);
                }
                // 3지망 분야 입력 확인
                if(field3.equals("3지망")) {
                    Toast.makeText(traineronly.this,"3지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    trainercurriculum_intent.putExtra("field3",field3);
                }
                //가격 입력 확인
                if(payinput.getText().toString().length() ==0) {
                    Toast.makeText(traineronly.this,"가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    payinput.requestFocus();
                    return;
                } else {
                    pay = payinput.getText().toString();
                    trainercurriculum_intent.putExtra("pay",pay);
                }
                //근무시간 입력 확인
                if(timeinput.getText().toString().length() ==0) {
                    Toast.makeText(traineronly.this,"근무시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                    timeinput.requestFocus();
                    return;
                } else {
                    time = timeinput.getText().toString();
                    trainercurriculum_intent.putExtra("time",time);
                }
                //경력 입력 확인
                if(carrerinput.getText().toString().length() ==0) {
                    Toast.makeText(traineronly.this,"경력을 입력하세요.", Toast.LENGTH_SHORT).show();
                    carrerinput.requestFocus();
                    return;
                } else {
                    exp = carrerinput.getText().toString();
                    trainercurriculum_intent.putExtra("exp",exp);
                }
                if(signcheck == true) {
                       new httpcom().execute(TrainerId, field1, field2, field3,
                                pay, time, exp);
                }
                if(signcheck == false) {
                    Intent intent = getIntent();
                    TrainerId = intent.getStringExtra("id");
                    TrainerPass = intent.getStringExtra("pass");
                    TrainerName = intent.getStringExtra("name");
                    TrainerSex = intent.getStringExtra("sex");
                    TrainerAge = intent.getStringExtra("age");
                    Address1 = intent.getStringExtra("address1");
                    Address2 = intent.getStringExtra("address2");
                    TrainerPhonenumber = intent.getStringExtra("phonenumber");

                    trainercurriculum_intent.putExtra("id", TrainerId);
                    trainercurriculum_intent.putExtra("pass", TrainerPass);
                    trainercurriculum_intent.putExtra("name", TrainerName);
                    trainercurriculum_intent.putExtra("sex", TrainerSex);
                    trainercurriculum_intent.putExtra("age", TrainerAge);
                    trainercurriculum_intent.putExtra("address1", Address1);
                    trainercurriculum_intent.putExtra("address2", Address2);
                    trainercurriculum_intent.putExtra("phonenumber", TrainerPhonenumber);

                    startActivity(trainercurriculum_intent); //확인 후 커리큘럼 액티비티 전환
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner)parent;
        if(spinner.getId()==R.id.realmTrainer) {
            field1 =  thealth_area1.getSelectedItem().toString();
            if(!field1.equals("1지망")){
                Toast.makeText(traineronly.this,field1+"을 입력하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(spinner.getId()==R.id.realmTrainer2) {
            field2 = thealth_area2.getSelectedItem().toString();
            if(!field2.equals("2지망")){
                Toast.makeText(traineronly.this,field2+"을 입력하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(spinner.getId()==R.id.realmTrainer3) {
            field3 = thealth_area3.getSelectedItem().toString();
            if(!field3.equals("3지망")){
                Toast.makeText(traineronly.this,field3+"을 입력하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void onBackPressed() {
        if (signcheck == true) {
            Intent Trainerafterlogin = new Intent(getApplicationContext(), trainerafterlogin.class);
            Trainerafterlogin.putExtra("trainer", trainer);
            startActivity(Trainerafterlogin);
            finish();
        } else if (signcheck == false) {
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
                StringBuilder postData = new StringBuilder();
                URL obj = new URL(url);
                HttpURLConnection conn;

                conn = (HttpURLConnection) obj.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");//post 메소드로 동작
                conn.setDoInput(true); // 입력 셋팅
                conn.setDoOutput(true); // 출력 셋팅
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


                String id = data[0];
                String field1 = data[1];
                String field2 = data[2];
                String field3 = data[3];
                String pay = data[4];
                String time = data[5];
                String exp = data[6];

                // "id"=id&"field1"=field1&"field2"=field2&"field3"=field3&"pay"=pay&
                // "time"=time&"exp"=exp 생성
                postData.append(URLEncoder.encode("id", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(id, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("field1", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(field1, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("field2", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(field2, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("field3", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(field3, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("pay", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(pay, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("time", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(time, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("exp", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(exp, "UTF-8"));

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
            if(s.contains("트레이너 정보 수정 완료!")) {
                Toast.makeText(traineronly.this, s, Toast.LENGTH_SHORT).show();
                trainer.SetTrainerfield1(field1);
                trainer.SetTrainerfield2(field2);
                trainer.SetTrainerfield3(field3);
                trainer.SetTrainerpay(pay);
                trainer.SetTrainertime(time);
                trainer.SetTrainerexp(exp);
                Intent Trainerafterlogin = new Intent(getApplicationContext(), trainerafterlogin.class);
                Trainerafterlogin.putExtra("trainer",trainer);
                startActivity(Trainerafterlogin);
                finish();
            }
            else {
                Toast.makeText(traineronly.this, "트레이너 정보 수정 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
