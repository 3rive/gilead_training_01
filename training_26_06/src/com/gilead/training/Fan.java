package com.gilead.training;

public class Fan implements Switchable{
    String color;

    String model;

    String type;

    @Override
    public void on() {
        System.out.println( "Fan --> Start");
    }

    @Override
    public void off() {
        System.out.println("Fan --> Stop");
    }
}
