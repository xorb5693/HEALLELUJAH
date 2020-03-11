package com.example.heallelujah;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.ArrayList;

public class MatchingList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner mhealth_area1, mhealth_area2, mhealth_area3;
    private ArrayList<Trainerlist> trainerlist;
    private String address1, address2, pay, mobject1 = "1지망", mobject2 = "2지망", mobject3 = "3지망", MemberId,membername;
    private Switch infoswitch;
    private Button startmatch;
    private TextView addressview;
    private EditText priceinput, addressinput;
    private Intent intent;
    private ListView trainerview;
    private String url = "URL 입력";
    public static MatchingList matchinglist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_list);

        matchinglist = MatchingList.this;
        //id가져오기
        intent = getIntent();
        MemberId = intent.getStringExtra("id");
        membername = intent.getStringExtra("name");
        //Button 변수
        startmatch = (Button) findViewById(R.id.startMatching);
        //TextView 변수
        //EditText변수
        priceinput = (EditText) findViewById(R.id.priceMatching);
        addressinput = (EditText) findViewById(R.id.backaddressMatching);
        //Listview
        trainerview = (ListView) findViewById(R.id.matchingView);

        //뒤로가기
        Button prev = (Button) findViewById(R.id.prevInfo);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        // 도로명 주소 찾기
        addressview = (TextView) findViewById(R.id.addressMatching);
        addressview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressfind = new Intent(getApplicationContext(), AddressFind.class);
                startActivityForResult(addressfind, 200);
            }
        });

        // 헬스 분야 스피너 1지망
        final String[] healtharea = getResources().getStringArray(R.array.헬스영역1);
        mhealth_area1 = (Spinner) findViewById(R.id.realmMatching);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea);
        mhealth_area1.setAdapter(adapter);
        mhealth_area1.setOnItemSelectedListener(this);
        // 헬스 분야 스피너 2지망
        final String[] healtharea2 = getResources().getStringArray(R.array.헬스영역2);
        mhealth_area2 = (Spinner) findViewById(R.id.realmMatching2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea2);
        mhealth_area2.setAdapter(adapter2);
        mhealth_area2.setOnItemSelectedListener(this);
        // 헬스 분야 스피너 3지망
        final String[] healtharea3 = getResources().getStringArray(R.array.헬스영역3);
        mhealth_area3 = (Spinner) findViewById(R.id.realmMatching3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, healtharea3);
        mhealth_area3.setAdapter(adapter3);
        mhealth_area3.setOnItemSelectedListener(this);

        //기존정보 사용 스위치
        infoswitch = (Switch) findViewById(R.id.matchingSwitch);
        infoswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //정보 가져오기
                    intent = getIntent();
                    address1 = intent.getStringExtra("address1");
                    address2 = intent.getStringExtra("address2");
                    pay = intent.getStringExtra("pay");
                    mobject1 = intent.getStringExtra("object1");
                    mobject2 = intent.getStringExtra("object2");
                    mobject3 = intent.getStringExtra("object3");
                    priceinput.setText(pay);
                    addressview.setText(address1);
                    addressinput.setText(address2);
                    for (int i = 0; i < healtharea.length; i++) {
                        if (healtharea[i].equals(mobject1)) {
                            mhealth_area1.setSelection(i);
                        }
                    }
                    for (int i = 0; i < healtharea2.length; i++) {
                        if (healtharea2[i].equals(mobject2)) {
                            mhealth_area2.setSelection(i);
                        }
                    }
                    for (int i = 0; i < healtharea3.length; i++) {
                        if (healtharea3[i].equals(mobject3)) {
                            mhealth_area3.setSelection(i);
                        }
                    }
                } else {
                    priceinput.setText("");
                    mhealth_area1.setSelection(0);
                    mhealth_area2.setSelection(0);
                    mhealth_area3.setSelection(0);
                    addressview.setText("도로명 주소");
                    addressinput.setText("");
                }
            }
        });

        //매칭시작 버튼
        startmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //주소 입력 확인
                if (addressview.getText().equals("도로명 주소")) {
                    Toast.makeText(MatchingList.this, "도로명 주소를 검색하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //상세주소 입력 확인
                if (addressinput.getText().toString().length() == 0) {
                    Toast.makeText(MatchingList.this, "상세 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                    addressinput.requestFocus();
                    return;
                } else {
                    address1 = addressview.getText().toString();
                    address2 = addressinput.getText().toString();
                }

                // 1지망 분야 입력 확인
                if (mobject1.equals("1지망")) {
                    Toast.makeText(MatchingList.this, "1지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2지망 분야 입력 확인
                if (mobject2.equals("2지망")) {
                    Toast.makeText(MatchingList.this, "2지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 3지망 분야 입력 확인
                if (mobject3.equals("3지망")) {
                    Toast.makeText(MatchingList.this, "3지망 분야를 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //횟수당 가격 입력 확인
                if (priceinput.getText().toString().length() == 0) {
                    Toast.makeText(MatchingList.this, "횟수 당 가격을 입력하세요.", Toast.LENGTH_SHORT).show();
                    priceinput.requestFocus();
                    return;
                } else {
                    pay = priceinput.getText().toString();
                }

                new httpcom().execute(mobject1,mobject2,mobject3,address1,pay);// http통신 클래스 생성 후 실행

            }
        });
    }

    // addressfind 액티비티에서 전달받은 데이터 셋팅.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            String address1 = data.getStringExtra("address1");
            String address2 = data.getStringExtra("address2");
            addressview.setText(address1 + " " + address2);
        } else {
            addressview.setText("도로명 주소");
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.realmMatching) {
            mobject1 = mhealth_area1.getSelectedItem().toString();
            if (!mobject1.equals("1지망")) {
                Toast.makeText(MatchingList.this, mobject1 + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (spinner.getId() == R.id.realmMatching2) {
            mobject2 = mhealth_area2.getSelectedItem().toString();
            if (!mobject2.equals("2지망")) {
                Toast.makeText(MatchingList.this, mobject2 + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (spinner.getId() == R.id.realmMatching3) {
            mobject3 = mhealth_area3.getSelectedItem().toString();
            if (!mobject3.equals("3지망")) {
                Toast.makeText(MatchingList.this, mobject3 + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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


                String object1 = data[0];
                String object2 = data[1];
                String object3 = data[2];
                String address = data[3];
                String pay = data[4];

                // "object1"=object1&"object2"=object2&"object3"=object3
                // "pay"=pay&"address"=address 생성
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
                postData.append("&");
                postData.append(URLEncoder.encode("address", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(address, "UTF-8"));
                postData.append("&");
                postData.append(URLEncoder.encode("pay", "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(pay, "UTF-8"));

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
            trainerlist = new ArrayList<Trainerlist>();
            Log.d("온데이터는 " ,s
            );
            if (s.length() != 0) {
                String[] semiarray = s.split(":");
                for (int i = 0; i < semiarray.length; i++) {
                    String[] orarray = semiarray[i].split("\\|");
                    trainerlist.add(new Trainerlist(orarray[0], orarray[1], orarray[2], orarray[3]));
                }
                final matchingAdapter listadapter = new matchingAdapter(getApplicationContext(), trainerlist);
                trainerview.setAdapter(listadapter);
                trainerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(), listadapter.getItem(position).GetTrainername(),Toast.LENGTH_SHORT).show();
                        Trainerlist temp = listadapter.getItem(position);
                        String infoid = temp.gettrainerid();
                        Boolean infoset = false;
                        Intent matchinginfo = new Intent(getApplicationContext(), MatchingInfo.class);
                        matchinginfo.putExtra("trainerid", infoid);
                        matchinginfo.putExtra("memberid", MemberId);
                        matchinginfo.putExtra("membername", membername);
                        matchinginfo.putExtra("matchingset", infoset);
                        startActivity(matchinginfo);
                    }
                });
            }
            else {
                Toast.makeText(getApplicationContext(), "매칭 트레이너가 없습니다.",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
