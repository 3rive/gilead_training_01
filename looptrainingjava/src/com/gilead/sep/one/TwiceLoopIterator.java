package com.gilead.sep.one;

public class TwiceLoopIterator {
    public void printTwiceTimes(int times){
        int twice = times *2;
        for (int i = 1; i <= twice; i++) {
            System.out.print(i + "  ");
        }
    }
}
