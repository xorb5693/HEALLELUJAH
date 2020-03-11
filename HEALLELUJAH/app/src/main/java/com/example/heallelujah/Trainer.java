package com.example.heallelujah;

import java.io.Serializable;

// 로그인 후 데이터를 저장할 트레이너 객체
public class Trainer implements Serializable {
    private String checkid,id,name,age,sex,phonenumber,field1,field2,
    field3, address1,address2, curriculum, carrer, pay, time, exp;

    public Trainer (String checkid, String id, String name, String age, String sex,
                    String phonenumber, String field1, String field2, String field3, String pay,
                   String address1,String address2, String curriculum, String carrer, String time, String exp) {
        this.checkid = checkid;
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex  = sex;
        this.phonenumber = phonenumber;
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.pay = pay;
        this.address1 = address1;
        this.address2 = address2;
        this.curriculum = curriculum;
        this.carrer = carrer;
        this.time = time;
        this.exp = exp;
    }

    public String GetcheckTrainerid () { return checkid; }
    public String GetTrainerid () { return id; }
    public String GetTrainername () {
        return this.name;
    }
    public String GetTrainerage () {
        return age;
    }
    public String GetTrainersex () {
        return sex;
    }
    public String GetTrainerphonenumber () {
        return phonenumber;
    }
    public String Getpay () {
        return pay;
    }
    public String GetTrainerfield1 () { return field1; }
    public String GetTrainerfield2 () { return field2; }
    public String GetTrainerfield3 () { return field3; }
    public String GetTraineraddress1 () {
        return address1;
    }
    public String GetTraineraddress2 () {
        return address2;
    }
    public String GetTrainercurriculum () {
        return curriculum;
    }
    public String GetTrainercarrer () {
        return carrer;
    }
    public String GetTrainertime () {
        return time;
    }
    public String GetTrainerexp () { return exp; }
    public void SetTrainername (String name) { this.name = name; }
    public void SetTrainerage (String age) { this.age = age; }
    public void SetTrainersex (String sex) { this.sex = sex; }
    public void SetTrainerphonenumber (String phonenumber) { this.phonenumber = phonenumber; }
    public void SetTrainerpay (String pay) { this.pay = pay; }
    public void SetTrainerfield1 (String field1) { this.field1 = field1; }
    public void SetTrainerfield2 (String field2) { this.field2 = field2; }
    public void SetTrainerfield3 (String field3) { this.field3 = field3; }
    public void SetTraineraddress1 (String address1) { this.address1 = address1; }
    public void SetTraineraddress2 (String address2) { this.address2 = address2; }
    public void SetTrainercurriculum (String curriculum) { this.curriculum = curriculum; }
    public void SetTrainercarrer (String carrer) { this.carrer = carrer; }
    public void SetTrainertime (String time) { this.time = time; }
    public void SetTrainerexp (String exp) { this.exp = exp; }
}
