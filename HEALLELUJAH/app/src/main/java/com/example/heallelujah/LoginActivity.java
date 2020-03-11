package com.example.heallelujah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private String url = "URL 입력";
    private String id,pass; // id, pass 저장변수
    private EditText idinput,passwordinput; // id, paswword 입력
    private Intent memberafterlogin_intent, trainerafterlogin_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idinput = (EditText) findViewById(R.id.idLogin);
        passwordinput = (EditText) findViewById(R.id.passLogin);

        //회원가입 버튼 활성화
        Button signup = (Button)findViewById(R.id.buttonSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent memberchoice_intent = new Intent(getApplicationContext(), membershipchoice.class);//회원가입 선택 액티비티 인텐트 생성
                startActivity(memberchoice_intent); //회원가입 선택 액티비티 전환
            }
        });

        //로그인 버튼 활성화
        // 버튼 클릭시 edittext에서 문자를 가져옴. 그후 공백
        // asynctask를 execute를 통해 실행
        Button login = (Button)findViewById(R.id.buttonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                id = idinput.getText().toString();
                pass = passwordinput.getText().toString();
                //Toast.makeText(LoginActivity.this,id+" "+pass, Toast.LENGTH_SHORT).show();
                new httpcom().execute(id, pass); // http통신 클래스 생성 후 실행
            }
        });
    }

    //로그인 액티비티에서 뒤로가기를 할시 종료.
    public void onBackPressed() {
        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0); // 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }

                                //매개변수 String , void, 결과값 String
    public class httpcom extends AsyncTask<String, Void, String> {
        @Override
        //백그라운드에서 http php 통신
        protected String doInBackground(String... data) {
            try {
                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");//post 메소드로 동작
                conn.setDoInput(true); // 입력 셋팅
                conn.setDoOutput(true); // 출력 셋팅
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String id = data[0]; // 위의 execute의 id 변수
                String pass = data[1];// 위의 excute의 pass 변수

                StringBuilder postData = new StringBuilder();

                // "id"=id&"pass"=pass 생성
                postData.append(URLEncoder.encode("id", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(id, "UTF-8"));
                postData.append('&');
                postData.append(URLEncoder.encode("pass","UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(pass, "UTF-8"));

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
        // doinBackground에서 값을 리턴 받은것을 파싱 후 객체에 저장
        // 객체저장된 값을 가지고 로그인 후 화면 결정.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String echotext = new String(s);
            String[] array = echotext.split("\\|");
            if(array[0].contains("1")) {
                Member loginmember = new Member(array[0],array[1],array[2],array[3], array[4],array[5],
                        array[6],array[7],array[8],array[9],array[10],array[11],array[12],array[13]);
                memberafterlogin_intent = new Intent(getApplicationContext(), membershipafterlogin.class);//로그인 액티비티 인텐트 생성
                memberafterlogin_intent.putExtra("member", loginmember);
                startActivity(memberafterlogin_intent); // 회원 로그인 액티비티 전환
                Toast.makeText(LoginActivity.this, "회원 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if(array[0].contains("0")) {
                Trainer logintrainer = new Trainer(array[0],array[1],array[2],array[3], array[4],array[5],
                        array[6],array[7],array[8],array[9], array[10],array[11],array[12],array[13],
                        array[14],array[15]);
                Log.d("log",s);
                trainerafterlogin_intent = new Intent(getApplicationContext(), trainerafterlogin.class);//로그인 액티비티 인텐트 생성
                trainerafterlogin_intent.putExtra("trainer", logintrainer);
                startActivity(trainerafterlogin_intent); // 트레이너 로그인 액티비티 전환
                Toast.makeText(LoginActivity.this, "트레이너 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Log.d("log",s);
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
