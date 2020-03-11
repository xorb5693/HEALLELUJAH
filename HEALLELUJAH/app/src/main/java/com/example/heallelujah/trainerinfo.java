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

public class trainerinfo extends AppCompatActivity {
    private TextView addressview, signupview;
        private EditText idinput, password, passwordconfirm, name, age,
                backaddress, backphonenumber;
        private String TrainerId, TrainerPass,
                TrainerName, TrainerSex=null, TrainerAge, TrainerPhonenumber,
                frontphonenumber,Backphonenumber, Address1, Address2;
        private Trainer trainer;
        private boolean iddup = false, signcheck;
        private String url = "URL 입력", url2="URL 입력";
        private Intent intent;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_trainerinfo);
        //회원가입인지 아닌지 변수로 판단.
        intent = getIntent();
        signcheck = intent.getBooleanExtra("signcheck", false);
        //EditText 변수
        name = (EditText) findViewById(R.id.nameTrainer);
        password = (EditText) findViewById(R.id.passTrainer);
        passwordconfirm = (EditText) findViewById(R.id.passTrainer2);
        idinput = (EditText)findViewById(R.id.idTrainer);
        age = (EditText)findViewById(R.id.ageTrainer);
        backaddress = (EditText)findViewById(R.id.trainerbackaddress);
        backphonenumber = (EditText)findViewById(R.id.backPhoneTrainer);
        // Radio 변수
        RadioGroup rg = (RadioGroup)findViewById(R.id.sexTrainer);
        RadioButton rm = (RadioButton)findViewById(R.id.maleTrainer);
        RadioButton rf = (RadioButton)findViewById(R.id.femaleTrainer);
        //TextView 변수
        signupview = (TextView)findViewById(R.id.textView25);
        addressview = (TextView)findViewById(R.id.trainerAddress);
        //ID 중복확인버튼
        Button idpost = (Button)findViewById(R.id.checkTrainer);

        // 통신사 번호 스피너
        final String[] frontnumber = getResources().getStringArray(R.array.통신사);
        final Spinner carrier = (Spinner)findViewById(R.id.frontPhoneTrainer);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, frontnumber);
        carrier.setAdapter(adapter);
        carrier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                frontphonenumber = (String)carrier.getSelectedItem();
                if(!frontphonenumber.equals("000"))
                    Toast.makeText(trainerinfo.this,frontphonenumber+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //true면 정보수정 false면 회원 가입
        if(signcheck == true) {
            signupview.setText("정보 수정");
            trainer = (Trainer)intent.getSerializableExtra("trainer");
            //변수 불러옴
            TrainerId = trainer.GetTrainerid();
            TrainerName = trainer.GetTrainername();
            TrainerAge = trainer.GetTrainerage();
            TrainerSex = trainer.GetTrainersex();
            Address1 = trainer.GetTraineraddress1();
            Address2 = trainer.GetTraineraddress2();
            Log.d("주소는 ", Address1 + " " + Address2);
            TrainerPhonenumber = trainer.GetTrainerphonenumber();

            idinput.setClickable(false);
            idinput.setFocusable(false);
            idinput.setText(TrainerId);
            idpost.setVisibility(View.INVISIBLE);
            name.setText(TrainerName);
            age.setText(TrainerAge);
            addressview.setText(Address1);
            backaddress.setText(Address2);

            if(TrainerSex.contains("남성")) rg.check(rm.getId());
            else if(TrainerSex.contains("여성")) rg.check(rf.getId());

            //핸드폰 앞자리 뒷자리 분리
            frontphonenumber = TrainerPhonenumber.substring(0,3);
            Backphonenumber = TrainerPhonenumber.substring(3, TrainerPhonenumber.length());
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

        // 성별 입력
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkbutton) {
                TrainerSex = ((RadioButton)findViewById(checkbutton)).getText().toString();
                Toast.makeText(trainerinfo.this,TrainerSex+"을 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //뒤로가기
        Button prev = (Button)findViewById(R.id.prevTrainerChoice);
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

        // 도로명 주소 뷰 클릭 시 이벤트
        addressview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressfind = new Intent(getApplicationContext(), AddressFind.class);
                startActivityForResult(addressfind, 200);
            }
        });

        //트레이너id 중복체크
        idpost.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idinput.getText().toString().length()==0) {
                    Toast.makeText(trainerinfo.this,"ID를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String state = "0";
                    TrainerId = idinput.getText().toString();
                    new httpcom().execute(state, TrainerId); // http통신 클래스 생성 후 실행
                }
            }
        });

        Button traineronly = (Button)findViewById(R.id.nextTrainerOnly);//다음트레이너정보버튼
        traineronly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //다음트레이너 정보액티비티 인텐트 생성
                Intent traineronly = new Intent(getApplicationContext(), traineronly.class);
                if(signcheck == true) {
                    String state = "1";


                    //비밀번호 입력확인
                    if (password.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    // 비밀번호 자릿수 확인
                    if (password.getText().toString().length() < 8) {
                        Toast.makeText(trainerinfo.this, "패스워드를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    //비밀번호 확인 입력 확인
                    if (passwordconfirm.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "패스워드 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                        passwordconfirm.requestFocus();
                        return;
                    }
                    //비밀번호 일치 확인
                    if (!password.getText().toString().equals(passwordconfirm.getText().toString())) {
                        Toast.makeText(trainerinfo.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        passwordconfirm.setText("");
                        password.requestFocus();
                        return;
                    } else {
                        TrainerPass = password.getText().toString();
                    }
                    //이름 입력 확인
                    if (name.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                        return;
                    } else {
                        TrainerName = name.getText().toString();
                    }
                    // 성별 입력 확인
                    if (TrainerSex == null) {
                        Toast.makeText(trainerinfo.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 나이 입력 확인
                    if (age.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "나이를 입력하세요.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                        return;
                    } else {
                        TrainerAge = age.getText().toString();
                    }
                    //주소 입력 확인
                    if (addressview.getText().equals("도로명 주소")) {
                        Toast.makeText(trainerinfo.this, "도로명 주소를 검색하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //상세주소 입력 확인
                    if (backaddress.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "상세 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backaddress.requestFocus();
                        return;
                    } else {
                        Address1 = addressview.getText().toString();
                        Address2 = backaddress.getText().toString();
                    }
                    //휴대폰 앞번호 입력확인
                    if (frontphonenumber.equals("000")) {
                        Toast.makeText(trainerinfo.this, "핸드폰 앞번호를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //휴대폰 뒷번호 입력확인
                    if (backphonenumber.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "핸드폰 뒷번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backphonenumber.requestFocus();
                        return;
                    } else {
                        Backphonenumber = backphonenumber.getText().toString();
                        TrainerPhonenumber = frontphonenumber + Backphonenumber;
                    }
                    new httpcom().execute(state, TrainerId, TrainerPass, TrainerName, TrainerSex, TrainerAge, Address1, Address2, TrainerPhonenumber);
                }
                else if(signcheck == false) {
                    //id입력 확인
                    if (idinput.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "ID를 입력하세요.", Toast.LENGTH_SHORT).show();
                        idinput.requestFocus();
                        return;
                    }
                    //중복확인을 눌렀는지 안했는지
                    if (iddup == false) {
                        Toast.makeText(trainerinfo.this, "ID 중복확인을 누르십시오.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        TrainerId = idinput.getText().toString();
                        traineronly.putExtra("id", TrainerId);
                    }
                    //비밀번호 입력확인
                    if (password.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "패스워드를 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    // 비밀번호 자릿수 확인
                    if (password.getText().toString().length() < 8) {
                        Toast.makeText(trainerinfo.this, "패스워드를 8자 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    }
                    //비밀번호 확인 입력 확인
                    if (passwordconfirm.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "패스워드 확인을 입력하세요.", Toast.LENGTH_SHORT).show();
                        passwordconfirm.requestFocus();
                        return;
                    }
                    //비밀번호 일치 확인
                    if (!password.getText().toString().equals(passwordconfirm.getText().toString())) {
                        Toast.makeText(trainerinfo.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                        password.setText("");
                        passwordconfirm.setText("");
                        password.requestFocus();
                        return;
                    } else {
                        TrainerPass = password.getText().toString();
                        traineronly.putExtra("pass", TrainerPass);
                    }
                    //이름 입력 확인
                    if (name.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                        name.requestFocus();
                        return;
                    } else {
                        TrainerName = name.getText().toString();
                        traineronly.putExtra("name", TrainerName);
                    }
                    // 성별 입력 확인
                    if (TrainerSex == null) {
                        Toast.makeText(trainerinfo.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        traineronly.putExtra("sex", TrainerSex);
                    }
                    // 나이 입력 확인
                    if (age.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "나이를 입력하세요.", Toast.LENGTH_SHORT).show();
                        age.requestFocus();
                        return;
                    } else {
                        TrainerAge = age.getText().toString();
                        traineronly.putExtra("age", TrainerAge);
                    }
                    //주소 입력 확인
                    if (addressview.getText().equals("도로명 주소")) {
                        Toast.makeText(trainerinfo.this, "도로명 주소를 검색하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //상세주소 입력 확인
                    if (backaddress.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "상세 주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backaddress.requestFocus();
                        return;
                    } else {
                        Address1 = addressview.getText().toString();
                        Address2 = backaddress.getText().toString();
                        traineronly.putExtra("address1", Address1);
                        traineronly.putExtra("address2", Address2);
                    }
                    //휴대폰 앞번호 입력확인
                    if (frontphonenumber.equals("000")) {
                        Toast.makeText(trainerinfo.this, "핸드폰 앞번호를 선택하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //휴대폰 뒷번호 입력확인
                    if (backphonenumber.getText().toString().length() == 0) {
                        Toast.makeText(trainerinfo.this, "핸드폰 뒷번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        backphonenumber.requestFocus();
                        return;
                    } else {
                        Backphonenumber = backphonenumber.getText().toString();
                        TrainerPhonenumber = frontphonenumber + Backphonenumber;
                        traineronly.putExtra("phonenumber", TrainerPhonenumber);
                    }
                    startActivity(traineronly); //다음트레이너 액티비티 전환
                }
            }
        });
    }
    // 도로명 주소 액티비티에서 주소를 받아옴.
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
                    //state, TrainerId, TrainerPass, TrainerName, TrainerSex, TrainerAge, Address1, Address2, TrainerPhonenumber
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
            if(s.length() == 15) {
                Toast.makeText(trainerinfo.this,"중복된 ID가 없습니다.", Toast.LENGTH_SHORT).show();
                idinput.setClickable(false);
                idinput.setFocusable(false);
                iddup = true;
            }
            else if(s.contains("트레이너 기본 정보 수정 완료!")) {
                Toast.makeText(trainerinfo.this,s, Toast.LENGTH_SHORT).show();
                trainer.SetTrainername(TrainerName);
                trainer.SetTrainerage(TrainerAge);
                trainer.SetTrainersex(TrainerSex);
                trainer.SetTraineraddress1(Address1);
                trainer.SetTraineraddress2(Address2);
                trainer.SetTrainerphonenumber(TrainerPhonenumber);
                Intent Trainerafterlogin = new Intent(getApplicationContext(), trainerafterlogin.class);
                Trainerafterlogin.putExtra("trainer",trainer);
                startActivity(Trainerafterlogin);
                finish();
            }
            else {
                Toast.makeText(trainerinfo.this,"중복된 ID가 있습니다.", Toast.LENGTH_SHORT).show();
                idinput.setSelection(idinput.length());
            }
        }
    }
}
