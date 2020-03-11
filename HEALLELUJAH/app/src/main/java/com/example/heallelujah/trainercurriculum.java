package com.example.heallelujah;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class trainercurriculum extends AppCompatActivity {
    private String TrainerId, TrainerPass, time, exp, pay, field1,field2,field3,curriculum,prizecarrer,
            TrainerName, TrainerSex, TrainerAge, Address1, Address2,TrainerPhonenumber;
    private String url = "URL 입력",url2="URL 입력";
    private EditText curriculuminput, prizecarrerinput;
    private Intent trainerlogin_intent, intent;
    private Trainer trainer;
    private Boolean signcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainercurriculum);

        //EditText 변수
        curriculuminput = (EditText)findViewById(R.id.curriculumEdit);
        prizecarrerinput = (EditText)findViewById(R.id.careerEdit);

        //signcheck 불러옴
        intent = getIntent();
        signcheck = intent.getBooleanExtra("signcheck", false);


        //signcheck이 true면 정보수정
        if(signcheck == true) {
            trainer = (Trainer)intent.getSerializableExtra("trainer");
            TrainerId = trainer.GetTrainerid();
            curriculum = trainer.GetTrainercurriculum();
            prizecarrer = trainer.GetTrainercarrer();

            curriculuminput.setText(curriculum);
            prizecarrerinput.setText(prizecarrer);
        }
        //뒤로가기
        Button prev = (Button)findViewById(R.id.prevtraineronly);
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

        Button trainerlogin = (Button)findViewById(R.id.nextTrainerLogin);//확인 후 로그인 버튼
        trainerlogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                trainerlogin_intent = new Intent(getApplicationContext(), LoginActivity.class);//확인 후 로그인 액티비티 인텐트 생성

                if(signcheck == true) {
                    //커리큘럼 입력 확인
                    if (curriculuminput.getText().toString().length() == 0) {
                        Toast.makeText(trainercurriculum.this, "커리큘럼을 입력하세요.", Toast.LENGTH_SHORT).show();
                        curriculuminput.requestFocus();
                        return;
                    } else {
                        curriculum = curriculuminput.getText().toString();
                    }

                    //수상경력 입력 확인
                    if (prizecarrerinput.getText().toString().length() == 0) {
                        Toast.makeText(trainercurriculum.this, "수상경력을 입력하세요.", Toast.LENGTH_SHORT).show();
                        prizecarrerinput.requestFocus();
                        return;
                    }
                    else {
                        prizecarrer = prizecarrerinput.getText().toString();
                    }
                    String state = "1";
                    new httpcom().execute(state, TrainerId, curriculum, prizecarrer); // http통신 클래스 생성 후 실행

                }
                else if(signcheck == false) {
                    //커리큘럼 입력 확인
                    if (curriculuminput.getText().toString().length() == 0) {
                        Toast.makeText(trainercurriculum.this, "커리큘럼을 입력하세요.", Toast.LENGTH_SHORT).show();
                        curriculuminput.requestFocus();
                        return;
                    } else {
                        curriculum = curriculuminput.getText().toString();
                    }
                    //수상경력 입력 확인
                    if (prizecarrerinput.getText().toString().length() == 0) {
                        Toast.makeText(trainercurriculum.this, "수상경력을 입력하세요.", Toast.LENGTH_SHORT).show();
                        prizecarrerinput.requestFocus();
                        return;
                    } else {
                        prizecarrer = prizecarrerinput.getText().toString();
                        Intent intent = getIntent();
                        String state = "0";
                        TrainerId = intent.getStringExtra("id");
                        TrainerPass = intent.getStringExtra("pass");
                        TrainerName = intent.getStringExtra("name");
                        TrainerSex = intent.getStringExtra("sex");
                        TrainerAge = intent.getStringExtra("age");
                        Address1 = intent.getStringExtra("address1");
                        Address2 = intent.getStringExtra("address2");
                        TrainerPhonenumber = intent.getStringExtra("phonenumber");
                        pay = intent.getStringExtra("pay");
                        time = intent.getStringExtra("time");
                        exp = intent.getStringExtra("exp");
                        field1 = intent.getStringExtra("field1");
                        field2 = intent.getStringExtra("field2");
                        field3 = intent.getStringExtra("field3");
                        /*Toast.makeText(trainercurriculum.this, state + " "+TrainerId + " " + TrainerPass + " " + TrainerName + " " + TrainerSex + " " + TrainerAge + " " + Address1 + " " +Address2 + " "+ TrainerPhonenumber + " " + pay + " " + time + " " + exp + " " + field1
                                + " " + field2 + " " + field3 + " " + curriculum + " " + prizecarrer, Toast.LENGTH_LONG).show();*/
                        new httpcom().execute(state,TrainerId, TrainerPass,TrainerName,TrainerAge,TrainerSex, TrainerPhonenumber,
                                pay,Address1,Address2,prizecarrer,curriculum,time,exp,field1, field2, field3); // http통신 클래스 생성 후 실행
                    }
                }
            }
        });
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
                String state = data[0];
                StringBuilder postData = new StringBuilder();
                URL obj = null;
                if(state.contains("1")) {
                    obj = new URL(url2);
                }
                else if(state.contains("0")) {
                    obj = new URL(url);
                }
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");//post 메소드로 동작
                conn.setDoInput(true); // 입력 셋팅
                conn.setDoOutput(true); // 출력 셋팅
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


                if(state.equals("1")) {
                    String id = data[1];
                    String curriculum = data[2];
                    String career = data[3];
                    //"id"=id&"carrer"=carrer&"curriculum"=curriculum 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("career", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(career, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("curriculum", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(curriculum, "UTF-8"));
                }
                else if(state.equals("0")) {
                    //state, TrainerId, TrainerPass,TrainerName,TrainerAge,TrainerSex, TrainerPhonenumber,
                    //pay,Address1,Address2,prizecarrer,curriculum,time,exp,field1, field2, field3
                    String id = data[1]; // 위의 execute의 id 변수
                    String password = data[2];
                    String name = data[3];
                    String age = data[4];
                    String checkid = "0";
                    String sex = data[5];
                    String phonenumber = data[6];
                    String pay = data[7];
                    String address1 = data[8];
                    String address2 = data[9];
                    String career = data[10];
                    String curriculum = data[11];
                    String time = data[12];
                    String exp = data[13];
                    String field1 = data[14];
                    String field2 = data[15];
                    String field3 = data[16];
                    // "id"=id&"password"=password&"name"=name&"age"=age"&
                    // "checkid"=checkid&"field"=field&"pay"=pay&"address1"=address1&"address2"=address2&
                    // "carrer"=carrer&"curriculum"=curriculum생성
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
                    postData.append(URLEncoder.encode("career", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(career, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("curriculum", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(curriculum, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("time", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(time, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("exp", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(exp, "UTF-8"));
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
                Log.d("s는 " ,s);
                Toast.makeText(trainercurriculum.this,s, Toast.LENGTH_SHORT).show();
                trainerlogin_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(trainerlogin_intent); //확인 후 로그인 액티비티 전환
                finish();
            }
            else if(s.contains("커리큘럼 수정 완료!")) {
                Toast.makeText(trainercurriculum.this,s, Toast.LENGTH_SHORT).show();
                trainer.SetTrainercarrer(prizecarrer);
                trainer.SetTrainercurriculum(curriculum);
                Intent Trainerafterlogin = new Intent(getApplicationContext(), trainerafterlogin.class);
                Trainerafterlogin.putExtra("trainer",trainer);
                startActivity(Trainerafterlogin);
                finish();
            }
            else {
                Log.d("s는 " ,s);
                Toast.makeText(trainercurriculum.this,"회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
