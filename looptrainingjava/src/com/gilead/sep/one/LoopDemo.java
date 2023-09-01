package com.gilead.sep.one;

public class LoopDemo {
    public static void main(String[] args) {
        ForRepeater r = new ForRepeater();
        r.printTimes(3);
        System.out.println();
        WhileRepeater w = new WhileRepeater();
        w.printTimes(3);
        System.out.println();
        DoWhileRepeater dw = new DoWhileRepeater();
        dw.printTimes(3);
    }
}
