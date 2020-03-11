package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class MemeberInfo extends AppCompatActivity {
    private Intent intent;
    private String memberid;
    private TextView nametext, agetext,sextext, object1text, object2text, object3text, paytext, heighttext, weighttext;
    private String url = "URL 입력";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber_info);

        nametext = (TextView) findViewById(R.id.nameMemberInfo);
        agetext = (TextView) findViewById(R.id.ageMemberInfo);
        sextext = (TextView) findViewById(R.id.sexMemberInfo);
        object1text = (TextView) findViewById(R.id.realmMemberInfo);
        object2text = (TextView) findViewById(R.id.realmMemberInfo2);
        object3text = (TextView) findViewById(R.id.realmMemberInfo3);
        paytext = (TextView) findViewById(R.id.priceMemberInfo);
        heighttext = (TextView) findViewById(R.id.heightMemberInfo);
        weighttext = (TextView) findViewById(R.id.weightMemberInfo);

        intent = getIntent();
        memberid = intent.getStringExtra("memberid");

        //뒤로가기
        Button prev = (Button) findViewById(R.id.prevMInfo);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        new httpcom().execute(memberid);
    }
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
                // "id"=id생성
                postData.append(URLEncoder.encode("id", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(id, "UTF-8"));

                byte[] postdatabytes = postData.toString().getBytes("UTF-8");

                conn.setRequestProperty("Content-Length", String.valueOf(postdatabytes.length));
                conn.getOutputStream().write(postdatabytes); //스트림에 write

                // echo값 받아서 저장후 연결 끊고 return해줌
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                conn.disconnect();
                return sb.toString().replaceFirst("\uFEFF\uFEFF", "");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            } catch (ProtocolException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        // doinBackground에서 값을 리턴 받음 그후 output에 echo텍스트 출력.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("회원은 ", s);
            String[] memberinfoarray = s.split("\\|");

            nametext.setText(memberinfoarray[1]);
            agetext.setText(memberinfoarray[2]);
            sextext.setText(memberinfoarray[3]);
            object1text.setText(memberinfoarray[4]);
            object2text.setText(memberinfoarray[5]);
            object3text.setText(memberinfoarray[6]);
            paytext.setText(memberinfoarray[7]);
            heighttext.setText(memberinfoarray[8]);
            weighttext.setText(memberinfoarray[9]);
        }
    }
}
