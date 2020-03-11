package com.example.heallelujah;

import androidx.annotation.Nullable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class membershipinfo extends AppCompatActivity {
    private TextView addressview, signupview;
    private EditText idinput, password, passwordconfirm, name, age,
                    backaddress, backphonenumber;
    private Member mem;
    private String MemberId, MemberPass,
            MemberName, MemberSex=null, MemberAge, Address1,Address2,MemberPhonenumber,
            frontphonenumber,Backphonenumber;
    private boolean iddup = false, signup = false, signcheck;
    private String url = "URL 입력", url2="URL 입력";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membershipinfo);

        //회원가입인지 아닌지 변수로 판단.
        intent = getIntent();
        signcheck = intent.getBooleanExtra("signcheck", false);
        //EditText 변수
        name = (EditText) findViewById(R.id.nameMember);
        password = (EditText) findViewById(R.id.passMember);
        passwordconfirm = (EditText) findViewById(R.id.passMember2);
        idinput = (EditText)findViewById(R.id.idMember);
        age = (EditText)findViewById(R.id.ageMember);
        backaddress = (EditText)findViewById(R.id.memberbackaddress);
        backphonenumber = (EditText)findViewById(R.id.backPhoneMember);
        //TextView 변수
        signupview = (TextView)findViewById(R.id.textView12);
        addressview = (TextView)findViewById(R.id.memberaddress);
        // Radio 변수
        RadioGroup rg = (RadioGroup)findViewById(R.id.sexMemeber);
        RadioButton rm = (RadioButton)findViewById(R.id.maleMember);
        RadioButton rf = (RadioButton)findViewById(R.id.femalMember);
        // id중복 확인 버튼
        Button idpost = (Button)findViewById(R.id.checkMember);

        // 성별 입력
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkbutton) {
                    MemberSex = ((RadioButton) findViewById(checkbutton)).getText().toString();
                    Toast.makeText(membershipinfo.this, MemberSex + "을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        // 통신사 번호 스피너 번호 셀렉트
        final String[] frontnumber = getResources().getStringArray(R.array.통신사);
        final Spinner carrier = (Spinner)findViewById(R.id.frontPhoneMember);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, frontnumber);
        carrier.setAdapter(adapter);
        carrier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frontphonenumber = (String)carrier.getSelectedItem();
                if(!frontphonenumber.equals("000"))
                    Toast.makeText(membershipinfo.this,frontphonenumber+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //true면 정보수정 false면 회원 가입
        if(signcheck == true) {
            signupview.setText("정보 수정");
            mem = (Member)intent.getSerializableExtra("member");
            //변수 불러옴
            MemberId = mem.Getid();
            MemberName = mem.Getname();
            MemberAge = mem.Getage();
            MemberSex = mem.Getsex();
            Address1 = mem.Getaddress1();
            Address2 = mem.Getaddress2();
            MemberPhonenumber = mem.Getphonenumber();

            idinput.setClickable(false);
            idinput.setFocusable(false);
            idinput.setText(MemberId);
            idpost.setVisibility(View.INVISIBLE);
            name.setText(MemberName);
            age.setText(MemberAge);
            addressview.setText(Address1);
            backaddress.setText(Address2);
            if(MemberSex.contains("남성")) rg.check(rm.getId());
            else if(MemberSex.contains("여성")) rg.check(rf.getId());

            //핸드폰 앞자리 뒷자리 분리
            frontphonenumber = MemberPhonenumber.substring(0,3);
            Backphonenumber = MemberPhonenumber.substring(3, MemberPhonenumber.length());
            Log.d("앞자리 : ", frontphonenumber);
            Log.d("뒷자리 : ", Backphonenumber);

            for(int i=0; i<6; i++) {
                if(frontnumber[i].equals(frontphonenumber)) {
                    carrier.setSelection(i);
                }
            }
            backphonenumber.setText(Backphonenumber);

            iddup = true;
        }
        else if(signcheck == false) {
            signupview.setText("회원 가입");
        }

        //뒤로가기
        Button prev = (Button)findViewById(R.id.prevMemberChoice);
        prev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(signcheck == true) {
                    Intent Memberafterlogin = new Intent(getApplicationContext(), membershipafterlogin.class);
                    Memberafterlogin.putExtra("member",mem);
                    startActivity(Memberafterlogin);
                    finish();
                }
            }
        });


        // 도로명 주소 찾기
        addressview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressfind = new Intent(getApplicationContext(), AddressFind.class);
                startActivityForResult(addressfind, 200);
            }
        });

        //id 중복체크
        idpost.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idinput.getText().toString().length()==0) {
                    Toast.makeText(membershipinfo.this,"ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String state = "0";
                    MemberId = idinput.getText().toString();
                    new httpcom().execute(state, MemberId); // http통신 클래스 생성 후 실행
                }
            }
        });

        //데이터 확인 후 액티비티 전환
        Button membershiponly = (Button)findViewById(R.id.nextMemberOnly);//다음회원정보버튼
        membershiponly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //다음회원 정보액티비티 인텐트 생성
                Intent membershiponly = new Intent(getApplicationContext(), membershiponly.class);
                if(signcheck == true) {
                    String state = "1";

                    //비밀번호 입력확인
                    if (password.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    // 비밀번호 자릿수 확인
                    if (password.getText().toString().length() < 8) {
                        Toast.makeText(membershipinfo.this, "패스워드를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    //비밀번호 확인 입력 확인
                    if (passwordconfirm.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "패스워드 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                        passwordconfirm.requestFocus();
                        return;
                    }
                    //비밀번호 일치 확인
                    if (!password.getText().toString().equals(passwordconfirm.getText().toString())) {
                        Toast.makeText(membershipinfo.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        passwordconfirm.setText("");
                        password.requestFocus();
                        return;
                    } else {
                        MemberPass = password.getText().toString();
                    }
                    //이름 입력 확인
                    if (name.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                        return;
                    } else {
                        MemberName = name.getText().toString();
                    }
                    // 성별 입력 확인
                    if (MemberSex == null) {
                        Toast.makeText(membershipinfo.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 나이 입력 확인
                    if (age.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "나이를 입력하세요.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                        return;
                    } else {
                        MemberAge = age.getText().toString();
                    }
                    //주소 입력 확인
                    if (addressview.getText().equals("도로명 주소")) {
                        Toast.makeText(membershipinfo.this, "도로명 주소를 검색하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //상세주소 입력 확인
                    if (backaddress.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "상세 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backaddress.requestFocus();
                        return;
                    } else {
                        Address1 = addressview.getText().toString();
                        Address2 = backaddress.getText().toString();
                    }
                    //휴대폰 앞번호 입력확인
                    if (frontphonenumber.equals("000")) {
                        Toast.makeText(membershipinfo.this, "핸드폰 앞번호를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //휴대폰 뒷번호 입력확인
                    if (backphonenumber.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "핸드폰 뒷번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backphonenumber.requestFocus();
                        return;
                    } else {
                        Backphonenumber = backphonenumber.getText().toString();
                        MemberPhonenumber = frontphonenumber + Backphonenumber;
                    }
                    new httpcom().execute(state, MemberId,MemberPass ,MemberName, MemberSex, MemberAge, Address1,Address2, MemberPhonenumber);
                }
                else if(signcheck == false) {
                    //id입력 확인
                    if (idinput.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                        idinput.requestFocus();
                        return;
                    }
                    //중복확인을 눌렀는지 안했는지
                    if (iddup == false) {
                        Toast.makeText(membershipinfo.this, "ID 중복확인을 누르십시오.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        MemberId = idinput.getText().toString();
                        membershiponly.putExtra("id", MemberId);
                    }
                    //비밀번호 입력확인
                    if (password.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    // 비밀번호 자릿수 확인
                    if (password.getText().toString().length() < 8) {
                        Toast.makeText(membershipinfo.this, "패스워드를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    //비밀번호 확인 입력 확인
                    if (passwordconfirm.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "패스워드 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                        passwordconfirm.requestFocus();
                        return;
                    }
                    //비밀번호 일치 확인
                    if (!password.getText().toString().equals(passwordconfirm.getText().toString())) {
                        Toast.makeText(membershipinfo.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        passwordconfirm.setText("");
                        password.requestFocus();
                        return;
                    } else {
                        MemberPass = password.getText().toString();
                        membershiponly.putExtra("pass", MemberPass);
                    }
                    //이름 입력 확인
                    if (name.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                        return;
                    } else {
                        MemberName = name.getText().toString();
                        membershiponly.putExtra("name", MemberName);
                    }

                    // 성별 입력 확인
                    if (MemberSex == null) {
                        Toast.makeText(membershipinfo.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        membershiponly.putExtra("sex", MemberSex);
                    }
                    // 나이 입력 확인
                    if (age.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "나이를 입력하세요.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                        return;
                    } else {
                        MemberAge = age.getText().toString();
                        membershiponly.putExtra("age", MemberAge);
                    }
                    //주소 입력 확인
                    if (addressview.getText().equals("도로명 주소")) {
                        Toast.makeText(membershipinfo.this, "도로명 주소를 검색하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //상세주소 입력 확인
                    if (backaddress.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "상세 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backaddress.requestFocus();
                        return;
                    } else {
                        Address1 = addressview.getText().toString();
                        Address2 = backaddress.getText().toString();
                        membershiponly.putExtra("address1", Address1);
                        membershiponly.putExtra("address2", Address2);
                    }
                    //휴대폰 앞번호 입력확인
                    if (frontphonenumber.equals("000")) {
                        Toast.makeText(membershipinfo.this, "핸드폰 앞번호를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //휴대폰 뒷번호 입력확인
                    if (backphonenumber.getText().toString().length() == 0) {
                        Toast.makeText(membershipinfo.this, "핸드폰 뒷번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backphonenumber.requestFocus();
                        return;
                    } else {
                        Backphonenumber = backphonenumber.getText().toString();
                        MemberPhonenumber = frontphonenumber + Backphonenumber;
                        membershiponly.putExtra("phonenumber", MemberPhonenumber);
                        membershiponly.putExtra("signup", signup);
                    }
                    startActivity(membershiponly); //다음회원정보 액티비티 전환
                }
            }
        });
    }

    // addressfind 액티비티에서 전달받은 데이터 셋팅.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if(requestCode == 200 && resultCode == RESULT_OK) {
            String address1 = data.getStringExtra("address1");
            String address2 = data.getStringExtra("address2");
            addressview.setText(address1+ " "+ address2);
        }
        else {
            addressview.setText("도로명 주소");
        }
    }
    public void onBackPressed() {
        if(signcheck == true) {
            Intent Memberafterlogin = new Intent(getApplicationContext(), membershipafterlogin.class);
            Memberafterlogin.putExtra("member",mem);
            startActivity(Memberafterlogin);
            finish();
        }
        else if(signcheck == false) {
            super.onBackPressed();
        }
    }

   //AsyncTask를 통한 인터넷 쓰레드 동작.
                                //매개변수 String , void, 결과값 String
    public class httpcom extends AsyncTask<String, Void, String> {
        @Override
        //백그라운드에서 http php 통신
        protected String doInBackground(String... data) {
            try {
                String state = data[0]; // 상태체크
                URL obj = null;
                if(state.equals("1")) {
                    obj = new URL(url2);
                }
                else if(state.equals("0")) {
                    obj = new URL(url);
                }
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");//post 메소드로 동작
                conn.setDoInput(true); // 입력 셋팅
                conn.setDoOutput(true); // 출력 셋팅
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                StringBuilder postData = new StringBuilder();
                if(state.equals("1")) {
                    //state, MemberId,MemberPass ,MemberName, MemberSex, MemberAge, Address1,Address2, MemberPhonenumber
                    String id = data[1];
                    String password = data[2];
                    String name = data[3];
                    String sex = data[4];
                    String age = data[5];
                    String address1 = data[6];
                    String address2 = data[7];
                    String phonenumber = data[8];
                    // "id"=id&"password"=password&"name"=name&"sex"=sex&"age"=age&
                    // "address1"=address1&"address2"=address2&"phonenumber"=phonenumber 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("password", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(password, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("name", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(name, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("sex", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(sex, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("age", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(age, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("address1", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(address1, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("address2", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(address2, "UTF-8"));
                    postData.append('&');
                    postData.append(URLEncoder.encode("phonenumber", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(phonenumber, "UTF-8"));

                }
                else if(state.equals("0")) {
                    String id = data[1]; // 위의 execute의 id 변수
                    // "id"=id 생성
                    postData.append(URLEncoder.encode("id", "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(id, "UTF-8"));
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
        // doinBackground에서 값을 리턴 받음 그후 echo값을 통하여 id 중복판별.
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.length() == 15) {
                Toast.makeText(membershipinfo.this, "중복된 ID가 없습니다.", Toast.LENGTH_SHORT).show();
                idinput.setClickable(false);
                idinput.setFocusable(false);
                iddup = true;
            }
            else if(s.contains("회원 기본 정보 수정 완료!")) {
                Toast.makeText(membershipinfo.this, s, Toast.LENGTH_SHORT).show();
                Log.d("기본정보는 ", s);
                mem.Setname(MemberName);
                mem.Setage(MemberAge);
                mem.Setsex(MemberSex);
                mem.Setaddress1(Address1);
                mem.Setaddress2(Address2);
                mem.Setphonenumber(MemberPhonenumber);
                Intent Memberafterlogin = new Intent(getApplicationContext(), membershipafterlogin.class);
                Memberafterlogin.putExtra("member",mem);
                startActivity(Memberafterlogin);
                finish();
            }
            else {
                Toast.makeText(membershipinfo.this, "중복된 ID가 있습니다.", Toast.LENGTH_SHORT).show();
                idinput.setSelection(idinput.length());
            }
        }
    }
}
