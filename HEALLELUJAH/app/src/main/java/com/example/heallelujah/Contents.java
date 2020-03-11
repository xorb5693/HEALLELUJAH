package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class Contents extends AppCompatActivity {
    private Intent intent;
    private Boolean check;
    private String myid, yourid, content;
    private String url = "URL 입력";
    private Button transcontent;
    private TextView to, from;
    private EditText contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        to = (TextView) findViewById(R.id.toinput);
        from = (TextView) findViewById(R.id.frominput);
        transcontent = (Button) findViewById(R.id.contentsTransmit);
        contents = (EditText) findViewById(R.id.contentsEdit);
        //인텐트 불러오기
        intent = getIntent();
        myid = intent.getStringExtra("myid");
        yourid = intent.getStringExtra("yourid");
        check = intent.getBooleanExtra("check",false);

        //뒤로가기
        Button prev = (Button) findViewById(R.id.prevmessage);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent chattingact = new Intent(getApplicationContext(), chatting.class);
                chattingact.putExtra("myid",myid);
                chattingact.putExtra("yourid",yourid);
                chattingact.putExtra("check",check);
                startActivity(chattingact);
                finish();
            }
        });

        //textview set
        to.setText(yourid);
        from.setText(myid);

        transcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contents.getText().length() == 0) {
                    Toast.makeText(Contents.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                    contents.requestFocus();
                    return;
                }
                else {
                    content = contents.getText().toString();
                    new httpcom().execute(content, myid, yourid);
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent chattingact = new Intent(getApplicationContext(), chatting.class);
        chattingact.putExtra("myid",myid);
        chattingact.putExtra("yourid",yourid);
        chattingact.putExtra("check",check);
        startActivity(chattingact);
        finish();
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

                String contents = data[0];
                Log.d("내용은 ",contents);
                String senderid = data[1];
                Log.d("샌더는  ",senderid.length()+"");
                String receiverid = data[2];
                Log.d("리시버는  ",receiverid.length()+"");
                // "contents"=contents&"senderid"=senderid&"receiverid"=receiverid 생성
                postData.append(URLEncoder.encode("contents", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(contents, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("senderid", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(senderid, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("receiverid", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(receiverid, "UTF-8"));

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
            if(s.contains("쪽지를 보냈습니다")) {
                Log.d("쪽지 : ",s);
                Toast.makeText(Contents.this, s, Toast.LENGTH_SHORT).show();
                Intent chattingActivity = new Intent(getApplicationContext(), chatting.class);
                chattingActivity.putExtra("myid",myid);
                chattingActivity.putExtra("yourid",yourid);
                chattingActivity.putExtra("check",check);
                startActivity(chattingActivity);
                finish();
            }

        }
    }
}
