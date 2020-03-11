package com.example.heallelujah;

public class Trainerlist {
    private String id,name,age,sex;

    public Trainerlist(String id, String name, String age, String sex) {
        this.id =id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String gettrainerid() {return id;}
    public String gettrainername() {return name;}
    public String gettrainerage() {return age;}
    public String gettrainersex() {return sex;}

}
