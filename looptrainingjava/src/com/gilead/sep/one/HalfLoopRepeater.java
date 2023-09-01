package com.gilead.sep.one;

public class HalfLoopRepeater {
    public void printHalfTimes(int times){
        int halfTimes = times/2;
        for (int i = 1; i <= halfTimes; i++) {
            System.out.print(i + "  ");
        }
    }
}
