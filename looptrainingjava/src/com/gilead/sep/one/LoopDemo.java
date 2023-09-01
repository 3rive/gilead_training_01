package com.gilead.sep.one;

public class LoopDemo {
    public static void main(String[] args) {
        Repeater r = new Repeater();
        r.printTimes(5);
        System.out.println();
        WhileRepeater w = new WhileRepeater();
        w.printTimes(10);
    }
}
