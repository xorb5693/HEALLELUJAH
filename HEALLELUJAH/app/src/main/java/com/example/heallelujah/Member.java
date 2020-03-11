package com.example.heallelujah;

import java.io.Serializable;

//로그인 후 데이터를 저장할 멤버 객체
public class Member implements Serializable {
    private String checkid ,id, name, age, sex, phonenumber,
                    object1, object2, object3, pay, address1,address2, height, weight;

    public Member (String checkid, String id, String name, String age,
                   String sex, String phonenumber,String object1, String object2,
                   String object3, String pay, String address1, String address2, String height, String weight) {
        this.checkid = checkid;
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phonenumber = phonenumber;
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
        this.pay = pay;
        this.address1 = address1;
        this.address2 = address2;
        this.height = height;
        this.weight = weight;
    }
    public String Getcheckid() {
        return checkid;
    }
    public String Getid() {
        return id;
    }
    public String Getname() {
        return name;
    }
    public String Getage() {
        return age;
    }
    public String Getsex() { return sex; }
    public String Getphonenumber() { return phonenumber; }
    public String Getobject1() {
        return object1;
    }
    public String Getobject2() {
        return object2;
    }
    public String Getobject3() {
        return object3;
    }
    public String Getpay() {
        return pay;
    }
    public String Getaddress1() {
        return address1;
    }
    public String Getaddress2() { return address2; }
    public String Getheight() {
        return height;
    }
    public String Getweight() {
        return weight;
    }
    public void Setname(String name) { this.name = name; }
    public void Setage(String age) { this.age = age; }
    public void Setsex(String sex) { this.sex = sex; }
    public void Setphonenumber(String phonenumber) {this.phonenumber= phonenumber;}
    public void Setobject1(String object1) {this.object1= object1;}
    public void Setobject2(String object2) {this.object2= object2;}
    public void Setobject3(String object3) {this.object3= object3;}
    public void Setpay(String pay) {this.pay= pay;}
    public void Setaddress1(String address1) {this.address1= address1;}
    public void Setaddress2(String address2) {this.address2= address2;}
    public void Setheight(String height) {this.height= height;}
    public void Setweight(String weight) {this.weight= weight;}
}
