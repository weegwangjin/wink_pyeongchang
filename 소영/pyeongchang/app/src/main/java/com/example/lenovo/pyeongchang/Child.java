package com.example.lenovo.pyeongchang;

/**
 * Created by lenovo on 2017-08-12.
 */

public class Child {
    private String name;
    private int age;
    private int Pphone_num;
    private String H_addr;
    private String note;
    private String note2;

    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public int getPphone_num(){
        return Pphone_num;
    }

    public String getH_addr(){
        return  H_addr;
    }
    public String getNote(){
        return note;
    }

    public String getNote2(){
        return note2;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setAge(int age){
        this.age = age;
    }
    public void Pphone_num(int Pphone_num){
        this.Pphone_num = Pphone_num;
    }
    public void setH_addr(String H_addr){
        this.H_addr = H_addr;
    }
    public void setNote(String note){
        this.note = note;
    }
    public void setNote2(String note2){
        this.note2 = note2;
    }
}