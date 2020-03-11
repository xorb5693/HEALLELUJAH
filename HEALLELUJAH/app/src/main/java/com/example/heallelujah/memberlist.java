package com.example.heallelujah;

public class memberlist {
    private String id,name,age,sex;

    public memberlist(String id, String name, String age, String sex) {
        this.id =id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getmemberid() {return id;}
    public String getmembername() {return name;}
    public String getmemberage() {return age;}
    public String getmembersex() {return sex;}
}
