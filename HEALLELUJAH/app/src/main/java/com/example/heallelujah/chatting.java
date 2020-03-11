package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class chatting extends AppCompatActivity {
    ListView m_ListView;
    ChatAdapter m_Adapter;
    ArrayList<ListContents> chatlist;
    Intent intent;
    String myid, yourid;
    Button info, contentTrans;
    Boolean check;
    String url = "URL 입력";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        contentTrans = (Button) findViewById(R.id.contenttrans);
        info = (Button) findViewById(R.id.minfo);
        chatlist = new ArrayList<ListContents>();
        intent = getIntent();
        myid = intent.getStringExtra("myid");
        Log.d("나의아이디는 ", myid.length()+"");
        yourid = intent.getStringExtra("yourid");
        yourid = yourid.replaceFirst("\uFEFF\uFEFF", "");
        Log.d("너의 아이디는 ", yourid.length()+"");
        check = intent.getBooleanExtra("check",false);

        if(check == true) {
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Matchinginfo = new Intent(getApplicationContext(), MatchingInfo.class);
                    Boolean messageset = true;
                    Matchinginfo.putExtra("infoset", messageset);
                    Matchinginfo.putExtra("trainerid", yourid);
                    startActivity(Matchinginfo);
                }
            });
        }
        else if(check == false) {
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent messagememberinfo = new Intent(getApplicationContext(), MemeberInfo.class);
                    messagememberinfo.putExtra("memberid", yourid);
                    startActivity(messagememberinfo);
                }
            });
        }

        //쪽지 버튼
        contentTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent content = new Intent(getApplicationContext(), Contents.class);
                content.putExtra("myid",myid);
                content.putExtra("yourid",yourid);
                content.putExtra("check", check);
                startActivity(content);
                finish();
            }
        });
        //뒤로가기
        Button prev = (Button) findViewById(R.id.button3);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        new httpcom().execute(myid,yourid);
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

                    String healthuser = data[0];
                    String trainer = data[1];
                    // "healthuser"=healthuser&"trainer"=trainer 생성
                    postData.append(URLEncoder.encode("healthuser", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(healthuser, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("trainer", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(trainer, "UTF-8"));

                byte[] postdatabytes = postData.toString().getBytes("UTF-8");

                conn.setRequestProperty("Content-Length", String.valueOf(postdatabytes.length));
                conn.getOutputStream().write(postdatabytes); //스트림에 write

                // echo값 받아서 저장후 연결 끊고 return해줌
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line.replaceFirst("\uFEFF\uFEFF", "");
                    Log.d("라인은 " ,line);
                    String[] chatarray = line.split("\\|");
                    if(chatarray[1].contains(myid)) {
                        chatlist.add(new ListContents(chatarray[0],1));
                    }
                    else if(chatarray[1].contains(yourid)) {
                        chatlist.add(new ListContents(chatarray[0],0));
                    }
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
            Log.d("s의 길이는 ", s.length() + "");
            m_Adapter = new ChatAdapter(chatlist);
            m_ListView = (ListView)findViewById(R.id.Chatlistview);

            m_ListView.setAdapter(m_Adapter);
        }
    }
}
