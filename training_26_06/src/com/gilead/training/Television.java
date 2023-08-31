package com.gilead.training;

public class Television implements Switchable{
    @Override
    public void on() {
        System.out.println( "TV --> ON");
    }

    @Override
    public void off() {
        System.out.println( "TV --> OFF");
    }
}
