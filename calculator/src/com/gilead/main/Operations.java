package com.gilead.main;

public interface Operations {
    public Integer add(Integer a, Integer b);
    public String add(String a, String b); //Compile Time polymorphism - JVM recognizes in compile time
    public Integer subtract(Integer a, Integer b);
}
