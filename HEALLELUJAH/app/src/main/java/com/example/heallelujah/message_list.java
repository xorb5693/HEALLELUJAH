package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;

public class message_list extends AppCompatActivity {
    private ArrayList<memberlist> membermesaagelist;
    private ArrayList<Trainerlist> trainermessagelist;
    private Intent intent;
    private String myid;
    private ListView messageview;
    private Boolean check;
    private String url = "URL 입력";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        //id,check가져오기
        intent = getIntent();
        myid = intent.getStringExtra("id");
        check = intent.getBooleanExtra("check",false);
        //ListView 변수
        messageview = (ListView) findViewById(R.id.messagelist);

        //뒤로가기
        Button prev = (Button) findViewById(R.id.backbutton);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        if(check == true) {
            trainermessagelist = new ArrayList<Trainerlist>();
            new httpcom().execute(myid);


        }
        else if(check == false) {
            membermesaagelist = new ArrayList<memberlist>();
            new httpcom().execute(myid);
        }

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


                if(check == true) {
                    String id = data[0];
                    String checkid = "1";
                    // "id"=id&"checkid"=checkid 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("checkid", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(checkid, "UTF-8"));
                }
                else if(check == false) {
                    String id = data[0];
                    String checkid = "0";
                    // "id"=id&"checkid"=checkid 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
                    postData.append("&");
                    postData.append(URLEncoder.encode("checkid", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(checkid, "UTF-8"));
                }

                byte[] postdatabytes = postData.toString().getBytes("UTF-8");

                conn.setRequestProperty("Content-Length", String.valueOf(postdatabytes.length));
                conn.getOutputStream().write(postdatabytes); //스트림에 write

                // echo값 받아서 저장후 연결 끊고 return해줌
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                if(check == true) {
                    while ((line = reader.readLine()) != null) {
                        Log.d("라인 ", line);
                        line.replaceFirst("\uFEFF\uFEFF", "");
                        String[] trainerarray = line.split("\\|");
                        // trainermessagelist.add(new Trainerlist("test1", "trainer1", "20", "남성"));
                       if(line.length() != 2) {
                           trainermessagelist.add(new Trainerlist(trainerarray[0], trainerarray[1],trainerarray[2], trainerarray[3]));
                        }
                        sb.append(line);
                    }
                }
                else if(check == false) {
                    while ((line = reader.readLine()) != null) {
                        line.replaceFirst("\uFEFF\uFEFF", "");
                        Log.d("라인 ", line.length()+"");
                        String[] memberarray = line.split("\\|");
                        //membermesaagelist.add(new memberlist("test1", "test1", "20", "남성"));
                        if(line.length() !=2) {
                            membermesaagelist.add(new memberlist(memberarray[0], memberarray[1], memberarray[2], memberarray[3]));
                        }
                        sb.append(line);
                    }
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
            Log.d("s의 길이는 ", s.length()+"");
            if(check == true && s.length() != 0) {
                final trainermessagelistAdapter listadapter = new trainermessagelistAdapter(getApplicationContext(), trainermessagelist);
                messageview.setAdapter(listadapter);
                messageview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent chatting1 = new Intent(getApplicationContext(), chatting.class);
                        String yourid = listadapter.getItem(position).gettrainerid();
                        chatting1.putExtra("myid", myid);
                        chatting1.putExtra("yourid",yourid);
                        chatting1.putExtra("check",check);
                        startActivity(chatting1);
                    }
                });
            }
            else if(check == false && s.length() != 0) {
                final membermessagelistAdapter listadapter = new membermessagelistAdapter(getApplicationContext(), membermesaagelist);
                messageview.setAdapter(listadapter);
                messageview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent chatting1 = new Intent(getApplicationContext(), chatting.class);
                        String yourid = listadapter.getItem(position).getmemberid();
                        chatting1.putExtra("myid", myid);
                        chatting1.putExtra("yourid",yourid);
                        chatting1.putExtra("check",check);
                        startActivity(chatting1);
                    }
                });
            }
            else {
                Toast.makeText(message_list.this, "쪽지가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
