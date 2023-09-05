package com.gilead.collections.training;

public class Student extends Person{
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Student(int rollNo, String std, String sec, String type, String group, Person person) {
        this.rollNo = rollNo;
        this.std = std;
        this.sec = sec;
        this.type = type;
        this.group = group;
        this.person = person;
    }

    private Person person;

    private int rollNo;

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    private String std;

    private String sec;

    private String type;

    private String group;
}
