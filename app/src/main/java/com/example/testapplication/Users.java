package com.example.testapplication;

public class Users {
    private String name, subName;
    private String note_Id;

    public Users(){}

    public String getNote_Id() {
        return note_Id;
    }

    public void setNote_Id(String note_Id) {
        this.note_Id = note_Id;
    }

    public Users(String name, String subName, String note_Id) {
        this.name = name;
        this.subName = subName;
        this.note_Id = note_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
