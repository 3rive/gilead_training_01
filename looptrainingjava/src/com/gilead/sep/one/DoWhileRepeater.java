package com.gilead.sep.one;

public class DoWhileRepeater {
    public void printTimes(int times) {
        int i = 1;
        do {
            System.out.print(i + "  ");
            i++;
        } while (i <= times);
    }
}
