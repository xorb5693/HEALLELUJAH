package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class MatchingInfo extends AppCompatActivity {
    //textview변수
    private TextView matchname,matchage,matchsex,matchaddress,matchfield1,matchfield2,matchfield3,matchprice,matchtime,matchcurriculum,matchcareer;
    //Trainer 변수
    private String trainername, age, sex, address, field1, field2, field3, price, time, curriculum, career, trainerid,memberid,membername;
    private Boolean matchingset = true;
    private Boolean messageset;
    private String url = "URL 입력";
    private String url2 = "URL 입력";
    private Button matchingbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_info);
        matchingbutton = (Button) findViewById(R.id.matchingInfo);
        //Intent 불러옴
        Intent intent = getIntent();
        trainerid = intent.getStringExtra("trainerid");
        memberid = intent.getStringExtra("memberid");
        membername = intent.getStringExtra("membername");
        messageset = intent.getBooleanExtra("infoset",false);

        if(matchingset==true){
            new httpcom().execute(trainerid);
        }
        //TextView변수 id
        matchname = (TextView) findViewById(R.id.nameInfo);
        matchage = (TextView) findViewById(R.id.ageInfo);
        matchsex = (TextView) findViewById(R.id.sexInfo);
        matchaddress = (TextView) findViewById(R.id.addressInfo);
        matchfield1 = (TextView) findViewById(R.id.realrmInfo);
        matchfield2 = (TextView) findViewById(R.id.realrmInfo2);
        matchfield3 = (TextView) findViewById(R.id.realrmInfo3);
        matchprice = (TextView) findViewById(R.id.priceInfo);
        matchtime = (TextView) findViewById(R.id.timeInfo);
        matchcurriculum = (TextView) findViewById(R.id.curriculumInfo);
        matchcareer = (TextView) findViewById(R.id.careerInfo);

        if(messageset == true) {
            matchingbutton.setVisibility(View.INVISIBLE);
        }
        else if(messageset == false) {
            matchingbutton.setVisibility(View.VISIBLE);
        }
        matchingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new httpcom().execute(memberid, trainerid, membername, trainername);
            }
        });

        //뒤로가기
        Button prev = (Button) findViewById(R.id.prevmatchinglist);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

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
                URL obj;
                if(matchingset == true) {
                    obj = new URL(url);
                }
                else {
                    obj = new URL(url2);
                }
                HttpURLConnection conn;

                conn = (HttpURLConnection) obj.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");//post 메소드로 동작
                conn.setDoInput(true); // 입력 셋팅
                conn.setDoOutput(true); // 출력 셋팅
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                if(matchingset == true) {
                    String id = data[0];
                    // "id"=id 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
                }
                else if(matchingset == false) {
                    String memberid = data[0];
                    String trainerid = data[1];
                    String membername = data[2];
                    String trainername = data[3];

                    // "healthuserid"=healthuserid&"trainerid"=trainerid& 생성
                    postData.append(URLEncoder.encode("healthuserid", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(memberid, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("trainerid", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(trainerid, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("healthname", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(membername, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("trainername", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(trainername, "UTF-8"));
                }

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

            if(matchingset == true) {
                Log.d("데이터는 : ", s);
                String array[] = s.split("\\|");
                trainername = array[0];
                age = array[1];
                sex = array[2];
                address = array[3];
                field1 = array[4];
                field2 = array[5];
                field3 = array[6];
                price = array[7];
                time = array[8];
                curriculum = array[9];
                career = array[10];

                //TextView 변수에 채워줌
                matchname.setText(trainername);
                matchage.setText(age);
                matchsex.setText(sex);
                matchaddress.setText(address);
                matchfield1.setText(field1);
                matchfield2.setText(field2);
                matchfield3.setText(field3);
                matchprice.setText(price);
                matchtime.setText(time);
                matchcurriculum.setText(curriculum);
                matchcareer.setText(career);
                matchingset = false;
            }
            else if(matchingset == false) {
                if(s.contains("매칭되었습니다.")) {
                    Log.d("뭐가 날라오니? : " ,s);
                    Toast.makeText(MatchingInfo.this, s, Toast.LENGTH_SHORT).show();
                    MatchingList matchingend = (MatchingList)MatchingList.matchinglist;
                    matchingend.finish();
                    finish();
                }
                else if(s.contains("이미 매칭된 사용자입니다.")) {
                    Log.d("뭐가 날라오니? : " ,s);
                    Toast.makeText(MatchingInfo.this, s, Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("뭐가 날라오니? : " ,s);
                }
            }
        }
    }
}
